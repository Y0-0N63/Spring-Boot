package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ArticleController {
    // 스프링 부트가 미리 생성한 리파지터리 객체 주입 (DI, 의존성 주입)
    @Autowired
    // article 엔티티 저장하기 위해 리파지터리 객체 선언
    private ArticleRepository articleRepository;

    // articles/new:브라우저에서 접속하는 URL 주소
    // Get이 기본값
    @GetMapping("/articles/new")
    // 페이지(new.mustache) 보여주기 위해 메서드 생성
    public String newArticleForm(){
        return "articles/new";
    }

    // URL 요청 접수
    @PostMapping("/articles/create")
    // 폼에서 전송한 데이터를 createArticle()의 매개변수로 받아옴 (ArticleForm 타입의 form 객체)
    public String createArticle(ArticleForm form){
        log.info(form.toString());
//        // 폼에서 전송한 데이터가 DTO에 잘 담겼는지 확인하기 위해 출력 (form 객체의 toString())
//        System.out.println(form.toString());
        // 1. DTO를 엔티티로 변환
        // toEntity() 호출해서 반환값 Article 타입의 article 엔티티로 저장
        Article article = form.toEntity();
        log.info(article.toString());
//        System.out.println(article.toString());
        // 2. 리파지터리로 엔티티를 DB에 저장
        Article saved=articleRepository.save(article);
        log.info(saved.toString());
//        System.out.println(saved.toString());
        return "articles/new"; // 404 에러 해결
    }
}
