package com.example.mushrooming.service;

import com.example.mushrooming.dto.FindRequest;
import com.example.mushrooming.dto.FindResponse;
import com.example.mushrooming.mapper.FindMapper;
import com.example.mushrooming.model.Find;
import com.example.mushrooming.repository.FindRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FindService {

    private final FindRepository findRepository;
    private final FindMapper findMapper;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public List<FindResponse> getFindsInArea(Double lat, Double lat2, Double lon, Double lon2){
        return findRepository.findFindsByLatBetweenAndLonBetween(lat, lat2, lon, lon2)
                .stream().map(findMapper::mapFindToDto).collect(Collectors.toList());
    }

    @Transactional
    public FindResponse save(FindRequest findRequest){
        Find find = findRepository.save(findMapper.mapDtoToFind(findRequest, authService.getCurrentUser()));
        return findMapper.mapFindToDto(find);
    }


}
