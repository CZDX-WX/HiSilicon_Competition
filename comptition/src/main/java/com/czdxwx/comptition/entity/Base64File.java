package com.czdxwx.comptition.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Base64File {
    private String file;
    private String fileName;
    private String storagePath;
}
