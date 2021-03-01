package com.example.mushrooming.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Find {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Mushroom species are required")
    private String species;
    private Double lon;
    private Double lat;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant createdDate;
    @OneToMany(mappedBy = "find",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Visit> visits = new ArrayList<>();
}
