package com.codegym.vndreamers.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "friend_request", schema = "vndreamers")
@Data
public class FriendRequest {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @Basic
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "modify_date", nullable = false)
    private Timestamp modifyDate = Timestamp.valueOf(LocalDateTime.now());

    @ManyToOne
    @JoinColumn(name = "user_send", referencedColumnName = "id", nullable = false)
    private User userSend;

    @ManyToOne
    @JoinColumn(name = "user_receive", referencedColumnName = "id", nullable = false)
    private User userReceive;
}
