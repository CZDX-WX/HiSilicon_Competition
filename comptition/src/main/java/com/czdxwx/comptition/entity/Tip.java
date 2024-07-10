package com.czdxwx.comptition.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tip {

    private String id;
    private String name;
    private String district;
    private String adcode;
    private String location;
    private String address;
    private String typecode;
    private List<String> city;

}