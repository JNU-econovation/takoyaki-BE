package com.bestbenefits.takoyaki.config.properties.user;

import lombok.Getter;

@Getter
public enum UserType {
    TAKO("tako"),
    YAKI("yaki"),
    OTHER("other");

    private String name; //아직 사용은 안됨, 혹시 몰라서 추가

    UserType(String name){
        this.name = name;
    }

}
