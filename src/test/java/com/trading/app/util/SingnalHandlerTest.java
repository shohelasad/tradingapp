package com.trading.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.Action;
import com.trading.app.dto.SignalRequest;
import com.trading.app.entity.Signal;
import com.trading.app.lib.Algo;
import com.trading.app.service.SignalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class SingnalHandlerTest {

    @Mock
    private SignalService signalService;

    @Autowired
    private ObjectMapper objectMapper;

    private Algo algo;

    @InjectMocks
    private SignalHandlerImpl signalHandler;

    @BeforeEach
    public void setup() {
        signalHandler = new SignalHandlerImpl(signalService, objectMapper);
        algo = new Algo();
    }

    @Test
    public void testHandleSignalWithValidSignal() throws JsonProcessingException {
        SignalRequest signalSpec = getSampleSignalSpec();
        Signal savedSignal = getSampleSignal(signalSpec);
        when(signalService.getSignalById(1)).thenReturn(savedSignal);
        signalHandler.handleSignal(1);
    }

    @Test
    public void testHandleSignalWithInvalidSignal() {
        Mockito.when(signalService.getSignalById(2)).thenReturn(null);
        signalHandler.handleSignal(2);
    }

    private SignalRequest getSampleSignalSpec() {
        List<Action> actionList = new ArrayList<>();
        // Add 'setUp' action
        Action setUpAction = new Action("setUp", new ArrayList<>());
        actionList.add(setUpAction);

        // Add 'performCalc' action
        Action performCalcAction = new Action("performCalc", new ArrayList<>());
        actionList.add(performCalcAction);

        // Add 'submitToMarket' action
        Action submitToMarketAction = new Action("submitToMarket", new ArrayList<>());
        actionList.add(submitToMarketAction);

        // Add 'setAlgoParam' action with parameters
        List<Integer> parameters = new ArrayList<>();
        parameters.add(1);
        parameters.add(60);
        Action setAlgoParamAction = new Action("setAlgoParam", parameters);
        actionList.add(setAlgoParamAction);

        // Add 'reverse' action
        Action reverseAction = new Action("reverse", new ArrayList<>());
        actionList.add(reverseAction);

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
