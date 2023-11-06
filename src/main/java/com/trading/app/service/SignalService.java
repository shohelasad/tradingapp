package com.trading.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.SignalSpec;
import com.trading.app.entity.Signal;
import com.trading.app.repository.SignalRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Service
public class SignalService {
    private final SignalRepository signalRepository;
    private final ObjectMapper objectMapper;

    public SignalService(SignalRepository signalRepository, ObjectMapper objectMapper) {
        this.signalRepository = signalRepository;
        this.objectMapper = objectMapper;
    }

    public Signal saveSignal(SignalSpec signalSpec) {
        try {
            Signal signal = new Signal();
            String jsonActions = objectMapper.writeValueAsString(signalSpec.getActions());
            signal.setActions(jsonActions);
            return signalRepository.save(signal);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid signal data: " + e.getMessage());
        }
    }

    public Signal getSignalById(int signalId) {
        return signalRepository.findById(signalId)
                .orElseThrow(() -> new ResourceAccessException("Signal not found for ID: " + signalId));
    }
}