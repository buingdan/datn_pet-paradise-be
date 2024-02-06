package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Role;
import com.example.petparadisebe.Entities.User;
import com.example.petparadisebe.Repositories.RoleCustomRepo;
import com.example.petparadisebe.Repositories.UserRepository;
import com.example.petparadisebe.auth.AuthenticationRequest;
import com.example.petparadisebe.auth.AuthenticationResponse;
import com.example.petparadisebe.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    //file nơi có hàm tạo login va register
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleCustomRepo roleCustomRepo;

    private final JwtService jwtService;
    private final UserService userService;

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        try {
            // Kiểm tra xem người dùng với email đã tồn tại hay chưa
            Optional<User> existingUserOptional = userRepository.findByEmail(registerRequest.getEmail());
            if (existingUserOptional.isPresent()) {
                throw new IllegalArgumentException("Người dùng với email " + registerRequest.getEmail() + " đã tồn tại");
            }

            // Lưu người dùng mới vào cơ sở dữ liệu
            userService.saveUser(new User(
                            registerRequest.getUsername(),
                            registerRequest.getFullName(),
                            registerRequest.getEmail(),
                            registerRequest.getPassword(),
//                    registerRequest.getImage(),
                            registerRequest.getAddress(),
                            registerRequest.getPhoneNumber(),
                            new HashSet<>())
            );

            // Thêm quyền mặc định cho người dùng (ví dụ: ROLE_USER)
            userService.addToUser(registerRequest.getEmail(), "ROLE_USER");

            // Lấy thông tin người dùng sau khi đã lưu vào cơ sở dữ liệu
            User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();

            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            // Trả về lỗi BAD_REQUEST nếu có lỗi kiểm tra tồn tại người dùng
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Trả về lỗi INTERNAL_SERVER_ERROR nếu có lỗi xử lý khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        try {
            User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
            List<Role> role = null;
            if (user != null) {
                role = roleCustomRepo.getRole(user);
            }
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Set<Role> set=new HashSet<>();
            role.stream().forEach(c -> {
                set.add(new Role(c.getName()));
                authorities.add(new SimpleGrantedAuthority(c.getName()));
            });
            user.setRoles(set);
            set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
            var jwtAccessToken = jwtService.generateToken(user, authorities);
            var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);
            return ResponseEntity.ok(AuthenticationResponse.builder().access_token(jwtAccessToken).refresh_token(jwtRefreshToken).email(user.getEmail()).user_name(user.getUsername()).role(user.getRoles()).user(user).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Thông tin không hợp lệ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi nào đó xảy ra");
        }
    }
}
