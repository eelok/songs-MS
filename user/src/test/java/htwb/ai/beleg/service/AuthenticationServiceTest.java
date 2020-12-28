package htwb.ai.beleg.service;

import htwb.ai.beleg.model.UserDTO;
import htwb.ai.beleg.repository.UserTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private AuthenticationService service;
    @Mock
    private UserService userService;
    @Mock
    private UserTokenRepository userTokenRepository;


    @BeforeEach
    void setUp() {
        service = new AuthenticationService(userService, userTokenRepository);
    }

    @Test
    void correctCredentialsShouldReturnToken() {
        UserDTO testUser = new UserDTO();
        testUser.setPassword("TestPassword");
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getPassword()).thenReturn("TestPassword");
        when(userService.findById(testUser.getUserId())).thenReturn(userDTO);

        String token = service.authenticate(testUser);
        System.out.println(token);

        verify(userTokenRepository).add(anyString(), any(UserDTO.class));
        assertNotNull(token);

    }

    @Test
    void nullShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.authenticate(null));
    }

    @Test
    void nullValuesShouldThrowException() {
        assertThrows(SecurityException.class, () -> service.authenticate(new UserDTO()));
    }

    @Test
    void wrongPasswordShouldThrowException() {
        UserDTO testUser = new UserDTO();
        testUser.setUserId("TestId");
        testUser.setPassword("Wrong");

        assertThrows(SecurityException.class, () -> service.authenticate(testUser));
    }

}