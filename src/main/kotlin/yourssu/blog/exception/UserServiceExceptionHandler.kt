package yourssu.blog.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import yourssu.blog.exception.userservice.EmailExistsException
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class UserServiceExceptionHandler {

    @ExceptionHandler(EmailExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidNameException(exception: EmailExistsException, request:HttpServletRequest):ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "error 01", exception.message, request.requestURI)
    }

}