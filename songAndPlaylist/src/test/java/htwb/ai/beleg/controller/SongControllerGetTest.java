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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongControllerGetTest {

    private MockMvc mockMvc;

    @InjectMocks
    private SongController songController;

    @Mock
    private SongServiceDB songServiceDB;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(songController).build();
    }

    @Test
    void should_return_songById_in_json() throws Exception {
        int id = 1;
        Song song = new Song();
        song.setId(1);
        song.setArtist("Richard Harris");
        song.setLabel("Dunhill Records");
        song.setTitle("MacArthur Park");
        song.setReleased(1968);

        when(songServiceDB.findById(id)).thenReturn(song);

        MockHttpServletRequestBuilder request = get("/songs/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(song)))
                .andReturn();
    }

    @Test
    void should_return_aLLSongs_in_json() throws Exception {
        Song song = new Song();
        song.setId(1);
        song.setArtist("Richard Harris");
        song.setLabel("Dunhill Records");
        song.setTitle("MacArthur Park");
        song.setReleased(1968);

        Song song2 = new Song();
        song2.setId(1);
        song2.setArtist("Richard Harris");
        song2.setLabel("Dunhill Records");
        song2.setTitle("MacArthur Park");
        song2.setReleased(1968);
        List<Song> songs = List.of(song, song2);

        when(songServiceDB.findAll()).thenReturn(songs);

        MockHttpServletRequestBuilder request = get("/songs")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(songs)))
                .andReturn();
    }


    @Test
    void should_return_status_notFound_when_songId_is_not_in_DB() throws Exception {
        int id = 100;

        when(songServiceDB.findById(id)).thenReturn(null);
        MockHttpServletRequestBuilder request = get("/songs/"+ id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

}
