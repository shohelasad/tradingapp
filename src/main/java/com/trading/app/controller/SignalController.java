package com.trading.app.controller;

import com.trading.app.dto.SignalSpec;
import com.trading.app.entity.Signal;
import com.trading.app.service.SignalHandlerImpl;
import com.trading.app.service.SignalService;
import com.trading.app.service.SignalHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/signals")
@Validated
public class SignalController {
    private final SignalService signalService;

    private final SignalHandler signalHandler;

    public SignalController(SignalService signalServiceImpl, SignalHandlerImpl signalHandlerImpl) {
        this.signalService = signalServiceImpl;
        this.signalHandler = signalHandlerImpl;
    }

    @PostMapping("/{signalId}")
    public ResponseEntity<String> sendSignal(@PathVariable int signalId) {
        if (signalId == 0) {
            throw new RuntimeException("Signal Id cannot be negative!");
        }
        signalHandler.handleSignal(signalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Signal> createSignal(@RequestBody SignalSpec signalSpec) {
        Signal signal = signalService.createSignal(signalSpec);
        return ResponseEntity.ok(signal);
    }
}