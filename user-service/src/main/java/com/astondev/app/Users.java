package com.astondev.app;

@Entity
@Table(name="users")
public class Users {
    public int id;
    public String name;
    public String email;
    public Date create_at;
 
}
