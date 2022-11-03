package yourssu.blog.config.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import yourssu.blog.exception.userservice.UserNotFoundException
import yourssu.blog.repository.UserRepository

@Service
class PrincipalDetailService:UserDetailsService {

    @Autowired
    private lateinit var userRepository:UserRepository

    override fun loadUserByUsername(email: String?): UserDetails {
        val principal= userRepository.findByEmail(email)
        if(principal==null)
            throw UserNotFoundException("해당 사용자를 찾을 수 없습니다")

        return PrincipalDetails(principal)
    }
}