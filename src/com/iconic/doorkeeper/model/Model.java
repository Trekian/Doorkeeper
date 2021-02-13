package com.iconic.doorkeeper.model;

/*
This class is a kind of Database, to store data and to make a better overview and accessibility
IMPORTANT: It stores data ONLY during runtime. As the running process is stopped, all data is gone!
 */

public class Model {

    private int current_active_member;
    private int amount_of_teams;
    private int just_test;

    public Model(){
        just_test = 0;
    }

    public int getJust_test() {
        return just_test;
    }

    public void setJust_test(int just_test) {
        this.just_test = just_test;
    }

    public void increase_Just_test(){
        just_test++;
    }
}
