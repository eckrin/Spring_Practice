package yourssu.blog.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import yourssu.blog.entity.QUser
import yourssu.blog.entity.Role
import yourssu.blog.entity.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class UserCustomRepositoryImpl:UserCustomRepository {

    @Autowired
    private lateinit var queryFactory: JPAQueryFactory

    val user = QUser("user")

    override fun searchAllUser(email:String, createdAtStart: LocalDate?, createdAtEnd: LocalDate?):List<User> {

        val builder = BooleanBuilder()

        //createdAtStart와 createdAtEnd 사이의 경우
        if(createdAtStart!=null && createdAtEnd!=null) {
            builder.and(user.created_at.between(
                createdAtStart.atStartOfDay(), //하루 시작시간
                LocalDateTime.of(createdAtEnd, LocalTime.MAX).withNano(0) //하루 끝
            ))
        }
        //createdAtStart 이전에 생성된 경우만
        else if(createdAtStart!=null) {
            builder.and(user.created_at.gt(
                createdAtStart.atStartOfDay()
            ))
        }
        //createdAtEnd 이후에 생성된 경우만
        else if(createdAtEnd!=null) {
            builder.and(user.created_at.lt(
                LocalDateTime.of(createdAtEnd, LocalTime.MAX).withNano(0)
            ))
        }

        val result = queryFactory
            .selectFrom(user)
            .where(user.role.eq(Role.USER).and(builder)) //role==user
            .orderBy(user.user_id.desc())
            .fetch()

         return result
    }

    override fun searchAllUserByEmail(email:String, createdAtStart: LocalDate?, createdAtEnd: LocalDate?): List<User> {

        val builder = BooleanBuilder()

        //createdAtStart와 createdAtEnd 사이의 경우
        if(createdAtStart!=null && createdAtEnd!=null) {
            builder.and(user.created_at.between(
                createdAtStart.atStartOfDay(), //하루 시작시간
                LocalDateTime.of(createdAtEnd, LocalTime.MAX).withNano(0) //하루 끝
            ))
        }
        //createdAtStart 이전에 생성된 경우만
        else if(createdAtStart!=null) {
            builder.and(user.created_at.gt(
                createdAtStart.atStartOfDay()
            ))
        }
        //createdAtEnd 이후에 생성된 경우만
        else if(createdAtEnd!=null) {
            builder.and(user.created_at.lt(
                LocalDateTime.of(createdAtEnd, LocalTime.MAX).withNano(0)
            ))
        }

        val result = queryFactory
            .selectFrom(user)
            .where(user.role.eq(Role.USER).and(user.email.eq(email)).and(builder)) //role==user
            .orderBy(user.user_id.desc())
            .fetch()

        return result
    }

    override fun searchAllUserByUsername(username:String, createdAtStart: LocalDate?, createdAtEnd: LocalDate?): List<User> {

        val builder = BooleanBuilder()

        //createdAtStart와 createdAtEnd 사이의 경우
        if(createdAtStart!=null && createdAtEnd!=null) {
            builder.and(user.created_at.between(
                createdAtStart.atStartOfDay(), //하루 시작시간
                LocalDateTime.of(createdAtEnd, LocalTime.MAX).withNano(0) //하루 끝
            ))
        }
        //createdAtStart 이전에 생성된 경우만
        else if(createdAtStart!=null) {
            builder.and(user.created_at.gt(
                createdAtStart.atStartOfDay()
            ))
        }
        //createdAtEnd 이후에 생성된 경우만
        else if(createdAtEnd!=null) {
            builder.and(user.created_at.lt(
                LocalDateTime.of(createdAtEnd, LocalTime.MAX).withNano(0)
            ))
        }

        val result = queryFactory
            .selectFrom(user)
            .where(user.role.eq(Role.USER).and(user.username.eq(username)).and(builder)) //role==user
            .orderBy(user.user_id.desc())
            .fetch()

        return result
    }


}
