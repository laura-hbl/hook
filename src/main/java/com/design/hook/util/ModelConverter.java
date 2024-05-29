package com.design.hook.util;

import com.design.hook.dto.PatternDTO;
import com.design.hook.model.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ModelConverter {

    public static Pattern toPattern(final PatternDTO patternDTO) {

        Pattern pattern = new Pattern();
        pattern.setTitle(patternDTO.getTitle());
        pattern.setDescription(patternDTO.getDescription());
        pattern.setLikes(patternDTO.getLikes());
        pattern.setLevel(pattern.getLevel());
        pattern.setImageUrl(patternDTO.getImageUrl());

        return pattern;
    }

}
