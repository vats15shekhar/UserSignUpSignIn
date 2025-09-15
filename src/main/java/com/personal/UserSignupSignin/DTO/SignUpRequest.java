package com.personal.UserSignupSignin.DTO;

public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    public String getUsername() {
        return firstName + " " + lastName;
    }

    public Object getPassword() {
        return password;
    }

    public record SignupRequest(String username, String password) {
    }
}
