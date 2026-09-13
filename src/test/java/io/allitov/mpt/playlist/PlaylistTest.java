package io.allitov.mpt.playlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ADT «Playlist»")
class PlaylistTest {

    private static final Track TRACK_A = new Track("Song A", "Artist A", 120);

    private static final Track TRACK_B = new Track("Song B", "Artist B", 75);

    private static final Track TRACK_C = new Track("Song C", "Artist C", 200);

    private static final Track TRACK_D = new Track("Song D", "Artist D", 61);

    @Nested
    @DisplayName("Конструктор")
    class Constructor {

        @ParameterizedTest(name = "capacity = {0}")
        @ValueSource(ints = {0, -1, -100})
        @DisplayName("бросает PlaylistException, если capacity <= 0")
        void shouldThrowWhenCapacityIsNotPositive(int capacity) {
            assertThatThrownBy(() -> new Playlist(capacity))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Вместимость должна быть больше 0");
        }

        @ParameterizedTest(name = "capacity = {0}")
        @ValueSource(ints = {1, 5, 100})
        @DisplayName("создаёт пустой плейлист заданной вместимости")
        void shouldCreateEmptyPlaylistOfGivenCapacity(int capacity) {
            Playlist playlist = new Playlist(capacity);

            assertThat(playlist.getCapacity()).isEqualTo(capacity);
            assertThat(playlist.getCount()).isZero();
        }
    }

    @Nested
    @DisplayName("getCapacity() / getCount()")
    class Properties {

        @Test
        @DisplayName("возвращает вместимость, переданную в конструктор")
        void shouldReturnCapacityFromConstructor() {
            assertThat(new Playlist(7).getCapacity()).isEqualTo(7);
        }

