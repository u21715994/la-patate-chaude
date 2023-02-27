package com.example.projetcleancodegroupe5.adapter.controller;

import com.example.projetcleancodegroupe5.functional.model.Hero;
import com.example.projetcleancodegroupe5.functional.model.Player;
import com.example.projetcleancodegroupe5.functional.service.PlayerOpenPackService;
import com.example.projetcleancodegroupe5.functional.service.SelectHeroFirstInDeckService;
import com.example.projetcleancodegroupe5.port.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController

public class PlayerController {
    @Autowired
    private PlayerDAO playerDAO;

    @PostMapping(
            path = "/player",
            consumes = "application/json",
            produces = "application/json"
    )
    public void addHero(@RequestBody Player player) {
        Player player_add = new Player(player.getName());
        playerDAO.addPlayer(player_add);
    }

    @PutMapping(
            path = "/player/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<Object> updatePlayer(@PathVariable String id, @RequestBody Player player) {
        playerDAO.updatePlayer(id, player);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(player.getID())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(
            path = "/player/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public Player findPlayer(@PathVariable String id) {
        return playerDAO.findPlayer(id);
    }

    @PostMapping(
            path = "/player/open_pack/{id}/{token}",
            consumes = "application/json",
            produces = "application/json"
    )
    public Player openPack(@PathVariable String id, @PathVariable int token){
        Player player = this.findPlayer(id);
        player = PlayerOpenPackService.openPack(player, token);
        updatePlayer(id, player);
        return player;
    }

    @GetMapping(
            path = "/player/open_pack/{id}/{token}",
            consumes = "application/json",
            produces = "application/json"
    )
    public List<Player> attackPlayer(@PathVariable String player_attacker_id,
                                     @PathVariable String player_defender_id){
        /*Player player_attacker = this.findPlayer(player_attacker_id);
        Player player_defender = this.findPlayer(player_defender_id);
        List<Hero> heroesInvolved = SelectHeroFirstInDeckService.selectHero(player_attacker, player_defender);
        new HeroController().updateHero(Float.toString(heroesInvolved.get(0).getID()), heroesInvolved.get(0).defend(heroesInvolved.get(1)));
        new HeroController().updateHero(Float.toString(heroesInvolved.get(1).getID()), heroesInvolved.get(1).defend(heroesInvolved.get(0)));*/
        return null;
    }
}
