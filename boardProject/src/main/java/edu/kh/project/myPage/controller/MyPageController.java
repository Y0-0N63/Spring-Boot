package edu.kh.project.myPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("myPage")
@Slf4j
public class MyPageController {
	@GetMapping("info") // /myPage/info GET 방식 요청 매핑
	public String info() {
		
		return "myPage/myPage-info";
	}
}