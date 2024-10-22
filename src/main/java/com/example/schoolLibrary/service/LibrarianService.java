package com.example.schoolLibrary.service;

import com.example.schoolLibrary.exceptions.CustomException;
import com.example.schoolLibrary.model.db.entity.Librarian;
import com.example.schoolLibrary.model.db.repository.LibrarianRepository;
import com.example.schoolLibrary.model.dto.request.LibrarianInfoRequest;
import com.example.schoolLibrary.model.dto.response.LibrarianInfoResponse;
import com.example.schoolLibrary.model.enums.LibrarianStatus;
import com.example.schoolLibrary.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
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
public class LibrarianService {
    public final ObjectMapper mapper;
    private final LibrarianRepository librarianRepository;

    public LibrarianInfoResponse createLibrarian(LibrarianInfoRequest request) {
        validateEmail(request);
        librarianRepository.findByEmailIgnoreCase(request.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(String.format("User with email: %s already exists", request.getEmail()), HttpStatus.BAD_REQUEST);
                });

        Librarian librarian = mapper.convertValue(request, Librarian.class);
        librarian.setCreatedAt(LocalDateTime.now());
        librarian.setStatus(LibrarianStatus.CREATED);

        Librarian save = librarianRepository.save(librarian);

        return mapper.convertValue(save, LibrarianInfoResponse.class);
    }

    private void validateEmail(LibrarianInfoRequest request) {
        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
            throw new CustomException("Invalid email format", HttpStatus.BAD_REQUEST);
        }
    }

    public LibrarianInfoResponse getLibrarian(Long id) {
        Librarian user = getLibrarianFromDB (id);
        return mapper.convertValue(user, LibrarianInfoResponse.class);
    }

    public Librarian getLibrarianFromDB (Long id) {
        return librarianRepository.findById(id).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    public LibrarianInfoResponse updateLibrarian(Long id, LibrarianInfoRequest request) {
        validateEmail(request);
        Librarian librarian = getLibrarianFromDB (id);
        librarian.setEmail(request.getEmail());
        librarian.setFirstName(request.getFirstName() == null ? librarian.getFirstName() : request.getFirstName());
        librarian.setLastName(request.getLastName() == null ? librarian.getLastName() : request.getLastName());
        librarian.setPassword(request.getPassword() == null ? librarian.getPassword() : request.getPassword());

        librarian.setUpdatedAt(LocalDateTime.now());
        librarian.setStatus(LibrarianStatus.UPDATED);

        Librarian save = librarianRepository.save(librarian);

        return mapper.convertValue(save, LibrarianInfoResponse.class);
    }

    public void deleteLibrarian(Long id) {
        Librarian librarian = getLibrarianFromDB(id);
        librarian.setUpdatedAt(LocalDateTime.now());
        librarian.setStatus(LibrarianStatus.DELETED);
        librarianRepository.save(librarian);
    }

    public Page<LibrarianInfoResponse> getAllLibrarians(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {

        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Librarian> all;
        if (filter == null) {
            all = librarianRepository.findAllByStatusNot(pageRequest, LibrarianStatus.DELETED);
        } else {
            all = librarianRepository.findAllByStatusNotFiltered(pageRequest, LibrarianStatus.DELETED, filter.toLowerCase());
        }

        List<LibrarianInfoResponse> content = all.getContent().stream()
                .map(user -> mapper.convertValue(user, LibrarianInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }
}
