package yourssu.blog.security

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import yourssu.blog.config.auth.PrincipalDetails
import yourssu.blog.exception.security.InvalidTokenException
import java.util.*
import java.util.stream.Collectors


@Component
class JwtTokenProvider(
    @Value("#{jwt.secret-key}")
    val SECRET_KEY:String,
    @Value("#{jwt.expire-len}")
    val VALID_TIME:Int
) {

    val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
    val key = Keys.hmacShaKeyFor(keyBytes)


    //jwt토큰을 복호화하여 토큰의 정보를 꺼내기
    fun getAuthentication(accessToken:String): UsernamePasswordAuthenticationToken {
        var claims = parseClaims(accessToken)

        if(claims.get("auth")==null)
            throw InvalidTokenException("Invalid JWT Token")

        // 클레임에서 권한 정보 가져오기
        val authorities: Collection<GrantedAuthority?> =
            Arrays.stream(claims.get("auth").toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())

        // UserDetails 객체를 만들어서 Authentication 리턴
        var principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    //토큰 정보 검증
    fun validateToken(token:String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e:SecurityException) {
            throw InvalidTokenException("Invalid JWT Token")
        } catch (e:MalformedJwtException) {
            throw InvalidTokenException("Invalid JWT Token")
        } catch (e:ExpiredJwtException) {
            throw InvalidTokenException("Expired JWT Token")
        } catch (e:UnsupportedJwtException) {
            throw InvalidTokenException("Unsupported JWT Token")
        } catch (e:IllegalArgumentException) {
            throw InvalidTokenException("Jwt String is Empty")
        }
    }

    //유저 정보 기반으로 accessToken, refreshToken 생성
    fun generateToken(authentication:Authentication): TokenInfo {
        //권한 가져오기
        var authorities = authentication.authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        var now = Date().time
        //accessToken 발급
        var expireTime = Date(now+VALID_TIME)
        var accessToken = Jwts.builder()
            .setSubject(authentication.name)
            .claim("auth", authorities)
            .setExpiration(expireTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
        //refreshToken 발급 (accessToken의 검증)
        var refreshToken = Jwts.builder()
            .setExpiration(expireTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

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