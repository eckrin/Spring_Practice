package yourssu.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import yourssu.blog.dto.req.*
import yourssu.blog.dto.res.CreateArticleResponseDTO
import yourssu.blog.dto.res.CreateCommentResponseDTO
import yourssu.blog.dto.res.UpdateArticleResponseDTO
import yourssu.blog.dto.res.UpdateCommentResponseDTO
import yourssu.blog.security.Auth
import yourssu.blog.security.AuthInfo
import yourssu.blog.service.ArticleService
import yourssu.blog.service.CommentService
import javax.validation.Valid

@RestController
@RequestMapping("/comment")
class CommentController {

    @Autowired
    private lateinit var commentService: CommentService

    @PostMapping("/create/{articleId}")
    fun createComment(@PathVariable articleId:Long, @Valid @RequestBody dto:CreateCommentRequestDTO, @Auth authInfo: AuthInfo):CreateCommentResponseDTO {
        return commentService.createComment(articleId, authInfo.email, dto.content)
    }

    @PostMapping("/update/{articleId}/{commentId}")
    fun updateComment(@PathVariable articleId:Long, @PathVariable commentId:Long, @Valid @RequestBody dto:UpdateCommentRequestDTO, @Auth authInfo: AuthInfo):UpdateCommentResponseDTO {
        return commentService.updateComment(articleId, commentId, authInfo.email, dto.content)
    }

    @PostMapping("/delete/{articleId}/{commentId}")
    fun deleteComment(@PathVariable articleId: Long, @PathVariable commentId: Long, @Auth authInfo: AuthInfo) {
        return commentService.deleteComment(articleId, commentId, authInfo.email)
    }
}