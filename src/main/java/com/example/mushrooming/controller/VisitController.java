package com.example.mushrooming.controller;

import com.example.mushrooming.dto.VisitDto;
import com.example.mushrooming.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
@AllArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping()
    public VisitDto create(@RequestBody @Valid VisitDto visitDto){
        return visitService.save(visitDto);
    }
}
