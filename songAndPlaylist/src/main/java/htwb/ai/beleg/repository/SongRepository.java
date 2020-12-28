package htwb.ai.beleg.repository;

import htwb.ai.beleg.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SongRepository extends JpaRepository<Song, Integer> {

    /**
     * Finds song by id
     * @param songId
     * @return Song
     */
    Song findById(int songId);

    /**
     * Saves song in DB
     * @param song
     * @return song
     */
    Song save(Song song);

    /**
     * Finds all songs
     * @return List of songs
     */
    List<Song> findAll();

    /**
     * Deletes Song by id
     * @param songId
     */
    void deleteById(int songId);

}
