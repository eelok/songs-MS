package htwb.ai.beleg.controller;

import htwb.ai.beleg.model.SongTextDTO;
import htwb.ai.beleg.service.SongTextService;
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
@RequestMapping("songText")
public class SongTextController {

    private SongTextService songTextService;

    public SongTextController(
            SongTextService songTextService) {
        this.songTextService = songTextService;
    }

    /**
     * The method posts SongTextDTO and returns ResponseEntity with Status code 201 and header,
     * in header location of SongTextDTO which was created
     *
     * @param songTextDTO of type SongTextDTO
     * @return ResponseEntity with Status code 201 and header
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createSongText(@Valid @RequestBody SongTextDTO songTextDTO) {
        SongTextDTO savedSongText = songTextService.createSongText(songTextDTO);
        String songTextId = savedSongText.getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(songTextId)
                .toUri();
        HttpHeaders header = new HttpHeaders();
        header.setLocation(location);
        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }

    /**
     * The Method returns all SongsTextDTO
     * @return ResponseEntity with List of SongsTextDTO and status code 200
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllSongTexts() {
        List<SongTextDTO> allSongTexts = songTextService.findAll();
        return new ResponseEntity<>(allSongTexts, HttpStatus.OK);
    }

    /**
     * The method returns ResponseEntity with corresponding SongText and status code 200 if id of SongText is valid
     * otherwise ResponseEntity with status code 404
     *
     * @param id of SongText in String
     * @return ResponseEntity with or without songTextDTO and corresponding status code
     */
    @GetMapping(
            path = "{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<?> getSingleSongTextById(@PathVariable String id) {
        return songTextService.findSongTextById(id)
                .map(songTextDTO -> new ResponseEntity<>(songTextDTO, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * The method updates songTextDTO if  update is successful, the method return corresponding status code.
     * If songTextDTO is invalid the method return  the other corresponding status code.
     * @param songTextDTO which supposed to be updated
     * @param id of songTextDTO
     * @return ResponseEntity with status code 204, or with 404
     */

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateSongText(
            @Valid @RequestBody SongTextDTO songTextDTO,
            @PathVariable String id
    ) {
        if (!id.equals(songTextDTO.getId())) {
            return ResponseEntity.notFound().build();
        }

        return songTextService.findSongTextById(id)
                .map(foundSongTextDTO -> {
                    foundSongTextDTO.setId(songTextDTO.getId());
                    foundSongTextDTO.setOwnerId(songTextDTO.getOwnerId());
                    foundSongTextDTO.setSongId(songTextDTO.getSongId());
                    foundSongTextDTO.setText(songTextDTO.getText());
                    songTextService.updateSongText(songTextDTO);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * The method deletes SongText, if SongText was deleted method returns ResponseEntity with status code 204,
     * otherwise ResponseEntity with status code 404
     * @param id of SongText to delete
     * @return ResponseEntity with corresponding status code
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteSongTextById(@PathVariable String id) {

        return songTextService.findSongTextById(id)
                .map(songTextDTO -> {
                    songTextService.deleteSongTextById(id);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}

