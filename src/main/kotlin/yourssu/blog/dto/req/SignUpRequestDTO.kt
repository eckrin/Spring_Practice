package yourssu.blog.dto.req

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignUpRequestDTO (
    @Email
    @NotBlank
    var email:String,
    @NotBlank
    var password:String,
    @NotBlank
    var username:String
)