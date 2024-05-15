package com.example.firstproject.dto;

import com.example.firstproject.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleForm {
    // 제목과 내용 받을 필드
    private String title;
    private String content;
    private String date;

    // 전송 받은 제목과 내용 필드에 저장하는 생성자 추가
//    public ArticleForm(String title, String content, String date) {
//        this.title = title;
//        this.content = content;
//        this.date=date;
//    }

//    // toString() : 데이터 받았는지 확인
//    @Override
//    public String toString(){
//        return "ArticleForm{"+"title='"+title+'\''+", content='"+content+'\''+", date='"+date+'}';
//    }
//
    public Article toEntity() {
        // ID 정보 없음 -> null
        return new Article(null, title, content, date);
    }
}
