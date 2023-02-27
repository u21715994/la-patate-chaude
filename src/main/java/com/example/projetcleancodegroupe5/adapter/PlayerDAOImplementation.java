package com.example.projetcleancodegroupe5.adapter;

import com.example.projetcleancodegroupe5.database.Database;
import com.example.projetcleancodegroupe5.database.MySQLDatabase;
import com.example.projetcleancodegroupe5.functional.model.Hero;
import com.example.projetcleancodegroupe5.functional.model.Player;
import com.example.projetcleancodegroupe5.port.PlayerDAO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class PlayerDAOImplementation implements PlayerDAO {
    MySQLDatabase database = new MySQLDatabase();
    @Override
    public void addPlayer(Player player){
        database.addPlayer(player);
        database.addDeck(player, new ArrayList<>());
    }

    @Override
    public void updatePlayer(String id, Player player){
        database.updatePlayer(id, player);
    }

    @Override
    public Player findPlayer(String id){
        Player player = database.findPlayerById(id);
        return player;
    }
}
