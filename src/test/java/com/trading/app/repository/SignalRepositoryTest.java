package com.trading.app.repository;

import com.trading.app.dto.Action;
import com.trading.app.dto.SignalRequest;
import com.trading.app.entity.Signal;
import com.trading.app.util.ObjectMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@SpringBootTest
public class SignalRepositoryTest {
    @Autowired
    private SignalRepository signalRepository;
    private Signal savedSignal;

    @BeforeEach
    void setUp() {
        SignalRequest signalSpec = getSampleSignalSpec();
        Signal mockSignal = ObjectMapperUtil.covertToSignal(signalSpec);
        savedSignal = signalRepository.save(mockSignal);
    }

    @Test
    public void testSaveSignal() {
        SignalRequest signalSpec = getSampleSignalSpec();
        Signal mockSignal = ObjectMapperUtil.covertToSignal(signalSpec);
        savedSignal = signalRepository.save(mockSignal);
        Signal retrievedSignal = signalRepository.findById(savedSignal.getId()).orElse(null);
        assertEquals(savedSignal.getId(), retrievedSignal.getId());
    }

    @Test
    public void testGetSignalByIdValidSignal() {
        Signal retrievedSignal = signalRepository.findById(1)
                .orElseThrow(() -> new ResourceAccessException("Signal not found for ID: 1"));
        assertThat(retrievedSignal.getId()).isEqualTo(savedSignal.getId());
    }

    private SignalRequest getSampleSignalSpec() {
        List<Action> actionList = new ArrayList<>();
        Action action = new Action("setUp", new ArrayList<>());
        actionList.add(action);

        return new SignalRequest(actionList);
    }
}
