package yourssu.blog.repository

import org.springframework.data.domain.Pageable
import yourssu.blog.entity.User
import java.time.LocalDate

interface UserCustomRepository {

    fun searchAllUser(createdAtStart: LocalDate?, createdAtEnd: LocalDate?):List<User>
    fun searchAllUserByEmail(email:String, createdAtStart: LocalDate?, createdAtEnd: LocalDate?):List<User>
    fun searchAllUserByUsername(username:String, createdAtStart: LocalDate?, createdAtEnd: LocalDate?):List<User>
}
