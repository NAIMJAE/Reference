package kr.co.Ref1.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    /*
        OpenAPI 객체 
        - Swagger 스펙의 최상위 개체로 API에 대한 전반적인 메타데이터를 표현하는 객체
        - API의 정보, 엔드포인트, 파라미터, 응답 등을 정의
        
        Info : API에 대한 기본 정보 설정
        - title : API의 제목 설정
        - version : API의 버전 설정
        - description : API에 대한 설명
     */
    @Bean
    public OpenAPI openAPI(){ 

        Info info = new Info().title("Reference API 목록").version("1.0").description("REST API 실습");

        return new OpenAPI().components(new Components()).info(info);
    }
}
