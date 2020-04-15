package com.gm.authorization.server.simple.service;

import com.gm.authorization.server.simple.entity.SysUser;

/**
 * 
 */
public interface UserService {

    SysUser getByUsername(String username);
}
