package com.example.schoolLibrary.service;

import com.example.schoolLibrary.exceptions.CustomException;
import com.example.schoolLibrary.model.db.entity.Book;
import com.example.schoolLibrary.model.db.repository.BookRepository;
import com.example.schoolLibrary.model.db.repository.StudentRepository;
import com.example.schoolLibrary.model.dto.request.BookInfoRequest;
import com.example.schoolLibrary.model.dto.response.BookInfoResponse;
import com.example.schoolLibrary.model.enums.BookStatus;
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
public class BookService {
    public final ObjectMapper mapper;
    private final BookRepository bookRepository;

    public BookInfoResponse createBook(BookInfoRequest request) {
        Book book = mapper.convertValue(request, Book.class);
        book.setStatus(BookStatus.CREATED);
        return mapper.convertValue(bookRepository.save(book), BookInfoResponse.class);
    }

    public BookInfoResponse getBook(Long id) {
        return mapper.convertValue(bookRepository.findById(id), BookInfoResponse.class);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new CustomException("Book not found", HttpStatus.NOT_FOUND));
    }

    public BookInfoResponse updateBook(Long id, BookInfoRequest request) {
        Book book = getBookById(id);

        book.setTitle(request.getTitle() == null ? book.getTitle() : request.getTitle());
        book.setAuthor(request.getAuthor() == null ? book.getAuthor() : request.getAuthor());
        book.setISBN(request.getISBN() == null ? book.getISBN() : request.getISBN());
        book.setYearPublished(request.getYearPublished() == null ? book.getYearPublished() : request.getYearPublished());
        book.setIsAvailable(request.getIsAvailable() == null ? book.getIsAvailable() : request.getIsAvailable());

        book.setUpdatedAt(LocalDateTime.now());
        book.setStatus(BookStatus.UPDATED);

        Book save = bookRepository.save(book);
        return mapper.convertValue(save, BookInfoResponse.class);
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        book.setStatus(BookStatus.DELETED);
        bookRepository.save(book);
    }

    public Book updateBookData(Book book) {
        return bookRepository.save(book);
    }

    public Page<BookInfoResponse> getAllBooks(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Book> all;
        if (filter == null) {
            all = bookRepository.findAllByStatusNot(pageRequest, BookStatus.DELETED);
        } else {
            all = bookRepository.findAllByStatusNotFiltered(pageRequest, BookStatus.DELETED, filter.toLowerCase());
        }

        List<BookInfoResponse> content = all.getContent().stream()
                .map(book -> mapper.convertValue(book, BookInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }
}
