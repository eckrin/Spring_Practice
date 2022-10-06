package yourssu.blog.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import yourssu.blog.dto.res.CreateArticleResponseDTO
import yourssu.blog.dto.res.UpdateArticleResponseDTO
import yourssu.blog.entity.Article
import yourssu.blog.exception.userservice.PasswordIncorrectException
import yourssu.blog.exception.userservice.UserNotFoundException
import yourssu.blog.repository.ArticleRepository
import yourssu.blog.repository.UserRepository
import javax.transaction.Transactional

@Service
class ArticleService {

    @Autowired
    private lateinit var articleRepository: ArticleRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var encoder:BCryptPasswordEncoder

    @Transactional
    fun createArticle(email:String, password:String, title:String, content:String):CreateArticleResponseDTO {
        var user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
        if(!encoder.matches(password, user.password))
            throw PasswordIncorrectException("유효하지 않는 비밀번호입니다.")

        articleRepository.save(
                Article(title, content, user)
        )
        var article = articleRepository.findByEmail(email)

        return CreateArticleResponseDTO(article.article_id, email, title, content)
    }

    @Transactional
    fun updateArticle(email: String, password: String, title: String, content: String): UpdateArticleResponseDTO {
        var user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
        if(!encoder.matches(password, user.password))
            throw PasswordIncorrectException("유효하지 않은 비밀번호입니다.")

        var article = articleRepository.findById()
    }
}