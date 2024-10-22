package com.example.schoolLibrary.model.db.repository;

import com.example.schoolLibrary.model.db.entity.Book;
import com.example.schoolLibrary.model.db.entity.Librarian;
import com.example.schoolLibrary.model.enums.LibrarianStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {

    Optional<Librarian> findByEmailIgnoreCase(@NotEmpty String email);

    @Query("select l from Librarian l where l.status <> :status")
    Page<Librarian> findAllByStatusNot(Pageable pageRequest, LibrarianStatus status);

    @Query("select l from Librarian l where l.status <> :status and (lower(l.firstName) like %:filter% or lower(l.lastName) like %:filter%)")
    Page<Librarian> findAllByStatusNotFiltered(Pageable pageRequest, LibrarianStatus status, String filter);
}
