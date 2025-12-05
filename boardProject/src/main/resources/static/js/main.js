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