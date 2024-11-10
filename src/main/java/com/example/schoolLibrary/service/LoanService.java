package com.example.schoolLibrary.service;

import com.example.schoolLibrary.exceptions.CustomException;
import com.example.schoolLibrary.model.db.entity.Book;
import com.example.schoolLibrary.model.db.entity.Card;
import com.example.schoolLibrary.model.db.entity.Loan;
import com.example.schoolLibrary.model.db.repository.LoanRepository;
import com.example.schoolLibrary.model.dto.request.LoanInfoRequest;
import com.example.schoolLibrary.model.dto.request.LoanToBookRequest;
import com.example.schoolLibrary.model.dto.request.LoanToCardRequest;
import com.example.schoolLibrary.model.dto.response.LoanInfoResponse;
import com.example.schoolLibrary.model.enums.LoanStatus;
import com.example.schoolLibrary.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {
    private final CardService cardService;
    private final BookService bookService;
    public final ObjectMapper mapper;
    private final LoanRepository loanRepository;

    public LoanInfoResponse createLoan(LoanInfoRequest request) {
        Loan loan = mapper.convertValue(request, Loan.class);
        loan.setStatus(LoanStatus.CREATED);
        return mapper.convertValue(loanRepository.save(loan), LoanInfoResponse.class);
    }

    public LoanInfoResponse getLoan(Long id) {
        return mapper.convertValue(loanRepository.findById(id), LoanInfoResponse.class);
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new CustomException("Loan not found", HttpStatus.NOT_FOUND));
    }

    public LoanInfoResponse updateLoan(Long id, LoanInfoRequest request) {
        Loan loan = getLoanById(id);

        loan.setLoanDate(request.getLoanDate() == null ? loan.getLoanDate() : request.getLoanDate());
        loan.setReturnDate(request.getReturnDate() == null ? loan.getReturnDate() : request.getReturnDate());
        loan.setIsReturned(request.getIsReturned() == null ? loan.getIsReturned() : request.getIsReturned());

        loan.setUpdatedAt(LocalDateTime.now());
        loan.setStatus(LoanStatus.UPDATED);

        Loan save = loanRepository.save(loan);
        return mapper.convertValue(save, LoanInfoResponse.class);
    }

    public void deleteLoan(Long id) {
        Loan loan = getLoanById(id);
        loan.setStatus(LoanStatus.DELETED);
        loanRepository.save(loan);
    }

    public Page<LoanInfoResponse> getAllLoans(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Loan> all;
        if (filter == null) {
            all = loanRepository.findAllByStatusNot(pageRequest, LoanStatus.DELETED);
        } else {
            all = loanRepository.findAllByStatusNotFiltered(pageRequest, LoanStatus.DELETED, filter.toLowerCase());
        }

        List<LoanInfoResponse> content = all.getContent().stream()
                .map(loan -> mapper.convertValue(loan, LoanInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }

    public void addLoanToBook(@Valid LoanToBookRequest request) {
        Loan loan = loanRepository.findById(request.getLoanId()).orElseThrow(() -> new CustomException("Loan not found", HttpStatus.NOT_FOUND));
        Book bookById = bookService.getBookById(request.getBookId());
        bookById.getLoans().add(loan);

        bookService.updateBookData(bookById);

        loan.setBook(bookById);
        loanRepository.save(loan);
    }

    public void addLoanToCard(@Valid LoanToCardRequest request) {
        Loan loan = loanRepository.findById(request.getLoanId()).orElseThrow(() -> new CustomException("Loan not found", HttpStatus.NOT_FOUND));
        Card cardById = cardService.getCardById(request.getCardId());
        cardById.getLoans().add(loan);

        cardService.updateCardData(cardById);

        loan.setCard(cardById);
        loanRepository.save(loan);

    }
}
