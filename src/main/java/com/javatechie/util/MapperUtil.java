package com.javatechie.util;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class MapperUtil {
    public static ModelMapper configModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }
}
