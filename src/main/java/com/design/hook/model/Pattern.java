package com.design.hook.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Pattern {

    private long id;
    private String title;
    private String level;
    private Timestamp createdDate;
    private long likes;
    private String description;

    public Pattern() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLevel() {
        return level;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public long getLikes() {
        return likes;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern = (Pattern) o;
        return id == pattern.id && likes == pattern.likes && Objects.equals(title, pattern.title)
                && Objects.equals(level, pattern.level) && Objects.equals(createdDate, pattern.createdDate)
                && Objects.equals(description, pattern.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, level, createdDate, likes, description);
    }
}
