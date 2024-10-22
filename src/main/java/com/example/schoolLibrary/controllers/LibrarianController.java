package com.example.schoolLibrary.controllers;

import com.example.schoolLibrary.model.dto.request.LibrarianInfoRequest;
import com.example.schoolLibrary.model.dto.response.LibrarianInfoResponse;
import com.example.schoolLibrary.service.LibrarianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/librarians")
@RequiredArgsConstructor
public class LibrarianController {
    private final LibrarianService librarianService;

    @PostMapping
    @Operation(summary = "Создать библиотекаря")
    public LibrarianInfoResponse createLibrarian(@RequestBody LibrarianInfoRequest request){
        return librarianService.createLibrarian(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить библиотекаря по ID")
    public LibrarianInfoResponse getLibrarian(@PathVariable Long id){
        return librarianService.getLibrarian(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить библиотекаря по ID")
    public LibrarianInfoResponse updateLibrarian(@PathVariable Long id, @RequestBody LibrarianInfoRequest request) {
        return librarianService.updateLibrarian(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить библиотекаря по ID")
    public void deleteLibrarian(@PathVariable Long id) {
        librarianService.deleteLibrarian(id);
    }

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "404", description = "Не найден"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @Operation(summary = "Получить список библиотекарей")
    public Page<LibrarianInfoResponse> getAllLibrarians(@RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer perPage,
                                                        @RequestParam(defaultValue = "lastName") String sort,
                                                        @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                        @RequestParam(required = false) String filter) {

        return librarianService.getAllLibrarians(page, perPage, sort, order, filter);
    }
}
