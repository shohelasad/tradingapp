package com.trading.app.dto;

import java.util.List;

public record SignalRequest(List<Action> actions) {
}