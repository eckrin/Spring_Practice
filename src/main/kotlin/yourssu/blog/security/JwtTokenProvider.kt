package yourssu.blog.security

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import yourssu.blog.config.auth.PrincipalDetails
import yourssu.blog.entity.Role
import yourssu.blog.exception.security.InvalidTokenException
import java.util.*
import java.util.stream.Collectors

//국제 인터넷 표준화 기구(IETF)에서는 이를 방지하기 위해 Refresh Token도 Access Token과 같은 유효 기간을 가지도록 하여,
//사용자가 한 번 Refresh Token으로 Access Token을 발급 받았으면, Refresh Token도 다시 발급 받도록 하는 것을 권장하고 있다.

@Component
class JwtTokenProvider(
    @Value("\${jwt.secretkey}")
    val SECRET_KEY:String,
    @Value("\${jwt.expirelen}")
    val VALID_TIME:Int
) {
    //키값 얻기
    val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
    val key = Keys.hmacShaKeyFor(keyBytes)

    //jwt토큰을 복호화하여 토큰의 정보를 꺼내기
    fun getAuthentication(accessToken:String): UsernamePasswordAuthenticationToken {
        var claims = parseClaims(accessToken)

        if(claims.get("email")==null || claims.get("ROLE_")==null)
            throw InvalidTokenException("Invalid JWT Token (Cannot get Email|Role)")

        // 클레임에서 권한 정보(role) 가져오기
        val authorities: Collection<GrantedAuthority?> =
            Arrays.stream(claims.get("ROLE_").toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())

        // User객체를 만들어서 Authentication 리턴
//        var principal = PrincipalDetails(yourssu.blog.entity.User(claims.get("email").toString(), "", claims.get("role").toString()))
        if(claims.subject==null)
            println("A")
        var user = User(claims.get("email") as String, "", authorities)
        return UsernamePasswordAuthenticationToken(user, "", user.authorities)
    }

    //토큰 정보 검증
    fun validateToken(accessToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
            return true
        } catch (e:SecurityException) {
            throw InvalidTokenException("Invalid JWT Token")
        } catch (e:MalformedJwtException) {
            throw InvalidTokenException("Invalid JWT Token (Malformed)")
        } catch (e:ExpiredJwtException) {
            throw InvalidTokenException("Expired JWT Token (Expired)")
        } catch (e:UnsupportedJwtException) {
            throw InvalidTokenException("Unsupported JWT Token")
        } catch (e:IllegalArgumentException) {
            throw InvalidTokenException("Jwt String is Empty")
        }
    }

    //유저 정보 기반으로 accessToken, refreshToken 생성
    fun generateToken(email:String, role:Role): TokenInfo {
        //권한 가져오기
//        var authorities = authentication.authorities.stream()
//            .map(GrantedAuthority::getAuthority)
//            .collect(Collectors.joining(","))

        var now = Date().time
        var expireTime = Date(now+VALID_TIME)
        //accessToken 발급
        var accessToken = Jwts.builder()
//            .setSubject(authentication.name)
            .claim("email", email)
            .claim("ROLE_", role)
            .setExpiration(expireTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
        //refreshToken 발급 (accessToken의 검증)
        var refreshToken = Jwts.builder()
//            .setSubject(authentication.name)
            .claim("email", email)
            .claim("ROLE_", role)
            .setExpiration(Date(now+VALID_TIME*10))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        println(accessToken)

        return TokenInfo(
            grantType = "Bearer",
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}