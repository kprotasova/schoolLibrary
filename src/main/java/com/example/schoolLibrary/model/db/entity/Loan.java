package com.example.schoolLibrary.model.db.entity;

import com.example.schoolLibrary.model.enums.BookStatus;
import com.example.schoolLibrary.model.enums.LoanStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "loans")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Loan {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "loan_date")
    LocalDate loanDate;

    @Column(name = "return_date")
    LocalDate returnDate;

    @Column(name = "is_returned")
    Boolean isReturned;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    LoanStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    @JsonBackReference(value = "book_loans")
    Book book;

    @ManyToOne
    @JsonBackReference(value = "card_loans")
    Card card;
}
