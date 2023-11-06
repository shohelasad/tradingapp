package com.trading.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.Action;
import com.trading.app.dto.SignalSpec;
import com.trading.app.entity.Signal;
import com.trading.app.repository.SignalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResourceAccessException;

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
        SignalSpec signalSpec = getSampleSignalSpec();
        Signal savedSignal = getSampleSignal(signalSpec);
        savedSignal.setId(1);

        when(signalRepository.save(Mockito.any(Signal.class))).thenReturn(savedSignal);
        Signal result = signalService.saveSignal(signalSpec);

        assertEquals(savedSignal.getId(), result.getId());
    }

    @Test
    public void testSaveSignalWithInvalidData() throws Exception {
        SignalSpec signalSpec = new SignalSpec(); // Invalid data
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

        assertThrows(ResourceAccessException.class, () -> {
            signalService.getSignalById(signalId);
        });
    }

    private SignalSpec getSampleSignalSpec() {
        SignalSpec signalSpec = new SignalSpec();
        List<Action> actionList = new ArrayList<>();
        Action action = new Action();
        action.setName("setUp");
        action.setParameters(new ArrayList<>());
        actionList.add(action);

        return signalSpec;
    }

    private Signal getSampleSignal(SignalSpec signalSpec) {
        Signal signal = new Signal();
        String jsonActions = null;
        try {
            jsonActions = objectMapper.writeValueAsString(signalSpec.getActions());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        signal.setActions(jsonActions);

        return signal;
    }
}
