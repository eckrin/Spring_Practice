package yourssu.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import yourssu.blog.dto.req.CreateArticleRequestDTO
import yourssu.blog.dto.req.UpdateArticleRequestDTO
import yourssu.blog.dto.res.CreateArticleResponseDTO
import yourssu.blog.dto.res.UpdateArticleResponseDTO
import yourssu.blog.security.Auth
import yourssu.blog.security.AuthInfo
import yourssu.blog.service.ArticleService
import javax.validation.Valid

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("")
    fun createArticle():String {
        return "api test"
    }
}
