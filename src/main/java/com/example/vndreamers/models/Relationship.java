package com.example.vndreamers.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "relationship", schema = "vndreamers")
@Data
public class Relationship {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @Basic
    @Column(name = "action_user_id", nullable = false)
    private int actionUserId;

    @ManyToOne
    @JoinColumn(name = "user_one_id", referencedColumnName = "id", nullable = false)
    private User userByUserOneId;

    @ManyToOne
    @JoinColumn(name = "user_two_id", referencedColumnName = "id", nullable = false)
    private User userByUserTwoId;
}
