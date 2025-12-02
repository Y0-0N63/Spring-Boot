// 목록으로 버튼 동작(메인페이지로 이동)
const goToList = document.querySelector("#goToList");
goToList.addEventListener("click", () => {
  location.href = "/"; // 메인페이지(/)로 Get 방식 요청
});

// 완료 버튼 클릭 시 동작
const completeBtn = document.querySelector(".complete-btn");
completeBtn.addEventListener("click", (e) => {
  // 요소.dataset : data-*속성에 저장된 값을 반환하는 키워드로 todo에서는 data-todo-no라고 세팅한 속성값을 얻어옴
  // (html 표기법) data-todo-no > (js 표기법(카멜 케이스)) dataset.todoNo 로 변환
  const todoNo = e.target.dataset.todoNo;

  // 기존 완료 여부 값 얻어오기
  let complete = e.target.innerText; 

  // Y <-> N
  complete = (complete === 'y') ? 'N' : 'Y';

  // 완료 여부 수정 요청하기
  location.href = `/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;
});

// 수정 버튼 클릭 시 동작
const updateBtn = document.querySelector("#updateBtn");
updateBtn.addEventListener("click", e => {
  location.href = `/todo/update?todoNo=${e.target.dataset.todoNo}`;
});

// 삭제 버튼 클릭 시 동작
const deleteBtn = document.querySelector("#deleteBtn");
deleteBtn.addEventListener("click", (e) => {
  if(confirm("정말 삭제하시겠습니까?")) {
    // 삭제 요청 (location.href는 Get 방식 요청 )
    location.href = `/todo/delete?todoNo=${e.target.dataset.todoNo}`;
  }
});