package com.example.schoolLibrary.controllers;

import com.example.schoolLibrary.model.dto.request.CardInfoRequest;
import com.example.schoolLibrary.model.dto.response.CardInfoResponse;
import com.example.schoolLibrary.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Карточки")
@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    @Operation(summary = "Создать карточку")
    public CardInfoResponse createCard(@RequestBody CardInfoRequest request){
        return cardService.createCard(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить карточку по ID")
    public CardInfoResponse getCard(@PathVariable Long id){
        return cardService.getCard(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить карточку по ID")
    public CardInfoResponse updateCard(@PathVariable Long id, @RequestBody CardInfoRequest request) {
        return cardService.updateCard(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить карточку по ID")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "404", description = "Не найден"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @Operation(summary = "Получить список карточек")
    public Page<CardInfoResponse> getAllCards(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "cardNumber") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String filter) {

        return cardService.getAllCards(page, perPage, sort, order, filter);
    }

}
