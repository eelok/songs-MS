package htwb.ai.beleg.controller;

import htwb.ai.beleg.entity.Song;
import htwb.ai.beleg.service.SongServiceDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongControllerDeleteTest {

    private MockMvc mockMvc;

    @Mock
    private SongServiceDB songServiceDB;

    @InjectMocks
    private SongController songController;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(songController).build();
    }

    @Test
    void should_return_status_noContent_when_song_was_deleted() throws Exception {
        Song savedSong = new Song();
        savedSong.setTitle("Saved");
        savedSong.setId(42);
        when(songServiceDB.findById(42)).thenReturn(savedSong);

        MockHttpServletRequestBuilder request = delete("/songs/42");

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(songServiceDB, times(1)).delete(42);
    }

    @Test
    void should_return_status_notFound_when_songId_is_not_in_DB() throws Exception {
        Song savedSong = new Song();
        savedSong.setTitle("Saved");
        savedSong.setId(42);
        when(songServiceDB.findById(42)).thenReturn(null);

        MockHttpServletRequestBuilder request = delete("/songs/42");

        mockMvc.perform(request)
                .andExpect(status().isNotFound());

        verify(songServiceDB, never()).delete(42);
    }

}
