package com.example.schoolLibrary.model.db.repository;

import com.example.schoolLibrary.model.db.entity.Book;
import com.example.schoolLibrary.model.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    @Query("select b from Book b where b.status <> :status")
    Page<Book> findAllByStatusNot(Pageable pageRequest, BookStatus status);

    @Query("select b from Book b where b.status <> :status and (lower(b.author) like %:filter% or lower(b.title) like %:filter%)")
    Page<Book> findAllByStatusNotFiltered(Pageable pageRequest, BookStatus status, String filter);
}
