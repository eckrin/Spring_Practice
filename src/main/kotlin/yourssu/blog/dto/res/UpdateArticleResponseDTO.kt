package yourssu.blog.dto.res

data class UpdateArticleResponseDTO (
    var articleId:Long,
    var email:String,
    var title:String,
    var content:String
)