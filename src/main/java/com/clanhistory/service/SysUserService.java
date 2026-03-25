package com.clanhistory.service;

import com.clanhistory.entity.SysUser;
import java.util.List;

public interface SysUserService {
    SysUser findByUsername(String username);
    SysUser findById(Long id);
    List<SysUser> findAll();
    boolean save(SysUser user);
    boolean updateById(SysUser user);
    boolean deleteById(Long id);
}
