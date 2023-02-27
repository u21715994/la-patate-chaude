package com.example.projetcleancodegroupe5.port;

import com.example.projetcleancodegroupe5.functional.model.Hero;

public interface HeroDAO {
    void addHero(Hero hero);

    void updateHero(String id, Hero hero);
    Hero getHero(String id);
}
