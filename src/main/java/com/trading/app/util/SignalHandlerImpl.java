package com.trading.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.Action;
import com.trading.app.entity.Signal;
import com.trading.app.lib.Algo;
import com.trading.app.service.SignalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SignalHandlerImpl implements SignalHandler {
    private final SignalService signalService;
    private final ObjectMapper objectMapper;
    private final Algo algo;

    public SignalHandlerImpl(SignalService signalService, ObjectMapper objectMapper) {
        this.signalService = signalService;
        this.objectMapper = objectMapper;
        this.algo = new Algo();
    }

    @Override
    public void handleSignal(int signalId) {
        Signal signalSpecification = signalService.getSignalById(signalId);
        if (signalSpecification != null) {
            List<Action> actions = null;
            try {
                actions = objectMapper.readValue(signalSpecification.getActions(),  new TypeReference<List<Action>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            processSignal(actions);
        } else {
            algo.cancelTrades();
        }
        algo.doAlgo();
    }

    private void processSignal(List<Action> actions) {
        actions.forEach(action -> {
            String actionName = action.getName();
            switch (actionName) {
                case "setUp":
                    algo.setUp();
                    break;
                case "setAlgoParam":
                    if (action.getParameters().size() == 2) {
                        int param = action.getParameters().get(0);
                        int value = action.getParameters().get(1);
                        algo.setAlgoParam(param, value);
                    }
                    break;
                case "performCalc":
                    algo.performCalc();
                    break;
                case "submitToMarket":
                    algo.submitToMarket();
                    break;
                case "cancelTrades":
                    algo.cancelTrades();
                    break;
                case "reverse":
                    algo.reverse();
                    break;
                default:
                    break;
            }
        });
    }
}