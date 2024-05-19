package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import org.junit.Assert
import org.junit.Test

class ArtistBiographyDescriptionHelperTest {

    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()

    @Test
    fun `on non local biography should return description`() {
        val artistBiography = ArtistBiography("artistName", "biography", "articleUrl")

        val description = artistBiographyDescriptionHelper.getDescription(artistBiography)

        val result = "<html><div width=400><font face=\"arial\">biography</font></div></html>"
        Assert.assertEquals(description, result)
    }
    @Test
    fun `on non local biography with double slash fixed should return description`() {
        val artistBiography = ArtistBiography("artistName", "biography\\n", "articleUrl")

        val description = artistBiographyDescriptionHelper.getDescription(artistBiography)

        val result = "<html><div width=400><font face=\"arial\">biography<br></font></div></html>"
        Assert.assertEquals(description, result)
    }
    @Test
    fun `on non local biography with apostrophe removed should return description`() {
        val artistBiography = ArtistBiography("artistName", "biography'n", "articleUrl")

        val description = artistBiographyDescriptionHelper.getDescription(artistBiography)

        val result = "<html><div width=400><font face=\"arial\">biography n</font></div></html>"
        Assert.assertEquals(description, result)
    }
    @Test
    fun `on non local biography should return description with bold artist name`() {
        val artistBiography = ArtistBiography("artistName", "artistName", "articleUrl")

        val description = artistBiographyDescriptionHelper.getDescription(artistBiography)

        val result = "<html><div width=400><font face=\"arial\"><b>ARTISTNAME</b></font></div></html>"
        Assert.assertEquals(description, result)
    }
}