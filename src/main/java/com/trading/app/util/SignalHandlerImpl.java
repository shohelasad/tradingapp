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
        log.info("Handle signal with signalId: " + signalId);
        Signal signal = signalService.getSignalById(signalId);
        if (signal != null) {
            List<Action> actions = null;
            try {
                actions = objectMapper.readValue(signal.getActions(),  new TypeReference<List<Action>>() {});
            } catch (JsonProcessingException e) {
                log.info("Invalid signal data: " + e.getMessage());
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
            String actionName = action.name();
            switch (actionName) {
                case "setUp":
                    algo.setUp();
                    break;
                case "setAlgoParam":
                    if (action.parameters().size() == 2) {
                        int param = action.parameters().get(0);
                        int value = action.parameters().get(1);
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