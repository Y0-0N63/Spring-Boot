package edu.kh.project.myPage.model.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.email.model.service.EmailServiceImpl;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@PropertySource("classpath:/config.properties") // Config 외의 클래스에도 작성할 수 있음 (Property를 읽어야 하는 모든 클래스에 작성)
public class MyPageServiceImpl implements MyPageService {
    private final EmailServiceImpl emailServiceImpl;
    
    MyPageServiceImpl(EmailServiceImpl emailServiceImpl) {
    	this.emailServiceImpl = emailServiceImpl;
    }
	
	@Autowired
	private MyPageMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

    @Value("${my.profile.web-path}")
    private String profileWebPath;
    
    @Value("${my.profile.folder-path}")
    private String profileFolderPath;
    
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		// 입력된 주소가 있을 경우
		// A^^^B^^^C 형태로 가공
		// 주소가 입력되었을 때 (입력되지 않았다면 ",,"형태로 들어옴
		if(!inputMember.getMemberAddress().equals(",,")) {
			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);
		} else {
			// 주소가 입력되지 않았을 때
			inputMember.setMemberAddress(null);
		}
		
		// inputMember : 수정 닉네임, 수정 전화번호, 수정 주소, 회원 번호까지 세팅...
		
		return mapper.updateInfo(inputMember);
	}

	@Override
	public int updatePw(String currentPw, String newPw, Member loginMember) {
		// 입력한 현재 비밀번호(currentPw)와 DB에 저장된 비밀번호(loginMember.getMemberNo()를 통해 비밀번호 가져오기) 
		// 일치 여부 확인(bcrypt.matches(평문, 암호화된 문장))
		if(bcrypt.matches(currentPw, mapper.getPw(loginMember.getMemberNo()))) {
			// 일치할 경우 > 새 비밀번호(newPw)를 암호화(bcrypt.encode(문자열))
			String encPw = bcrypt.encode(newPw);
			loginMember.setMemberPw(encPw);
			// DB 업데이트
			return mapper.updatePw(loginMember);
		} else {
			// 일치하지 않을 경우
			return 0;
		}
	}

	/**
	 * 회원 탈퇴 서비스
	 */
	@Override
	public int secession(String memberPw, int memberNo) {
		
		// 1. 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String encPw = mapper.getPw(memberNo);
		
		// 2. 입력받은 비밀번호 & 암호화된 DB 비밀번호가 같은지 비교
		// 다를 경우
		if(!bcrypt.matches(memberPw, encPw)) {
			return 0;
		}
		
		// 일치할 경우
		return mapper.secession(memberNo);
	}

	/**
	 * 파일 업로드 테스트 1
	 * @throws Exception 
	 */
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws Exception {
		// 클라이언트가 실제로 업로드한 파일이 없을 경우 > uploadFile이 빈 상태로 넘어옴
		if(uploadFile.isEmpty()) {
			return null;
		}
		
		// 업로드한 파일이 있을 경우 > C:/uploadFiles/test/파일명으로 서버에 저장
		uploadFile.transferTo(new File("C:/uploadFiles/test/" + uploadFile.getOriginalFilename()));
		
		// 웹에서 해당 파일에 접근할 수 있는 경로 만들어 반환
		// 이미지가 최종 저장된 서버 컴퓨터상의 경로 > C:/uploadFiles/test/ 파일명.jpg
		// 클라이언트가 브라우저에 해당 이미지를 보기 위해 요청하는 경로 <img src="경로">
		
		return "/myPage/file/" + uploadFile.getOriginalFilename();
	}

	/**
	 * 파일 업로드 테스트 2 (서버에 파일 저장 + DB에 파일 저장)
	 */
	@Override
	public int fileUpload2(MultipartFile uploadFile, int memberNo) throws Exception {
		// MultipartFile이 제공하는 메소드
		// - isEmpty() : 실제로 업로드된 파일이 없을 경우 > true, 존재한다면 false
		// - getSize() : 파일 크기
		// - getOriginalFileName() : 원본 파일명
		// - transferTo(경로) : 메모리 또는 임시 저장 경로에 업로드된 파일을 원하는 경로에 실제로 전송(서버의 어떤 폴더에 저장할지 지정)
		
		// 클라이언트가 실제로 업로드한 파일이 없을 경우 > uploadFile이 빈 상태로 넘어옴
		if(uploadFile.isEmpty()) {
			return 0;
		}
		
		// 업로드된 파일이 있다면
		// 1. 서버에 저장될 서버 폴더 경로 만들기
		// 파일이 저장될 서버 폴더 경로
		String folderPath = "C:/uploadFiles/test/";

		// 클라이언트가 파일이 저장된 폴더에 접근할 수 있는 주소 (요청 주소)
		String webPath = "/myPage/file/";
		
		// 2. DB에 전달할 데이터를 DTO로 묶어서 INSERT > webPath, memberNo, 원본파일명, 변경된파일명
		String fileRename = Utility.fileRename(uploadFile.getOriginalFilename());
		
		// Builder 패턴을 이용해서 UploadFile 객체 생성하기
		// Builder 패턴의 장점 > 반복되는 참조변수명, setter에서의 set 구문 생략 가능
		// > method chaining을 이용하여 한 줄로 작석 가능
		UploadFile uf = UploadFile.builder().memberNo(memberNo).filePath(webPath)
						.fileOriginalName(uploadFile.getOriginalFilename()).fileRename(fileRename).build();
		
		int result = mapper.insertUploadFile(uf);
		
		// 3. 삽입(INSERT) 성공 시 파일을 지정된 서버 폴더에 저장
		// 삽입 실패 시
		if(result == 0) return 0;
		
		// 삽입 성공 시 > C:/uploadFiles/test/변경된파일명 으로 파일을 서버 컴퓨터에 저장하기
		uploadFile.transferTo(new File(folderPath + fileRename));
		
		return result;
	}

	/**
	 * 파일 목록 조회 서비스
	 */
	@Override
	public List<UploadFile> fileList(int memberNo) {
		return mapper.fileList(memberNo);
	}

	/**
	 * 여러 파일 업로드 서비스
	 */
	@Override
	public int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws Exception {
		
		// 1. aaaList 처리
		int result1 = 0;
		
		// 업로드된 파일이 없을 경우를 제외한 후 업로드
		for(MultipartFile file : aaaList) {
			if(file.isEmpty()) { // 파일이 없으면 다음 파일
				continue; // 아래 코드는 더이상 수행하지 않고 다음 반복으로 넘어감
			}
			
			// fileUpload2() 메서드 호출(재활용) -> 파일 하나 업로드 + DB INSERT
			result1 += fileUpload2(file, memberNo);
		}
		
		// 2. bbbList 처리
		int result2 = 0;
		for(MultipartFile file : bbbList) {
			if(file.isEmpty()) continue;
			result2 += fileUpload2(file, memberNo);
		}
		
		return result1 + result2;
	}

	/**
	 * 프로필 이미지 변경 서비스
	 */
	@Override
	public int profile(MultipartFile profileImg, Member loginMember) throws Exception {

		// 프로필 이미지 경로
		String updatePath = null;
		// 변경명 저장
		String rename = null;
		
		// 업로드한 이미지가 있을 경우
		if(!profileImg.isEmpty()) {
			// updatePath 경로 조합
			// 1. 파일명 변경
			rename = Utility.fileRename(profileImg.getOriginalFilename());
			
			// 2. /myPage/profile/변경된파일명
			updatePath = profileWebPath + rename;
		}
		
		// 수정된 프로필 이미지 경로 + 회원 정보를 저장할 DTO 객체
		Member member = Member.builder().memberNo(loginMember.getMemberNo()).profileImg(updatePath).build();
		
		// UPDATE 수행
		int result = mapper.profile(member);
		
		if(result > 0) {
			// DB에 업데이트 성공
			// 프로필 이미지를 없앤 경우(Null로 수정한 경우) > 업로드한 이미지가 실제로 있을 경우
			if(!profileImg.isEmpty()) {
				// 파일을 서버의 지정 폴더에 저장하기
				profileImg.transferTo(new File(profileFolderPath + rename));
			}
			
			// 세션에 등록된 현재 로그인한 회원 정보에서 프로필 이미지 경로를 DB에 업데이트한 경로로 변경
			loginMember.setProfileImg(updatePath);
		}
		
		return result;
	}
}
