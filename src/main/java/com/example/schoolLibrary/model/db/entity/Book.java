package com.example.schoolLibrary.model.db.entity;

import com.example.schoolLibrary.model.enums.BookStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title")
    String title;

    @Column(name = "author")
    String author;

    @Column(name = "ISBN")
    String ISBN;

    @Column(name = "year_published")
    Integer yearPublished;

    @Column(name = "is_available")
    Boolean isAvailable;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    BookStatus status;

    @OneToMany
    @JsonBackReference(value = "book_loans")
    List<Loan> loans;

}
