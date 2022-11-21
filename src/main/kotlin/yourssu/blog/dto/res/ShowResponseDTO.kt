package yourssu.blog.dto.res

import yourssu.blog.entity.Role

data class ShowResponseDTO (
    var id:Long,
    var email:String,
    var username:String,
    var role:Role,
    var createdAt:String,
    var updatedAt:String
)