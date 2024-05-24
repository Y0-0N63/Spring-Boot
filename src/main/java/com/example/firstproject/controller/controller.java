package com.example.firstproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class controller {
    @GetMapping("/input")
    public String input() {
            return "input";
    }

    @PostMapping("/result")
    public String result(Model model, int num1, int num2) {
        int result=num1+num2;
        model.addAttribute("num1", num1);
        model.addAttribute("num2", num2);
        model.addAttribute("result", result);
        return "result";
    }

    // '/hi'->URL 주소
    @GetMapping("/hi")
    public String niceToMeetYou(Model model){
        model.addAttribute("username", "Y0-0N63");

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        model.addAttribute("date", formattedDate);

        // greetings.mustache 파일 반환
        // (파일 이름만 적어주면 됨!->templates에서 파일 찾아서 자체적으로 웹 브라우저로 전송)
        return "greetings";
    }

    @GetMapping("/bye")
    public String seeYouNext(Model model){
        model.addAttribute("nickname", "Y0-0N63");
        return "goodbye";
    }

    @GetMapping("/SelfCheck02")
    public String printSaying(Model model){
        String[] quotes={
                "행복은 습관이다. 그것을 몸에 지니라. "+"-허버드-",
                "고개 숙이지 마십시오. 세상을 똑바로 정면으로 "+"바라보십시오. -헬렌 켈러-",
                "고난의 시기에 동요하지 않는 것, 이것은 진정 "+"칭찬받을 만한 뛰어난 인물의 증거다. -베토벤-",
                "당신이 할 수 있다고 믿든 할 수 없다고 믿든 "+"믿는 대로 될 것이다. -헨리 포드-",
                "작은 기회로부터 종종 위대한 업적이 시작된다. "+"-데모스테네스-"
        };
        int randInt=(int)(Math.random()*quotes.length);
        model.addAttribute("randomQuote", quotes[randInt]);
        return "quote";
    }
}
