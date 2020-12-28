package htwb.ai.beleg.controller;

import htwb.ai.beleg.entity.Song;
import htwb.ai.beleg.service.SongServiceDB;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("songs")
public class SongController {

    private SongServiceDB songServiceDB;

    public SongController(SongServiceDB songServiceDB) {
        this.songServiceDB = songServiceDB;
    }

    /**
     * The Method returns all songs
     * @return ResponseEntity with List of Songs and status code 200
     */
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllSongs() {
        List<Song> allSongs = songServiceDB.findAll();

        return new ResponseEntity<>(allSongs, HttpStatus.OK);
    }

    /**
     * The method returns song by id with corresponding status code if song
     * otherwise the method returns with the other status code
     *
     * @param id of song
     * @return ResponseEntity with song and corresponding status code
     */
    @GetMapping(
            path = "{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> getSingleSong(@PathVariable int id) {
        Song song = songServiceDB.findById(id);
        if (song == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    /**
     *
     * The method posts new Song and returns ResponseEntity with Status code 201 and location of song in header,
     * @param song of type Song
     * @return ResponseEntity with corresponding status code and header
     */

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createSong(@Valid @RequestBody Song song) {
        song.setId(0);
        Song savedSong = songServiceDB.save(song);
        int id = savedSong.getId();
        HttpHeaders httpHeaders = new HttpHeaders();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        httpHeaders.setLocation(location);
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    /**
     * The method updates song if update is successful, the method return corresponding status code.
     * If song or id are invalid the method return  the other corresponding status code.
     * @param id of song type integer
     * @param song to update
     * @return ResponseEntity with corresponding status code
     */

    @PutMapping(path = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateSong(@PathVariable int id, @Valid @RequestBody Song song) {
        if (id != song.getId()) {
            return ResponseEntity.badRequest().build();
        }
        Song storedSong = songServiceDB.findById(id);
        if (storedSong == null) {
            return ResponseEntity.notFound().build();
        }
        storedSong.setArtist(song.getArtist());
        storedSong.setTitle(song.getTitle());
        storedSong.setLabel(song.getLabel());
        storedSong.setReleased(song.getReleased());
        storedSong.setImageUrl(song.getImageUrl());
        songServiceDB.save(storedSong);

        return ResponseEntity.noContent().build();
    }

    /**
     * The method deletes song by id in Parameter and returns corresponding status code,
     * if song wasn't deleted, the method returns the other status code
     * @param id of song to delete
     * @return ResponseEntity with corresponding status code.
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteSong(@PathVariable int id) {
        Song song = songServiceDB.findById(id);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }
        song.removeSongFromLists(song);
        songServiceDB.delete(id);

        return ResponseEntity.noContent().build();
    }

}
