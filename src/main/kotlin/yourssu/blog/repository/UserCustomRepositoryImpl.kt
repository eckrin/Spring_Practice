package yourssu.blog.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import yourssu.blog.entity.QUser
import yourssu.blog.entity.User

@Repository
class UserCustomRepositoryImpl:UserCustomRepository {

    @Autowired
    private lateinit var queryFactory: JPAQueryFactory

    override fun searchAll():List<User> {
        val user = QUser("user")

        return queryFactory
            .selectFrom(user)
            .fetch()
    }


}