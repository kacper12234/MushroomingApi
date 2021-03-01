package com.example.mushrooming.mapper;

import com.example.mushrooming.dto.FindRequest;
import com.example.mushrooming.dto.FindResponse;
import com.example.mushrooming.dto.VisitDto;
import com.example.mushrooming.model.Find;
import com.example.mushrooming.model.User;
import com.example.mushrooming.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class FindMapper {

    @Autowired
    private VisitMapper visitMapper;

    @Mapping(target = "user", expression = "java(mapUser(find.getUser()))")
    @Mapping(target = "visits", expression = "java(mapVisits(find.getVisits()))")
    public abstract FindResponse mapFindToDto(Find find);

    String mapUser(User user){
       return user.getName()+' '+user.getSurname();
    }

    List<VisitDto> mapVisits(List<Visit> visits){
        if(visits!=null)
        return visits.stream().map(visitMapper::mapVisitToDto).collect(Collectors.toList());
        else
            return null;
    }

    @Mapping(target = "visits", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    public abstract Find mapDtoToFind(FindRequest findRequest, User user);
}
