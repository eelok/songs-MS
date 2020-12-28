package htwb.ai.beleg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.beleg.entity.Song;
import htwb.ai.beleg.entity.SongList;
import htwb.ai.beleg.service.SongListService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongListControllerGetTest {

    private MockMvc mockMvc;
    @Mock
    private SongListService songListService;

    @InjectMocks
    private SongListController songListController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(songListController).build();
    }

    @Test
    public void should_return_songList_in_json_and_status_ok() throws Exception {
        String authorizedUserId = "userId";
        int id = 1;
        SongList songList = new SongList();
        songList.setId(1);
        songList.setOwnerId("userId");
        songList.setName("songList");
        songList.setSongs(Collections.singletonList(new Song()));

        when(songListService.getBySongListId(id)).thenReturn(songList);
        MockHttpServletRequestBuilder request = get("/songLists/1")
                .header("authorizedUserId", authorizedUserId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(songList)))
                .andReturn();
    }

    @Test
    void should_return_status_notFound_when_no_such_songList_in_DB() throws Exception {
        String authorizedUserId = "userId";

        when(songListService.getBySongListId(101)).thenReturn(null);
        MockHttpServletRequestBuilder request = get("/songLists/101")
                .header("authorizedUserId", authorizedUserId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_status_forbidden_for_non_public_songList() throws Exception {
        String authorizedUserId = "userId";
        SongList songList = new SongList();
        songList.setId(3);
        songList.setOwnerId("user");
        songList.setName("songList");
        songList.setPrivate(true);
        songList.setSongs(Collections.singletonList(new Song()));

        when(songListService.getBySongListId(3)).thenReturn(songList);
        MockHttpServletRequestBuilder request = get("/songLists/3")
                .header("authorizedUserId", authorizedUserId)
                .accept(MediaType.APPLICATION_XML);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_return_public_songList_when_user_from_token_and_owner_of_songList_are_different() throws Exception {
        String authorizedUserId = "userId";
        SongList songList = new SongList();
        songList.setId(3);
        songList.setOwnerId("user");
        songList.setName("songList");
        songList.setPrivate(false);
        songList.setSongs(Collections.singletonList(new Song()));

        when(songListService.getBySongListId(3)).thenReturn(songList);
        MockHttpServletRequestBuilder request = get("/songLists/3")
                .header("authorizedUserId", authorizedUserId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(songList)))
                .andReturn();

    }

    @Test
    public void should_return_all_songLists_in_json_and_status_ok() throws Exception {
        String authorizedUserId = "userId";
        String userIdParam = "userId";

        SongList songList = new SongList();
        songList.setId(3);
        songList.setOwnerId("userId");
        songList.setName("songList");
        songList.setPrivate(false);
        List<SongList> expected = List.of(songList);

        when(songListService.getAllSongLists(userIdParam)).thenReturn(expected);
        MockHttpServletRequestBuilder request = get("/songLists?userId=" + userIdParam)
                .header("authorizedUserId", authorizedUserId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)))
                .andReturn();

    }

    @Test
    public void should_get_all_public_songLists_for_different_user() throws Exception {
        String authorizedUserId = "userId";
        String userIdParam = "userId";

        SongList songList = new SongList();
        songList.setId(3);
        songList.setOwnerId(userIdParam);
        songList.setName("songList");
        songList.setPrivate(false);
        SongList songList1 = new SongList();
        songList1.setId(2);
        songList1.setOwnerId(userIdParam);
        songList1.setName("songList");
        songList1.setPrivate(false);
        List<SongList> expected = List.of(songList, songList1);

        when(songListService.getAllSongLists(userIdParam)).thenReturn(expected);
        MockHttpServletRequestBuilder request = get("/songLists?userId=" + userIdParam)
                .header("authorizedUserId", authorizedUserId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)))
                .andReturn();
    }

}
