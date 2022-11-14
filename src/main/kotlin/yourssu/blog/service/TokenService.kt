package yourssu.blog.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import yourssu.blog.dto.res.ReissueResponseDTO
import yourssu.blog.exception.userservice.UserNotFoundException
import yourssu.blog.repository.UserRepository
import yourssu.blog.security.JwtTokenProvider
import javax.transaction.Transactional

@Service
class TokenService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    val BEARER_LITERAL = "Bearer "

    @Transactional
    fun reissue(email:String):ReissueResponseDTO {
        val user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")

        var reissueToken = jwtTokenProvider.generateToken(user.email!!, user.role!!)
        user.updateRefreshToken(reissueToken.refreshToken)

        return ReissueResponseDTO(BEARER_LITERAL + reissueToken.accessToken, BEARER_LITERAL + reissueToken.refreshToken)
    }
}