package com.waracle.cakemanager.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.springframework.beans.factory.annotation.Autowired;

import com.waracle.cakemanager.model.CakeEntity;
import com.waracle.cakemanager.model.Cake;
import com.waracle.cakemanager.repository.CakeRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CakeServiceTest {

    private static final Long INVALID_ID = Long.parseLong("100");

    @Mock
    private CakeRepository cakeRepository;

    @Autowired
    @InjectMocks
    private CakeServiceImpl cakeService;

    @Test
    public void test_getNoCakes() throws Exception {

        when(cakeRepository.findAll()).thenReturn(new ArrayList<CakeEntity>());

        List<Cake> cakes = cakeService.getAllCakes();

        assertEquals(0, cakes.size());

        verify(cakeRepository, times(1)).findAll();
    }

    @Test
    public void test_getAllCakes() throws Exception {

        CakeEntity entityCake1 = new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");
        CakeEntity entityCake2 = new CakeEntity("Butter", "Contains Butter", "ImageOfButterCake");
        List<CakeEntity> entityCakes = new ArrayList<>();
        entityCakes.add(entityCake1);
        entityCakes.add(entityCake2);

        when(cakeRepository.findAll()).thenReturn(entityCakes);

        List<Cake> cakes = cakeService.getAllCakes();

        assertEquals(2, cakes.size());
        assertEquals("Butter", cakes.get(0).getTitle());
        assertEquals("Fruit", cakes.get(1).getTitle());

        verify(cakeRepository, times(1)).findAll();
    }

    @Test
    public void test_NoCakeReturnedWithInvalidId() throws Exception {

        Optional<CakeEntity> optionalEntityCake = Optional.ofNullable(null);

        when(cakeRepository.findById(INVALID_ID)).thenReturn(optionalEntityCake);

        Cake cake = cakeService.getCakeById(INVALID_ID);

        assertNull(cake);

        verify(cakeRepository, times(1)).findById(INVALID_ID);
    }

    @Test
    public void test_getCakeById() throws Exception {

        Long cakeId = Long.parseLong("10");

        Optional<CakeEntity> optionalEntityCake = Optional.of(new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake"));
        optionalEntityCake.get().setId(cakeId);

        when(cakeRepository.findById(cakeId)).thenReturn(optionalEntityCake);

        Cake cake = cakeService.getCakeById(cakeId);

        assertEquals("Fruit", cake.getTitle());
        assertEquals(cakeId, cake.getId());

        verify(cakeRepository, times(1)).findById(cakeId);
    }

    @Test
    public void test_SaveCake() throws Exception {

        Long cakeId = Long.parseLong("5");

        CakeEntity entityCake = new CakeEntity("Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");
        entityCake.setId(cakeId);

        when(cakeRepository.save(any(CakeEntity.class))).thenReturn(entityCake);

        Cake cake = new Cake(null, "Fruit", "Contains Multiple Fruits", "ImageOfFruitCake");

        cake = cakeService.saveCake(cake);

        assertEquals("Fruit", cake.getTitle());
        assertEquals(cakeId, cake.getId());

        verify(cakeRepository, times(1)).save(any(CakeEntity.class));
    }

    @Test
    public void test_DeleteCake() throws Exception {

        Long cakeId = Long.parseLong("5");

        cakeService.deleteCakeById(cakeId);

        verify(cakeRepository, times(1)).deleteById(cakeId);
    }
}
