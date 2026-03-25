package com.clanhistory.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clanhistory.entity.SysRole;
import com.clanhistory.entity.SysUser;
import com.clanhistory.mapper.SysRoleMapper;
import com.clanhistory.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getDeleted, 0)
        );

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        SysRole role = null;
        if (user.getRoleId() != null) {
            role = roleMapper.selectById(user.getRoleId());
        }

        String roleCode = role != null ? role.getCode() : "GUEST";

        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleCode))
        );
    }
}
