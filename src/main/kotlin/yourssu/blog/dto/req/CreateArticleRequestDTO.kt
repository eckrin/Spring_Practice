package yourssu.blog.dto.req

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CreateArticleRequestDTO (
    @field:Email
    @field:NotBlank
    var email:String,
    @field:NotBlank
    var password:String,
    @field:NotBlank
    var title:String,
    @field:NotBlank
    var content:String
)