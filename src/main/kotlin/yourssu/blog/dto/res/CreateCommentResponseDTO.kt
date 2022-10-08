package yourssu.blog.dto.res

data class CreateCommentResponseDTO (
    var commentId:Long,
    var email:String,
    var content:String
)