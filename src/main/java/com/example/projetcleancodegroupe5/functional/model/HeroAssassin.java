package com.example.projetcleancodegroupe5.functional.model;

import java.util.Objects;

public class HeroAssassin implements Hero {
    float id = 1;
    String name;
    int lifePoint;
    int experiencePoint;
    int power;
    int armor;
    Rarity rarity;
    int level;

    public HeroAssassin(String name, Rarity rarity){
        this.name = name;
        this.lifePoint = 800 + (800/10) * rarity.getValue();
        this.experiencePoint = 0;
        this.power = 200 + (200/10) * rarity.getValue();
        this.armor = 5 + (5/10) * rarity.getValue();
        this.rarity = rarity;
        this.level = 1;
    }

    public HeroAssassin(float id, String name, int lifePoint, int experiencePoint, int power, int armor,
    Rarity rarity, int level){
        this.id = id;
        this.name = name;
        this.lifePoint = lifePoint;
        this.experiencePoint = experiencePoint;
        this.power = power;
        this.armor = armor;
        this.rarity = rarity;
        this.level = level;
    }

    @Override
    public void setId(float id) {
        this.id = id;
    }

    @Override
    public float getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRarity() {
        return rarity.toString();
    }

    @Override
    public String getSpeciality() {
        return "Assassin";
    }


    @Override
    public int getLifePoint() {
        return lifePoint;
    }

    @Override
    public int getArmor() {
        return armor;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getExperiencePoint() {
        return experiencePoint;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public Hero setLifePoint(int lifePoint) {
        return new HeroAssassin(this.id, this.name, this.lifePoint + lifePoint, this.experiencePoint,
                this.power, this.armor, this.rarity, this.level
        );
    }

    @Override
    public Hero levelUp(){
        return new HeroAssassin(this.id, this.name, this.lifePoint + this.lifePoint / 10, this.experiencePoint,
                this.power + this.power / 10, this.armor + this.armor / 10, this.rarity, this.level+1
        );
    }

    @Override
    public String toString() {
        return "Hero{" +
                "name='" + name + '\'' +
                "speciality=Assassin"+'\''+
                ", lifePoint=" + lifePoint +
                ", experiencePoint=" + experiencePoint +
                ", power=" + power +
                ", armor=" + armor +
                ", rarity='" + rarity + '\'' +
                ", level=" + level +
                '}';
    }

    @Override
    public Hero battleWin(){
        return new HeroAssassin(this.id, this.name, this.lifePoint + this.lifePoint, this.experiencePoint+1,
                this.power, this.armor, this.rarity, this.level
        );
    }

    @Override
    public Hero defend(Hero hero) {
        return new HeroAssassin(this.id, this.name, this.lifePoint - (hero.getPower() + hero.getPower() - this.getArmor()),
                this.experiencePoint, this.power, this.armor, this.rarity, this.level
        );
    }

    @Override
    public boolean isDead() {
        return this.lifePoint <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeroAssassin that = (HeroAssassin) o;
        return lifePoint == that.lifePoint && experiencePoint == that.experiencePoint && power == that.power && armor == that.armor && level == that.level && Objects.equals(name, that.name) && rarity == that.rarity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lifePoint, experiencePoint, power, armor, rarity, level);
    }
}
