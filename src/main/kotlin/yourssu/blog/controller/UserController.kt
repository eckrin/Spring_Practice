package yourssu.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yourssu.blog.dto.req.SignInRequestDTO
import yourssu.blog.dto.req.SignUpRequestDTO
import yourssu.blog.dto.res.ShowResponseDTO
import yourssu.blog.dto.res.SignInResponseDTO
import yourssu.blog.dto.res.SignUpResponseDTO
import yourssu.blog.entity.User
import yourssu.blog.security.Auth
import yourssu.blog.security.AuthInfo
import yourssu.blog.service.UserService
import javax.validation.Valid
import javax.validation.constraints.Email

@RestController
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/signUp")
    fun signUp(@Valid @RequestBody dto: SignUpRequestDTO):SignUpResponseDTO {
        return userService.signUp(dto.email, dto.password, dto.username, dto.role)
    }

    @PostMapping("/signIn")
    fun signIn(@Valid @RequestBody dto:SignInRequestDTO):SignInResponseDTO {
        return userService.signIn(dto.email, dto.password)
    }

    @PostMapping("/withdraw")
    fun withdraw(@Auth authInfo: AuthInfo) {
        return userService.withdraw(authInfo.email)
    }

    @GetMapping("/show")
    fun show(@RequestParam(required = false) username: String?,
             @RequestParam(required = false) email: String?,
             @RequestParam(required = false) createdAtStart: String?,
             @RequestParam(required = false) createdAtEnd: String?,
             @RequestParam(required = false) updatedAtStart: String?,
             @RequestParam(required = false) updatedAtEnd: String?,
             @Auth authInfo: AuthInfo): List<ShowResponseDTO> {

        return userService.show(authInfo.email, username, email, createdAtStart, createdAtEnd, updatedAtStart, updatedAtEnd)
    }
}