package htwb.ai.beleg.repository;

import htwb.ai.beleg.entity.SongText;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SongTextRepository extends MongoRepository<SongText, String> {

    /**
     * Finds all songs texts
     * @return List of SongsTexts
     */
    List<SongText> findAll();

    /**
     * Finds a song text by id
     *
     * @param id of song text
     * @return Optional of song Text
     */
    Optional<SongText> findById(String id);

    /**
     * Deletes song text by id
     * @param id of song text
     */
    void deleteById(String id);
}
