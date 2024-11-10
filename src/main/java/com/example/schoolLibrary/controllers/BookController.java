package com.example.schoolLibrary.controllers;

import com.example.schoolLibrary.model.dto.request.BookInfoRequest;
import com.example.schoolLibrary.model.dto.response.BookInfoResponse;
import com.example.schoolLibrary.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Книги")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Создать книгу")
    public BookInfoResponse createBook(@RequestBody BookInfoRequest request){
        return bookService.createBook(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить книгу по ID")
    public BookInfoResponse getBook(@PathVariable Long id){
        return bookService.getBook(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить книгу по ID")
    public BookInfoResponse updateBook(@PathVariable Long id, @RequestBody BookInfoRequest request) {
        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить книгу по ID")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "404", description = "Не найден"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @Operation(summary = "Получить список книг")
    public Page<BookInfoResponse> getAllBooks(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "title") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String filter) {

        return bookService.getAllBooks(page, perPage, sort, order, filter);
    }
}
