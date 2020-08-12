package com.codegym.vndreamers.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "post", schema = "vndreamers")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "image")
    private String image;

    @Basic
    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Basic
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "modify_date", nullable = false)
    private Timestamp modifyDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Comment> comments;

    @Transient
    private int likeQuantity = 0;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<PostReaction> likes;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;
}
