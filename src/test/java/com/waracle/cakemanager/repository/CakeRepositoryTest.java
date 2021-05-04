package com.waracle.cakemanager.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.waracle.cakemanager.model.CakeEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CakeRepositoryTest {

    private static final Long INVALID_ID = Long.parseLong("100");

    @Autowired
    private CakeRepository cakeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void test_ReturnsNoCakes() {

        List<CakeEntity> allCakes = cakeRepository.findAll();

        assertEquals(0, allCakes.size());
    }

    @Test
    public void test_ReturnsOneCake() {
        CakeEntity cake = new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");

        entityManager.persist(cake);
        entityManager.flush();

        List<CakeEntity> allCakes = cakeRepository.findAll();

        assertEquals(1, allCakes.size());
        assertEquals("Fruit", allCakes.get(0).getTitle());
    }

    @Test
    public void test_ReturnsMultipleCakes() {
        CakeEntity cake1 = new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");
        CakeEntity cake2 = new CakeEntity("Butter", "Contains Butter", "ImageOfButter");

        entityManager.persist(cake1);
        entityManager.flush();

        entityManager.persist(cake2);
        entityManager.flush();

        List<CakeEntity> allCakes = cakeRepository.findAll().stream().sorted().collect(Collectors.toList());

        assertEquals(2, allCakes.size());
        assertEquals("Butter", allCakes.get(0).getTitle());
        assertEquals("Fruit", allCakes.get(1).getTitle());
    }

    @Test
    public void test_ReturnsCakeWithCorrectId() {
        CakeEntity cake = new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");

        cakeRepository.save(cake);

        Optional<CakeEntity> cakefoundOptional = cakeRepository.findById(cake.getId());

        assertTrue(cakefoundOptional.isPresent());
        assertEquals("Fruit", cakefoundOptional.get().getTitle());
    }

    @Test
    public void test_DoesNotReturnCakeWithInvalidId() {
        CakeEntity cake = new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");

        cakeRepository.save(cake);

        Optional<CakeEntity> cakefoundOptional = cakeRepository.findById(INVALID_ID);

        assertFalse(cakefoundOptional.isPresent());
    }

    @Test
    public void test_FindCakeById() {
        CakeEntity cake1 = new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");
        CakeEntity cake2 = new CakeEntity("Butter", "Contains Butter", "ImageOfButterCake");

        cake1 = cakeRepository.save(cake1);
        cake2 = cakeRepository.save(cake2);

        List<CakeEntity> allCakes = cakeRepository.findAll().stream().sorted().collect(Collectors.toList());

        assertEquals(2, allCakes.size());
        assertEquals("Butter", allCakes.get(0).getTitle());
        assertEquals("Fruit", allCakes.get(1).getTitle());

        Optional<CakeEntity> cakefoundOptional = cakeRepository.findById(cake1.getId());

        assertTrue(cakefoundOptional.isPresent());
        assertEquals("Fruit", cakefoundOptional.get().getTitle());
     }

    @Test
    public void test_DeleteCakeById() {
        CakeEntity cake1 = new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");
        CakeEntity cake2 = new CakeEntity("Butter", "Contains Butter", "ImageOfButterCake");

        cake1 = cakeRepository.save(cake1);
        cake2 = cakeRepository.save(cake2);

        List<CakeEntity> allCakes = cakeRepository.findAll().stream().sorted().collect(Collectors.toList());

        assertEquals(2, allCakes.size());
        assertEquals("Butter", allCakes.get(0).getTitle());
        assertEquals("Fruit", allCakes.get(1).getTitle());

        cakeRepository.deleteById(cake2.getId());

        allCakes = cakeRepository.findAll();

        assertEquals(1, allCakes.size());
        assertEquals("Fruit", allCakes.get(0).getTitle());
    }
}
