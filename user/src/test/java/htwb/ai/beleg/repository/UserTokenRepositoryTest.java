package htwb.ai.beleg.repository;

import htwb.ai.beleg.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserTokenRepositoryTest {

    private UserTokenRepository userTokenRepository;

    private Map<String, UserDTO> authenticatedTokens;

    @BeforeEach
    public void setup(){
        this.authenticatedTokens = mock(Map.class);
        this.userTokenRepository = new UserTokenRepository(authenticatedTokens);
    }

    @Test
    void should_add_token_and_user(){
        String token = "token";
        UserDTO userDTO = new UserDTO("userId", "pass");

        userTokenRepository.add(token, userDTO);

        verify(authenticatedTokens, times(1)).put(anyString(), any());
    }

    @Test
    void should_return_userDTO(){
        String token = "token";
        UserDTO expected = new UserDTO("userId", "pass");
        when(authenticatedTokens.get(token)).thenReturn(expected);

        UserDTO user = userTokenRepository.getUser(token);
        assertThat(user).isEqualTo(expected);
    }
}
