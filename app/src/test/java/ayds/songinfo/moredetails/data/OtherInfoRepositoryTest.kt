package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class OtherInfoRepositoryTest {

    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxUnitFun = true)
    private val otherInfoService: OtherInfoService = mockk()
    private val otherInfoRepository: OtherInfoRepository =
        OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)

    @Test
    fun `on local stored artist should return it`() {
        val artistBiography = ArtistBiography("artistName", "biography", "url")
        every { otherInfoLocalStorage.getArticle("artistName") } returns artistBiography

        val result = otherInfoRepository.getArtistInfo("artistName")

        Assert.assertEquals(artistBiography, result)
        Assert.assertEquals(artistBiography.isLocallyStored, true)
    }

    @Test
    fun `on non local stored artist should return service artist`() {
        val artistBiography = ArtistBiography("artistName", "biography", "url")
        every { otherInfoLocalStorage.getArticle("artistName") } returns null
        every { otherInfoService.getArticle("artistName") } returns artistBiography

        val result = otherInfoRepository.getArtistInfo("artistName")

        Assert.assertEquals(artistBiography, result)
        Assert.assertEquals(artistBiography.isLocallyStored, false)
        verify { otherInfoLocalStorage.insertArtist(artistBiography) }
    }
    @Test
    fun `on non local stored artist should return service artist with empty biography`() {
        val artistBiography = ArtistBiography("artistName", "", "url")
        every { otherInfoLocalStorage.getArticle("artistName") } returns null
        every { otherInfoService.getArticle("artistName") } returns artistBiography

        val result = otherInfoRepository.getArtistInfo("artistName")

        Assert.assertEquals(artistBiography, result)
        Assert.assertEquals(artistBiography.isLocallyStored, false)
        verify(inverse = true) { otherInfoLocalStorage.insertArtist(artistBiography) }
    }
}