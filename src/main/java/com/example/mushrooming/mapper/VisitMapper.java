package com.example.mushrooming.mapper;

import com.example.mushrooming.dto.VisitDto;
import com.example.mushrooming.model.Find;
import com.example.mushrooming.model.User;
import com.example.mushrooming.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mapping(target = "findId", source = "find.id")
    @Mapping(target ="userId", source = "user.id")
    @Mapping(target = "user", expression = "java(mapUser(visit.getUser()))")
    VisitDto mapVisitToDto(Visit visit);

    default String mapUser(User user){
        return user.getName()+' '+user.getSurname();
    }

    @Mapping(target = "user", source = "user")
    @Mapping(target = "find",source = "find")
    @Mapping(target = "visitedAt", expression = "java(java.time.Instant.now())")
    Visit mapDtoToVisit(VisitDto visitDto, User user, Find find);
}
