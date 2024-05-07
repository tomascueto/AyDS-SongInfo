package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.fulllogic.LastFMAPI
import ayds.songinfo.moredetails.presentation.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

interface OtherInfoService {
    fun getArticle(artistName: String): ArtistBiography
}

internal class OtherInfoServiceImpl(
    private val lastFMAPI: LastFMAPI
): OtherInfoService {

    override fun getArticle(artistName: String): ArtistBiography {

        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }

    private fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistBiography(artistName, text, url.asString)
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

}