package yourssu.blog.security

data class TokenInfo(
    var grantType:String, //bearer
    var accessToken:String,
    var refreshToken:String
)