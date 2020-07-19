package com.codegym.vndreamers.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "post", schema = "vndreamers")
@Data
public class Post {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "image", nullable = false)
    private String image;

    @Basic
    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Basic
    @Column(name = "share_link", nullable = false)
    private String shareLink;

    @Basic
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "modify_date", nullable = false)
    private Timestamp modifyDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post")
    private Set<PostReaction> likes;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}
