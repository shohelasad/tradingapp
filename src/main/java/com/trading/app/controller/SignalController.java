package com.trading.app.controller;

import com.trading.app.dto.SignalSpec;
import com.trading.app.entity.Signal;
import com.trading.app.service.SignalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/signals")
@Validated
public class SignalController {
    private final SignalService signalService;

    public SignalController(SignalService signalServiceImpl) {
        this.signalService = signalServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Signal> createSignal(@RequestBody SignalSpec signalSpec) {
        Signal signal = signalService.createSignal(signalSpec);
        return ResponseEntity.ok(signal);
    }
}