package com.clanhistory.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clanhistory.entity.SysUser;
import com.clanhistory.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("ss")
@RequiredArgsConstructor
public class SecurityPermissionEvaluator {

    private final SysUserMapper userMapper;

    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || permission == null) {
            return false;
        }

        String permissionStr = (String) permission;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );

        if (user == null || user.getRoleId() == null) {
            return false;
        }

        // SUPER_ADMIN has all permissions
        if (user.getRoleId() == 1L) {
            return true;
        }

        // CONTENT_ADMIN can only perform read operations without approval
        if (user.getRoleId() == 2L) {
            return permissionStr.startsWith("read") || permissionStr.startsWith("list") || permissionStr.startsWith("get");
        }

        return false;
    }

    public boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || role == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }

    public boolean isSuperAdmin(Authentication authentication) {
        return hasRole(authentication, "SUPER_ADMIN");
    }

    public boolean isContentAdmin(Authentication authentication) {
        return hasRole(authentication, "CONTENT_ADMIN");
    }
}
