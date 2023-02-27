package com.example.projetcleancodegroupe5.adapter;

import com.example.projetcleancodegroupe5.database.MySQLDatabase;
import com.example.projetcleancodegroupe5.functional.model.Hero;
import com.example.projetcleancodegroupe5.port.HeroDAO;
import org.springframework.stereotype.Repository;

@Repository
public class HeroDAOImplementation implements HeroDAO {
    MySQLDatabase database = new MySQLDatabase();
    @Override
    public void addHero(Hero hero) {
        database.addHero(hero);
    }

    @Override
    public void updateHero(String id, Hero hero) {
        database.updateHero(id, hero);
    }

    @Override
    public Hero getHero(String id){
        Hero hero = database.findHeroById(Float.parseFloat(id));
        return hero;
    }
}
