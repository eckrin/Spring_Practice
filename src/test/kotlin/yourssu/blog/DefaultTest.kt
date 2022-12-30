package yourssu.blog

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import javax.transaction.Transactional

@AutoConfigureMockMvc
@SpringBootTest
@Rollback
@Transactional
class DefaultTest {

    val email = "email@urssu.com"
    val password = "password"
    val username = "username"
    val title = "title1"
    val content = "content1"
    val email2 = "email2@urssu.com"
    val password2 = "password2"
    val username2 = "username2"
	val role = "USER"
	lateinit var accessToken:String
	lateinit var accessToken2:String
}
