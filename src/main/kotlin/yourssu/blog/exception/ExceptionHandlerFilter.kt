package yourssu.blog.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import yourssu.blog.exception.security.EmptyHeaderAuthorizationException
import yourssu.blog.exception.security.InvalidTokenException
import java.io.IOException
import java.time.LocalDateTime
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {

    private var objectMapper = ObjectMapper()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (exception: InvalidTokenException) {
            setErrorResponse(
                HttpStatus.BAD_REQUEST,
                request, response, exception.message
            )
        } catch (exception: EmptyHeaderAuthorizationException) {
            setErrorResponse(
                HttpStatus.BAD_REQUEST,
                request, response, exception.message
            )
        }
    }

    private fun setErrorResponse(
        status: HttpStatus,
        request: HttpServletRequest,
        response: HttpServletResponse,
        exceptionMessage: String?,
    ) {
        objectMapper.registerModule(JavaTimeModule()) //역직렬화 오류 해결
        response.status = status.value()
        response.contentType = "application/json"
        val errorResponse = ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.toString(),
            exceptionMessage, request.requestURI
        )
        try {
            response.characterEncoding = "UTF-8"
            response.writer.write(objectMapper.writeValueAsString(errorResponse))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}