package yourssu.blog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import yourssu.blog.config.auth.PrincipalDetailService


@Configuration
@EnableWebSecurity
class WebSecurityConfig {

//    @Autowired
//    private lateinit var userDetailsService: UserDetailsService

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http:HttpSecurity):SecurityFilterChain {
        http
            .httpBasic().disable()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/signUp/**", "/signIn/**")
                .permitAll()
                .anyRequest()
                .authenticated()

        return http.build()
    }

    @Bean
    fun contextSourceFactoryBean(): EmbeddedLdapServerContextSourceFactoryBean? {
        val contextSourceFactoryBean = EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer()
        contextSourceFactoryBean.setPort(0)
        return contextSourceFactoryBean
    }

    //deprecated되면서 안써도 되는거같음
//    protected fun configure(auth: AuthenticationManagerBuilder) {
//        // add our Users for in memory authentication
//        // auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
//        auth.userDetailsService<UserDetailsService>(PrincipalDetailService())
//    }

}