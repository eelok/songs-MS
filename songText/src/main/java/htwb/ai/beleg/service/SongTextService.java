package htwb.ai.beleg.service;

import htwb.ai.beleg.entity.SongText;
import htwb.ai.beleg.model.SongTextDTO;
import htwb.ai.beleg.repository.SongTextRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SongTextService {

    private SongTextRepository songTextRepository;
    private ModelMapper modelMapper;

    public SongTextService(SongTextRepository songTextRepository, ModelMapper modelMapper) {
        this.songTextRepository = songTextRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a song text
     * @param songTextDTO
     * @return SongTextDTO
     */
    public SongTextDTO createSongText(SongTextDTO songTextDTO) {
        SongText songText = modelMapper.map(songTextDTO, SongText.class);
        songText.setId(UUID.randomUUID().toString());
        SongText savedSongText = songTextRepository.save(songText);
        return modelMapper.map(savedSongText, SongTextDTO.class);
    }

    /**
     * Updates a song text
     * @param songTextDTO
     */
    public void updateSongText(SongTextDTO songTextDTO){
        songTextRepository.save(modelMapper.map(songTextDTO, SongText.class));
    }


    /**
     * Finds all songs text from DB
     * @return List of SongTextDTO
     */
    public List<SongTextDTO> findAll() {
        List<SongText> allSongTexts = songTextRepository.findAll();

        return allSongTexts.stream()
                .map(songText -> modelMapper.map(songText, SongTextDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Finds a song text by id
     * @param songTextId
     * @return Optional of SongTextDTO
     */
    public Optional<SongTextDTO> findSongTextById(String songTextId) {
        return songTextRepository.findById(songTextId)
                .map(songText -> modelMapper.map(songText, SongTextDTO.class));
    }

    /**
     * Deletes song text by id
     * @param songTextId
     */
    public void deleteSongTextById(String songTextId) {
        songTextRepository.deleteById(songTextId);
    }
}
