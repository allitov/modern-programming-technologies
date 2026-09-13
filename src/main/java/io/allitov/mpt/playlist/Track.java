package io.allitov.mpt.playlist;

import java.util.Objects;

public record Track(String title, String artist, int durationSec) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Track track = (Track) o;

        return Objects.equals(title, track.title) && Objects.equals(artist, track.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }

    @Override
    public String toString() {
        return String.format("%s — %s (%02d:%02d)",
                artist, title, durationSec / 60, durationSec % 60);
    }
}
