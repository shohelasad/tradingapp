package com.trading.app.controller;

import com.trading.app.dto.SignalRequest;
import com.trading.app.dto.SignalResponse;
import com.trading.app.exception.BadRequestException;
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
    public ResponseEntity<SignalResponse> createSignal(@RequestBody SignalRequest signalSpec) {
        log.info("Adding signal with: {}", signalSpec);
        return ResponseEntity.ok(signalService.saveSignal(signalSpec));
    }

    @PostMapping("/{signalId}")
    public ResponseEntity<String> sendSignal(@PathVariable int signalId) {
        log.info("Adding signal Id: " + signalId);
        if (signalId == 0) {
            throw new BadRequestException("Signal Id cannot be negative!");
        }
        signalHandler.handleSignal(signalId);
        return ResponseEntity.ok().build();
    }
}