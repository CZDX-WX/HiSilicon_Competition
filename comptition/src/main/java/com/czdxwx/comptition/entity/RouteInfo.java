package com.czdxwx.comptition.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteInfo {
    private String origin;
    private String destination;
    private List<Path> paths;
}
