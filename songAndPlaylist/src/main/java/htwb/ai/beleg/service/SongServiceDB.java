package htwb.ai.beleg.service;

import htwb.ai.beleg.entity.Song;
import htwb.ai.beleg.repository.SongRepository;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class SongServiceDB {

    private SongRepository songRepository;

    public SongServiceDB(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    /**
     * Saves song in Db
     * @param song
     * @return song
     */
    public Song save(Song song) {
        return songRepository.save(song);
    }

    /**
     * Finds Song by id
     * @param songId of song
     * @return song
     */
    public Song findById(int songId) {
        return songRepository.findById(songId);
    }

    /**
     * Find all song in Db
     * @return List of songs
     */
    public List<Song> findAll() {
        return songRepository.findAll();
    }

    /**
     * Deletes song by id
     * @param id of song
     * @throws PersistenceException
     */
    public void delete(int id) throws PersistenceException {
        songRepository.deleteById(id);
    }

}
