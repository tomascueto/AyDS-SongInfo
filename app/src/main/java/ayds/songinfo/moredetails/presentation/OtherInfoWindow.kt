package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import ayds.songinfo.moredetails.data.OtherInfoRepository
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.external.OtherInfoServiceImpl
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageDB
import ayds.songinfo.moredetails.fulllogic.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.LastFMAPI
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Locale

private const val ARTICLE_BD_NAME = "database-article"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

data class ArtistBiography(val artistName: String, val biography: String, val articleUrl: String)

class OtherInfoWindow : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView

    private lateinit var repository: OtherInfoRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViewProperties()
        initGraph()
        getArtistInfoAsync()
    }

    private fun initGraph() {
        val articleDatabase =
            databaseBuilder(this, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val lastFMAPI = retrofit.create(LastFMAPI::class.java)

        val otherInfoLocalStorage = OtherInfoLocalStorageDB(articleDatabase)
        val otherInfoService = OtherInfoServiceImpl(lastFMAPI)

        repository = OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        val artistBiography = repository.getArtistInfo(getArtistName())
        updateUi(artistBiography)
    }

    private fun updateUi(artistBiography: ArtistBiography) {
        runOnUiThread {
            updateOpenUrlButton(artistBiography)
            updateLastFMLogo()
            updateArticleText(artistBiography)
        }
    }

    private fun updateOpenUrlButton(artistBiography: ArtistBiography) {
        openUrlButton.setOnClickListener {
            navigateToUrl(artistBiography.articleUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateLastFMLogo() {
        Picasso.get().load(LASTFM_IMAGE_URL).into(lastFMImageView)
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateArticleText(artistBiography: ArtistBiography) {
        val text = artistBiography.biography.replace("\\n", "\n")
        articleTextView.text = Html.fromHtml(textToHtml(text, artistBiography.artistName))
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}


