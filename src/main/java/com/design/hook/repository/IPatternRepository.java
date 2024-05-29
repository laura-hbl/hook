package com.design.hook.repository;

import com.design.hook.model.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPatternRepository extends JpaRepository<Pattern, Long>  {
}
