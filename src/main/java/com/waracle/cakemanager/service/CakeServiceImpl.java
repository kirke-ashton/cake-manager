package com.waracle.cakemanager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.waracle.cakemanager.repository.CakeRepository;
import com.waracle.cakemanager.model.CakeEntity;
import com.waracle.cakemanager.model.Cake;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CakeServiceImpl implements CakeService {

    @Autowired
    private CakeRepository cakeRepository;

    @Override
    public List<Cake> getAllCakes() {

        List<Cake> cakes = cakeRepository.findAll().stream().sorted().map(cakeEntity ->
                new Cake(cakeEntity.getId(), cakeEntity.getTitle(), cakeEntity.getDesc(), cakeEntity.getImage()))
                .collect(Collectors.toList());

        return cakes;
    }

    @Override
    public Cake getCakeById(Long id) {

        Cake cake = cakeRepository.findById(id)
                .map(cakeEntity ->
                        new Cake(cakeEntity.getId(), cakeEntity.getTitle(), cakeEntity.getDesc(), cakeEntity.getImage())
                )
                .orElseGet(() -> {
                    return null;
                });

        return cake;
    }

    @Override
    public Cake saveCake(Cake cake) {

        CakeEntity cakeEntity = new CakeEntity(cake.getTitle(), cake.getDesc(), cake.getImage());
        cakeEntity.setId(cake.getId());

        cakeEntity = cakeRepository.save(cakeEntity);

        cake.setId(cakeEntity.getId());

        return cake;
    }

    @Override
    public void deleteCakeById(Long id) {

        cakeRepository.deleteById(id);
    }
}
