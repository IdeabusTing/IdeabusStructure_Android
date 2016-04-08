package com.ideabus.ideabus_structure.database;

/**
 * Created by Ting on 15/11/20.
 */
public class UserData {

    public final static String USER_TB_NAME = "user";

    private String name;
    private int age;
    private int weight;
    private int gender;
    private String goals;
    private String notes;

    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String WEIGHT = "weight";
    public static final String GENDER = "gender";
    public static final String GOALS = "goals";
    public static final String NOTES = "notes";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
