package com.moydev.cibertecproject.db;

import com.orm.SugarRecord;

/**
 * Created by ATTAKON on 5/22/15.
 */
public class Cities extends SugarRecord<Cities> {
    String name;

    public Cities(){

    }

    public Cities(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
