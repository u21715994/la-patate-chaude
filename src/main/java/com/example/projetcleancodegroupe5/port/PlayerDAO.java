package com.example.projetcleancodegroupe5.port;

import com.example.projetcleancodegroupe5.functional.model.Player;

public interface PlayerDAO {
    void addPlayer(Player player);

    void updatePlayer(String id, Player player);
}
