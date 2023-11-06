package com.trading.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.Action;
import com.trading.app.dto.SignalRequest;
import com.trading.app.dto.SignalResponse;
import com.trading.app.entity.Signal;
import com.trading.app.exception.ResourceNotFoundException;
import com.trading.app.repository.SignalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SignalService {
    private final SignalRepository signalRepository;
    private final ObjectMapper objectMapper;

    public SignalService(SignalRepository signalRepository, ObjectMapper objectMapper) {
        this.signalRepository = signalRepository;
        this.objectMapper = objectMapper;
    }

    public SignalResponse saveSignal(SignalRequest signalSpec) {
        try {
            Signal signal = new Signal();
            String jsonActions = objectMapper.writeValueAsString(signalSpec.getActions());
            signal.setActions(jsonActions);
            return toDto(signalRepository.save(signal));
        } catch (Exception e) {
            log.info("Invalid signal data: " + e.getMessage());
            throw new IllegalArgumentException("Invalid signal data: " + e.getMessage());
        }
    }

    public Signal getSignalById(int signalId) {
        log.info("Fetch Signal with signalId: " + signalId);
        return signalRepository.findById(signalId)
                .orElseThrow(() -> new ResourceNotFoundException("Signal not found for ID: " + signalId));
    }

    private SignalResponse toDto(Signal signal) {
        if (signal != null) {
            List<Action> actions = null;
            try {
                actions = objectMapper.readValue(signal.getActions(), new TypeReference<List<Action>>() {});
                return new SignalResponse(signal.getId(), actions);
            } catch (JsonProcessingException e) {
                log.info("Invalid signal data: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}