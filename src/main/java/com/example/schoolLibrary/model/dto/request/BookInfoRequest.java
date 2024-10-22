package com.example.schoolLibrary.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookInfoRequest {
    String title;
    String author;
    String ISBN;
    Integer yearPublished;
    Boolean isAvailable;
}
