package com.trading.app.repository;

import com.trading.app.dto.Action;
import com.trading.app.dto.SignalSpec;
import com.trading.app.entity.Signal;
import com.trading.app.service.SignalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class SignalRepositoryTest {

    @Autowired
    private SignalRepository signalRepository;

    @Autowired
    private SignalService signalService;

    @Test
    public void testSaveSignal() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action("setUp", new ArrayList<>()));
        SignalSpec signalSpec = new SignalSpec(actions);

        Signal savedSignal = signalService.saveSignal(signalSpec);
        Signal retrievedSignal = signalRepository.findById(savedSignal.getId()).orElse(null);

        assertEquals(savedSignal.getId(), retrievedSignal.getId());
    }
}
