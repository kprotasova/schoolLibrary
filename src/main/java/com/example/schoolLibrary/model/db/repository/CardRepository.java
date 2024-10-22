package com.example.schoolLibrary.model.db.repository;

import com.example.schoolLibrary.model.db.entity.Book;
import com.example.schoolLibrary.model.db.entity.Card;
import com.example.schoolLibrary.model.enums.BookStatus;
import com.example.schoolLibrary.model.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findCardById(Long id);

    @Query("select c from Card c where c.status <> :status")
    Page<Card> findAllByStatusNot(Pageable pageRequest, CardStatus status);

    @Query("select c from Card c where c.status <> :status and (lower(c.cardNumber) like %:filter%)")
    Page<Card> findAllByStatusNotFiltered(Pageable pageRequest, CardStatus status, String filter);
}
