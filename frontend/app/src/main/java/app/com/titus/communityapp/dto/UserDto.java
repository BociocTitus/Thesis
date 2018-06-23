package app.com.titus.communityapp.dto;

public class UserDto {
    String email;

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    String password;
}
