package com.gm.authorization.server.simple.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.authorization.server.simple.entity.SysUser;
import com.gm.authorization.server.simple.repository.SysUserRepository;
import com.gm.authorization.server.simple.service.UserService;

/**
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public SysUser getByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
}
