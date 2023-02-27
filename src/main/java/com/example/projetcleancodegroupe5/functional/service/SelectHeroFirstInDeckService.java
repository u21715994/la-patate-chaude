package com.example.projetcleancodegroupe5.functional.service;

import com.example.projetcleancodegroupe5.functional.model.Deck;
import com.example.projetcleancodegroupe5.functional.model.Hero;
import com.example.projetcleancodegroupe5.functional.model.Player;

import java.util.ArrayList;
import java.util.List;

public class SelectHeroFirstInDeckService implements SelectHeroType{
    @Override
    public static List<Hero> selectHero(Player attacker, Player defender) {
        Deck deckAttacker = ShowDeckPlayerService.showDeck(attacker);
        Hero hero_attacker = deckAttacker.getCards().get(0);
        List<Hero> heroOfPlayer = HeroFinderService.findHero(defender.getDeck(), hero_attacker.getLevel());
        Hero hero_defender = heroOfPlayer.get(0);
        List<Hero> heroesInvolved = new ArrayList<>();
        heroesInvolved.add(hero_attacker);
        heroesInvolved.add(hero_defender);
        return heroesInvolved;
    }
}
