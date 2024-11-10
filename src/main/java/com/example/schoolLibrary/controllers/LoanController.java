package com.example.schoolLibrary.controllers;

import com.example.schoolLibrary.model.dto.request.LoanInfoRequest;
import com.example.schoolLibrary.model.dto.request.LoanToBookRequest;
import com.example.schoolLibrary.model.dto.request.LoanToCardRequest;
import com.example.schoolLibrary.model.dto.response.LoanInfoResponse;
import com.example.schoolLibrary.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Заемы")
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    @Operation(summary = "Создать заем книги")
    public LoanInfoResponse createLoan(@RequestBody LoanInfoRequest request){
        return loanService.createLoan(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заем книги по ID")
    public LoanInfoResponse getLoan(@PathVariable Long id){
        return loanService.getLoan(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить заем книги по ID")
    public LoanInfoResponse updateLoan(@PathVariable Long id, @RequestBody LoanInfoRequest request) {
        return loanService.updateLoan(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заем книги по ID")
    public void deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
    }

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "404", description = "Не найден"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @Operation(summary = "Получить список заемов книг")
    public Page<LoanInfoResponse> getAllLoans(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "id") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String filter) {

        return loanService.getAllLoans(page, perPage, sort, order, filter);
    }

    @PostMapping("/loanToBook")
    @Operation(summary = "Добавить заем книги книге")
    public void addLoanToBook(@RequestBody @Valid LoanToBookRequest request ){
        loanService.addLoanToBook(request);
    }

    @PostMapping("/loanToCard")
    @Operation(summary = "Добавить заем книги карточке")
    public void addLoanToCard(@RequestBody @Valid LoanToCardRequest request ){
        loanService.addLoanToCard(request);
    }

}
