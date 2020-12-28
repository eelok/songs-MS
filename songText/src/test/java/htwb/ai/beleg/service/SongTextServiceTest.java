package htwb.ai.beleg.service;

import htwb.ai.beleg.entity.SongText;
import htwb.ai.beleg.model.SongTextDTO;
import htwb.ai.beleg.repository.SongTextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SongTextServiceTest {

    private SongTextService songTextService;
    @Mock
    private SongTextRepository songTextRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        this.songTextService = new SongTextService(songTextRepository, modelMapper);
    }

    @Test
    void should_return_SongTextDTO_after_save_it_in_DB() {
        SongText songText = new SongText("songTextId", 2, "ownerId", "test text");
        SongTextDTO expectedSongTextDTO = new SongTextDTO("songTextId", 2, "ownerId", "test text");
        when(modelMapper.map(expectedSongTextDTO, SongText.class)).thenReturn(songText);
        when(songTextRepository.save(songText)).thenReturn(songText);
        when(modelMapper.map(songText, SongTextDTO.class)).thenReturn(expectedSongTextDTO);

        SongTextDTO songTextDTO = songTextService.createSongText(expectedSongTextDTO);

        assertThat(songTextDTO).isEqualTo(expectedSongTextDTO);
    }

    @Test
    void should_call_method_save(){
        SongTextDTO songTextDTO = new SongTextDTO("one", 1, "owner1", "test");
        SongText songText = new SongText("one", 1, "owner1", "test");
        when(modelMapper.map(songTextDTO, SongText.class)).thenReturn(songText);

        songTextService.updateSongText(songTextDTO);

        verify(songTextRepository).save(songText);
    }

    @Test
    void should_return_list_of_all_songTextDTO() {
        SongText st1 = new SongText("one", 1, "owner1", "text1");
        SongText st2 = new SongText("two", 2, "owner2", "text2");
        List<SongText> allSongTexts = List.of(st1, st2);
        SongTextDTO songTextDTO1 = new SongTextDTO("one", 1, "owner1", "text1");
        SongTextDTO songTextDTO2 = new SongTextDTO("two", 2, "owner2", "text2");

        List<SongTextDTO> expectedSongTextDTOs = List.of(songTextDTO1, songTextDTO2);
        when(songTextRepository.findAll()).thenReturn(allSongTexts);

        when(modelMapper.map(st1, SongTextDTO.class)).thenReturn(songTextDTO1);
        when(modelMapper.map(st2, SongTextDTO.class)).thenReturn(songTextDTO2);

        List<SongTextDTO> songTextDTOs = songTextService.findAll();

        assertThat(songTextDTOs).isEqualTo(expectedSongTextDTOs);
    }

    @Test
    void should_return_SongTextDTO() {
        SongText songText = new SongText("one", 1, "owner1", "text");
        SongTextDTO expectedSongTextDTO = new SongTextDTO("one", 1, "owner1", "text");
        when(songTextRepository.findById("id")).thenReturn(Optional.of(songText));
        when(modelMapper.map(songText, SongTextDTO.class)).thenReturn(expectedSongTextDTO);

        Optional<SongTextDTO> songTextDTO = songTextService.findSongTextById("id");

        assertThat(songTextDTO).isEqualTo(Optional.of(expectedSongTextDTO));
    }

    @Test
    void should_return_empty_SongTextDTO(){
        when(songTextRepository.findById("id")).thenReturn(Optional.empty());

        Optional<SongTextDTO> songTextDTO = songTextService.findSongTextById("id");
        assertThat(songTextDTO).isEmpty();
    }

    @Test
    void should_call_deleteById(){
        songTextService.deleteSongTextById("id");

        verify(songTextRepository).deleteById("id");
    }

}

