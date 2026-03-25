package com.clanhistory.controller.admin;

import com.clanhistory.dto.LoginRequest;
import com.clanhistory.dto.LoginResponse;
import com.clanhistory.entity.SysUser;
import com.clanhistory.service.AuthService;
import com.clanhistory.service.SysUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SysUserService userService;
    private final PasswordEncoder passwordEncoder;

    // 测试端点 - 直接验证密码绕过Spring Security
    @PostMapping("/test-login")
    public ResponseEntity<String> testLogin(@RequestBody LoginRequest request) {
        System.out.println("=== TEST LOGIN ===");
        System.out.println("Input: " + request.getUsername() + " / " + request.getPassword());

        SysUser user = userService.findByUsername(request.getUsername());
        if (user == null) {
            System.out.println("User not found!");
            return ResponseEntity.ok("USER_NOT_FOUND");
        }

        System.out.println("DB Password: " + user.getPassword());
        System.out.println("Input Password: " + request.getPassword());
        System.out.println("Matches: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.ok("LOGIN_SUCCESS");
        }
        return ResponseEntity.ok("LOGIN_FAILED - wrong password");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        LoginResponse response = authService.refreshToken(actualToken);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).build();
    }
}
