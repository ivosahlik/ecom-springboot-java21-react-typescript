package cz.ivosahlik.library.requestmodels;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String role; // Optional, defaults to "user" if not specified
}
