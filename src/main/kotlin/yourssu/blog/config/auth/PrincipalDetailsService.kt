package yourssu.blog.config.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import yourssu.blog.exception.userservice.UserNotFoundException
import yourssu.blog.repository.UserRepository

@Service
class PrincipalDetailsService:UserDetailsService {

    @Autowired
    private lateinit var userRepository:UserRepository

    //로그인 요청(username, password)를 가로챌때 username이 DB에 있는지 확인하여 리턴 (pw는 알아서 처리)
    override fun loadUserByUsername(email: String?): UserDetails {
        val principal= userRepository.findByEmail(email)
        if(principal==null)
            throw UserNotFoundException("해당 사용자를 찾을 수 없습니다")

        return PrincipalDetails(principal) //시큐리티 세션에 커스텀한 UserDetails 저장
    }
}