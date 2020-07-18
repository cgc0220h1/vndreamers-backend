package com.example.vndreamers.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "permission", schema = "vndreamers")
@Data
public class Permission {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "permission_name", nullable = false, length = 150)
    private String permissionName;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
