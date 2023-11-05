package com.trading.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class Action {
    private String name;
    private List<Integer> parameters;
}
