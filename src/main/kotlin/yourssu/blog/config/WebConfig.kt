package yourssu.blog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import yourssu.blog.security.AuthArgumentResolver

@Configuration
class WebConfig:WebMvcConfigurer {

    @Bean
    fun authArgumentResolver():AuthArgumentResolver {
        return AuthArgumentResolver()
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver())
        super.addArgumentResolvers(resolvers)
    }
}