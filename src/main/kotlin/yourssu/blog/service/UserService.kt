package yourssu.blog.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import yourssu.blog.dto.res.ShowResponseDTO
import yourssu.blog.dto.res.SignInResponseDTO
import yourssu.blog.dto.res.SignUpResponseDTO
import yourssu.blog.entity.Role
import yourssu.blog.entity.User
import yourssu.blog.exception.userservice.EmailExistsException
import yourssu.blog.exception.userservice.PasswordIncorrectException
import yourssu.blog.exception.userservice.UserNotFoundException
import yourssu.blog.repository.UserRepository
import yourssu.blog.security.JwtTokenProvider
import java.time.LocalDate
import java.util.stream.Collectors
import javax.transaction.Transactional

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder
//    @Autowired
//    private lateinit var authenticationManagerBuilder:AuthenticationManagerBuilder
    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    val BEARER_LITERAL = "Bearer "

    @Transactional
    fun signUp(email:String, password:String, username:String, role:String):SignUpResponseDTO {
        if(userRepository.existsByEmail(email)) {
            throw EmailExistsException("중복된 이메일입니다.")
        }
        userRepository.save(
            User(email, encoder.encode(password), username, role)
        )

        return SignUpResponseDTO(email, username, Role.valueOf(role))
    }

    @Transactional
    fun signIn(email:String, password:String):SignInResponseDTO {
        val user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
        if(!encoder.matches(password, user.password))
            throw PasswordIncorrectException("유효하지 않은 비밀번호입니다.")

//        var authenticationToken = UsernamePasswordAuthenticationToken(email, password)
//        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        val tokenInfo = jwtTokenProvider.generateToken(user.email!!, user.role!!)
        user.updateRefreshToken(tokenInfo.refreshToken)

        return SignInResponseDTO(user.email!!, user.username!!, user.role!!, BEARER_LITERAL+tokenInfo.accessToken, BEARER_LITERAL+tokenInfo.refreshToken)
    }

    @Transactional
    fun withdraw(email: String) {
        val user = userRepository.findByEmail(email)
        if(user==null) {
            throw UserNotFoundException("해당 유저가 존재하지 않습니다.")
        }
//        if(!encoder.matches(password, user.password)) {
//            throw PasswordIncorrectException("비밀번호가 틀립니다.")
//        }
        userRepository.deleteById(user.user_id)
    }

    @Transactional
    fun show(adminEmail: String,
             username:String?,
             userEmail:String?,
             createdAtStart: LocalDate?,
             createdAtEnd:LocalDate?,
             updatedAtStart:LocalDate?,
             updatedAtEnd:LocalDate?): List<ShowResponseDTO> {

        val user = userRepository.findByEmail(adminEmail)
        if(user==null)
            throw UserNotFoundException("ADMIN 정보를 찾을 수 없습니다.")

        lateinit var userList: List<ShowResponseDTO>

        //모든 유저의 정보 출력
        if(username==null && userEmail==null) {
            userList = userRepository.searchAllUser(createdAtStart, createdAtEnd).stream()
                .map { user ->
                    ShowResponseDTO(
                        user.user_id,
                        user.email,
                        user.username,
                        user.role,
                        user.created_at.toString(),
                        user.updated_at.toString()
                    )
                }
                .collect(Collectors.toList())
        }
        else if(username==null) {
            userList = userRepository.searchAllUserByEmail(userEmail!!, createdAtStart, createdAtEnd).stream()
                .map { user ->
                    ShowResponseDTO(
                        user.user_id,
                        user.email,
                        user.username,
                        user.role,
                        user.created_at.toString(),
                        user.updated_at.toString()
                    )
                }
                .collect(Collectors.toList())
        }
        else if(userEmail==null) {
            userList = userRepository.searchAllUserByUsername(username, createdAtStart, createdAtEnd).stream()
                .map { user ->
                    ShowResponseDTO(
                        user.user_id,
                        user.email,
                        user.username,
                        user.role,
                        user.created_at.toString(),
                        user.updated_at.toString()
                    )
                }
                .collect(Collectors.toList())
        }

        return userList //User리스트로 변환하여 리턴
    }
}
