package com.nokinori.services.impl;

import com.nokinori.api.io.SimCardRs;
import com.nokinori.repository.api.SimCardRepo;
import com.nokinori.repository.entities.MinutesPack;
import com.nokinori.repository.entities.SimCard;
import com.nokinori.services.api.BillingService;
import com.nokinori.services.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.function.Supplier;

import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MinutesServiceImplTest {

    private final Long notExistId = 999L;

    private final Long simCardId = 1001L;

    private final Integer amount = 100;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    @Qualifier("minutesService")
    private BillingService<SimCardRs> service;

    @MockBean
    private SimCardRepo repo;

    private SimCard simCard;

    private Supplier<MinutesPack> packSupplier = () -> {
        MinutesPack pack = new MinutesPack();
        pack.setSimCard(simCard);
        pack.setAmount(amount);
        pack.setExpiresAt(now());
        return pack;
    };

    @Before
    public void setUp() {
        simCard = new SimCard();
        simCard.setId(simCardId);

        willReturn(Optional.of(simCard)).given(repo)
                .findById(simCardId);
    }

    @Test
    public void get() {
        SimCardRs rs = service.get(simCardId);

        assertEquals(simCardId, rs.getSimCardId());
        assertTrue(rs.getMinutesPacks()
                .isEmpty());
    }

    @Test
    public void getTwoPacks() {
        addNPacksToEntity(2);

        SimCardRs rs = service.get(simCardId);

        assertEquals(2, rs.getMinutesPacks()
                .size());
    }

    @Test
    public void add() {
        service.add(simCardId, amount);

        verify(repo).save(simCard);
        assertEquals(1, simCard.getMinutesPacks()
                .size());
        assertEquals(amount, simCard.getMinutesPacks()
                .get(0)
                .getAmount());
    }

    @Test
    public void addTwoPacks() {
        service.add(simCardId, amount);
        service.add(simCardId, amount);

        verify(repo, times(2)).save(simCard);
        assertEquals(2, simCard.getMinutesPacks()
                .size());
        assertEquals(amount, simCard.getMinutesPacks()
                .get(0)
                .getAmount());
        assertEquals(amount, simCard.getMinutesPacks()
                .get(1)
                .getAmount());
    }

    @Test
    public void subtract() {
        addNPacksToEntity(1);

        service.subtract(simCardId, amount);

        verify(repo).save(simCard);
        assertEquals(0, simCard.getMinutesPacks()
                .size());
    }

    @Test
    public void subtractFromTwoPacks() {
        addNPacksToEntity(2);

        service.subtract(simCardId, amount);

        verify(repo).save(simCard);
        assertEquals(1, simCard.getMinutesPacks()
                .size());
    }

    @Test
    public void subtractMoreThanOnePack() {
        addNPacksToEntity(2);

        service.subtract(simCardId, amount + 50);

        verify(repo).save(simCard);
        assertEquals(1, simCard.getMinutesPacks()
                .size());
        assertEquals(50, simCard.getMinutesPacks()
                .get(0)
                .getAmount()
                .longValue());
    }

    @Test
    public void subtractLessThanOnePack() {
        int half = amount / 2;
        addNPacksToEntity(2);

        service.subtract(simCardId, half);

        verify(repo).save(simCard);
        assertEquals(2, simCard.getMinutesPacks()
                .size());
        assertEquals(half, simCard.getMinutesPacks()
                .get(0)
                .getAmount()
                .longValue());
    }

    @Test
    public void getNotFound() {
        expectNotFoundEx();

        service.get(notExistId);
    }


    @Test
    public void addNotFound() {
        expectNotFoundEx();

        service.add(notExistId, amount);

        verify(repo, never()).save(simCard);
    }

    @Test
    public void subtractNotFound() {
        expectNotFoundEx();

        service.subtract(notExistId, amount);

        verify(repo, never()).save(simCard);
    }

    @Test
    public void packIsEmptyInSubtract() {
        expectNotFoundEx();

        service.subtract(simCardId, amount);

        verify(repo, never()).save(simCard);
    }


    private void addNPacksToEntity(int n) {
        for (int i = 0; i < n; i++) {
            simCard.getMinutesPacks()
                    .add(packSupplier.get());
        }
    }

    private void expectNotFoundEx() {
        exceptionRule.expect(NotFoundException.class);
    }
}