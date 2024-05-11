package com.example.firstproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Entity
public class Article{
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String title;
    @Column
    private String content;

    @Column
    private String date;

    public Article() {

    }

//    // Article 생성자 추가
//    public Article(Long id, String title, String content, String date) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.date=date;
//    }
//
//    public Article() {
//
//    }
//
//    // toString() 메서드 추가
//    @Override
//    public String toString() {
//        return "Article{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                ", date='"+date+'\''+
//                '}';
//    }
}
