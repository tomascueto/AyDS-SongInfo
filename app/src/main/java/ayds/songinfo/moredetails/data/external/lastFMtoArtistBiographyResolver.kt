package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject

interface LastFMtoArtistBiographyResolver {
    fun map(
        serviceData: String?,
        artistName: String
    ): ArtistBiography
}

private const val ARTIST = "artist"
private const val BIO = "bio"
private const val CONTENT = "content"
private const val URL = "url"
private const val NO_RESULTS = "No Results"

class LastFMtoArtistBiographyResolverImpl() : LastFMtoArtistBiographyResolver{


    override fun map(
        serviceData: String?,
        artistName: String
    ): ArtistBiography {
        val gson = Gson()
        val jasonObject = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jasonObject[ARTIST].getAsJsonObject()
        val bio = artist[BIO].getAsJsonObject()
        val extract = bio[CONTENT]
        val url = artist[URL]
        val text = extract?.asString ?: NO_RESULTS

        return ArtistBiography(artistName, text, url.asString)
    }
}