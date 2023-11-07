package com.trading.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.app.dto.SignalRequest;
import com.trading.app.entity.Signal;

public class ObjectMapperUtil {

    public static Signal covertToSignal(SignalRequest signalSpec) {
        Signal signal = new Signal();
        String jsonActions = null;
        try {
            jsonActions = new ObjectMapper().writeValueAsString(signalSpec.actions());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        signal.setActions(jsonActions);

        return signal;
    }
}
