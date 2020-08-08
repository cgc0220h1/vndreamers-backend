package com.codegym.vndreamers.models;

import com.codegym.vndreamers.enums.EnumRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role", schema = "vndreamers")
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @Basic
    @Column(name = "role_name", nullable = false, length = 10)
    @JsonProperty(value = "role_name")
    @Enumerated(EnumType.STRING)
    @NonNull
    private final EnumRole enumRole;

    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Permission> permissions;

    @ManyToMany
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<User> users;

    @Override
    @JsonIgnore
    public String getAuthority() {
        assert enumRole != null;
        return enumRole.toString();
    }
}
