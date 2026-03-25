package com.clanhistory.service;

import com.clanhistory.dto.LoginRequest;
import com.clanhistory.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void logout(String token);
    LoginResponse refreshToken(String token);
}
