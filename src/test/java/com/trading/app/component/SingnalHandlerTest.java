package com.trading.app.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.Action;
import com.trading.app.dto.SignalRequest;
import com.trading.app.entity.Signal;
import com.trading.app.exception.ResourceNotFoundException;
import com.trading.app.service.SignalServiceImpl;
import com.trading.app.util.ObjectMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class SingnalHandlerTest {
    @Mock
    private SignalServiceImpl signalService;
    @Autowired
    private ObjectMapper objectMapper;

    private SignalHandlerImpl signalHandler;

    @BeforeEach
    public void setup() {
        signalHandler = new SignalHandlerImpl(signalService, objectMapper);
    }

    @Test
    public void testHandleSignalWithValidSignal() {
        SignalRequest signalSpec = getSampleSignalSpec();
        Signal savedSignal = ObjectMapperUtil.covertToSignal(signalSpec);
        when(signalService.getSignalById(1)).thenReturn(savedSignal);
        signalHandler.handleSignal(1);
        Mockito.verify(signalService, Mockito.times(1)).getSignalById(1);
    }

    @Test
    public void testHandleSignalWithInvalidSignal() {
        int invalidSignalId = 2;

        when(signalService.getSignalById(invalidSignalId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            signalHandler.handleSignal(invalidSignalId);
        });
    }

    private SignalRequest getSampleSignalSpec() {
        List<Action> actionList = new ArrayList<>();
        Action setUpAction = new Action("setUp", new ArrayList<>());
        actionList.add(setUpAction);

        Action performCalcAction = new Action("performCalc", new ArrayList<>());
        actionList.add(performCalcAction);

        Action submitToMarketAction = new Action("submitToMarket", new ArrayList<>());
        actionList.add(submitToMarketAction);

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
}
