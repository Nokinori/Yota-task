package com.nokinori.repository.api;

import com.nokinori.repository.entities.Gigabytes;
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
public class GigabytesRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GigabytesRepo repo;

    private Gigabytes entity;

    private final Long USER_ID = 1001L;

    private final Long AMOUNT = 100L;

    @Before
    public void setUp() {
        entity = new Gigabytes();
        entity.setUserId(USER_ID);
        entity.setAmount(AMOUNT);
    }

    @Test
    public void entityCreated() {
        Optional<Gigabytes> found = createEntity();
        assertTrue(found.isPresent());
    }

    @Test
    public void entityUpdated() {
        Optional<Gigabytes> found = createEntity();
        assertTrue(found.isPresent());
        assertEquals(AMOUNT, found.get()
                .getAmount());

        Long newAmount = 200L;
        entity.setAmount(newAmount);
        entityManager.merge(entity);
        Optional<Gigabytes> found2 = repo.findById(entity.getId());
        assertTrue(found2.isPresent());
        assertEquals(newAmount, found2.get()
                .getAmount());
    }

    @Test
    public void entityRemoved() {
        createEntity();
        entityManager.remove(entity);
        Optional<Gigabytes> found3 = repo.findById(entity.getId());
        assertFalse(found3.isPresent());
    }

    @Test
    public void whenFindByUserIdThenReturnSimCard() {
        entityManager.persist(entity);

        Optional<Gigabytes> found = repo.findByUserId(USER_ID);
        assertTrue(found.isPresent());
        assertEquals(USER_ID, found.get()
                .getUserId());
    }

    @Test
    public void notFoundEntity() {
        Optional<Gigabytes> found = repo.findByUserId(USER_ID);

        assertFalse(found.isPresent());
    }

    @Test
    public void entityEquals() {
        Gigabytes persist = entityManager.persist(entity);
        Optional<Gigabytes> found = repo.findById(persist.getId());

        assertTrue(found.isPresent());
        assertEquals(persist, found.get());
    }


    private Optional<Gigabytes> createEntity() {
        Gigabytes persist = entityManager.persist(entity);
        return repo.findById(persist.getId());
    }

}