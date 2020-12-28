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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class SongControllerPutTest {

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
    void should_return_status_noContent_when_song_was_updated() throws Exception {
        int id = 8;
        Song song = new Song();
        song.setId(8);
        song.setArtist("Richard Harris");
        song.setLabel("Dunhill Records");
        song.setTitle("MacArthur Park");
        song.setReleased(1968);

        when(songServiceDB.findById(id)).thenReturn(song);

        MockHttpServletRequestBuilder request = put("/songs/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(song));

        mockMvc.perform(request)
                .andExpect(status().isNoContent())
                .andReturn();

        verify(songServiceDB, times(1)).save(song);
    }

    @Test
    void should_return_status_notFound_when_no_such_song_in_DB() throws Exception {
        int id = 42;
        Song song = new Song();
        song.setId(42);
        song.setArtist("Richard Harris");
        song.setLabel("Dunhill Records");
        song.setTitle("MacArthur Park");
        song.setReleased(1968);

        when(songServiceDB.findById(id)).thenReturn(null);

        MockHttpServletRequestBuilder request = put("/songs/"+ id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song));

        mockMvc.perform(request)
                .andExpect(status().isNotFound());

        verify(songServiceDB, never()).save(song);
    }


    @Test
    void should_return_badRequest_when_ids_are_different() throws Exception {
        int id = 42;
        Song song = new Song();
        song.setId(8);
        song.setArtist("Richard Harris");
        song.setLabel("Dunhill Records");
        song.setTitle("MacArthur Park");
        song.setReleased(1968);

        MockHttpServletRequestBuilder request = put("/songs/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

        verify(songServiceDB, never()).save(song);
    }

}
