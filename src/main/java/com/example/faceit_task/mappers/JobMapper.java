package com.example.faceit_task.mappers;

import com.example.faceit_task.dtos.DataDTO;
import com.example.faceit_task.models.DataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", source = "tags", qualifiedByName = "listToString")
    @Mapping(target = "jobTypes", source = "jobTypes", qualifiedByName = "listToString")
    DataEntity toEntity(DataDTO dataDTO);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringToList")
    @Mapping(target = "jobTypes", source = "jobTypes", qualifiedByName = "stringToList")
    DataDTO toDto(DataEntity dataEntity);

    @Named("listToString")
    default String listToString(List<String> list) {
        return String.join(",", list);
    }

    @Named("stringToList")
    default List<String> stringToList(String str) {
        return Arrays.asList(str.split(","));
    }
}
