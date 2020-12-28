package htwb.ai.beleg.controller;

import htwb.ai.beleg.entity.SongList;
import htwb.ai.beleg.service.SongListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongListControllerDeleteTest {

    private MockMvc mockMvc;
    @Mock
    private SongListService songListService;

    @InjectMocks
    private SongListController songListController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(songListController).build();
    }


    @Test
    void should_delete_songList_and_return_status_noContent() throws Exception {
        String authorizedUserId = "userId";
        int id = 1;
        SongList songList = mock(SongList.class);
        when(songList.getOwnerId()).thenReturn("userId");

        when(songListService.getBySongListId(id)).thenReturn(songList);
        MockHttpServletRequestBuilder request = delete("/songLists/" + id)
                .header("authorizedUserId", authorizedUserId);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(songListService, times(1)).deleteSongList(id);
    }

    @Test
    void should_return_status_forbidden_when_delete_not_his_own_songList() throws Exception {
        int id = 1;
        String authorizedUserId = "userId";
        SongList songList = mock(SongList.class);
        when(songList.getOwnerId()).thenReturn("alex");

        when(songListService.getBySongListId(id)).thenReturn(songList);
        MockHttpServletRequestBuilder request = delete("/songLists/" + id)
                .header("authorizedUserId", authorizedUserId);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        verify(songListService, never()).deleteSongList(id);
    }

    @Test
    void should_return_notFound_when_songList_not_in_DB() throws Exception {
        String authorizedUserId = "userId";
        int id = 333;
        when(songListService.getBySongListId(id)).thenReturn(null);
        MockHttpServletRequestBuilder request = delete("/songLists/" + id)
                .header("authorizedUserId", authorizedUserId);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

}
