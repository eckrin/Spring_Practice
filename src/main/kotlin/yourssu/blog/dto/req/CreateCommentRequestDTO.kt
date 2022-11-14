package yourssu.blog.dto.req

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CreateCommentRequestDTO (
    @field:NotBlank
    var content:String
)