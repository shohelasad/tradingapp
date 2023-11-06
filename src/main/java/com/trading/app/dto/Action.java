package com.trading.app.dto;

import java.util.List;

public record Action (String name, List<Integer> parameters) {
}


