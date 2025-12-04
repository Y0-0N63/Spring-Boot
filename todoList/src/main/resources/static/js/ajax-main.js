// HTML상 요소를 얻어와서 변수에 저장
// 할 일 개수 관련 요소
const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");
const reloadBtn = document.querySelector("#reloadBtn");

// 할 일 추가 관련 요소
const todoTitle = document.querySelector("#todoTitle");
const todoContent = document.querySelector("#todoContent");
const addBtn = document.querySelector("#addBtn");

// 할 일 목록 조회 관련 요소
const tbody = document.querySelector("#tbody");

// 할 일 상세 조회 관련 요소
const popupLayer = document.querySelector("#popupLayer");
const popupTodoNo = document.querySelector("#popupTodoNo");
const popupTodoTitle = document.querySelector("#popupTodoTitle");
const popupComplete = document.querySelector("#popupComplete");
const popupRegDate = document.querySelector("#popupRegDate");
const popupTodoContent = document.querySelector("#popupTodoContent");
const popupClose = document.querySelector("#popupClose");

// 상세 조회 팝업레이어 관련 버튼 요소
const changeComplete = document.querySelector("#changeComplete");
const updateView = document.querySelector("#updateView");
const deleteBtn = document.querySelector("#deleteBtn");

// 수정 레이어 관련 요소
const updateLayer = document.querySelector("#updateLayer");
const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");
const updateBtn = document.querySelector("#updateBtn");
const updateCancel = document.querySelector("#updateCancel");

/* fetch() API : 비동기 요청을 수행하는 최신 JS API 중 하나
  - Promise(객체) : 비동기 작업의 결과를 처리하는 방법
    - 어떤 결과 거부, 대기 중 등...)가 올지 모르지만 반드시 결과를 응답하겠다는 일종의 약속
    - 비동기 작업이 맞이할 완료 또는 실패와 그 결과값을 나타냄
    - 비동기 작업이 완료되었을 때 실행할 콜백함수 지정, 해당 작업의 성공/실패 여부 처리할 수 있도록 함
    - Promise 객체는 세 가지의 상태를 가짐
      - Pending (대기 중) : 비동기 작업이 완료되지 않은 상태
      - Fulfilled(이행됨) : 비동기 작업이 성공적으로 완료된 상태
      - Rejected(거부됨) : 비동기 작업이 실패한 상태

*/

// 전체 Todo 개수를 조회하고 html 화면상에 출력하는 함수
function getTotalCount() {
  // 비동기로 서버에 전체 Todo 개수를 조회하는 요청 : fetch() API로 코드 작성
  fetch("/ajax/totalCount") // 서버로 "/ajax/totalCount"로 GET 요청
    // 첫 번째 then 구문 : 응답을 처리하는 역할
    // 서버에서 응답을 받으면 > 이 응답(response)을 텍스트 형식으로 변환하는 콜백함수
    .then((response) => {
      // 매개변수 response : 비동기 요청에 대한 응답이 담긴 객체
      // response.text() : 응답 데이터를 문자열/숫자 형태로 변환한 결과를 가지는 Promise 객체 반환
      // 콜백함수 종료하고 > 값을 가지고 fetch API쪽으로 돌아감
      return response.text();
    })

    // 두 번째 then 구문 : 첫 번째에서 return(반환)된 데이터(ex_response.text())를 활용하는 역할
    // = 첫 번째 콜백함수가 완료된 후 호출되는 콜백함수 : 매개변수로 전달된 데이터(result) 받아 > 어떤 식으로 처리할지 정의
    // id가 totalCount인 sapn 태그의 내용으로 result 값 삽입
    .then((result) => {
      totalCount.innerText = result;
    });
}

// 완료된 할 일 개수 조회 및 html 화면상에 출력하는 함수
function getCompleteCount() {
  fetch("/ajax/completeCount")
    .then((response) => response.text())
    .then((result) => {
      // 아이디가 completeCount인 요소의 내용으로 result 값 출력
      completeCount.innerText = result;
    });
}

addBtn.addEventListener("click", () => {
  if (
    todoTitle.value.trim().length === 0 ||
    todoContent.value.trim().length === 0
  ) {
    alert("제목이나 내용은 비어있을 수 없습니다.");
    return;
  }

  // POST 방식으로 fetch() 비동기 요청 보내기
  // - 요청 주소 : "/ajax/add", - 데이터 전달 방식 : POST, - 전달 데이터 : todoTitle 값, todoContent 값
  // JS <-> Java : JSON(JavaScript Object Notation, 데이터를 표현하는 문법)을 통해 서로 해석할 수 있게 함

  // todoTitle과 todoContent를 저장한 JS 객체
  const param = {
    // key : value
    todoTitle: todoTitle.value,
    todoContent: todoContent.value,
  };

  fetch("/ajax/add", {
    // key:value
    method: "POST",
    // headers : 요청 헤더, 요청 데이터의 형식을 JSON으로 지정
    headers: { "Content-Type": "application/json" },
    // body : 요청 본문, param이라는 JS 객체를 JSON(문자열 형태)으로 변환(해 서버에 제출)
    body: JSON.stringify(param),
  })
    .then((resp) => resp.text())
    .then((result) => {
      if (result > 0) {
        alert("추가 성공!");

        // 추가 성공했다면 작성했던 제목과 내용 입력값 지우기
        todoTitle.value = "";
        todoContent.value = "";

        // 할 일이 새롭게 추가되었으므로 > 전체 Todo 개수 조회하는 함수 재호출
        getTotalCount();

        // 전체 Todo 목록 조회하는 함수 재호출
        selectTodoList();
      } else {
        alert("추가 실패...");
      }
    });
});

