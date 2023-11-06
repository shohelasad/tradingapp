package com.trading.app.dto;

import java.util.List;


public record SignalResponse (Integer id, List<Action> actions) {
}
