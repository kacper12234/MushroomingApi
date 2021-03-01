package com.example.mushrooming.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
public class VisitDto {
    private Long findId;
    private Long userId;
    private String user;
    private String state;
    private Integer count;
    private Instant visitedAt;
}
