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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class SongTextControllerPostTest {

    private MockMvc mockMvc;

    @Mock
    private SongTextService songTextService;

    @InjectMocks
    private SongTextController songTextController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(songTextController).build();
    }

    @Test
    public void should_create_new_songText_and_return_location() throws Exception {
        SongTextDTO songTextDTO = new SongTextDTO("songTextId", 1, "ownerId", "text");
        when(songTextService.createSongText(any())).thenReturn(songTextDTO);

        MockHttpServletRequestBuilder request = post("/songText")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(songTextDTO));

        this.mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header()
                        .string("Location", "http://localhost/songText/" + songTextDTO.getId()));
    }

}
