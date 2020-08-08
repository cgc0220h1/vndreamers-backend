package com.codegym.vndreamers.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "user", schema = "vndreamers")
@Data
public class User implements UserDetails {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "first_name", nullable = false, length = 50)
    @Pattern(regexp = "\\p{L}{3,32}", flags = Pattern.Flag.CASE_INSENSITIVE)
    @Size(max = 32, min = 3)
    @NotNull
    @JsonProperty(value = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 50)
    @Pattern(regexp = "\\p{L}{3,32}", flags = Pattern.Flag.CASE_INSENSITIVE)
    @Size(max = 32, min = 3)
    @NotNull
    @JsonProperty(value = "last_name")
    private String lastName;

    @Basic
    @Column(name = "phone_number")
    @Size(min = 10, max = 11)
    @Pattern(regexp = "((0|\\+[1-9]{1,3})+([0-9]{9,10}))")
    @JsonProperty(value = "phone")
    private String phoneNumber;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "username", length = 50, unique = true)
    @Getter(AccessLevel.NONE)
    private String username;

    @Basic
    @Column(name = "email", nullable = false, length = 100, unique = true)
    @Email
    @NotNull
    private String email;

    @Basic
    @Column(name = "password", nullable = false, length = 32)
    @Size(max = 32, min = 6)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,32}$")
    private String password;

    @Transient
    @Size(max = 32, min = 6)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "confirm_password")
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,32}$")
    private String confirmPassword;

    @Basic
    @Column(name = "birth_date", nullable = false)
    @NotNull
    @JsonProperty(value = "birth_date")
    private Date birthDate;

    @Basic
    @Column(name = "gender", nullable = false)
    @NotNull
    private int gender = 1;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "modified_date", nullable = false)
    @JsonProperty(value = "modified_date")
    private Timestamp modifiedDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "status", nullable = false)
    private int status = 1;

    @Basic
    @Column(name = "image")
    @JsonProperty(value = "avatar")
    private String image = "https://images.unsplash.com/photo-1569428034239-f9565e32e224?ixlib=rb-1.2.1&auto=format&fit=crop&w=1958&q=80";

    @Basic
    @Column(name = "about_me")
    @JsonProperty(value = "about_me")
    private String aboutMe;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Comment> comments;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<PostReaction> postLikes;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<CommentReaction> commentLikes;

    @OneToMany(mappedBy = "userSend")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<FriendRequest> requestsThisUserSent;

    @OneToMany(mappedBy = "userReceive")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<FriendRequest> requestsThisUserReceived;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private Set<Role> roles;

    public String getUsername() {
        if (username == null) {
            return email;
        }
        return username;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
