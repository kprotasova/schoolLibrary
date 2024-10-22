package com.example.schoolLibrary.model.db.entity;

import com.example.schoolLibrary.model.enums.BookStatus;
import com.example.schoolLibrary.model.enums.LibrarianStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "librarians")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Librarian {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    LibrarianStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;
}
