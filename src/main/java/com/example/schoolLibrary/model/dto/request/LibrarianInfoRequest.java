package com.example.schoolLibrary.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LibrarianInfoRequest {
    @NotEmpty
    String firstName;
    @NotEmpty
    String lastName;
    @NotEmpty
    String email;
    @NotEmpty
    String password;
}
