package htwb.ai.beleg.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserDTO {

    @NotNull
    @NotBlank
    private String userId;

    @NotNull
    @NotBlank
    private String password;

    public UserDTO() {
    }

    public UserDTO(@NotNull @NotBlank String userId, @NotNull @NotBlank String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
