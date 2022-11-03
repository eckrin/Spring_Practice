package yourssu.blog.dto.res

import yourssu.blog.entity.Role

data class SignInResponseDTO (
    var email:String,
    var username:String,
    var role: Role,
    var accessToken: String,
    var refreshToken: String
)