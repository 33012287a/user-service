package com.astondev.app.model.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name="created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name= "age")
    private Integer age;

    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
