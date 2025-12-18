/* 글쓰기 버튼 클릭 시 */
const insertBtn = document.querySelector("#insertBtn");

// 글쓰기 버튼이 존재할 때 (로그인 상태인 경우)
if(insertBtn != null) {
    insertBtn.addEventListener('click', () => {

        // html : 타임리프 "${java에서 가지고 온 값}"
        // JS : "${}", '${}', `일반문자열${JS변수}`
        // get 방식 요청
        // /editBoard/1/insert
        // 동기식 요청 : location을 이용, 비동기식 요청 : fetch() API 이용
        location.href = `/editBoard/${boardCode}/insert`;
    });
}