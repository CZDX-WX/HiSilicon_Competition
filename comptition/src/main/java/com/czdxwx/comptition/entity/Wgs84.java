/**
 * Copyright 2024 bejson.com
 */
package com.czdxwx.comptition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wgs84 {

    private String status;
    private String info;
    private String infocode;
    private String locations;

}