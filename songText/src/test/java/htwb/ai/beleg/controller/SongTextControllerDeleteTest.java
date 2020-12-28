package htwb.ai.beleg.controller;

import htwb.ai.beleg.model.SongTextDTO;
import htwb.ai.beleg.service.SongTextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongTextControllerDeleteTest {

    MockMvc mockMvc;

    @Mock
    private SongTextService songTextService;

    @InjectMocks
    private SongTextController songTextController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(songTextController).build();
    }

    @Test
    void should_return_status_noContent_when_songText_was_deleted() throws Exception {
        String id = "songTextId";
        SongTextDTO songTextDTO = new SongTextDTO("songTextId", 1, "userId", "text");
        when(songTextService.findSongTextById(id)).thenReturn(Optional.of(songTextDTO));

        MockHttpServletRequestBuilder request = delete("/songText/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(songTextService, times(1)).deleteSongTextById(id);
    }

    @Test
    void should_return_status_notFound_when_no_songText_in_DB() throws Exception {
        String id = "songTextId";
        when(songTextService.findSongTextById(id)).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = delete("/songText/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());

        verify(songTextService, never()).deleteSongTextById(id);
    }

}
