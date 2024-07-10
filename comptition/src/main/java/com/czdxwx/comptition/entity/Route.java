package com.czdxwx.comptition.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private String status;
    private String info;
    private String infocode;
    private String count;
    private RouteInfo route;
}
