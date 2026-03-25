package com.clanhistory.service.impl;

import com.clanhistory.dto.LoginRequest;
import com.clanhistory.dto.LoginResponse;
import com.clanhistory.entity.SysRole;
import com.clanhistory.entity.SysUser;
import com.clanhistory.mapper.SysRoleMapper;
import com.clanhistory.security.JwtTokenProvider;
import com.clanhistory.service.AuthService;
import com.clanhistory.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final SysUserService userService;
    private final SysRoleMapper roleMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        System.out.println("=== AUTH LOGIN DEBUG ===");
        System.out.println("Username: " + request.getUsername());
        System.out.println("Password: " + request.getPassword());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            System.out.println("Authentication success: " + authentication);
        } catch (Exception e) {
            System.out.println("Authentication FAILED: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        SysUser user = userService.findByUsername(request.getUsername());
        String roleCode = "GUEST";
        if (user != null && user.getRoleId() != null) {
            SysRole role = roleMapper.selectById(user.getRoleId());
            if (role != null) {
                roleCode = role.getCode();
            }
        }

        return new LoginResponse(token, tokenProvider.getExpiration(), user.getId(), user.getUsername(), roleCode);
    }

    @Override
    public void logout(String token) {
        SecurityContextHolder.clearContext();
    }

    @Override
    public LoginResponse refreshToken(String token) {
        if (tokenProvider.validateToken(token)) {
            String username = tokenProvider.getUsernameFromToken(token);
            SysUser user = userService.findByUsername(username);
            String roleCode = "GUEST";
            if (user != null && user.getRoleId() != null) {
                SysRole role = roleMapper.selectById(user.getRoleId());
                if (role != null) {
                    roleCode = role.getCode();
                }
            }
            String newToken = tokenProvider.generateToken(username);
            return new LoginResponse(newToken, tokenProvider.getExpiration(), user.getId(), user.getUsername(), roleCode);
        }
        return null;
    }
}
