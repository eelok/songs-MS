package htwb.ai.beleg.repository;

import htwb.ai.beleg.model.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserTokenRepository {
    private Map<String, UserDTO> authenticatedTokens;

    public UserTokenRepository(Map<String, UserDTO> authenticatedTokens) {
        this.authenticatedTokens = authenticatedTokens;
    }

    public UserTokenRepository() {
        this(new HashMap<>());
    }

    /**
     * Stores user and token in key value pair in repository
     * @param token authentication token
     * @param userDTO user
     */
    public void add(String token, UserDTO userDTO) {
        authenticatedTokens.put(token, userDTO);
    }

    /**
     * Retrieves User by authentication token
     * @param token authentication token
     * @return User by corresponding token
     */
    public UserDTO getUser(String token) {
        return authenticatedTokens.get(token);
    }
}
