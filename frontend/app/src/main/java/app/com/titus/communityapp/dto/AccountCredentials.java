package app.com.titus.communityapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCredentials {
    private String username;
    private String password;
}
