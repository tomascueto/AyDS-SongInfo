package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.lang.Exception
import java.util.Locale

data class ArtistBiography(val artistName: String, val biography: String, val articleUrl: String)

class OtherInfoWindow : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView

    private lateinit var articleDatabase: ArticleDatabase

    private lateinit var lastFMAPI: LastFMAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViewProperties()
        initArticleDatabase()
        initLastFMAPI()
        getArtistInfoAsync()
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }

    private fun initArticleDatabase() {
        articleDatabase = databaseBuilder(this, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    }

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {

        var artistBiography = getArtistInfoFromRepository()

        updateUi(artistBiography)
    }

    private fun getArtistInfoFromRepository(): ArtistBiography {
        val artistName = getArtistName()

        val dbArticle = getArticleFromDB(artistName)

        var artistBiography = ArtistBiography("", "", "")

        if (dbArticle != null) {
            artistBiography = dbArticle.copy(biography = "[*]" + dbArticle.biography)
        } else {
            artistBiography = getArticleFromService(artistName, artistBiography)
            insertArtistIntoDB(artistBiography)
        }
        return artistBiography
    }

    private fun getArticleFromService(
        artistName: String,
        artistBiography: ArtistBiography
    ): ArtistBiography {
        var artistBiography1 = artistBiography
        val callResponse: Response<String>

        try {
            callResponse = lastFMAPI.getArtistInfo(artistName).execute()

            val gson = Gson()
            val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val artist = jobj["artist"].getAsJsonObject()
            val bio = artist["bio"].getAsJsonObject()
            val extract = bio["content"]
            val url = artist["url"]

            var text = ""
            if (extract == null) {
                text = "No Results"
            } else {

                text = extract.asString.replace("\\n", "\n")
                text = textToHtml(text, artistName)
            }

            artistBiography1 = ArtistBiography(artistName, text, url.asString)

        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return artistBiography1
    }

    private fun getArticleFromDB(artistName: String): ArtistBiography? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, "[*]" + artistEntity.biography, artistEntity.articleUrl)
        }
    }

    private fun insertArtistIntoDB(artistBiography: ArtistBiography) {
        Thread {
            articleDatabase.ArticleDao().insertArticle(
                ArticleEntity(
                    artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
                )
            )
        }.start()
    }

    private fun updateUi(artistBiography: ArtistBiography) {
        runOnUiThread {
            openUrlButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(artistBiography.articleUrl))
                startActivity(intent)
            }

            Picasso.get().load(LASTFM_IMAGE_URL).into(lastFMImageView)

            articleTextView.text = Html.fromHtml(artistBiography.biography)
        }
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")
    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const  val ARTICLE_BD_NAME = "database-article"
        const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
        const val LASTFM_IMAGE_URL =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        fun textToHtml(text: String, term: String?): String {
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
    }
}
