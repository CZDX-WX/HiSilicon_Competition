package com.czdxwx.comptition.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Path {
    private String distance;
    private String duration;
    private List<Step> steps;
}
