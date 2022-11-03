package yourssu.blog.dto.req


import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignUpRequestDTO (
    @field:Email
    @field:NotBlank
    var email:String,
    @field:NotBlank
    var password:String,
    @field:NotBlank
    var username:String,
    @field:NotBlank
    var role:String
)