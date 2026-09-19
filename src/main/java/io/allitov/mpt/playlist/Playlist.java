package io.allitov.mpt.playlist;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Playlist {

    @Getter
    private final int capacity;

    private final List<Track> items;

    public Playlist(int capacity) {
        if (capacity <= 0) {
            throw new PlaylistException("Вместимость должна быть больше 0, получено: " + capacity);
        }
        this.capacity = capacity;
        this.items = new ArrayList<>(capacity);
    }

    public int getCount() {
        return items.size();
    }

    public Track getTrack(int index) {
        checkIndex(index);
        return items.get(index);
    }

    public void setTrack(int index, Track track) {
        checkIndex(index);
        if (track == null) {
            throw new PlaylistException("Трек не может быть null");
        }
        items.set(index, track);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= items.size()) {
            throw new PlaylistException("Индекс вне диапазона [0, " + (items.size() - 1) + "]: " + index);
        }
    }

    public void addTrack(Track track) {
        if (track == null) {
            throw new PlaylistException("Трек не может быть null");
        }
        if (items.size() >= capacity) {
            throw new PlaylistException("Плейлист переполнен: вместимость " + capacity);
        }
        if (items.contains(track)) {
            throw new PlaylistException("Трек уже есть в плейлисте: " + track);
        }
        items.add(track);
    }

    public void merge(Playlist other) {
        if (other == null) {
            throw new PlaylistException("Другой плейлист не может быть null");
        }
        List<Track> toAdd = new ArrayList<>();
        for (Track t : other.items) {
            if (!items.contains(t) && !toAdd.contains(t)) {
                toAdd.add(t);
            }
        }
        if (items.size() + toAdd.size() > capacity) {
            throw new PlaylistException("Недостаточно вместимости для объединения: текущих "
                    + items.size() + " + уникальных " + toAdd.size() + " > вместимости " + capacity);
        }
        items.addAll(toAdd);
    }

    public void removeTracksOf(Playlist other) {
        if (other == null) {
            throw new PlaylistException("Другой плейлист не может быть null");
        }
        int before = items.size();
        items.removeIf(other.items::contains);
        if (items.size() == before) {
            throw new PlaylistException("Ни один трек из указанного плейлиста не найден");
        }
    }

    public int totalDuration() {
        return items.stream()
                .mapToInt(Track::durationSec)
                .sum();
    }

    public int findByArtist(String artist) {
        for (int i = 0; i < items.size(); i++) {
            if (Objects.equals(items.get(i).artist(), artist)) {
                return i;
            }
        }
        throw new PlaylistException("Трек исполнителя не найден: " + artist);
    }

    @Override
    public String toString() {
        return items.stream()
                .map(Track::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Playlist other)) {
            return false;
        }
        return items.equals(other.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}