package htwb.ai.beleg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.beleg.entity.Song;
import htwb.ai.beleg.entity.SongList;
import htwb.ai.beleg.service.SongListService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongListControllerPostTest {

    private MockMvc mockMvc;
    @Mock
    private SongListService songListService;
    @Mock
    private SongServiceDB songServiceDB;

    @InjectMocks
    private SongListController songListController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(songListController).build();
    }


    @Test
    void should_return_location_and_status_created_when_songList_was_created() throws Exception {
        String authorizedUserId = "userId";

        Song song = new Song();
        song.setId(2);
        song.setTitle("title");

        SongList songList = new SongList();
        songList.setId(1);
        songList.setName("name");
        songList.setOwnerId(authorizedUserId);
        songList.setSongs(List.of(song));

        String expectedLocation = "http://localhost/songLists/" + songList.getId();

        when(songListService.save(any())).thenReturn(songList);
        when(songServiceDB.findById(2)).thenReturn(song);
        MockHttpServletRequestBuilder request = post("/songLists")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorizedUserId", authorizedUserId)
                .content(objectMapper.writeValueAsString(songList));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", expectedLocation));
    }


    @Test
    public void should_return_bad_request_when_no_songs_in_songList() throws Exception {
        SongList songListWithoutSongs = new SongList();
        songListWithoutSongs.setName("MaximesPrivate");
        songListWithoutSongs.setPrivate(true);

        String authorizedUserId = "userId";

        MockHttpServletRequestBuilder request = post("/songLists")
                .header("authorizedUserId", authorizedUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(songListWithoutSongs));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_bad_request_when_songlist_contains_not_existing_song() throws Exception {
        Song song1 = new Song();
        song1.setId(1);
        song1.setArtist("Richard Harris");
        song1.setLabel("Dunhill Records");
        song1.setTitle("MacArthur Park");
        song1.setReleased(1968);
        Song song = new Song();

        song.setId(500);
        song.setArtist("Richard Harris");
        song.setLabel("Dunhill Records");
        song.setTitle("MacArthur Park");
        song.setReleased(1968);

        SongList playlist = new SongList();
        playlist.setName("MaximesPrivate");
        playlist.setPrivate(true);
        playlist.setSongs(List.of(song, song1));

        String authorizedUserId = "userId";

        MockHttpServletRequestBuilder request = post("/songLists")
                .header("authorizedUserId", authorizedUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playlist));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_bad_request_with_empty_payload() throws Exception {
        String authorizedUserId = "userId";

        MockHttpServletRequestBuilder request = post("/songLists")
                .header("authorizedUserId", authorizedUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_bad_request_with_invalid_payload() throws Exception {
        String authorizedUserId = "userId";
        MockHttpServletRequestBuilder request = post("/songLists")
                .header("authorizedUserId", authorizedUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"argument\":\"wrong\"}");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

}
