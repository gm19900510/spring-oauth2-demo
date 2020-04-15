package com.gm.authorization.server.simple.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.gm.authorization.server.simple.entity.SysPermission;
import com.gm.authorization.server.simple.entity.SysRolePermission;
import com.gm.authorization.server.simple.entity.SysUserRole;
import com.gm.authorization.server.simple.repository.SysPermissionRepository;
import com.gm.authorization.server.simple.repository.SysRolePermissionRepository;
import com.gm.authorization.server.simple.repository.SysUserRoleRepository;
import com.gm.authorization.server.simple.service.PermissionService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private SysUserRoleRepository sysUserRoleRepository;
	@Autowired
	private SysRolePermissionRepository sysRolePermissionRepository;
	@Autowired
	private SysPermissionRepository sysPermissionRepository;

	@Override
	public List<SysPermission> findByUserId(Integer userId) {
		List<SysUserRole> sysUserRoleList = sysUserRoleRepository.findByUserId(userId);
		if (CollectionUtils.isEmpty(sysUserRoleList)) {
			return null;
		}
		List<Integer> roleIdList = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
		List<SysRolePermission> rolePermissionList = sysRolePermissionRepository.findByRoleIds(roleIdList);
		if (CollectionUtils.isEmpty(rolePermissionList)) {
			return null;
		}
		List<Integer> permissionIdList = rolePermissionList.stream().map(SysRolePermission::getPermissionId).distinct()
				.collect(Collectors.toList());
		List<SysPermission> sysPermissionList = sysPermissionRepository.findByIds(permissionIdList);
		if (CollectionUtils.isEmpty(sysPermissionList)) {
			return null;
		}
		return sysPermissionList;
	}
}
