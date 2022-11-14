package yourssu.blog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

@Configuration
@EnableWebMvc
class SwaggerConfig:WebMvcConfigurer {

    @Bean
    //swagger 설정의 핵심이 되는 bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false) //Swagger에서 제공해주는 기본 응답 코드를 노출할지 여부
            .select()
            .apis(RequestHandlerSelectors.basePackage("yourssu.blog")) //api스펙이 작성되어있는 패키지를 설정
            .paths(PathSelectors.any()) //apis()로 들어간 패키지 중에 특별한 경로를 지정할 수 있음
            .build()
            .apiInfo(apiInfo())
            .securityContexts(mutableListOf(securityContext()))
            .securitySchemes(mutableListOf(apiKey()) as List<SecurityScheme>?) //그냥 캐스팅해주면 되나
    }

    //swagger UI로 노출할 정보 (Authoriza버튼 클릭시 입력값 설정)
    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Yourssu Swagger Practice")
            .description("Backend Security Incubating")
            .version("1.0")
            .build()
    }

    //인증 방식 설정
    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build()
        //일반적으로 코틀린에서는 빌더패턴이 필요없다고 하는데 이런경우 어떻게?
        //SecurityContext가 java로 작성되어서 빌더패턴을 사용해야하는건가
//        return SecurityContext(
//            securityReferences = defaultAuth()
//        )
    }

    private fun apiKey(): ApiKey {
        return ApiKey("Authorization", "Authorization", "header")
    }

    private fun defaultAuth():List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEvery")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return mutableListOf(SecurityReference("Authorization", authorizationScopes))
    }
}