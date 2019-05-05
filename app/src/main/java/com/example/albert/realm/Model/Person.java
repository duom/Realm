package com.example.albert.realm.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Person extends RealmObject {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return  "\n"+
                "ID: "+id+"\n"+
                "Nombre: " + name +"\n"+
                "Edad: " + age+"" +"\n"+
                "Genero: " + gender +"\n"+"\n"
               ;
    }
//
    String name;
    String age;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    String gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey
    private int id;

}
