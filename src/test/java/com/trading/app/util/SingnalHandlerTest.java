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
        SignalRequest signalSpec = new SignalRequest();
        List<Action> actionList = new ArrayList<>();

        // Add 'setUp' action
        Action setUpAction = new Action();
        setUpAction.setName("setUp");
        setUpAction.setParameters(new ArrayList<>());
        actionList.add(setUpAction);

        // Add 'performCalc' action
        Action performCalcAction = new Action();
        performCalcAction.setName("performCalc");
        performCalcAction.setParameters(new ArrayList<>());
        actionList.add(performCalcAction);

        // Add 'submitToMarket' action
        Action submitToMarketAction = new Action();
        submitToMarketAction.setName("submitToMarket");
        submitToMarketAction.setParameters(new ArrayList<>());
        actionList.add(submitToMarketAction);

        // Add 'setAlgoParam' action with parameters
        Action setAlgoParamAction = new Action();
        setAlgoParamAction.setName("setAlgoParam");
        List<Integer> parameters = new ArrayList<>();
        parameters.add(1);
        parameters.add(60);
        setAlgoParamAction.setParameters(parameters);
        actionList.add(setAlgoParamAction);

        // Add 'reverse' action
        Action reverseAction = new Action();
        reverseAction.setName("reverse");
        reverseAction.setParameters(new ArrayList<>());
        actionList.add(reverseAction);

        signalSpec.setActions(actionList);
        return signalSpec;
    }

    private Signal getSampleSignal(SignalRequest signalSpec) {
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
