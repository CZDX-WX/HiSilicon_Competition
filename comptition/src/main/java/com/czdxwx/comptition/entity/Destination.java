package com.czdxwx.comptition.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destination {

    private String count;
    private String infocode;
    private List<Pois> pois;
    private String status;
    private String info;


}
