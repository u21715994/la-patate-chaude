package com.example.projetcleancodegroupe5.database;

import com.example.projetcleancodegroupe5.functional.model.Hero;
import com.example.projetcleancodegroupe5.functional.model.Player;

import java.util.List;

public interface Database {
    void close();

    void addHero(Hero hero);

    void updateHero(String id, Hero hero);

    void addPlayer(Player player);

    void updatePlayer(String id, Player player);

    void addDeck(Player player, List<Hero> heroList);

    void updateDeck(Player player, List<Hero> heroList);

    Player findPlayerById(String id);

    Hero findHeroById(float id);

    Player findPlayerByName(String name);

    Hero findHeroByName(String name);
}