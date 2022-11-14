package yourssu.blog.dto.req

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UpdateCommentRequestDTO (
    @field:NotBlank
    var content:String
)