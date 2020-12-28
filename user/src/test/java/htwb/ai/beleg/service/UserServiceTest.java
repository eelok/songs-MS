package htwb.ai.beleg.service;

import htwb.ai.beleg.entity.User;
import htwb.ai.beleg.model.UserDTO;
import htwb.ai.beleg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    private UserService userService;

    @BeforeEach
    public void setup(){
        this.userService = new UserService(userRepository, modelMapper);
    }

    @Test
    void should_return_userDTO_when_id_provided(){
        User user = new User();
        user.setUserId("userId");
        user.setPassword("pass");

        UserDTO expected = mock(UserDTO.class);

        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(expected);

        UserDTO userDTO = userService.findById("userId");

        assertThat(userDTO).isEqualTo(expected);
    }
}
