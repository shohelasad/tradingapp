package com.trading.app.controller;

import com.trading.app.dto.SignalSpec;
import com.trading.app.entity.Signal;
import com.trading.app.util.SignalHandler;
import com.trading.app.util.SignalHandlerImpl;
import com.trading.app.service.SignalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/signals")
public class SignalController {
    private final SignalService signalService;
    private final SignalHandler signalHandler;

    public SignalController(SignalService signalService, SignalHandlerImpl signalHandlerImpl) {
        this.signalService = signalService;
        this.signalHandler = signalHandlerImpl;
    }

    @PostMapping
    public ResponseEntity<Signal> createSignal(@RequestBody SignalSpec signalSpec) {
        Signal signal = signalService.saveSignal(signalSpec);
        return ResponseEntity.ok(signal);
    }

    @PostMapping("/{signalId}")
    public ResponseEntity<String> sendSignal(@PathVariable int signalId) {
        if (signalId == 0) {
            throw new RuntimeException("Signal Id cannot be negative!");
        }
        signalHandler.handleSignal(signalId);
        return ResponseEntity.ok().build();
    }
}