package yourssu.blog.dto.res

import yourssu.blog.entity.Role

data class SignUpResponseDTO (
    var email:String,
    var username:String,
    var role: Role
)