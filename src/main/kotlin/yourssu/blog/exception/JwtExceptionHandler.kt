package yourssu.blog.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import yourssu.blog.exception.articleservice.ArticleNotFoundException
import yourssu.blog.exception.commentservice.CommentNotFoundException
import yourssu.blog.exception.security.EmptyHeaderAuthorizationException
import yourssu.blog.exception.security.InvalidTokenException
import yourssu.blog.exception.userservice.EmailExistsException
import yourssu.blog.exception.userservice.NoPermissionException
import yourssu.blog.exception.userservice.PasswordIncorrectException
import yourssu.blog.exception.userservice.UserNotFoundException
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class JwtExceptionHandler {

    @ExceptionHandler(InvalidTokenException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidTokenException(exception: InvalidTokenException, request: HttpServletRequest):ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), "BAD_REQUEST", exception.message, request.requestURI)
    }

    @ExceptionHandler(EmptyHeaderAuthorizationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEmptyHeaderAuthorizationException(exception: EmptyHeaderAuthorizationException, request: HttpServletRequest):ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), "BAD_REQUEST", exception.message, request.requestURI)
    }
}