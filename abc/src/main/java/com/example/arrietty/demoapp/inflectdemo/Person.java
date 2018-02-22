package com.example.arrietty.demoapp.inflectdemo;

/**
 * Created by asus on 2017/12/7.
 */

public class Person {
    private int age;
    private String name;
    protected int height;
    public String school;

    Person(){
        this.name = "Person";
        age = 22;
    }

    Person(String name) {
        this.name = name;
    }

    private String showName(String _name) {
        return "My name is " + _name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "My name is " + name + ", i'm " + age + " years old";
    }
}
