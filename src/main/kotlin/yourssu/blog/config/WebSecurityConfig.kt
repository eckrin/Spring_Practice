package yourssu.blog.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import yourssu.blog.exception.ExceptionHandlerFilter
import yourssu.blog.security.JwtTokenFilter
import yourssu.blog.security.JwtTokenProvider


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig {

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

//    @Bean
//    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
//        return authenticationConfiguration.authenticationManager
//    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    //httpsecurity: allow configuring web-based security
    fun filterChain(http:HttpSecurity):SecurityFilterChain {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            //이럻게 하면 필터 제외 안돼서 밑에 WebSecurityCustomiszer Bean 이용
//            .antMatchers("/signUp/**", "/signIn/**").permitAll()
//            .anyRequest().authenticated()
            //이것도 적용되지 않음
//            .antMatchers(HttpMethod.GET,"/show").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java) //AuthenticationFilter전에 JwtToken필터 실행
            .addFilterBefore(ExceptionHandlerFilter(), JwtTokenFilter::class.java) //필터에서 발생한 Exception 처리


        return http.build()
    }

    @Bean
    //websecurity: configuring that have global effect of web security
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web: WebSecurity -> web.ignoring().antMatchers(
            "/signIn/**",
            "/signUp/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**"
        )}
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