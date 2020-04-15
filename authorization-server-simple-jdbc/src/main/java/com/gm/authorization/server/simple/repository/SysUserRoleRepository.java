package com.gm.authorization.server.simple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.gm.authorization.server.simple.entity.SysUserRole;
import java.util.List;

/**
 *
 */
public interface SysUserRoleRepository extends JpaSpecificationExecutor<SysUserRole>, JpaRepository<SysUserRole, Integer> {

    List<SysUserRole> findByUserId(Integer userId);
}
