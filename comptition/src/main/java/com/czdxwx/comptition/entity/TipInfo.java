package com.czdxwx.comptition.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipInfo {

    private List<Tip> tips;
    private String status;
    private String info;
    private String infocode;
    private String count;

}