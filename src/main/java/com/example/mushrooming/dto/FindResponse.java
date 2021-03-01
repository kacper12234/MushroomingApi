package com.example.mushrooming.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class FindResponse {
    private Long id;
    private String species;
    private Double lon;
    private Double lat;
    private String user;
    private Instant createdDate;
    private List<VisitDto> visits;
}
