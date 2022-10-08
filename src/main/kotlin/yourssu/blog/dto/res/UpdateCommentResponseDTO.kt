package yourssu.blog.dto.res

data class UpdateCommentResponseDTO (
    var commentId:Long,
    var email:String,
    var content:String
)