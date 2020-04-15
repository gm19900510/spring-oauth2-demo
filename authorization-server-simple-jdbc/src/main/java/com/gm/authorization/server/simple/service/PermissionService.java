package com.gm.authorization.server.simple.service;

import java.util.List;
import com.gm.authorization.server.simple.entity.SysPermission;

/**
 * 
 */
public interface PermissionService {

    List<SysPermission> findByUserId(Integer userId);

}
