package com.example.mushrooming.controller;

import com.example.mushrooming.dto.FindRequest;
import com.example.mushrooming.dto.FindResponse;
import com.example.mushrooming.service.FindService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/finds")
@AllArgsConstructor
public class FindController {

private final FindService findService;

@GetMapping("{lat},{lon}/{lat2},{lon2}")
    public List<FindResponse> getFinds(@PathVariable Double lat, @PathVariable Double lat2, @PathVariable Double lon, @PathVariable Double lon2)
{
    return findService.getFindsInArea(lat, lat2, lon, lon2);
}

@PostMapping
    public FindResponse create(@RequestBody @Valid FindRequest findRequest)
{
    return findService.save(findRequest);
}

}
