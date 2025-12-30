package edu.kh.project.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import edu.kh.project.websocket.handler.ChattingWebsocketHandler;
import edu.kh.project.websocket.handler.TestWebsocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration // 서버 실행 시 작성된 메서드를 모두 수행
@EnableWebSocket // 웹소켓 활성화 설정
// WebSocketConfig : 설정용 클래스 > 설정에서 사용할 동작이 작성된 클래스 필요 > SessionHandshakeInterceptor, TestWebsocketHandler
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
	// WebSocketConfigurer : Spring에서 웹소켓 통신을 어디에서, 어떤 방식으로 할 것인지 규칙을 정의하는 설정용 인터페이스
	// 1. 핸들러 등록
	// 2. 접속 주소 매핑
	// 3. 부가 기능 설정
	
	private final TestWebsocketHandler testWebsocketHandler;
	
	private final ChattingWebsocketHandler chattingWebsocketHandler;
	
	// SessionHandshakeInterceptor와 상속 관계이며 Session...Interceptor가 Bean으로 등록되어있기 때문에 handshakeInterceptor로 주입됨
	private final HandshakeInterceptor handshakeInterceptor;
	
	// 웹소켓 핸들러를 등록하는 메서드
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// addHandler(웹소켓핸들러, "웹소켓 요청 주소") : 
		registry
		// http://localhost/testSock으로 클라이언트 요청을 하면 > Websocket 통신으로 변환 후 > testWebsocketHandler가 처리하도록 등록
		.addHandler(testWebsocketHandler, "/testSock")
		// 클라이언트 연결 시 > 클라이언트의 session을 가로채 핸들러에게 전달하는 handshakeInterceptor 등록
		.addInterceptors(handshakeInterceptor)
		// 웹소켓 요청이 허용되는 ip/도메인 지정
		// > http://localhost/ : 루트 이하의 모든 주소, http://127.0.0.1/ : 본인의 로컬 ip, 192.168.132.22 : IPv4 주소
		.setAllowedOriginPatterns("http://localhost/", "http://127.0.0.1/", "http://192.168.132.22/")
		// SockJs 라이브러리 지원(허용)
		.withSockJS();
		
		// --------------- Chatting ---------------
		registry.addHandler(chattingWebsocketHandler, "/chattingSock").addInterceptors(handshakeInterceptor)
		.setAllowedOriginPatterns("http://localhost/", "http://127.0.0.1/", "http://192.168.132.22/").withSockJS();
	}
}