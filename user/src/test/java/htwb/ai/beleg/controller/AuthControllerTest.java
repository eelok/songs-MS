package htwb.ai.beleg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.beleg.model.UserDTO;
import htwb.ai.beleg.service.AuthenticationService;
import htwb.ai.beleg.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void should_return_token() throws Exception {
        UserDTO authUserDTO = new UserDTO("userId", "userPass");
        String testToken = "testToken";
        when(authenticationService.authenticate(any())).thenReturn(testToken);

        MockHttpServletRequestBuilder request = post("/rest/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authUserDTO));

        mockMvc.perform(
                request)
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string(testToken));
    }

    @Test
    void should_return_status_Unauthorized() throws Exception {
        UserDTO authUserDTO = new UserDTO("userId", "userPass");
        when(authenticationService.authenticate(any())).thenThrow(SecurityException.class);

        MockHttpServletRequestBuilder request = post("/rest/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authUserDTO));

        mockMvc.perform(
                request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_return_status_BadRequest() throws Exception {
        UserDTO authUserDTO = new UserDTO("userId", "userPass");
        when(authenticationService.authenticate(any())).thenThrow(IllegalArgumentException.class);

        MockHttpServletRequestBuilder request = post("/rest/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authUserDTO));

        mockMvc.perform(
                request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_user_json() throws Exception {
        UserDTO user = new UserDTO("donnaId", "pass");
        when(userService.findById("donnaId")).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/rest/user/donnaId");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void should_return_status_notFound_when_unknown_userId() throws Exception {
        when(userService.findById("unknownUserId")).thenReturn(null);

        MockHttpServletRequestBuilder request = get("/rest/user/unknownUserId");

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_user_json_from_token() throws Exception {
        UserDTO user = new UserDTO("testId", "testPass");
        when(authenticationService.getUser("token")).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/rest/userByToken/token");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void should_return_status_notFound() throws Exception {
        when(authenticationService.getUser("token")).thenReturn(null);

        MockHttpServletRequestBuilder request = get("/rest/userByToken/token");

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

}
