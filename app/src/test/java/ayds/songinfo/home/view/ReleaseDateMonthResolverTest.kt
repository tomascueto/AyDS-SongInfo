package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseDateMonthResolverTest {

    private val song: Song.SpotifySong = mockk()
    private val releaseDateMonthResolver: ReleaseDateMonthResolver = ReleaseDateMonthResolver(song)

    @Test
    fun `on get release date should return month 1`() {
        every { song.releaseDate } returns "1992-01"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "January, 1992")
    }

     @Test
     fun `on get release date should return month 2`() {
         every { song.releaseDate } returns "1992-02"

         val result = releaseDateMonthResolver.getReleaseDate()

         assertEquals(result, "February, 1992")
     }

    @Test
    fun `on get release date should return month 3`() {
        every { song.releaseDate } returns "1992-03"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "March, 1992")
    }

    @Test
    fun `on get release date should return month 4`() {
        every { song.releaseDate } returns "1992-04"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "April, 1992")
    }

    @Test
    fun `on get release date should return month 5`() {
        every { song.releaseDate } returns "1992-05"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "May, 1992")
    }

    @Test
    fun `on get release date should return month 6`() {
        every { song.releaseDate } returns "1992-06"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "June, 1992")
    }

    @Test
    fun `on get release date should return month 7`() {
        every { song.releaseDate } returns "1992-07"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "July, 1992")
    }

    @Test
    fun `on get release date should return month 8`() {
        every { song.releaseDate } returns "1992-08"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "August, 1992")
    }

    @Test
    fun `on get release date should return month 9`() {
        every { song.releaseDate } returns "1992-09"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "September, 1992")
    }

    @Test
    fun `on get release date should return month 10`() {
        every { song.releaseDate } returns "1992-10"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "October, 1992")
    }

    @Test
    fun `on get release date should return month 11`() {
        every { song.releaseDate } returns "1992-11"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "November, 1992")
    }

    @Test
    fun `on get release date should return month 12`() {
        every { song.releaseDate } returns "1992-12"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, "December, 1992")
    }

    @Test
    fun `on get release date should return month 0`() {
        every { song.releaseDate } returns "1992-00"

        val result = releaseDateMonthResolver.getReleaseDate()

        assertEquals(result, ", 1992")
    }
}