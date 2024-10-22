package com.example.schoolLibrary.model.db.repository;

import com.example.schoolLibrary.model.db.entity.Loan;
import com.example.schoolLibrary.model.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findLoanById(Long id);

    @Query("select l from Loan l where l.status <> :status")
    Page<Loan> findAllByStatusNot(Pageable pageRequest, LoanStatus status);

    @Query("select l from Loan l where l.status <> :status and cast(l.loanDate as string) like %:filter%")
    Page<Loan> findAllByStatusNotFiltered(Pageable pageRequest, LoanStatus status, String filter);
}
