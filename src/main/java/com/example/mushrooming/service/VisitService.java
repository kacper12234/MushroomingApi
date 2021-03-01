package com.example.mushrooming.service;

import com.example.mushrooming.dto.VisitDto;
import com.example.mushrooming.exception.FindNotFoundException;
import com.example.mushrooming.mapper.VisitMapper;
import com.example.mushrooming.model.Find;
import com.example.mushrooming.model.Visit;
import com.example.mushrooming.repository.FindRepository;
import com.example.mushrooming.repository.VisitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final FindRepository findRepository;
    private final VisitMapper visitMapper;
    private final AuthService authService;

    @Transactional
    public VisitDto save(VisitDto visitDto){
        Find find = findRepository.findFindById(visitDto.getFindId()).orElseThrow(FindNotFoundException::new);
        Visit visit = visitRepository.save(visitMapper.mapDtoToVisit(visitDto, authService.getCurrentUser(), find));
        return visitMapper.mapVisitToDto(visit);
    }

}