        @Test
        @DisplayName("Count отражает текущее число треков")
        void countShouldReflectNumberOfTracks() {
            Playlist playlist = new Playlist(5);

            assertThat(playlist.getCount()).isZero();

            playlist.addTrack(TRACK_A);
            playlist.addTrack(TRACK_B);

            assertThat(playlist.getCount()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("getTrack(int)")
    class GetTrack {

        @ParameterizedTest(name = "index = {0} => {1}")
        @CsvSource({
                "0, Artist A",
                "1, Artist B",
                "2, Artist C"
        })
        @DisplayName("возвращает трек по корректному индексу")
        void shouldReturnTrackAtValidIndex(int index, String expectedArtist) {
            Playlist playlist = playlistOf(3, TRACK_A, TRACK_B, TRACK_C);

            assertThat(playlist.getTrack(index).artist()).isEqualTo(expectedArtist);
        }

        @ParameterizedTest(name = "index = {0}")
        @ValueSource(ints = {-1, -100, 3, 100})
        @DisplayName("бросает PlaylistException при индексе вне [0, Count - 1]")
        void shouldThrowWhenIndexIsOutOfRange(int index) {
            Playlist playlist = playlistOf(3, TRACK_A, TRACK_B, TRACK_C);

            assertThatThrownBy(() -> playlist.getTrack(index))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Индекс вне диапазона");
        }

        @Test
        @DisplayName("бросает PlaylistException для пустого плейлиста")
        void shouldThrowWhenPlaylistIsEmpty() {
            assertThatThrownBy(() -> new Playlist(3).getTrack(0))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Индекс вне диапазона");
        }
    }

    @Nested
    @DisplayName("setTrack(int, Track)")
    class SetTrack {

        @ParameterizedTest(name = "index = {0}")
        @ValueSource(ints = {0, 1, 2})
        @DisplayName("заменяет трек по индексу, не меняя Count")
        void shouldReplaceTrackAtValidIndex(int index) {
            Playlist playlist = playlistOf(3, TRACK_A, TRACK_B, TRACK_C);

            playlist.setTrack(index, TRACK_D);

            assertThat(playlist.getTrack(index)).isEqualTo(TRACK_D);
            assertThat(playlist.getCount()).isEqualTo(3);
        }

        @ParameterizedTest(name = "index = {0}")
        @ValueSource(ints = {-1, 3, 100})
        @DisplayName("бросает PlaylistException при индексе вне [0, Count - 1]")
        void shouldThrowWhenIndexIsOutOfRange(int index) {
            Playlist playlist = playlistOf(3, TRACK_A, TRACK_B, TRACK_C);

            assertThatThrownBy(() -> playlist.setTrack(index, TRACK_D))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Индекс вне диапазона");
        }

        @Test
        @DisplayName("бросает PlaylistException при null-треке и не меняет состояние")
        void shouldThrowWhenTrackIsNull() {
            Playlist playlist = playlistOf(2, TRACK_A, TRACK_B);

            assertThatThrownBy(() -> playlist.setTrack(0, null))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Трек не может быть null");

            assertThat(playlist.getTrack(0)).isEqualTo(TRACK_A);
            assertThat(playlist.getCount()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("addTrack(Track)")
    class AddTrack {

        @Test
        @DisplayName("добавляет трек в конец плейлиста")
        void shouldAddTrackToTheEnd() {
            Playlist playlist = playlistOf(3, TRACK_A);

            playlist.addTrack(TRACK_B);

            assertThat(playlist.getCount()).isEqualTo(2);
            assertThat(playlist.getTrack(0)).isEqualTo(TRACK_A);
            assertThat(playlist.getTrack(1)).isEqualTo(TRACK_B);
        }

        @ParameterizedTest(name = "вместимость = {0}")
        @ValueSource(ints = {1, 3, 5})
        @DisplayName("позволяет заполнить плейлист ровно до вместимости")
        void shouldAllowFillingPlaylistUpToCapacity(int capacity) {
            Playlist playlist = new Playlist(capacity);

            for (int i = 0; i < capacity; i++) {
                playlist.addTrack(new Track("Artist " + i, "Song " + i, 60 + i));
            }

            assertThat(playlist.getCount()).isEqualTo(capacity);
        }

        @ParameterizedTest(name = "вместимость = {0}")
        @ValueSource(ints = {1, 3})
        @DisplayName("бросает PlaylistException при переполнении и не меняет состояние")
        void shouldThrowWhenPlaylistIsFull(int capacity) {
            Playlist playlist = new Playlist(capacity);
            Track last = null;
            for (int i = 0; i < capacity; i++) {
                last = new Track("Artist " + i, "Song " + i, 60 + i);
                playlist.addTrack(last);
            }

            assertThatThrownBy(() -> playlist.addTrack(TRACK_D))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Плейлист переполнен");

            assertThat(playlist.getCount()).isEqualTo(capacity);
            assertThat(playlist.getTrack(capacity - 1)).isSameAs(last);
        }

        @ParameterizedTest(name = "позиция существующего дубликата = {0}")
        @ValueSource(ints = {0, 1, 2})
        @DisplayName("бросает PlaylistException при добавлении дубликата")
        void shouldThrowWhenAddingDuplicateTrack(int duplicateIndex) {
            Playlist playlist = playlistOf(5, TRACK_A, TRACK_B, TRACK_C);

            assertThatThrownBy(() -> playlist.addTrack(playlist.getTrack(duplicateIndex)))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Трек уже есть в плейлисте");

            assertThat(playlist.getCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("бросает PlaylistException при null-треке")
        void shouldThrowWhenTrackIsNull() {
            Playlist playlist = new Playlist(3);

            assertThatThrownBy(() -> playlist.addTrack(null))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Трек не может быть null");

            assertThat(playlist.getCount()).isZero();
        }
    }

    @Nested
    @DisplayName("merge(Playlist)")
    class Merge {

        @Test
        @DisplayName("бросает PlaylistException, если other == null")
        void shouldThrowWhenOtherIsNull() {
            Playlist playlist = playlistOf(2, TRACK_A);

            assertThatThrownBy(() -> playlist.merge(null))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Другой плейлист не может быть null");
        }

        @Test
        @DisplayName("добавляет уникальные треки в конец, сохраняя порядок")
        void shouldAppendUniqueTracksPreservingOrder() {
            Playlist playlist = playlistOf(5, TRACK_A);
            Playlist other = playlistOf(2, TRACK_B, TRACK_C);

            playlist.merge(other);

            assertThat(playlist.getCount()).isEqualTo(3);
            assertThat(playlist.getTrack(0)).isEqualTo(TRACK_A);
            assertThat(playlist.getTrack(1)).isEqualTo(TRACK_B);
            assertThat(playlist.getTrack(2)).isEqualTo(TRACK_C);
            assertThat(other.getCount()).isEqualTo(2); // other не изменился
        }

        @Test
        @DisplayName("пропускает треки, уже имеющиеся в текущем плейлисте")
        void shouldSkipTracksAlreadyPresentInCurrentPlaylist() {
            Playlist playlist = playlistOf(5, TRACK_A, TRACK_B);
            Playlist other = playlistOf(2, TRACK_B, TRACK_C); // TRACK_B уже есть

            playlist.merge(other);

            assertThat(playlist.getCount()).isEqualTo(3);
            assertThat(playlist.getTrack(2)).isEqualTo(TRACK_C);
        }

        @Test
        @DisplayName("не дублирует треки, повторяющиеся внутри other")
        void shouldNotDuplicateTracksRepeatedInsideOther() {
            Playlist playlist = playlistOf(4, TRACK_A);
            // other = [B, B]: дубликат внутри other создаём через setTrack,
            // т.к. addTrack запрещает добавление одинаковых треков
            Playlist other = playlistOf(2, TRACK_B, TRACK_C);
            other.setTrack(1, TRACK_B);

            playlist.merge(other);

            assertThat(playlist.getCount()).isEqualTo(2);
            assertThat(playlist.getTrack(0)).isEqualTo(TRACK_A);
            assertThat(playlist.getTrack(1)).isEqualTo(TRACK_B);
        }

        @Test
        @DisplayName("ничего не меняет, если все треки other уже присутствуют")
        void shouldChangeNothingWhenAllTracksAreDuplicates() {
            Playlist playlist = playlistOf(4, TRACK_A, TRACK_B, TRACK_C);
            Playlist other = playlistOf(2, TRACK_A, TRACK_C);

            playlist.merge(other);

            assertThat(playlist.getCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("ничего не меняет при слиянии с пустым плейлистом")
        void shouldChangeNothingWhenOtherIsEmpty() {
            Playlist playlist = playlistOf(3, TRACK_A);

            playlist.merge(new Playlist(5));

            assertThat(playlist.getCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("допускает результат ровно по вместимости (граница)")
        void shouldMergeWhenResultExactlyFitsCapacity() {
            Playlist playlist = playlistOf(3, TRACK_A);
            Playlist other = playlistOf(2, TRACK_B, TRACK_C);

            playlist.merge(other);

            assertThat(playlist.getCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("бросает PlaylistException при нехватке вместимости и не меняет состояние")
        void shouldThrowWhenNotEnoughCapacityAndKeepState() {
            Playlist playlist = playlistOf(2, TRACK_A);
            Playlist other = playlistOf(3, TRACK_B, TRACK_C, TRACK_D);

            assertThatThrownBy(() -> playlist.merge(other))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Недостаточно вместимости");

            assertThat(playlist.getCount()).isEqualTo(1);
            assertThat(playlist.getTrack(0)).isEqualTo(TRACK_A);
        }
    }

    @Nested
    @DisplayName("removeTracksOf(Playlist)")
    class RemoveTracksOf {

        @Test
        @DisplayName("бросает PlaylistException, если other == null")
        void shouldThrowWhenOtherIsNull() {
            Playlist playlist = playlistOf(2, TRACK_A, TRACK_B);

            assertThatThrownBy(() -> playlist.removeTracksOf(null))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Другой плейлист не может быть null");
        }

        @Test
        @DisplayName("удаляет совпадающие треки, сохраняя порядок оставшихся")
        void shouldRemoveMatchingTracksAndKeepOrderOfRemaining() {
            Playlist playlist = playlistOf(5, TRACK_A, TRACK_B, TRACK_C, TRACK_D);
            Playlist other = playlistOf(2, TRACK_B, TRACK_D);

            playlist.removeTracksOf(other);

            assertThat(playlist.getCount()).isEqualTo(2);
            assertThat(playlist.getTrack(0)).isEqualTo(TRACK_A);
            assertThat(playlist.getTrack(1)).isEqualTo(TRACK_C);
        }

        @Test
        @DisplayName("позволяет удалить все треки")
        void shouldAllowRemovingAllTracks() {
            Playlist playlist = playlistOf(3, TRACK_A, TRACK_B);
            Playlist other = playlistOf(2, TRACK_A, TRACK_B);

            playlist.removeTracksOf(other);

            assertThat(playlist.getCount()).isZero();
        }

        @Test
        @DisplayName("бросает PlaylistException, если ни один трек не найден, и не меняет состояние")
        void shouldThrowWhenNoTracksRemovedAndKeepState() {
            Playlist playlist = playlistOf(3, TRACK_A, TRACK_B);
            Playlist other = playlistOf(2, TRACK_C, TRACK_D);

            assertThatThrownBy(() -> playlist.removeTracksOf(other))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Ни один трек из указанного плейлиста не найден");

            assertThat(playlist.getCount()).isEqualTo(2);
            assertThat(playlist.getTrack(0)).isEqualTo(TRACK_A);
            assertThat(playlist.getTrack(1)).isEqualTo(TRACK_B);
        }

        @Test
        @DisplayName("бросает PlaylistException для пустого other")
        void shouldThrowWhenOtherIsEmpty() {
            Playlist playlist = playlistOf(2, TRACK_A);

            assertThatThrownBy(() -> playlist.removeTracksOf(new Playlist(3)))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Ни один трек из указанного плейлиста не найден");

            assertThat(playlist.getCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("totalDuration()")
    class TotalDuration {

        @ParameterizedTest(name = "длительности {1} => {0} сек")
        @MethodSource("totalDurationCases")
        @DisplayName("возвращает сумму длительностей всех треков")
        void shouldReturnSumOfTrackDurations(int expected, List<Integer> durations) {
            Playlist playlist = new Playlist(Math.max(1, durations.size()));
            for (int i = 0; i < durations.size(); i++) {
                playlist.addTrack(new Track("Artist " + i, "Song " + i, durations.get(i)));
            }

            assertThat(playlist.totalDuration()).isEqualTo(expected);
        }

        private static Stream<Arguments> totalDurationCases() {
            return Stream.of(
                    Arguments.of(0, List.of()),
                    Arguments.of(120, List.of(120)),
                    Arguments.of(195, List.of(120, 75)),
                    Arguments.of(456, List.of(120, 75, 140, 121))
            );
        }
    }

    @Nested
    @DisplayName("findByArtist(String)")
    class FindByArtist {

        @ParameterizedTest(name = "артист ''{0}'' => индекс {1}")
        @CsvSource({
                "Artist A, 0",
                "Artist B, 1",
                "Artist C, 2"
        })
        @DisplayName("возвращает индекс трека исполнителя")
        void shouldReturnIndexOfFirstTrackOfArtist(String artist, int expectedIndex) {
            Playlist playlist = playlistOf(3, TRACK_A, TRACK_B, TRACK_C);

            assertThat(playlist.findByArtist(artist)).isEqualTo(expectedIndex);
        }

        @Test
        @DisplayName("возвращает индекс первого из нескольких треков одного исполнителя")
        void shouldReturnFirstIndexWhenArtistHasMultipleTracks() {
            Playlist playlist = playlistOf(3, TRACK_A, new Track("Song A2", "Artist A", 90), TRACK_B);

            assertThat(playlist.findByArtist("Artist A")).isZero();
        }

        @Test
        @DisplayName("бросает PlaylistException, если исполнитель не найден")
        void shouldThrowWhenArtistNotFound() {
            Playlist playlist = playlistOf(2, TRACK_A, TRACK_B);

            assertThatThrownBy(() -> playlist.findByArtist("Unknown Artist"))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Трек исполнителя не найден");
        }

        @Test
        @DisplayName("бросает PlaylistException для пустого плейлиста")
        void shouldThrowWhenPlaylistIsEmpty() {
            assertThatThrownBy(() -> new Playlist(2).findByArtist("Artist A"))
                    .isInstanceOf(PlaylistException.class)
                    .hasMessageContaining("Трек исполнителя не найден");
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToString {

        @Test
        @DisplayName("пустой плейлист => пустая строка")
        void shouldReturnEmptyStringForEmptyPlaylist() {
            assertThat(new Playlist(2).toString()).isEmpty();
        }

        @Test
        @DisplayName("один трек => одна строка без переводов строки")
        void shouldReturnSingleLineForSingleTrack() {
            Playlist playlist = playlistOf(1, TRACK_A);

            assertThat(playlist.toString())
                    .isEqualTo("Artist A — Song A (02:00)")
                    .doesNotContain(System.lineSeparator());
        }

        @Test
        @DisplayName("несколько треков => строки, разделённые переводом строки")
        void shouldJoinTracksWithLineSeparator() {
            Playlist playlist = playlistOf(3, TRACK_A, TRACK_B, TRACK_C);

            assertThat(playlist).hasToString(
                    "Artist A — Song A (02:00)" + System.lineSeparator()
                            + "Artist B — Song B (01:15)" + System.lineSeparator()
                            + "Artist C — Song C (03:20)");
        }
    }

    @Nested
    @DisplayName("equals / hashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("плейлист равен самому себе (this == o)")
        void shouldBeEqualToItself() {
            Playlist playlist = playlistOf(2, TRACK_A, TRACK_B);

            assertThat(playlist.equals(playlist)).isTrue();
        }

        @Test
        @DisplayName("не равен null")
        void shouldNotBeEqualToNull() {
            assertThat(playlistOf(2, TRACK_A).equals(null)).isFalse();
        }

        @Test
        @DisplayName("не равен объекту другого типа")
        void shouldNotBeEqualToObjectOfDifferentType() {
            assertThat(playlistOf(2, TRACK_A).equals("плейлист")).isFalse();
        }

        @ParameterizedTest(name = "[{index}] {0} => равны: {3}")
        @MethodSource("equalityCases")
        @DisplayName("сравнивает по числу треков и трекам на одинаковых позициях")
        void shouldComparePlaylistsByContent(String description, Playlist first, Playlist second, boolean expected) {
            if (expected) {
                assertThat(first).isEqualTo(second);
            } else {
                assertThat(first).isNotEqualTo(second);
            }
        }

        private static Stream<Arguments> equalityCases() {
            return Stream.of(
                    Arguments.of("одинаковое содержимое при разной вместимости",
                            playlistOf(2, TRACK_A, TRACK_B), playlistOf(10, TRACK_A, TRACK_B), true),
                    Arguments.of("разное число треков",
                            playlistOf(2, TRACK_A), playlistOf(3, TRACK_A, TRACK_B), false),
                    Arguments.of("одинаковое число треков, но разные треки",
                            playlistOf(2, TRACK_A, TRACK_B), playlistOf(2, TRACK_A, TRACK_C), false),
                    Arguments.of("одинаковые треки в разном порядке",
                            playlistOf(2, TRACK_A, TRACK_B), playlistOf(2, TRACK_B, TRACK_A), false)
            );
        }

        @Test
        @DisplayName("равные плейлисты имеют одинаковый hashCode")
        void equalPlaylistsShouldHaveSameHashCode() {
            Playlist first = playlistOf(2, TRACK_A, TRACK_B);
            Playlist second = playlistOf(10, TRACK_A, TRACK_B);

            assertThat(first).hasSameHashCodeAs(second);
        }
    }

    private static Playlist playlistOf(int capacity, Track... tracks) {
        Playlist playlist = new Playlist(capacity);
        for (Track track : tracks) {
            playlist.addTrack(track);
        }
        return playlist;
    }
}