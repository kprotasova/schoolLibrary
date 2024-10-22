package com.example.schoolLibrary.model.db.repository;

import com.example.schoolLibrary.model.db.entity.Book;
import com.example.schoolLibrary.model.db.entity.Librarian;
import com.example.schoolLibrary.model.db.entity.Student;
import com.example.schoolLibrary.model.enums.LibrarianStatus;
import com.example.schoolLibrary.model.enums.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findStudentById(Long id);

    @Query("select s from Student s where s.status <> :status")
    Page<Student> findAllByStatusNot(Pageable pageRequest, StudentStatus status);

    @Query("select s from Student s where s.status <> :status and (lower(s.firstName) like %:filter% or lower(s.lastName) like %:filter%)")
    Page<Student> findAllByStatusNotFiltered(Pageable pageRequest, StudentStatus status, String filter);
}
