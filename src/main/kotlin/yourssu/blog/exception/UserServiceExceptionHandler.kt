package yourssu.blog.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import yourssu.blog.exception.articleservice.ArticleNotFoundException
import yourssu.blog.exception.userservice.EmailExistsException
import yourssu.blog.exception.userservice.PasswordIncorrectException
import yourssu.blog.exception.userservice.UserNotFoundException
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class UserServiceExceptionHandler {

    @ExceptionHandler(EmailExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidNameException(exception: EmailExistsException, request:HttpServletRequest):ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), "BAD_REQUEST", exception.message, request.requestURI)
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUserNotFountException(exception: UserNotFoundException, request: HttpServletRequest):ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), "BAD_REQUEST", exception.message, request.requestURI)
    }

    @ExceptionHandler(PasswordIncorrectException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlePasswordIncorrectException(exception: PasswordIncorrectException, request: HttpServletRequest):ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), "BAD_REQUEST", exception.message, request.requestURI)
    }

    @ExceptionHandler(ArticleNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleArticleNotFoundException(exception: ArticleNotFoundException, request: HttpServletRequest):ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), "BAD_REQUEST", exception.message, request.requestURI)
    }

}