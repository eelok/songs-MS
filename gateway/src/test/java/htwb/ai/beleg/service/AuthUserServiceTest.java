package htwb.ai.beleg.service;

import htwb.ai.beleg.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthUserServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private String userServiceUrl = "http://localhost:8081";

    private AuthUserService authUserService;


    @BeforeEach
    public void setup(){
        authUserService = new AuthUserService(restTemplate);
    }

    @Test
    void should_return_user(){
        String token = "token";
        User expected = new User("userId");
        when(restTemplate.getForObject(userServiceUrl + "/rest/userByToken/" + token, User.class))
                .thenReturn(expected);

        User user = authUserService.getUserByToken(token);

        assertThat(user).isEqualTo(expected);
    }

    @Test
    void should_return_null(){
        String token = "token";
        when(restTemplate.getForObject(userServiceUrl + "/rest/userByToken/" + token, User.class))
                .thenThrow(HttpClientErrorException.class);

        User user = authUserService.getUserByToken(token);

        assertThat(user).isNull();
    }
}
