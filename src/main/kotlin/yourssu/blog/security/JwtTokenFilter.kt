package yourssu.blog.security

import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import yourssu.blog.exception.security.EmptyHeaderAuthorizationException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(private var jwtTokenProvider: JwtTokenProvider):OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var token = resolveToken(request)
        if(jwtTokenProvider.validateToken(token)) { //토큰 유효성 확인
            val authentication = jwtTokenProvider.getAuthentication(token) //권한 정보 확인(email, role)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request:HttpServletRequest): String {
        var bearerToken = request.getHeader("Authorization")
        if(bearerToken==null) {
//            println("JwtTokenFilter:bearerToken null")
            throw EmptyHeaderAuthorizationException("Authorization 헤더에 토큰이 없습니다.")
        }
        return bearerToken
    }

}
