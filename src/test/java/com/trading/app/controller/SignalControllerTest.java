package com.trading.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.Action;
import com.trading.app.dto.SignalRequest;
import com.trading.app.entity.Signal;
import com.trading.app.service.SignalService;
import com.trading.app.util.SignalHandlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest({SignalController.class, SignalHandlerImpl.class})
class SignalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignalService signalService;

    private SignalHandlerImpl signalHandler;

    @InjectMocks
    private SignalController signalController;

    @BeforeEach
    void setup() {
        signalHandler = new SignalHandlerImpl(signalService, objectMapper);
        Mockito.reset(signalService);
    }

    @Test
    void createSignalValidRequestReturns200() throws Exception {
        SignalRequest signalSpec = getSampleSignalSpec();
        String requestJson = objectMapper.writeValueAsString(signalSpec);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/signals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testSendSignalValidSignalId() throws Exception {
        int validSignalId = 1;

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/signals/{signalId}", validSignalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
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
