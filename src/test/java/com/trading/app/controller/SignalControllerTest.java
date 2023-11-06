package com.trading.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.Action;
import com.trading.app.dto.SignalSpec;
import com.trading.app.entity.Signal;
import com.trading.app.service.SignalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(SignalController.class)
class SignalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignalService signalService;

    @BeforeEach
    void setup() {
        Mockito.reset(signalService);
    }

    @Test
    void createSignal_ValidRequest_Returns200() throws Exception {
        SignalSpec signalSpec = getSampleSignalSpec();
        Signal mockSignal = getSampleSignal(signalSpec);

        Mockito.when(signalService.saveSignal(signalSpec)).thenReturn(mockSignal);

        String requestJson = objectMapper.writeValueAsString(signalSpec);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/signals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
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
