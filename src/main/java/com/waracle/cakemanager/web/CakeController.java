package com.waracle.cakemanager.web;

import com.waracle.cakemanager.service.CakeService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.waracle.cakemanager.model.Cake;

import java.util.List;

@RestController
public class CakeController {

    @Autowired
    private CakeService cakeService;

    public CakeController() {
    }

    @GetMapping(value = "/cakes", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ROLE_GLOBAL_ADMIN') or hasRole('ROLE_GLOBAL_ACCESS')")
    public ResponseEntity<List<Cake>> getAllCakes() {

        return ResponseEntity.ok(cakeService.getAllCakes());
    }

    @GetMapping(value = "/cakes/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ROLE_GLOBAL_ADMIN') or hasRole('ROLE_GLOBAL_ACCESS')")
    public ResponseEntity<?> getCake(@PathVariable Long id) {

        Cake cake = cakeService.getCakeById(id);

        if (cake != null) {
            return ResponseEntity.ok(cake);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/cakes", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ROLE_GLOBAL_ADMIN')")
    public ResponseEntity<Cake> saveCake(@RequestBody Cake cake) {

        return ResponseEntity.ok(cakeService.saveCake(cake));
    }

    @PutMapping(value = "/cakes/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ROLE_GLOBAL_ADMIN')")
    public ResponseEntity<Cake> updateCake(@RequestBody Cake cake, @PathVariable Long id) {

        Cake updatedCake = cakeService.getCakeById(id);

        Cake cakeSaved = null;

        if (updatedCake != null) {
            updatedCake.setTitle(cake.getTitle());
            updatedCake.setDesc(cake.getDesc());
            updatedCake.setImage(cake.getImage());
            cakeSaved = cakeService.saveCake(updatedCake);
        } else {
            cakeSaved = cakeService.saveCake(cake);
        }

        return ResponseEntity.ok(cakeSaved);
    }

    @DeleteMapping("/cakes/{id}")
    @PreAuthorize("hasRole('ROLE_GLOBAL_ADMIN')")
    public ResponseEntity<Cake> deleteCake(@PathVariable long id) {
        cakeService.deleteCakeById(id);

        return ResponseEntity.ok().build();
    }
}