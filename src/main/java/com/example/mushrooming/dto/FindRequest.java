package com.example.mushrooming.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindRequest {
    private String species;
    private Double lon;
    private Double lat;
}
