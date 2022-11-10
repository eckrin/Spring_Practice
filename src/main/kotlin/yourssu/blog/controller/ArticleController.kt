package yourssu.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
    // @AuthenticatedPrincipal로 UserDetailsService에서 리턴한값을 받아서 사용할 수 있다.
    // UserDetails가 아니라 PrincipalDetails로 놓으면 어노테이션이 작동안하는거같은데 이게 PrincipalDetails가 작동을 안하는건지 아니면 받아오는것만 그런건지 모르겠다.
    fun createArticle(@Valid @RequestBody dto:CreateArticleRequestDTO, @AuthenticationPrincipal user:UserDetails):CreateArticleResponseDTO {
//        println("test:"+user.toString())
        return articleService.createArticle(user.username, dto.title, dto.content)
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