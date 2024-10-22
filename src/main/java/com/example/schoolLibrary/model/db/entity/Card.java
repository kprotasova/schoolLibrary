package com.example.schoolLibrary.model.db.entity;

import com.example.schoolLibrary.model.enums.BookStatus;
import com.example.schoolLibrary.model.enums.CardStatus;
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
@Table(name = "cards")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "card_number")
    String cardNumber;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    CardStatus status;

    @OneToOne
    @JsonBackReference(value = "student_card")
    Student student;

    @OneToMany
    @JsonBackReference(value = "card_loans")
    List<Loan> loans;
}
