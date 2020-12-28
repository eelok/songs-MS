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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class SongTextControllerGetTest {

    MockMvc mockMvc;
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
    void should_return_allSongTexts() throws Exception {
        List<SongTextDTO> expected = List.of(
                new SongTextDTO("one", 1, "ownerId1", "text"),
                new SongTextDTO("two", 2, "ownerId2", "text")
        );
        when(songTextService.findAll()).thenReturn(expected);

        MockHttpServletRequestBuilder request = get("/songText")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_ATOM_XML_VALUE);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void should_return_songText() throws Exception {
        SongTextDTO expected = new SongTextDTO("one", 1, "ownerId1", "text");

        when(songTextService.findSongTextById("id")).thenReturn(Optional.of(expected));

        MockHttpServletRequestBuilder request = get("/songText/id")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_ATOM_XML_VALUE);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void should_return_status_notFound() throws Exception {
        when(songTextService.findSongTextById("id")).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = get("/songText/id")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_ATOM_XML_VALUE);

        mockMvc.perform(request).andExpect(status().isNotFound());
    }
}
