// 쿠키에 저장된 이메일 input창에 출력하기
// 이메일 작성 input 태그 요소
const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");

// 쿠키에서 매개변수로 전달받은 key가 일치하는 value를 얻어오는 함수
const getCookie = (key) => {
  // document상에 존재하는 cookie를 모두 K:V의 형태로 얻어옴(한 줄의 문자열의 형태로 ';'로만 구분되어 있음) > "K=V; K=V; ....."
  //console.log(cookies); // saveId=user01@kh.or.kr; testKey=testValue
  const cookies = document.cookie;

  // cookies 문자열을 배열 형태로 변환
  const cookieList = cookies.split("; ") // ["K=V", "K=V"...]
 			              .map( el => el.split("=") );  // ["K", "V"]..

//console.log(cookieList);
// ['saveId', 'user01@kh.or.kr'], 
// ['testKey', 'testValue']
	// 배열.map(함수) : 배열의 각 요소를 이용해 함수 수행 후
	//					결과 값으로 새로운 배열을 만들어서 반환
	// 배열 -> 객체로 변환 (그래야 다루기 쉽다)
	
	const obj = {}; // 비어있는 객체 선언
	
	for(let i=0; i < cookieList.length; i++) {
		const k = cookieList[i][0]; // key 값
		const v = cookieList[i][1]; // value 값
		obj[k] = v; // 객체에 추가
		// obj["saveId"] = "user01@kh.or.kr";
		// obj["testKey"]  = "testValue";
	}
	
	//console.log(obj); // {saveId: 'user01@kh.or.kr', testKey: 'testValue'}
	
  // obj 객체에 저장된 key가 일치하는 요소의 value값 반환
	return obj[key]; // 매개변수로 전달받은 key와
}

// 로그인하기 전에만 이메일 주소 입력창이 존재 > 화면상에 존재할 때(!null)만 코드 수행
if(loginEmail != null) {
  // 쿠키 중 key 값이 "saveId"인 쿠키의 value 값 얻어오기 > 함수로 만들어 호출할 것
  const saveId = getCookie("saveId"); // 이메일 주소 또는 undefined

  // saveId 값이 있을 경우
  if(saveId != undefined) {
    // 쿠키에서 얻어온 이메일 값을 input 요소의 value에 세팅
    loginEmail.value = saveId;

    // 아이디 저장 체크 박스에 체크해두기
    document.querySelector("input[name='saveId']").checked = true;
  }
}

// ==================== Ajax ====================

// 회원 목록 조회 > #selectMemberList 버튼을 눌렀을 때 > #memberList 내부에 > <tr> 출력
const selectMemberList = document.querySelector("#selectMemberList");
const memberList = document.querySelector("#memberList");

// td 요소를 만든 후 > text 추가 > td 반환
const createTd = (text) => {
  const td = document.createElement("td");
  td.innerText = text;
  return td;
}

// 회원 목록 조회 > fetch() API로 서버로 요청 보내기 > 응답 받아(첫 번째 then) > 화면에 출력(두 번째 then)
function selectAllMember() {
  fetch("/ajax/memberList")
  // 화살표 함수에서 중괄호 사용 시 > return을 명시적으로 작성하지 않으면 > 함수는 undefined 반환
  // > 따라서 중괄호 생략하거나 return response.json()으로 작성해야
  // response.json() : 서버에서 보내준 응답 본문(Body)을 읽어서, JSON 데이터를 자바스크립트 객체(Object)로 변환 (호출 즉시 데이터를 반환하는 것이 아님!)
  // > 응답 본문의 실제 데이터를 모두 읽고 객체로 변환할 때까지 기다림 > 원하는 데이터(list 등)을 반환해줌 
  .then(response => response.json()) // Promise 반환
  .then(list => { // Promise가 이행(fulfilled)되면 실행
    // memberList를 비워주지 않으면 > 버튼을 누를 때마다 표가 계속 추가(append)되어 출력됨
    memberList.innerHTML = "";

    // forEach(element, index) : 배열의 모든 요소를 돌며, 각 요소와 인덱스를 받는 함수 실행
    list.forEach(member => {
      // tr 생성 > tr 내부에 td 추가
      const tr = document.createElement("tr");
      
      const keyList = ['memberNo', 'memberEmail', 'memberNickname', 'memberDelFl'];

      keyList.forEach(key => {
        tr.append(createTd(member[key]));
      });

      // tbody에 tr 추가
      memberList.append(tr);
    })
  })
}
selectMemberList.addEventListener("click", selectAllMember);

// 특정 회원 비밀번호 초기화(#resetPw) > 입력받은 회원 번호(#resetMemberNo)의 비밀번호를 > pass01!로 초기화(PUT, Update)
document.querySelector("#resetPw").addEventListener("click", () => {
  const resetMemberNo = document.querySelector("#resetMemberNo").value;

  if(resetMemberNo.trim().length == 0) {
    alert("회원 번호를 입력해주세요.");
    return;
  }

  fetch("/ajax/resetPw", {
    method : "PUT",
    headers : {"Content-Type" : "application/json"},
    body : resetMemberNo
  })
  .then(response => response.text())
  .then(result => {
    if(result > 0) {
      alert("비밀번호가 pass01!로 초기화되었습니다.")
    } else {
      alert("비밀번호 초기화에 실패하였습니다.");
    }
  })
});

// 특정 회원(회원번호) 탈퇴 복구 > 입력받은 회원 번호의 > 탈퇴 여부(member_del_fl)을 'N'로 수정(PUT)
document.querySelector("#restorationBtn").addEventListener("click", () => {
  const memberNo = document.querySelector("#restorationMemberNo").value;

  if(memberNo.trim().length == 0) {
    alert("회원 번호를 입력해주세요.");
    return;
  }

  fetch("/ajax/restoreMember", {
    method : "PUT",
    headers : {"Content-Type" : "application/json"},
    body : memberNo
  })
  .then(response => response.text())
  .then(result => {
    if(result > 0) {
      alert("회원 정보를 복구하였습니다.");
      document.querySelector("#restorationMemberNo").value = "";
      selectAllMember();
    } else {
      alert("회원 정보 복구에 실패하였습니다.");
    }
  })
});