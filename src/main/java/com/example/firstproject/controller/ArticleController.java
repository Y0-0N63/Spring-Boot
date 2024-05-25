    package com.example.firstproject.controller;

    import com.example.firstproject.dto.ArticleForm;
    import com.example.firstproject.entity.Article;
    import com.example.firstproject.repository.ArticleRepository;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;

    import java.util.ArrayList;

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

        // 데이터 조회 요청 접수 (id를 통하여)
        @GetMapping("/articles/{id}")
        // 매개변수로 id를 받아옴
        // @PathVariable : URL 요청으로 들어온 전달값->컨트롤러의 매개변수로 가져옴
        // 2. 모델에 데이터 등록
        public String show(@PathVariable Long id, Model model){
            log.info("id = "+id);
            // 1. id를 조회해서 데이터 가져오기
            // 해당 id 값 없으면->null 값 반환
            Article articleEntity = articleRepository.findById(id).orElse(null);
            // 2. 모델에 데이터 등록
            model.addAttribute("article", articleEntity);
            // 3. 뷰 페이지 반환
            return "articles/show";
        }

        @GetMapping("/articles")
        public String index(Model model){
            // 1. 모든 데이터 가져오기
            // findAll() : 해당 레파지터리의 모든 데이터를 가져옴
            ArrayList<Article> articleEntityList = articleRepository.findAll();

            // 2. 모델에 데이터 등록하기
            model.addAttribute("articleList", articleEntityList);

            // 3. 뷰 페이지 설정하기
            return "articles/index";
        }

        @GetMapping("/articles/even")
        public String evenIndex(Model model){
            ArrayList<Article> articleEntityList = articleRepository.findAll();
            // articleEntityList.stream().filter(article -> article.getId()%2==0);

            ArrayList<Article> evenList=new ArrayList<>();
            for(int i=0; i<articleEntityList.size(); i++){
                if(i%2==1)
                evenList.add(articleEntityList.get(i));
            }

            // 2. 모델에 데이터 등록하기
            model.addAttribute("articleList", evenList);

            // 3. 뷰 페이지 설정하기
            return "articles/index";
        }
    }

