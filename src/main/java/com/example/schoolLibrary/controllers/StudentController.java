package com.example.schoolLibrary.controllers;

import com.example.schoolLibrary.model.dto.request.StudentInfoRequest;
import com.example.schoolLibrary.model.dto.response.StudentInfoResponse;
import com.example.schoolLibrary.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Создать ученика")
    public StudentInfoResponse createStudent(@RequestBody StudentInfoRequest request){
        return studentService.createStudent(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить ученика по ID")
    public StudentInfoResponse getStudent(@PathVariable Long id){
        return studentService.getStudent(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить ученика по ID")
    public StudentInfoResponse updateStudent(@PathVariable Long id, @RequestBody StudentInfoRequest request) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ученика по ID")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "404", description = "Не найден"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @Operation(summary = "Получить список учеников")
    public Page<StudentInfoResponse> getAllStudents(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer perPage,
                                                    @RequestParam(defaultValue = "lastName") String sort,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                    @RequestParam(required = false) String filter) {

        return studentService.getAllStudents(page, perPage, sort, order, filter);
    }
}
