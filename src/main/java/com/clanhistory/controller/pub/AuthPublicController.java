package com.clanhistory.controller.pub;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clanhistory.dto.LoginRequest;
import com.clanhistory.dto.LoginResponse;
import com.clanhistory.entity.SysRole;
import com.clanhistory.entity.SysUser;
import com.clanhistory.mapper.SysRoleMapper;
import com.clanhistory.mapper.SysUserMapper;
import com.clanhistory.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthPublicController {

    private final AuthService authService;
    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // 注册管理员账号（如果不存在）
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest request) {
        // 检查是否已有管理员
        SysUser existingAdmin = userMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, request.getUsername())
        );

        if (existingAdmin != null) {
            return ResponseEntity.ok("管理员已存在，请直接登录");
        }

        // 获取超级管理员角色
        SysRole adminRole = roleMapper.selectOne(
            new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, "SUPER_ADMIN")
        );

        // 创建管理员账号
        SysUser newUser = new SysUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setNickname("系统管理员");
        newUser.setRoleId(adminRole != null ? adminRole.getId() : 1L);
        newUser.setStatus(1);
        newUser.setDeleted(0);

        userMapper.insert(newUser);

        return ResponseEntity.ok("管理员账号创建成功！用户名: " + request.getUsername());
    }
}
