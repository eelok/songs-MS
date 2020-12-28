package htwb.ai.beleg.service;


import htwb.ai.beleg.model.UserDTO;
import htwb.ai.beleg.repository.UserTokenRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final int TOKEN_LENGTH = 16;
    private UserService userService;
    private UserTokenRepository userTokenRepository;

    public AuthenticationService(UserService userService, UserTokenRepository userTokenRepository) {
        this.userService = userService;
        this.userTokenRepository = userTokenRepository;
    }

    /**
     * Generates authentication token stores is and user in repository and returns token
     * @param userDTO user
     * @return token
     */
    public String authenticate(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("User is null");
        }
        UserDTO foundUser = userService.findById(userDTO.getUserId());

        if (foundUser == null) {
            throw new SecurityException("User not found");
        }

        if (!userDTO.getPassword().equals(foundUser.getPassword())) {
            throw new SecurityException("Password is wrong");
        }
        String token = RandomString.make(TOKEN_LENGTH);
        userTokenRepository.add(token, userDTO);
        return token;
    }

    /**
     * Finds User for by token
     * @param token authentication token
     * @return User for corresponding token
     */
    public UserDTO getUser(String token) {
        return userTokenRepository.getUser(token);
    }

}
