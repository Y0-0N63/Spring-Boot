// 목록으로 버튼 동작(메인페이지로 이동)
const goToList = document.querySelector("#goToList");
goToList.addEventListener("click", () => {
  location.href = "/"; // 메인페이지(/)로 Get 방식 요청
});

// 삭제 버튼 클릭 시 동장
const deleteBtn = document.querySelector("#deleteBtn");
deleteBtn.addEventListener("click", (e) => {
  if(confirm("정말 삭제하시겠습니까?")) {
    // 삭제 요청
    location.href = `/todo/delete?todoNo=${e.target.dataset.todoNo}`;
  }
});
