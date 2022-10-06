package yourssu.blog.dto.res

data class CreateArticleResponseDTO (
    var articleId:Long,
    var email:String,
    var title:String,
    var content:String
)