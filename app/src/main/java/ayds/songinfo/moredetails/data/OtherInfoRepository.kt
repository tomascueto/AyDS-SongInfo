package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.presentation.ArtistBiography

interface OtherInfoRepository {
    fun getArtistInfo(artistName: String): ArtistBiography
}

class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoService: OtherInfoService
): OtherInfoRepository {

    override fun getArtistInfo(artistName: String): ArtistBiography {

        val dbArticle = otherInfoLocalStorage.getArticle(artistName)

        val artistBiography: ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.markItAsLocal()
        } else {
            artistBiography = otherInfoService.getArticle(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                otherInfoLocalStorage.insertArtist(artistBiography)
            }
        }
        return artistBiography
    }

    private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")

}