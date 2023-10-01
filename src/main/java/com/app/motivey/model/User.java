package com.app.motivey.model;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    private String username;
    private String password;
    private String email;

}