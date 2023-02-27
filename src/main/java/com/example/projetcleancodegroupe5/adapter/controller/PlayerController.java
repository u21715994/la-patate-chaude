package com.example.projetcleancodegroupe5.adapter.controller;

import com.example.projetcleancodegroupe5.functional.model.Player;
import com.example.projetcleancodegroupe5.port.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController

public class PlayerController {
    @Autowired
    private PlayerDAO playerDAO;
    @PostMapping(
            path = "/player",
            consumes = "application/json",
            produces = "application/json"
    )
    public void addHero(Player player){
        playerDAO.addPlayer(player);
    }

    @PutMapping(
            path = "/player/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<Object> updatePlayer(@PathVariable String id, @RequestBody Player player){
        playerDAO.updatePlayer(id, player);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(player.getID())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
