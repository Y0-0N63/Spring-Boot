package edu.kh.project.myPage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 매개변수가 없는 생성자(기본 생성자)
@AllArgsConstructor // 모든 매개변수가 있는 생성자(모든 필드 초기화용 생성자)
@Builder
public class UploadFile {
	private int fileNo;
	private String filePath;
	private String fileOriginalName;
	private String fileRename;
	private String fileUploadDate;
	private int memberNo;
}