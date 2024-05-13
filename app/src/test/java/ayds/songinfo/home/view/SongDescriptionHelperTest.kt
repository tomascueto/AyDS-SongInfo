package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class SongDescriptionHelperTest {

    private val releaseDateResolverFactory: ReleaseDateResolverFactory = mockk {
        every { getReleaseDateResolver(any()) } returns mockk {
            every { getReleaseDate() } returns "1992"
        }
    }
    private val songDescriptionHelper = SongDescriptionHelperImpl(releaseDateResolverFactory)

    @Test
    fun `given a local song it should return the description`() {
        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "url",
            "url",
            "url",
            true,
        )

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush [*]\n" +
                "Artist: Stone Temple Pilots\n" +
                "Album: Core\n" +
                "Release date: 1992"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local song it should return the description`() {
        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "url",
            "url",
            "url",
            false,
        )

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush \n" +
                "Artist: Stone Temple Pilots\n" +
                "Album: Core\n" +
                "Release date: 1992"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non spotify song it should return the song not found description`() {
        val song: Song = mockk()

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected = "Song not found"

        assertEquals(expected, result)
    }
}