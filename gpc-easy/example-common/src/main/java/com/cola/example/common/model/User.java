package com.cola.example.common.model;

import java.io.Serializable;

/**
 * 用户实体类
 * @author Maobohe
 * @createData 2024/3/17 20:19
 */
public class User implements Serializable {

    private static final long serialVersionUID = -6952101479380935156L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
