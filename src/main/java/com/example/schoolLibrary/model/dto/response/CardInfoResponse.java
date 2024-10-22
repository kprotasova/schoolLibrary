package com.example.schoolLibrary.model.dto.response;

import com.example.schoolLibrary.model.dto.request.CardInfoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardInfoResponse extends CardInfoRequest {
    Long id;
}
