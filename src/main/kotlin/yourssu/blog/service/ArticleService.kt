package yourssu.blog.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import yourssu.blog.dto.res.CreateArticleResponseDTO
import yourssu.blog.dto.res.UpdateArticleResponseDTO
import yourssu.blog.entity.Article
import yourssu.blog.exception.articleservice.ArticleNotFoundException
import yourssu.blog.exception.userservice.NoPermissionException
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
    fun createArticle(email:String, title:String, content:String):CreateArticleResponseDTO {
        var user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
//        if(!encoder.matches(password, user.password))
//            throw PasswordIncorrectException("유효하지 않은 비밀번호입니다.")
        val article = Article(title, content, user)
        articleRepository.save(article)

        return CreateArticleResponseDTO(article.article_id, user.email!!, article.title!!, article.content!!)
    }

    @Transactional
    fun updateArticle(articleId:Long, email: String, password: String, title: String, content: String): UpdateArticleResponseDTO {
        var user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
        if(!encoder.matches(password, user.password))
            throw PasswordIncorrectException("유효하지 않은 비밀번호입니다.")


        lateinit var article:Article
        try {
            article = articleRepository.findById(articleId).orElse(null) //orElse처리 대신 여기서 Exception이 터져버리는듯?
        } catch (e:Exception) {
            throw ArticleNotFoundException("게시글 정보를 가져올 수 없습니다.")
        }
        if(article.user!!.email!=email)
            throw NoPermissionException("게시글 수정 권한이 없습니다.") //작성자만 수정 가능

        article.update(title, content)
        return UpdateArticleResponseDTO(article.article_id, user.email!!, article.title!!, article.content!!)
    }

    @Transactional
    fun deleteArticle(articleId: Long, email: String, password: String) {
        var user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
        if(!encoder.matches(password, user.password))
            throw PasswordIncorrectException("유효하지 않은 비밀번호입니다.")

        lateinit var article:Article
        try {
            article = articleRepository.findById(articleId).orElse(null) //orElse처리 대신 여기서 Exception이 터져버리는듯?
        } catch (e:Exception) {
            throw ArticleNotFoundException("게시글 정보를 가져올 수 없습니다.")
        }
        if(article.user!!.email!=email)
            throw NoPermissionException("게시글 삭제 권한이 없습니다.") //작성자만 삭제 가능

        articleRepository.deleteById(articleId)
    }

}