package htwb.ai.beleg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.beleg.entity.Song;
import htwb.ai.beleg.service.SongServiceDB;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongControllerPostTest {

    private MockMvc mockMvc;

    @Mock
    private SongServiceDB songServiceDB;

    @InjectMocks
    private SongController songController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(songController).build();
    }

    @Test
    void should_return_location_and_status_created() throws Exception {
        Song song = new Song();
        song.setId(42);
        song.setArtist("Richard Harris");
        song.setLabel("Dunhill Records");
        song.setTitle("MacArthur Park");
        song.setReleased(1968);

        String expectedLocation = "http://localhost/songs/42";
        when(songServiceDB.save(any())).thenReturn(song);

        MockHttpServletRequestBuilder request = post("/songs")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", expectedLocation));
    }

}
