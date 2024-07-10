package com.czdxwx.comptition.entity;


import com.czdxwx.comptition.utils.NullOrEmptyArrayDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step {
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String instruction;
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String orientation;
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String road;
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String distance;
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String duration;
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String polyline;
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String action;
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String assistantAction;
    @JsonDeserialize(using = NullOrEmptyArrayDeserializer.class)
    private String walkType;
}
