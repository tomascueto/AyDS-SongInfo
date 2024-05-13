package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class ReleaseDateYearResolverTest {

    private val song: Song.SpotifySong = mockk()
    private val releaseDateYearResolver: ReleaseDateYearResolver = ReleaseDateYearResolver(song)

    @Test
    fun `on get release date should return not leap year`() {
        every { song.releaseDate } returns "1991"

        val result = releaseDateYearResolver.getReleaseDate()

        Assert.assertEquals(result, "1991 (not a leap year)")
    }
    @Test
    fun `on get release date should return leap year`() {
        every { song.releaseDate } returns "2024"

        val result = releaseDateYearResolver.getReleaseDate()

        Assert.assertEquals(result, "2024 (leap year)")
    }
}