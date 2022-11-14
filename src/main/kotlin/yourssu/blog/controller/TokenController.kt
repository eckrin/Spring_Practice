package yourssu.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import yourssu.blog.dto.res.ReissueResponseDTO
import yourssu.blog.security.Auth
import yourssu.blog.security.AuthInfo
import yourssu.blog.service.TokenService

@RestController
class TokenController {

    @Autowired
    private lateinit var tokenService: TokenService

    //재발급은 가능하지만 jwt는 토큰이 만료되기 전에는 삭제 불가능
    @PostMapping("reissue")
    fun reissue(@Auth authInfo: AuthInfo): ReissueResponseDTO {
        return tokenService.reissue(authInfo.email)
    }
}