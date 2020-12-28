package htwb.ai.beleg.service;

import htwb.ai.beleg.entity.SongList;
import htwb.ai.beleg.repository.SongListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongListService {

    private SongListRepository songListRepository;

    public SongListService(SongListRepository songListRepository) {
        this.songListRepository = songListRepository;
    }

    /**
     * Finds Songs List by user Id
     * @param userId
     * @return List of SongsList
     */
    public List<SongList> getAllSongLists(String userId) {
        return songListRepository.findAllByOwnerId(userId);
    }

    /**
     * Finds song list by id
     * @param id of SongLis
     * @return songlist
     */
    public SongList getBySongListId(int id) {
        return songListRepository.findById(id);
    }

    /**
     * Finds public SongsList by user Id
     * @param userId
     * @return List of SongsLists
     */
    public List<SongList> getPublicSongLists(String userId) {
        return songListRepository.findAllByOwnerIdAndIsPrivateFalse(userId);
    }

    /**
     * Returns saved song list
     * @param songList
     * @return songList
     */
    public SongList save(SongList songList) {
        return songListRepository.save(songList);
    }

    /**
     * Deletes SongList by id
     * @param id of SongList
     */
    public void deleteSongList(int id) {
        SongList songList = songListRepository.findById(id);
        songList.removeFromSongs();
        songListRepository.save(songList);
        songListRepository.delete(songList);
    }
}
