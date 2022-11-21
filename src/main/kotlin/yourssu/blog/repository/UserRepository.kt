package yourssu.blog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import yourssu.blog.entity.User

@Repository
interface UserRepository:JpaRepository<User, Long>, UserCustomRepository {
    fun findByEmail(email:String?): User?
    fun existsByEmail(email:String):Boolean
}