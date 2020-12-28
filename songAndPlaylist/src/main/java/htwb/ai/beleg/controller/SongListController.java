package htwb.ai.beleg.controller;

import htwb.ai.beleg.entity.Song;
import htwb.ai.beleg.entity.SongList;
import htwb.ai.beleg.service.SongListService;
import htwb.ai.beleg.service.SongServiceDB;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("songLists")
public class SongListController {

    private SongListService songListService;
    private SongServiceDB songServiceDB;

    public SongListController(
            SongListService songListService,
            SongServiceDB songServiceDB
    ) {
        this.songListService = songListService;
        this.songServiceDB = songServiceDB;
    }

    /**
     * Returns all songsList if userIdParam  matches with authorizedUserId
     * Returns only public songsList if authorizedUserId doesn't matches with authorizedUserId
     * @param authorizedUserId id of authorized user type String
     * @param userIdParam User Id
     * @return ResponseEntity with List of songsList and corresponding status code
     */

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllSongLists(
        @RequestHeader String authorizedUserId,
        @RequestParam(name = "userId") String userIdParam
    ) {
        if (sameAsAuthorized(userIdParam, authorizedUserId)) {
            List<SongList> allSongLists = songListService.getAllSongLists(userIdParam);
            return new ResponseEntity<>(allSongLists, HttpStatus.OK);
        }
        List<SongList> songListsPublic = songListService.getPublicSongLists(userIdParam);

        return new ResponseEntity<>(songListsPublic, HttpStatus.OK);
    }

    /**
     * The method returns songList by id,
     * if id of songList is valid and  songList is public method return songList and status code 200,
     * if owner of songList matches with authorizedUserId method return songList and status code 200,
     * if owner of songList and authorizedUserId are different and songList is private method returns status code 403
     *
     * @param authorizedUserId id of authorized user type String
     * @param id of LongList
     * @return ResponseEntity ResponseEntity with songList and corresponding status code,
     * or ResponseEntity with out songList but with corresponding status code
     */
    @GetMapping(
            path = "{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> getSongList(@RequestHeader String authorizedUserId, @PathVariable int id) {
        SongList songList = songListService.getBySongListId(id);
        if (songList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String ownerId = songList.getOwnerId();
        if (ownerId.equals(authorizedUserId)) {
            return new ResponseEntity<>(songList, HttpStatus.OK);
        }
        if (songList.isPrivate()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return new ResponseEntity<>(songList, HttpStatus.OK);
    }

    /**
     * The method creates new SongList if SongList is valid and returns status code and header with location
     * of created songList.
     * If SongList is invalid, method returns corresponding status code.
     *
     * @param authorizedUserId  of authorized user type String
     * @param songList which supposed to be created
     * @return ResponseEntity with header and corresponding status code
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSongList(@RequestHeader String authorizedUserId, @RequestBody SongList songList) {
        if (ObjectUtils.isEmpty(songList.getSongs())) {
            return ResponseEntity.badRequest().build();
        }
        songList.setId(0);
        songList.setOwnerId(authorizedUserId);
        addSongsToList(songList);

        SongList savedSongList = songListService.save(songList);
        int id = savedSongList.getId();
        HttpHeaders header = new HttpHeaders();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        header.setLocation(location);
        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }


    /**
     * The method deletes SongList by id in Parameter.
     * If id is valid and SongList belongs to authorizedUserId SongList will be deleted and method returns status code 204.
     * if id of SongList is invalid method returns status code 404,
     * if SongList belongs not to authorizedUserId method returns status code 403
     *
     * @param authorizedUserId of authorized user type String
     * @param id of SongList for delete
     * @return ResponseEntity with corresponding status code
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteSongList(
            @RequestHeader String authorizedUserId,
            @PathVariable int id
    ) {
        SongList songList = songListService.getBySongListId(id);
        if (songList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (sameAsAuthorized(songList.getOwnerId(), authorizedUserId)) {
            songListService.deleteSongList(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * The method updates SongList and returns status code 204 if id in Parameter and id of songListForUpdate coincide
     * and if authorizedUserId matches with owner of SongList.
     * if id of SongList and if of songList in Parameter differ the method returns status code 400,
     * If authorizedUserId doesn't match with owner of SongList the method returns status code 403.
     *
     * @param id of songList
     * @param songListForUpdate SongList with new data
     * @param authorizedUserId id of authorized User
     * @return ResponseEntity with corresponding status code
     */
    @PutMapping(
            path = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateSongList(
            @PathVariable int id,
            @RequestBody SongList songListForUpdate,
            @RequestHeader String authorizedUserId
    ) {
        if (songListForUpdate == null) {
            return ResponseEntity.badRequest().build();
        }
        if (id != songListForUpdate.getId()) {
            return ResponseEntity.badRequest().build();
        }
        SongList storedSongList = songListService.getBySongListId(id);
        if (storedSongList == null) {
            return ResponseEntity.notFound().build();
        }
        String songListOwnerId = songListForUpdate.getOwnerId();
        if (sameAsAuthorized(songListOwnerId, authorizedUserId)) {
            storedSongList.setName(songListForUpdate.getName());
            storedSongList.setPrivate(songListForUpdate.isPrivate());
            songListService.save(storedSongList);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    private boolean sameAsAuthorized(String userId, String userIdFromToken) {
        return userId.trim().equals(userIdFromToken);
    }

    private void addSongsToList(SongList songList) {
        List<Integer> ids = songList.getSongs().stream().map(Song::getId).collect(Collectors.toList());
        List<Song> songs = new LinkedList<>();

        ids.forEach(id -> {
            Song song = songServiceDB.findById(id);
            if (song == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            song.addSongList(songList);
            songs.add(song);
        });

        songList.setSongs(songs);
    }

}
