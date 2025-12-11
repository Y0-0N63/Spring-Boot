package edu.kh.todo.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/* @Configuration : 스프링 설정용 클래스임을 스프링에게 명시 (스프링이 해당 클래스를 설정 정보로 인식하고 사용)
 *					+ 객체로 생성해 내부 코드를 서버 실행 시 모두 바로 실행할 수 있게 함 (스프링이 @Configuration이 있는 코드부터 확인)
 * @PropertySource : properties 파일의 내용을 이용하겠다는 어노테이션
 *  				> 다른 properties도 추가하고 싶으면 어노테이션과 경로를 계속 추가 (classpath: -> src/main/resources) */
@Configuration
@PropertySource("classpath:/config.properties")
public class DBConfig {
	@Autowired // DI(의존성 주입)
	// ApplicationContext : application scop 객체로 현재 프로젝트 그 자체 > 현재 프로젝트의 전반적인 DB 설정과
	// Bean 관리에 접근할 수 있도록 함
	// 스프링이 관리하고 있는 ApplicationContext 객체를 의존성 주입받는다

	// 메서드
	// HikariCP 설정
	private ApplicationContext applicationContext;

	@Bean // : 개발자가 수동으로 bean을 등록하는 어노테이션, @Bean이 작성된 메서드에서 반환된 객체는 Spring Container가
			// 관리함(IOC)
	// @ConfigurationProperties(prefix = "spring.datasource.hikari") : properties
	// 파일의 내용을 이용해서 생성되는 bean을 설정하는 어노테이션
	// prefix(접두사)를 지정하여 spring.datasource.hikari으로 시작하는 설정(DB 연결 정보)을 모두 적용해 객체
	// 반환(return new HikariConfig();)
	// 반환된 객체(new 연산자를 통해 만들어진 인스턴스)는 @Bean 어노테이션을 통해 Bean으로 등록됨
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		// config.properties 파일에서 읽어온 spring.datasource.hikari로 시작하는 모든 값이 자동으로
		// HikariConfig라는 객체의 알맞은 필드에 세팅
		// > 객체화가 진행됨 > Bean으로 등록
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource(HikariConfig config) {
		// 매개변수 HikariConfig config > 등록된 Bean 중 HikariConfig 타입의 Bean을 바인딩 > DataSource
		// 객체를 생성하는 데 이용됨
		// 부모 타입인 DataSource에 자식 타입인 HikariDataSource를 넣어주는 업캐스팅의 형태로 작성됨
		DataSource dataSource = new HikariDataSource(config);

		// Datasource : 애플리케이션이 데이터베이스에 연결할 때 사용하는 설정
		// 1) DB 연결 정보 제공(url, username, password...)
		// 2) Connection pool 관리 (Connection 생성/생명 주기 관리)
		// 3) 트랜잭션 관리
		return dataSource;
	}

	// ===================== Mybatis 설정 추가 =====================
	// Mybatis : Java 애플리케이션에서 SQL을 더 쉽게 사용할 수 있도록 도와주는 영속성 프레임워크(Persistence Framework)
	// 영속성 프레임워크 : 애플리케이션의 데이터를 DB와 같은 저장소에 영구적으로 저장하고, 쉽게 CRUD할 수 있도록 도와주는 프레임워크
	// SqlSessionFactory : SqlSession을 만드는 객체
	@Bean
	public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		// 세팅 1. 매퍼 파일이 모여있는 경로 지정
		sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**.xml"));

		// 세팅 2. 별칭을 지정해야하는 DTO가 모여있는 패키지 지정 -> 해당 패키지에 있는 모든 클래스가 클래스명으로 별칭이 지정됨
//		sessionFactoryBean.setTypeAliasesPackage("edu.kh.project.member.model.dto");
		// edu.kh.todo 세팅 시 패키지 하위에 있는 모든 클래스가 클래스명으로 별칭이 지정됨
		// ex) edu.kh.todo.model.dto.Todo -> Todo (별칭 등록)
		sessionFactoryBean.setTypeAliasesPackage("edu.kh.todo");
		
		// 세팅 3. 마이바티스 설정 파일 경로 지정
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		// SqlSession 객체 반환
		return sessionFactoryBean.getObject();
	}

	// SqlSessionTemplate : 기본 SQL 실행 + 트랜잭션 처리
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}

	// DataSourceTransactionManager : 트랜잭션 매니저
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}