package htwb.ai.beleg.repository;


import htwb.ai.beleg.entity.SongList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongListRepository extends CrudRepository<SongList, Integer> {

    /**
     * Finds all Songs List by Owner id
     * @param id of SongList owner
     * @return List of SongsLists
     */
    List<SongList> findAllByOwnerId(String id);

    /**
     * Finds all public Songs Lists by Onwer id
     * @param id of SongList owner
     * @return List of SongsLists
     */
    List<SongList> findAllByOwnerIdAndIsPrivateFalse(String id);


    /**
     * Finds SongList by id
     * @param id of SongList
     * @return SongList
     */
    SongList findById(int id);

    /**
     * Saves songList in DB
     * @param songList
     * @return SongList
     */
    SongList save(SongList songList);

}
