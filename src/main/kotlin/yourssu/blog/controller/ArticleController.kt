package yourssu.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import yourssu.blog.dto.req.CreateArticleRequestDTO
import yourssu.blog.dto.req.DeleteArticleRequestDTO
import yourssu.blog.dto.req.UpdateArticleRequestDTO
import yourssu.blog.dto.res.CreateArticleResponseDTO
import yourssu.blog.dto.res.UpdateArticleResponseDTO
import yourssu.blog.service.ArticleService
import javax.validation.Valid

@RestController
@RequestMapping("/article")
class ArticleController {

    @Autowired
    private lateinit var articleService:ArticleService

    @PostMapping("/create")
    fun createArticle(@Valid @RequestBody dto:CreateArticleRequestDTO):CreateArticleResponseDTO {
        return articleService.createArticle(dto.email, dto.password, dto.title, dto.content)
    }

    @PostMapping("/update/{articleId}")
    fun updateArticle(@PathVariable articleId:Long, @Valid @RequestBody dto:UpdateArticleRequestDTO):UpdateArticleResponseDTO {
        return articleService.updateArticle(articleId, dto.email, dto.password, dto.title, dto.content)
    }

    @PostMapping("/delete/{articleId}")
    fun deleteArticle(@PathVariable articleId: Long, @Valid @RequestBody dto:DeleteArticleRequestDTO) {
        return articleService.deleteArticle(articleId, dto.email, dto.password)
    }
}