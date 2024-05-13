package ayds.songinfo.home.controller

import ayds.observer.Subject
import ayds.songinfo.home.model.HomeModel
import ayds.songinfo.home.model.entities.Song.SpotifySong
import ayds.songinfo.home.view.HomeUiEvent
import ayds.songinfo.home.view.HomeUiState
import ayds.songinfo.home.view.HomeView
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test


class HomeControllerTest {

    private val homeModel: HomeModel = mockk(relaxUnitFun = true)

    private val onActionSubject = Subject<HomeUiEvent>()
    private val homeView: HomeView = mockk(relaxUnitFun = true) {
        every { uiEventObservable } returns onActionSubject
    }

    private val homeController = HomeControllerImpl(homeModel)

    @Before
    fun setup() {
        homeController.setHomeView(homeView)
    }

    @Test
    fun `on search event should search song`() {
        every { homeView.uiState } returns HomeUiState(searchTerm = "song")

        onActionSubject.notify(HomeUiEvent.Search)

        verify { homeModel.searchSong("song") }
    }

    @Test
    fun `on more details event should navigate to more details`() {
        every { homeView.uiState } returns HomeUiState(songId = "id")
        val song: SpotifySong = mockk { every { artistName } returns "artist" }
        every { homeModel.getSongById("id") } returns song

        onActionSubject.notify(HomeUiEvent.MoreDetails)

        verify { homeView.navigateToOtherDetails("artist") }
    }

    @Test
    fun `on open song url event should open external link`() {
        every { homeView.uiState } returns HomeUiState(songUrl = "url")

        onActionSubject.notify(HomeUiEvent.OpenSongUrl)

        verify { homeView.openExternalLink("url") }
    }
}