package com.trading.app.service;

import com.trading.app.dto.SignalRequest;
import com.trading.app.dto.SignalResponse;
import com.trading.app.entity.Signal;

public interface SignalService {
    SignalResponse saveSignal(SignalRequest signalSpec);
    Signal getSignalById(int signalId);
}
