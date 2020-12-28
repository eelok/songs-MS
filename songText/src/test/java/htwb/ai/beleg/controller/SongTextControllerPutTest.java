package htwb.ai.beleg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongTextControllerPutTest {
    private MockMvc mockMvc;

    @Mock
    private SongTextService songTextService;

    @InjectMocks
    private SongTextController songTextController;

    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(songTextController).build();
    }


    @Test
    void should_return_status_noContent_when_songText_was_updated() throws Exception {
        String id = "songTextId";
        SongTextDTO songTextDTO = new SongTextDTO("songTextId", 1, "userId", "text");
        SongTextDTO foundSongText = new SongTextDTO("songTextId", 1, "userId", "text");

        when(songTextService.findSongTextById(id)).thenReturn(Optional.of(foundSongText));

        MockHttpServletRequestBuilder request = put("/songText/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(songTextDTO));

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(songTextService, times(1)).updateSongText(any());
    }

    @Test
    void should_return_status_notFound_when_no_such_songText_in_DB() throws Exception {
        String id = "songTextId";
        SongTextDTO songTextDTO = new SongTextDTO("songTextId", 1, "userId", "text");

        when(songTextService.findSongTextById(id)).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder request = put("/songText/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(songTextDTO));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());

        verify(songTextService, never()).updateSongText(songTextDTO);
    }

}
