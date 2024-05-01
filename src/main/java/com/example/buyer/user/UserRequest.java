package com.example.buyer.user;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserRequest {

    @Data
    public static class LoginDTO {
        private String username;
        private String password;

    }

    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String name;
        private String phone;
        private LocalDate birth;
        private String address;
    }

}
