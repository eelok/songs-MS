package htwb.ai.beleg.service;


import htwb.ai.beleg.entity.User;
import htwb.ai.beleg.model.UserDTO;
import htwb.ai.beleg.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Finds User by Id
     * @param id user id
     * @return User if user exists and null if user doesn't exists
     */
    public UserDTO findById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> modelMapper.map(u, UserDTO.class)).orElse(null);
    }
}
