package htwb.ai.beleg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongListControllerPutTest {

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
    void should_return_status_badRequest_when_id_in_path_and_in_songList_for_update_are_different() throws Exception {
        String authorizedUserId = "userId";
        int id = 3;
        SongList songListForUpdate = new SongList();
        songListForUpdate.setId(4);
        songListForUpdate.setPrivate(false);
        songListForUpdate.setName("name");
        songListForUpdate.setOwnerId("userId");
        MockHttpServletRequestBuilder request = put("/songLists/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(songListForUpdate))
                .header("authorizedUserId", authorizedUserId);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(songListService);
    }

    @Test
    void should_return_status_notFound_when_no_such_songList_in_DB() throws Exception {
        String authorizedUserId = "userId";
        int id = 4;
        SongList songListForUpdate = new SongList();
        songListForUpdate.setId(4);
        when(songListService.getBySongListId(id)).thenReturn(null);
        MockHttpServletRequestBuilder request = put("/songLists/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(songListForUpdate))
                .header("authorizedUserId", authorizedUserId);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());

        verifyNoMoreInteractions(songListService);
    }

    @Test
    void should_return_status_forbidden_when_owner_of_songList_and_authorized_user_are_different() throws Exception {
        String authorizedUserId = "userId";
        int id = 4;
        SongList songListForUpdate = new SongList();
        songListForUpdate.setId(4);
        songListForUpdate.setOwnerId("ownerId");

        SongList storedSongList = new SongList();
        storedSongList.setOwnerId("ownerId");

        when(songListService.getBySongListId(id)).thenReturn(storedSongList);
        MockHttpServletRequestBuilder request = put("/songLists/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(songListForUpdate))
                .header("authorizedUserId", authorizedUserId);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        verifyNoMoreInteractions(songListService);
    }

    @Test
    void should_return_status_noContent_when_SongList_was_updated() throws Exception {
        String authorizedUserId = "ownerId";
        int id = 4;
        SongList songListForUpdate = new SongList();
        songListForUpdate.setId(4);
        songListForUpdate.setPrivate(false);
        songListForUpdate.setName("name");
        songListForUpdate.setOwnerId("ownerId");

        SongList storedSongList = new SongList();
        storedSongList.setOwnerId("ownerId");

        when(songListService.getBySongListId(id)).thenReturn(storedSongList);
        MockHttpServletRequestBuilder request = put("/songLists/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(songListForUpdate))
                .header("authorizedUserId", authorizedUserId);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(songListService, times(1)).save(storedSongList);
    }

}
