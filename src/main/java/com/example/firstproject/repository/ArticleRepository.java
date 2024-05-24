package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

// JPA에서 제공하는 리파지터리 인터페이스(CrudRepository) 활용하기
public interface ArticleRepository extends CrudRepository<Article, Long>{
    @Override
    ArrayList<Article> findAll();
}