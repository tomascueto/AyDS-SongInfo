package ayds.songinfo.home.view

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseDateDayResolverTest {

    @Test
    fun `on get release date should return day 1`() {
        val releaseDateDayResolver =
            ReleaseDateDayResolver(mockk { every { releaseDate } returns "1992-02-01" })

        val result = releaseDateDayResolver.getReleaseDate()

        assertEquals(result, "01/02/1992")
    }

}