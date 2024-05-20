package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val otherInfoRepository: OtherInfoRepository = mockk()
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk()
    private val otherInfoPresenter: OtherInfoPresenter =
        OtherInfoPresenterImpl(otherInfoRepository, artistBiographyDescriptionHelper)

    @Test
    fun `getArtistInfo should return artist biography ui state`() {
        val artistBiography = ArtistBiography("artistName", "biography", "articleUrl")
        every { otherInfoRepository.getArtistInfo("artistName") } returns artistBiography
        every { artistBiographyDescriptionHelper.getDescription(artistBiography) } returns "description"
        val artistBiographyTester: (ArtistBiographyUiState) -> Unit = mockk(relaxed = true)

        otherInfoPresenter.artistBiographyObservable.subscribe(artistBiographyTester)
        otherInfoPresenter.getArtistInfo("artistName")

        val result = ArtistBiographyUiState("artistName", "description", "articleUrl")
        verify { artistBiographyTester(result) }
    }
}