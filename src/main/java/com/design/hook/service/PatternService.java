package com.design.hook.service;

import com.design.hook.dto.PatternDTO;
import com.design.hook.exception.ResourceNotFoundException;
import com.design.hook.model.Pattern;
import com.design.hook.repository.IPatternRepository;
import com.design.hook.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.design.hook.util.DTOConverter.toPatternDTO;
import static com.design.hook.util.ModelConverter.toPattern;

@Service
public class PatternService {

    private final IPatternRepository patternRepository;
    @Autowired
    public PatternService(final IPatternRepository patternRepository) {

        this.patternRepository = patternRepository;
    }

    public Collection<PatternDTO> getAllPatterns() {
         return patternRepository.findAll()
                .stream()
                .map(DTOConverter::toPatternDTO)
                .collect(Collectors.toList());
    }

    public PatternDTO getPattern(long patternId) {

        Pattern pattern = patternRepository.findById(patternId).orElseThrow(() ->
                new ResourceNotFoundException("No pattern registered with this id"));

        return toPatternDTO(pattern);
    }

    public PatternDTO addPattern(final PatternDTO patternDTO) {

        Pattern pattern = toPattern(patternDTO);
        pattern.setLikes(0);

        return toPatternDTO(patternRepository.save(pattern));
    }

    public PatternDTO updatePattern(long patternId, String isLike) {
        Pattern pattern = patternRepository.findById(patternId).orElseThrow(() ->
                new ResourceNotFoundException("No pattern registered with this id"));

        if(Objects.equals(isLike, "like")) {
            long likes = pattern.getLikes();
            pattern.setLikes(likes+1);
        }

        patternRepository.save(pattern);
        return toPatternDTO(pattern);
    }
}
