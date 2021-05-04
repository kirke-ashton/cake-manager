package com.waracle.cakemanager.service;

import com.waracle.cakemanager.model.Cake;

import java.util.List;

public interface CakeService {

    public List<Cake> getAllCakes();

    public Cake getCakeById(Long id);

    public Cake saveCake(Cake cake);

    public void deleteCakeById(Long id);
}
