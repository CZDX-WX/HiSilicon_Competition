package com.czdxwx.comptition.entity;

import com.czdxwx.comptition.model.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateAndImage {
    private State state;
    private Base64File base64File;
}
