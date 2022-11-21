package yourssu.blog.security

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthArgumentResolver:HandlerMethodArgumentResolver {

    //대상이 되는 파라미터를 정의
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Auth::class.java)
                && parameter.parameterType == AuthInfo::class.java
    }

    //Auth 어노테이션을 사용하면 jwt토큰의 email값이 AuthInfo의 email로 바인딩되도록 해야 한다.
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        //SecurityContextHolder에서 인증객체의 username(email) 반환되게끔
        val authentication = SecurityContextHolder.getContext().authentication.principal as UserDetails
        return AuthInfo(authentication.username)
    }
}