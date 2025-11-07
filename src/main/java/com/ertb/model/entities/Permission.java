package com.ertb.model.entities;

import com.ertb.enumerations.PermissionEnum;
import com.ertb.enumerations.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "permission")
@Data
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "permission_id")
    private String permissionId;

    @Column(name = "permission_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionEnum permissionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<RolePermission> rolePermission;
}
