package com.example.schoolLibrary.model.dto.response;

import com.example.schoolLibrary.model.dto.request.StudentInfoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentInfoResponse extends StudentInfoRequest {
    Long id;
}
