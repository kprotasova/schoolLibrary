package com.example.schoolLibrary.service;

import com.example.schoolLibrary.exceptions.CustomException;
import com.example.schoolLibrary.model.db.entity.Student;
import com.example.schoolLibrary.model.db.repository.StudentRepository;
import com.example.schoolLibrary.model.dto.request.StudentInfoRequest;
import com.example.schoolLibrary.model.dto.response.StudentInfoResponse;
import com.example.schoolLibrary.model.enums.StudentStatus;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    public final ObjectMapper mapper;
    private final StudentRepository studentRepository;

    public StudentInfoResponse createStudent(StudentInfoRequest request) {
        Student student = mapper.convertValue(request, Student.class);
        student.setStatus(StudentStatus.CREATED);
        return mapper.convertValue(studentRepository.save(student), StudentInfoResponse.class);
    }

    public StudentInfoResponse getStudent(Long id) {
        return mapper.convertValue(studentRepository.findById(id), StudentInfoResponse.class);
    }

    private Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new CustomException("Student not found", HttpStatus.NOT_FOUND));
    }

    public StudentInfoResponse updateStudent(Long id, StudentInfoRequest request) {
        Student student = getStudentById(id);

        student.setFirstName(request.getFirstName() == null ? student.getFirstName() : request.getFirstName());
        student.setLastName(request.getLastName() == null ? student.getLastName() : request.getLastName());
        student.setGrade(request.getGrade() == null ? student.getGrade() : request.getGrade());

        student.setUpdatedAt(LocalDateTime.now());
        student.setStatus(StudentStatus.UPDATED);

        Student save = studentRepository.save(student);
        return mapper.convertValue(save, StudentInfoResponse.class);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        student.setStatus(StudentStatus.DELETED);
        studentRepository.save(student);
    }

    public Page<StudentInfoResponse> getAllStudents(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Student> all;
        if (filter == null) {
            all = studentRepository.findAllByStatusNot(pageRequest, StudentStatus.DELETED);
        } else {
            all = studentRepository.findAllByStatusNotFiltered(pageRequest, StudentStatus.DELETED, filter.toLowerCase());
        }

        List<StudentInfoResponse> content = all.getContent().stream()
                .map(student -> mapper.convertValue(student, StudentInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }
}
