package com.gm.authorization.server.simple.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色与功能权限关系实体
 */
@Data
@Entity
@Table(name = "sys_role_permission")
public class SysRolePermission implements Serializable {
    private static final long serialVersionUID = 7402412601579098788L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_id")
    private Integer permissionId;
}
