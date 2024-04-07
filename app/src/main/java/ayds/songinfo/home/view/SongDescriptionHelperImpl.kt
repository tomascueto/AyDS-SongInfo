package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${song.releaseDate()}"

            else -> "Song not found"
        }
    }

    private fun SpotifySong.releaseDate() =
        when (this.releaseDatePrecision) {
            "day" -> {
                val year = this.releaseDate.split("-").first()
                val month = this.releaseDate.split("-")[1]
                val day = this.releaseDate.split("-")[2]
                "$day/$month/$year"
            }

            "month" -> {
                val year = this.releaseDate.split("-").first()
                val month = this.releaseDate.split("-")[1]
                "${month.toInt().toMonthString()},$year"
            }

            "year" -> {
                val isLeapYear = isLeapYear(this.releaseDate.toInt())
                "${this.releaseDate} ${if (isLeapYear) "(leap year)" else "(not a leap year)"}"
            }

            else -> ""
        }

    private fun Int.toMonthString() =
        when (this) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> ""
        }

    private fun isLeapYear(n: Int) = (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)
}