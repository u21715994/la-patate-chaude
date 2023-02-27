package com.example.projetcleancodegroupe5.database;

import com.example.projetcleancodegroupe5.functional.model.Hero;
import com.example.projetcleancodegroupe5.functional.model.HeroFactory;
import com.example.projetcleancodegroupe5.functional.model.Player;

import java.sql.*;
import java.util.List;

public class MySQLDatabase implements Database{
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://91.121.230.45:3306/cleancode";
    private static final String USER = "cleancode";
    private static final String PASS = "OX0nQ7%4*!5cjm_A";

    private Connection conn;

    public MySQLDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connexion à la base de données réussie !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement du driver JDBC : " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    @Override
    public void addHero(Hero hero) {
        String sql = "INSERT INTO hero (name, life_point, speciality, experience_point, power, armor, rarity, level) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, hero.getName());
            statement.setFloat(2, hero.getLifePoint());
            statement.setString(3, hero.getSpeciality());
            statement.setFloat(4, hero.getExperiencePoint());
            statement.setFloat(5, hero.getPower());
            statement.setFloat(6, hero.getArmor());
            statement.setString(7, hero.getRarity());
            statement.setFloat(8, hero.getLevel());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    hero.setId(rs.getFloat(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du hero à la base de données : " + e.getMessage());
        }
    }

    @Override
    public void updateHero(String id, Hero hero) {
        String sql = "UPDATE hero SET name = ?, life_point = ?, speciality = ?, experience_point = ?, power = ?, armor = ?, rarity = ?, level = ? WHERE id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, hero.getName());
            statement.setFloat(2, hero.getLifePoint());
            statement.setString(3, hero.getSpeciality());
            statement.setFloat(4, hero.getExperiencePoint());
            statement.setFloat(5, hero.getPower());
            statement.setFloat(6, hero.getArmor());
            statement.setString(7, hero.getRarity());
            statement.setFloat(8, hero.getLevel());
            statement.setString(9, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du hero dans la base de données : " + e.getMessage());
        }
    }

    @Override
    public void addPlayer(Player player) {
        String sql = "INSERT INTO player (name, token, numberBattleWin) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getName());
            statement.setLong(2, player.getToken());
            statement.setLong(3, player.getNumberBattleWin());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    player.setId(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du joueur à la base de données : " + e.getMessage());
        }
    }

    @Override
    public void updatePlayer(String id, Player player) {
        String sql = "UPDATE player SET name = ?, token = ?, numberBattleWin = ? WHERE id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getName());
            statement.setLong(2, player.getToken());
            statement.setLong(3, player.getNumberBattleWin());
            statement.setString(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du joueur dans la base de données : " + e.getMessage());
        }
    }

    @Override
    public void addDeck(Player player, List<Hero> heroList) {
        String sql = "INSERT INTO deck (player_id, hero_id) VALUES (?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (Hero hero : heroList) {
                statement.setString(1, player.getID());
                statement.setFloat(2, hero.getID());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du deck à la base de données : " + e.getMessage());
        }
    }

    @Override
    public void updateDeck(Player player, List<Hero> heroList) {
        String deleteSql = "DELETE FROM deck WHERE player_id = ?";
        String insertSql = "INSERT INTO deck (player_id, hero_id) VALUES (?, ?)";
        try {
            PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
            deleteStatement.setString(1, player.getID());
            deleteStatement.executeUpdate();
            PreparedStatement insertStatement = conn.prepareStatement(insertSql);
            for (Hero hero : heroList) {
                insertStatement.setString(1, player.getID());
                insertStatement.setFloat(2, hero.getID());
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du deck dans la base de données : " + e.getMessage());
        }
    }

    @Override
    public Player findPlayerById(String id) {
        String sql = "SELECT * FROM player WHERE id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Player player = new Player(result.getString("id"), result.getString("name"), result.getLong("token"), result.getLong("numberBattleWin"));
                return player;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du joueur dans la base de données : " + e.getMessage());
        }
        return null;
    }

    @Override
    public Hero findHeroById(float id) {
        String sql = "SELECT * FROM hero WHERE id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setFloat(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Hero hero = HeroFactory.createHero(result.getFloat("id"), result.getString("name"), result.getFloat("life_point"), result.getString("speciality"), result.getFloat("experience_point"), result.getFloat("power"), result.getFloat("armor"), result.getString("rarity"), result.getFloat("level"));
                return hero;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du hero dans la base de données : " + e.getMessage());
        }
        return null;
    }

    @Override
    public Player findPlayerByName(String name) {
        String sql = "SELECT * FROM player WHERE name = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Player player = new Player(result.getString("id"), result.getString("name"), result.getLong("token"), result.getLong("numberBattleWin"));
                return player;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du joueur dans la base de données : " + e.getMessage());
        }
        return null;
    }

    @Override
    public Hero findHeroByName(String name) {
        String sql = "SELECT * FROM hero WHERE name = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Hero hero = HeroFactory.createHero(result.getFloat("id"), result.getString("name"), result.getFloat("life_point"), result.getString("speciality"), result.getFloat("experience_point"), result.getFloat("power"), result.getFloat("armor"), result.getString("rarity"), result.getFloat("level"));
                return hero;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du hero dans la base de données : " + e.getMessage());
        }
        return null;
    }

}