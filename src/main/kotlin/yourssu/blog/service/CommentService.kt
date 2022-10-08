package yourssu.blog.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import yourssu.blog.dto.res.CreateArticleResponseDTO
import yourssu.blog.dto.res.CreateCommentResponseDTO
import yourssu.blog.dto.res.UpdateArticleResponseDTO
import yourssu.blog.dto.res.UpdateCommentResponseDTO
import yourssu.blog.entity.Article
import yourssu.blog.entity.Comment
import yourssu.blog.exception.articleservice.ArticleNotFoundException
import yourssu.blog.exception.commentservice.CommentNotFoundException
import yourssu.blog.exception.userservice.NoPermissionException
import yourssu.blog.exception.userservice.PasswordIncorrectException
import yourssu.blog.exception.userservice.UserNotFoundException
import yourssu.blog.repository.ArticleRepository
import yourssu.blog.repository.CommentRepository
import yourssu.blog.repository.UserRepository
import javax.transaction.Transactional

@Service
class CommentService {

    @Autowired
    lateinit var encoder:BCryptPasswordEncoder
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var articleRepository: ArticleRepository
    @Autowired
    lateinit var commentRepository: CommentRepository

    @Transactional
    fun createComment(articleId:Long, email:String, password:String, content:String):CreateCommentResponseDTO {
        var user = userRepository.findByEmail(email)
        lateinit var article:Article
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
        if(!encoder.matches(password, user.password))
            throw PasswordIncorrectException("유효하지 않은 비밀번호입니다.")
        try {
            article = articleRepository.findById(articleId).orElse(null)

        } catch(e:Exception) {
            throw ArticleNotFoundException("게시글 정보를 찾을 수 없습니다.")
        }

        var comment = Comment(content, article, user)
        commentRepository.save(comment)

        return CreateCommentResponseDTO(comment.comment_id, user.email!!, comment.content!!)
    }

    @Transactional
    fun updateComment(articleId: Long, commentId:Long, email:String, password:String, content:String):UpdateCommentResponseDTO {
        lateinit var article:Article
        lateinit var comment:Comment
        try {
            article = articleRepository.findById(articleId).orElse(null)

        } catch(e:Exception) {
            throw ArticleNotFoundException("게시글 정보를 찾을 수 없습니다.")
        }

        try {
            comment = commentRepository.findById(commentId).orElse(null)

        } catch(e:Exception) {
            throw CommentNotFoundException("댓글 정보를 찾을 수 없습니다.")
        }

        var user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
        if(!encoder.matches(password, user.password))
            throw PasswordIncorrectException("유효하지 않은 비밀번호입니다.")
        if(comment.user_id!!.email!=user.email)
            throw NoPermissionException("댓글 수정 권한이 없습니다.") //댓글 작성자만 수정 가능
        if(comment.article_id!!.article_id!=articleId)
            throw CommentNotFoundException("${commentId}번 댓글은 ${articleId}번 게시글에 없습니다.")

        comment.updateContent(content)
        return UpdateCommentResponseDTO(comment.comment_id, user.email!!, comment.content!!)
    }

    @Transactional
    fun deleteComment(articleId: Long, commentId: Long, email: String, password: String) {
        lateinit var article:Article
        lateinit var comment:Comment
        try {
            article = articleRepository.findById(articleId).orElse(null)

        } catch(e:Exception) {
            throw ArticleNotFoundException("게시글 정보를 찾을 수 없습니다.")
        }

        try {
            comment = commentRepository.findById(commentId).orElse(null)

        } catch(e:Exception) {
            throw CommentNotFoundException("댓글 정보를 찾을 수 없습니다.")
        }

        var user = userRepository.findByEmail(email)
        if(user==null)
            throw UserNotFoundException("유저 정보를 찾을 수 없습니다.")
        if(!encoder.matches(password, user.password))
            throw PasswordIncorrectException("유효하지 않은 비밀번호입니다.")
        if(comment.user_id!!.email!=user.email || article.user_id!!.email!=user.email)
            throw NoPermissionException("댓글 삭제 권한이 없습니다.") //댓글 작성자, 글 작성자만 삭제 가능
        if(comment.article_id!!.article_id!=articleId)
            throw CommentNotFoundException("${commentId}번 댓글은 ${articleId}번 게시글에 없습니다.")


        commentRepository.deleteById(comment.comment_id)
    }
}