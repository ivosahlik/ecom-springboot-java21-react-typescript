package cz.ivosahlik.library.requestmodels;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
