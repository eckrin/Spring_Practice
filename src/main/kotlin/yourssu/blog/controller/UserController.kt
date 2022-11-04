package yourssu.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yourssu.blog.dto.req.SignInRequestDTO
import yourssu.blog.dto.req.SignUpRequestDTO
import yourssu.blog.dto.req.WithdrawRequestDTO
import yourssu.blog.dto.res.SignInResponseDTO
import yourssu.blog.dto.res.SignUpResponseDTO
import yourssu.blog.service.UserService
import javax.validation.Valid

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
    fun withdraw(@Valid @RequestBody dto:WithdrawRequestDTO) {
        return userService.withdraw(dto.email, dto.password)
    }
}