// 비동기로 할 일 전체 목록을 조회 & html 화면에 출력까지 하는 함수
const selectTodoList = () => {
  fetch("/ajax/selectList")
    // List 형태로 받은 resp를 resp.json 형태로 변환 (응답 결과를 해석하기 위해)
    .then(resp => resp.json())
    .then(todoList => {
      // 매개변수 todoList :
      // 첫 번째 then에서 resp.text() / resp.json()을 했는가에 따라 > 단순 텍스트(String형, 하나의 문자열) / JS Object일 수 있음
      // List, Map, 객체 형태일 때 > JSON을 사용한다  //  단일 String 값의 경우, .text()로 받아도 무관하다
      console.log(todoList);

      // -------------------------------------
      // 기존에 출력되어 있던 할 일 목록을 모두 비우기
      tbody.innerHTML = "";
      // tbody에 tr/td 요소를 생성해서 내용 추가
      for (let todo of todoList) { // 향상된 for문
        // tr 태그 생성
        const tr = document.createElement("tr"); // <tr></tr>
        // JS 객체에 존재하는 key 모음 배열 생성
        const arr = ["todoNo", "todoTitle", "complete", "regDate"];
        for (let key of arr) {
          const td = document.createElement("td"); // <td></td>

          // 제목인 경우
          if (key === "todoTitle") {
            const a = document.createElement("a"); // a태그 생성
            a.innerText = todo[key]; // todo["todoTitle"]
            a.href = "/ajax/detail?todoNo=" + todo.todoNo;
            // <a href="/ajax/detail?todoNo=1">테스트 1 제목</a> : a 태그를 누르면 제출(submit) 이벤트 발생(기본적으로 이벤트를 갖고 있음)
            td.append(a);
            tr.append(td);
            // a태그 클릭 시 페이지 이동 막기(비동기 요청 사용을 위해)
            a.addEventListener("click", (e) => {
              e.preventDefault(); // 기본 이벤트(a 태그의 submit 이벤트) 방지
              // 할 일 상세 조회 비동기 요청 함수 호출
              selectTodo(e.target.href);
            });
            continue;
          }

          // 제목이 아닌 경우
          td.innerText = todo[key]; // todo['todoNo']
          tr.append(td); // tr의 마지막요소 현재 td 추가하기
        }

        // tbody 의 자식으로 tr 추가
        tbody.append(tr);
      }
    });
};

// 비동기로 할 일을 상세 조회하는 함수
const selectTodo = (url) => {
  // fetch() 요청 보내기
  // url == /ajax/detail?todoNo=1
  fetch(url)
  
  // 서버로부터 받은 응답 객체 처리
  // resp.text()로 사용할 시 > 객체가 하나의 문자열로 출력되기 때문에 > 객체 내 속성을 하나씩 뽑아오기가 힘듦
  // .json()을 사용해야 JS object로 가져올 수 있음
  // .then(resp => resp.text())
  // .then (
    //   result => {
      //     console.log(JSON.parse(result)); // .text()를 사용해도 JS Object 형태로 변환할 수는 있긴 함
      //   }
      // )
      .then(resp => resp.json())
      .then(todo => {
        // popup lyaer에 조회해온 값 출력
        popupTodoNo.innerText = todo.todoNo;
    popupTodoTitle.innerText = todo.todoTitle;
    popupComplete.innerText = todo.complete;
    popupRegDate.innerText = todo.regDate;
    popupTodoContent.innerText = todo.todoContent;
    
    // popuplayer의 제목 클릭 시 popuplayer 보이게 하기
    popupLayer.classList.remove("popup-hidden");
  });
};

// popuplayer의 x 클릭 시 popuplayer 숨기기
popupClose.addEventListener("click", () => {
  // display:none 처리해주는 class추가
  popupLayer.classList.add("popup-hidden");
});

