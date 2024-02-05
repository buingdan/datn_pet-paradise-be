package com.example.petparadisebe.auth;

import com.example.petparadisebe.Entities.Role;
import com.example.petparadisebe.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String access_token;
    private String refresh_token;
    private String user_name;
    private String email;
    private Set<Role> role;
    private User user;
}
