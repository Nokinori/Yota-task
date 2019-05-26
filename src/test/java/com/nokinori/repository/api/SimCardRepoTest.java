package com.nokinori.repository.api;

import com.nokinori.repository.entities.MinutesPack;
import com.nokinori.repository.entities.SimCard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SimCardRepoTest {

    @Autowired
    private SimCardRepo repo;
    private SimCard entity;

    @Before
    public void setUp() {
        entity = new SimCard();
        entity.setActive(true);
    }

    @Test
    public void entityCreated() {
        Optional<SimCard> found = createEntity();
        assertTrue(found.isPresent());
    }

    @Test
    public void entityUpdated() {
        Optional<SimCard> found = createEntity();
        assertTrue(found.isPresent());
        assertTrue(found.get()
                .isActive());

        entity.setActive(false);
        repo.save(entity);
        Optional<SimCard> found2 = repo.findById(entity.getId());
        assertTrue(found2.isPresent());
        assertFalse(found2.get()
                .isActive());
    }

    @Test
    public void entityRemoved() {
        createEntity();
        repo.delete(entity);
        Optional<SimCard> found3 = repo.findById(entity.getId());
        assertFalse(found3.isPresent());
    }

    @Test
    public void notFoundEntity() {
        Long id = 1001L;
        Optional<SimCard> found = repo.findById(id);

        assertFalse(found.isPresent());
    }

    @Test
    public void entityEquals() {
        SimCard persist = repo.save(entity);
        Optional<SimCard> found = repo.findById(persist.getId());

        assertTrue(found.isPresent());
        assertEquals(persist, found.get());
    }

    @Test
    public void entityChildCreated() {
        Optional<SimCard> entityWithChild = createEntityWithChild();
        assertTrue(entityWithChild.isPresent());
        assertFalse(entityWithChild.get()
                .getMinutesPacks()
                .isEmpty());
        assertEquals(2, entityWithChild.get()
                .getMinutesPacks()
                .size());
    }

    private Optional<SimCard> createEntity() {
        SimCard persist = repo.save(entity);
        return repo.findById(persist.getId());
    }

    private Optional<SimCard> createEntityWithChild() {
        MinutesPack m1 = createMinutes();
        MinutesPack m2 = createMinutes();
        List<MinutesPack> minutes = Arrays.asList(m1, m2);
        entity.setMinutesPacks(minutes);
        SimCard persist = repo.save(entity);
        return repo.findById(persist.getId());
    }

    private MinutesPack createMinutes() {
        MinutesPack minutesPack = new MinutesPack();
        minutesPack.setSimCard(entity);
        minutesPack.setAmount(100);
        minutesPack.setExpiresAt(LocalDateTime.now()
                .plusMinutes(10));
        return minutesPack;
    }
}