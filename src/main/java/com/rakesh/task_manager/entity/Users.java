package com.rakesh.task_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String username;
    @NotBlank(message = "Password is Mandatory")
    @JsonIgnore
    private String password;
    @Email(message = "Enter a valid email")
    @Column(nullable = false, unique = true)
    private String email;
    private String role;
    private String status;
    @CreationTimestamp
    private LocalDateTime createdAt;

//    public Users(){
//        this.createdAt=LocalDateTime.now();
//    }
}
