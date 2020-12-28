package htwb.ai.beleg.controller;

import htwb.ai.beleg.model.UserDTO;
import htwb.ai.beleg.service.AuthenticationService;
import htwb.ai.beleg.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("rest")
public class AuthController {

    private AuthenticationService authenticationService;
    private UserService userService;

    public AuthController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     *  This method returns status cod 200 and token if UserDTO is valid
     *  if UserDTO is invalid the method returns status cod Unauthorized
     *  The method returns status cod bad request if instead of UserDTO will be some other object
     *
     * @param authUserDTO a UserDTO your want to receive token for
     * @return  ResponseEntity with authentication token
     */
    @PostMapping(
            path = "auth",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserDTO authUserDTO) {
        try {
            String token = authenticationService.authenticate(authUserDTO);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE).body(token);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * The method returns status code ok and User in ResponseEntity if token is valid,
     * otherwise ResponseEntity with status code 404
     * @param token String
     * @return ResponseEntity with User and corresponding status code
     */
    @GetMapping(path = "userByToken/{token}")
    public ResponseEntity<?> getUser(@PathVariable String token) {
        UserDTO userDTO = authenticationService.getUser(token);
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(userDTO);
    }

}
