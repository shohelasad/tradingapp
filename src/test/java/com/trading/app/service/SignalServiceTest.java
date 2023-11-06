package com.trading.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.Action;
import com.trading.app.dto.SignalRequest;
import com.trading.app.dto.SignalResponse;
import com.trading.app.entity.Signal;
import com.trading.app.exception.ResourceNotFoundException;
import com.trading.app.repository.SignalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class SignalServiceTest {

    @Mock
    private SignalRepository signalRepository;

    private ObjectMapper objectMapper;
    private SignalService signalService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        signalService = new SignalService(signalRepository, objectMapper);
    }

    @Test
    public void testSaveSignal() throws Exception {
        SignalRequest signalSpec = getSampleSignalSpec();
        Signal savedSignal = getSampleSignal(signalSpec);
        savedSignal.setId(1);

        when(signalRepository.save(Mockito.any(Signal.class))).thenReturn(savedSignal);
        SignalResponse result = signalService.saveSignal(signalSpec);

        assertEquals(savedSignal.getId(), result.id());
    }

    @Test
    public void testSaveSignalWithInvalidData() throws Exception {
        SignalRequest signalSpec = new SignalRequest(new ArrayList<>()); // Invalid data
        when(signalRepository.save(Mockito.any(Signal.class))).thenThrow(new IllegalArgumentException("Invalid signal data"));
        try {
            signalService.saveSignal(signalSpec);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid signal data"));
        }
    }

    @Test
    public void testGetSignalByIdValidSignal() {
        int signalId = 1;
        Signal expectedSignal = new Signal();
        expectedSignal.setId(signalId);

        when(signalRepository.findById(signalId)).thenReturn(Optional.of(expectedSignal));

        Signal result = signalService.getSignalById(signalId);

        assertEquals(expectedSignal, result);
    }

    @Test
    public void testGetSignalByIdInvalidSignal() {
        int signalId = 2;

        when(signalRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            signalService.getSignalById(signalId);
        });
    }

    private SignalRequest getSampleSignalSpec() {
        List<Action> actionList = new ArrayList<>();
        Action action = new Action("setUp", new ArrayList<>());
        actionList.add(action);

        return new SignalRequest(actionList);
    }

    private Signal getSampleSignal(SignalRequest signalSpec) {
        Signal signal = new Signal();
        String jsonActions = null;
        try {
            jsonActions = objectMapper.writeValueAsString(signalSpec.actions());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        signal.setActions(jsonActions);

        return signal;
    }
}
