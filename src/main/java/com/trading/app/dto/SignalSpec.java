package com.trading.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class SignalSpec {
    private List<Action> actions;
}