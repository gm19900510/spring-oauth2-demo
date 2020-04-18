package com.gm.authorization.server.simple.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.gm.authorization.server.simple.entity.SysPermission;
import com.gm.authorization.server.simple.entity.SysUser;
import com.gm.authorization.server.simple.service.PermissionService;
import com.gm.authorization.server.simple.service.UserService;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = userService.getByUsername(username);
		if (null == sysUser) {
			log.warn("用户{}不存在", username);
			throw new UsernameNotFoundException(username);
		}
		List<SysPermission> permissionList = permissionService.findByUserId(sysUser.getId());
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(permissionList)) {
			for (SysPermission sysPermission : permissionList) {
				authorityList.add(new SimpleGrantedAuthority(sysPermission.getCode()));
			}
		}

		User myUser = new User(sysUser.getUsername(), passwordEncoder.encode(sysUser.getPassword()), authorityList);

		log.info("用户存在:  {}", JSON.toJSONString(myUser));

		return myUser;
	}
}
