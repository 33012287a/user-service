package com.astondev.app;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class Users {
    @Id
    @Column(name="id")
    public Integer id;

    @Column(name="name")
    public String name;

    @Column(name = "email")
    public String email;

    @Column(name="create_at", nullable = false)
    public Date create_at;
 
}
