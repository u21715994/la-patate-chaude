package com.example.projetcleancodegroupe5.functional.model;

public final class HeroFactory {
    public static Hero createHero(Speciality speciality, Rarity rarity, String name){
        if(speciality == Speciality.TANK)
            return new HeroTank(name, rarity);
        if(speciality == Speciality.ASSASSIN)
            return new HeroAssassin(name, rarity);
        return new HeroWizard(name, rarity);
    }

    public static Hero createHero(float id, String name, float lifePoint, String speciality, float experiencePoint,
    float power, float armor, String rarity, float level){
        if(Speciality.valueOf(speciality) == Speciality.TANK)
            return new HeroTank(id, name, (int) lifePoint, (int) experiencePoint, (int) power, (int) armor,
        Rarity.valueOf(rarity), (int) level);
        if(Speciality.valueOf(speciality) == Speciality.ASSASSIN)
            return new HeroAssassin(id, name, (int) lifePoint, (int) experiencePoint, (int) power, (int) armor,
                    Rarity.valueOf(rarity), (int) level);
        return new HeroWizard(id, name, (int) lifePoint, (int) experiencePoint, (int) power, (int) armor,
                Rarity.valueOf(rarity), (int) level);
    }
}
