package com.comunityapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@RequiredArgsConstructor
public class Post extends BaseEntity<Long> {
    private String content;
    @ManyToOne
    private User author;

    public Post(String content, User author) {
        this.content = content;
        this.author = author;
    }
}
