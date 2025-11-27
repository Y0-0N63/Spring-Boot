package edu.kh.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Spring EL의 경우, DTO 객체를 출력할 때 getter가 필수 작성되어 있어야 함
// ${Student.name} == ${Student.getName()}
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	private String studentNo;
	private String name;
	private int age;
}
