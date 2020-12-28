package htwb.ai.beleg.service;

import htwb.ai.beleg.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthUserService {

    private RestTemplate restTemplate;
    private String userServiceURL = "http://localhost:8081";

    public AuthUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Returns User by token of null if user doesn't exists
     * @param token authentication token
     * @return User or null
     */
    public User getUserByToken(String token) {
        try {
            User userByToken = restTemplate.getForObject(userServiceURL + "/rest/userByToken/" + token, User.class);
            return userByToken;
        } catch (HttpClientErrorException ex) {
            return null;
        }
    }

}
