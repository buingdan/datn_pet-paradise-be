package com.example.petparadisebe.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Repositories.RoleCustomRepo;
import com.example.petparadisebe.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {
    //file nơi có hàm tạo Token và accessToken
    private static final String Secret_key = "123";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleCustomRepo roleCustomRepo;

    public String generateToken(User user, Collection<SimpleGrantedAuthority> authorities){
        try {
            Algorithm algorithm = Algorithm.HMAC256(Secret_key.getBytes());
            return JWT.create()
                    .withSubject(user.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 50 * 60 * 1000))
                    .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);
        }catch (Exception e){
            System.err.println("Không thể tạo token, lỗi: "+e.getMessage());
            return null;
        }
    }
    public String generateRefreshToken(User user, Collection<SimpleGrantedAuthority> authorities){
        try {
        Algorithm algorithm = Algorithm.HMAC256(Secret_key.getBytes());
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 70 * 60 * 1000))
                .sign(algorithm);
        }catch (Exception e){
            System.err.println("Không thể tạo refresh_token, lỗi: "+e.getMessage());
            return null;
        }
    }
}
