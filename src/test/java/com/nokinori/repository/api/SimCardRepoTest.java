package com.nokinori.repository.api;

import com.nokinori.repository.entities.SimCards;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SimCardRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SimCardRepo repo;

    private SimCards entity;

    private final Long USER_ID = 1001L;

    @Before
    public void setUp() {
        entity = new SimCards();
        entity.setUserId(USER_ID);
        entity.setActive(true);
    }

    @Test
    public void entityCreated() {
        Optional<SimCards> found = createEntity();
        assertTrue(found.isPresent());
    }

    @Test
    public void entityUpdated() {
        Optional<SimCards> found = createEntity();
        assertTrue(found.isPresent());
        assertTrue(found.get()
                .isActive());

        entity.setActive(false);
        entityManager.merge(entity);
        Optional<SimCards> found2 = repo.findById(entity.getId());
        assertTrue(found2.isPresent());
        assertFalse(found2.get()
                .isActive());
    }

    @Test
    public void entityRemoved() {
        createEntity();
        entityManager.remove(entity);
        Optional<SimCards> found3 = repo.findById(entity.getId());
        assertFalse(found3.isPresent());
    }

    @Test
    public void whenFindByUserIdThenReturnSimCard() {
        entityManager.persist(entity);

        Optional<SimCards> found = repo.findByUserId(USER_ID);
        assertTrue(found.isPresent());
        assertEquals(USER_ID, found.get()
                .getUserId());
    }

    @Test
    public void notFoundEntity() {
        Optional<SimCards> found = repo.findByUserId(USER_ID);

        assertFalse(found.isPresent());
    }

    @Test
    public void entityEquals() {
        SimCards persist = entityManager.persist(entity);
        Optional<SimCards> found = repo.findById(persist.getId());

        assertTrue(found.isPresent());
        assertEquals(persist, found.get());
    }


    private Optional<SimCards> createEntity() {
        SimCards persist = entityManager.persist(entity);
        return repo.findById(persist.getId());
    }
}