// websocket_test.js : 웹소켓 테스트(라이프 사이클에 대한)

// 1. SockJS 라이브러리 추가(common.html) > JS에서 Socket을 제공하긴 하지만 lib을 사용하는 편이 간편하고, pullback을 제공해줌

// 2. SockJS 객체 생성
const testSock = new SockJS("/testSock");
// - WebSockewt 통신을 할 수 있게끔 해주는 객체를 생성함과 동시에 자동으로 http://localhost/testSock으로 연결 요청을 보냄
// > 연결되면 ws://local...로 변환됨

// 3. 생성된 SockJS 객체를 이용해서 서버에 메시지 전달
const sendMessageFn = (name, str) => {
  // JSON을 이용해서 데이터를 TEXT 형태로 전달
  const obj = {
    "name" : name,
    "str" : str
  }
  testSock.send(JSON.stringify(obj));
}

// 4. 서버로부터 클라이언트에게 웹소켓을 이용한 메시지가 전달된 경우
testSock.addEventListener("message", e => {
  // e.dagta : 서버로부터 전달받은 JSON 형태의 message
  const msg = JSON.parse(e.data); // JSON -> JS 객체
  console.log(`${msg.name}의 메시지 : ${msg.str}`);
});