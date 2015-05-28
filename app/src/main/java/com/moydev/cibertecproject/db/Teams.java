package com.moydev.cibertecproject.db;

import com.orm.SugarRecord;

/**
 * Created by ATTAKON on 5/23/15.
 */
public class Teams extends SugarRecord<Teams> {
    String name;

    public Teams(){}

    public Teams(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
