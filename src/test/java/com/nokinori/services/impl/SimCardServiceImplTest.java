package com.nokinori.services.impl;

import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.api.SimCardService;
import com.nokinori.services.exceptions.NotFoundException;
import com.nokinori.services.exceptions.SimCardActivationException;
import com.nokinori.services.exceptions.SimCardBlockageException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.willReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimCardServiceImplTest {

    private final Long notExistId = 999L;

    private final Long simCardId = 1001L;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private SimCardService service;
    @MockBean
    private SimCardRepo repo;
    private SimCard simCard;

    @Before
    public void setUp() {
        simCard = new SimCard();
        simCard.setId(simCardId);

        willReturn(Optional.of(simCard)).given(repo)
                .findById(simCardId);
    }

    @Test
    public void activate() {
        simCard.setActive(false);
        service.activate(simCardId);

        assertTrue(simCard.isActive());
    }

    @Test
    public void block() {
        simCard.setActive(true);
        service.block(simCardId);

        assertFalse(simCard.isActive());
    }


    @Test
    public void alreadyActivated() {
        expectSimCardEx(true);

        simCard.setActive(true);
        service.activate(simCardId);
    }

    @Test
    public void alreadyBlocked() {
        expectSimCardEx(false);

        simCard.setActive(false);
        service.block(simCardId);
    }

    @Test
    public void simCardNotFoundForActivation() {
        expectNotFoundEx(notExistId);

        service.activate(notExistId);
    }

    @Test
    public void simCardNotFoundForBlockage() {
        expectNotFoundEx(notExistId);

        service.block(notExistId);
    }

    private void expectNotFoundEx(Long id) {
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("Sim-card with id: " + id + " not found");
    }

    private void expectSimCardEx(boolean isActivated) {
        String msg = "Sim-card with id: " + simCardId + " already ";
        if (isActivated) {
            exceptionRule.expect(SimCardActivationException.class);
            exceptionRule.expectMessage(msg + "activated");
        } else {
            exceptionRule.expect(SimCardBlockageException.class);
            exceptionRule.expectMessage(msg + "blocked");
        }
    }
}