// 삭제 버튼 클릭 시
deleteBtn.addEventListener("click", () => {
  // 취소 클릭 시(= confirm 창의 취소를 클릭 = False) 해당 함수 종료
  if( !confirm("정말 삭제하시겠습니까?") ) {
    return;
  }

  // 삭제할 할 일 번호 얻어오기
  const todoNo = popupTodoNo.innerText;

  // 확인 버튼 클릭 시 삭제 비동기 요청 (DELETE 방식으로)
  fetch("/ajax/delete", {
    method : "DELETE", // @DeleteMapping() 처리
    headers : {"Content-Type" : "application/json"},
    // body : JSON.stringfy(todoNo) > 로 명시하는 것이 옳음(엄격한 환경에서는 꼭 명시)
    body : todoNo // 단일 값 > JSON 형태로 자동 변환되어 전달됨
  })
  .then(resp => resp.text())
  .then(result => {
    if(result > 0) {
      alert("삭제 성공!");

      // 상세 조회 팝업 레이어 닫기
      popupLayer.classList.add("popup-hidden");

      // 전체, 완료된 할 일 개수 다시 조회하기
      getTotalCount();
      getCompleteCount();
      selectTodoList();
    } else {
      alert("삭제 실패...")
    };
  })
});

// 완료 여부 변경 클릭 시
changeComplete.addEventListener("click", () => {
  // 현재 완료 여부를 반대값으로 변경한 값, 변경할 할 일 번호
  const complete = popupComplete.innerText === 'Y' ? 'N' : 'Y';
  const todoNo = popupTodoNo.innerText;

  // SQL 수행에 필요한 두 값을 JS 객체로 묶음
  const obj = {"todoNo" : todoNo,
              "complete" : complete };

  // 비동기로 완료 여부 변경 요청(PUT 요청 방식)
  fetch("/ajax/changeComplete", {
    method : "PUT", // @PutMapping
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(obj)
  })
  .then(resp => resp.text())
  .then(result => {
    if(result > 0) { // 성공
      // selectTodo()를 호출해 사용할 경우 > 서버에 부하가 큼 > 완료 여부 뿐만 아니라 다른 속성도 다시 조회됨
      // > 상세 조회 팝업에서 Y/N 바꾸기 (= complete로 교체해주기)
      popupComplete.innerText = complete;

      // 기존 완료된 Todo 개수(getCompleteCount())에서 +1 or -1
      const count = Number(completeCount.innerText);
      if(complete === 'Y') completeCount.innerText = count + 1;
      else                 completeCount.innerText = count - 1;

      // 서버 부하 줄이기나 가능하나 > 코드가 복잡 >> 인적 비용, 시간 비용을 따져 > 서버 부하를 선택하는 게 나을 수도 (트레이드 오프)
      selectTodoList();
    } else { // 실패
      alert("완료 여부 변경 실패...")
    }
  })
});

// 상세 조회 팝업에서 수정 버튼(#updateView) 클릭 시
updateView.addEventListener("click", () => {
  // 기존 상세 조회 팝업 레이어는 숨긴 후
  popupLayer.classList.add("popup-hidden");

  // 수정 팝업 레이어가 보이게끔 함
  updateLayer.classList.remove("popup-hidden");

  // 수정 레이어가 보일 때 > 상세 조회 팝업 레이어에 기존 작성된 제목과 내용을 얻어와 세팅
  updateTitle.value = popupTodoTitle.innerText;

  // innerText로 작성할 경우 > 개행이 무시됨
  // innerHTML으로 작성할 경우 > 개행 문자 부분에 <br> 출력 > textarea 창에서 실제 줄바꿈으로 인식시켜주기 위해 "\n"으로 바꿔주기
  updateContent.value = popupTodoContent.innerHTML.replaceAll("<br>", "\n");

  // 수정 레이어의 수정 버튼에 data-todo-no 속성 추가하기
  // <button id = "updateBtn" data-todo-no = "3">수정</button>과 동일
  updateBtn.setAttribute("data-todo-no", popupTodoNo.innerText);
});

// 수정 레이어에서 취소 버튼 클릭 시
updateCancel.addEventListener("click", () => {
  // 수정 팝업 레이어 숨기고
  updateLayer.classList.add("popup-hidden");

  // 상세 팝업 레이어 보이게 하기
  popupLayer.classList.remove("popup-hidden");
});

// 수정 레이어 -> 수정 버튼 클릭 시
updateBtn.addEventListener("click", (e) => {
  // 서버로 전달해야 하는 값을 JS 객체로 묶음
  const obj = {
    "todoNo" : e.target.dataset.todoNo,
    "todoTitle" : updateTitle.value,
    "todoContent" : updateContent.value
  };

  // 비동기 요청 (PUT)
  fetch("/ajax/update", {
    method : "PUT", // @PutMapping
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(obj)
  })
  .then(resp => resp.text())
  .then(result => {
    if(result > 0) {
      alert("수정 성공!!");
      // 수정 레이어 숨기기
      updateLayer.classList.add("popup-hidden");

      // 상세 조회 레이어 보이기 > 수정한 내용이 출력되도록
      popupTodoTitle.innerText = updateTitle.value;

      popupTodoContent.innerHTML = updateContent.value.replaceAll("\n", "<br>");

      popupLayer.classList.remove("popup-hidden");

      selectTodoList(); // 전체 목록 다시 조회

      updateTitle.value = "";
      updateContent.value = "";
      updateBtn.removeAttribute("data-todo-no");
    } else {
      alert("실패");
    }
  })
});

getTotalCount();
getCompleteCount();
selectTodoList();