package com.example.schoolLibrary.service;

import com.example.schoolLibrary.exceptions.CustomException;
import com.example.schoolLibrary.model.db.entity.Card;
import com.example.schoolLibrary.model.db.repository.CardRepository;
import com.example.schoolLibrary.model.dto.request.CardInfoRequest;
import com.example.schoolLibrary.model.dto.response.CardInfoResponse;
import com.example.schoolLibrary.model.enums.CardStatus;
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
public class CardService {
    public final ObjectMapper mapper;
    private final CardRepository cardRepository;

    public CardInfoResponse createCard(CardInfoRequest request) {
        Card card = mapper.convertValue(request, Card.class);
        card.setStatus(CardStatus.CREATED);
        return mapper.convertValue(cardRepository.save(card), CardInfoResponse.class);
    }

    public CardInfoResponse getCard(Long id) {
        return mapper.convertValue(cardRepository.findById(id), CardInfoResponse.class);
    }

    private Card getCardById(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new CustomException("Card not found", HttpStatus.NOT_FOUND));
    }

    public CardInfoResponse updateCard(Long id, CardInfoRequest request) {
        Card card = getCardById(id);

        card.setCardNumber(request.getCardNumber() == null ? card.getCardNumber() : request.getCardNumber());

        card.setUpdatedAt(LocalDateTime.now());
        card.setStatus(CardStatus.UPDATED);

        Card save = cardRepository.save(card);
        return mapper.convertValue(save, CardInfoResponse.class);
    }

    public void deleteCard(Long id) {
        Card card = getCardById(id);
        card.setStatus(CardStatus.DELETED);
        cardRepository.save(card);
    }

    public Page<CardInfoResponse> getAllCards(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Card> all;
        if (filter == null) {
            all = cardRepository.findAllByStatusNot(pageRequest, CardStatus.DELETED);
        } else {
            all = cardRepository.findAllByStatusNotFiltered(pageRequest, CardStatus.DELETED, filter.toLowerCase());
        }

        List<CardInfoResponse> content = all.getContent().stream()
                .map(card -> mapper.convertValue(card, CardInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }
}
