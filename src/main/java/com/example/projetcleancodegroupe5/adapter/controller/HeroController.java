package com.example.projetcleancodegroupe5.adapter.controller;

import com.example.projetcleancodegroupe5.functional.model.Hero;
import com.example.projetcleancodegroupe5.port.HeroDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController

public class HeroController {
    @Autowired
    private HeroDAO heroDAO;

    @GetMapping(
            path = "/hero",
            produces = "application/json"
    )
    public void addHero(Hero hero){
        heroDAO.addHero(hero);
    }

    @GetMapping(
            path = "/hero/{id}",
            produces = "application/json"
    )
    public ResponseEntity<Object> updateHero(@PathVariable String id, @RequestBody Hero hero){
        heroDAO.updateHero(id, hero);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(hero.getID())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
