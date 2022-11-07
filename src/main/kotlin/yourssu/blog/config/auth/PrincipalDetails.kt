package yourssu.blog.config.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import yourssu.blog.entity.User

//스프링 시큐리티가 로그인 요청을 가로채서 UserDetails를 구현한 객체를 저장하게끔 하자.
//UserDetails를 구현한 객체(PrincipalDetails)를 만들고, UserDetailsService를 구현한 객체(PrincipalDetailsService)에서 그것을 저장.
class PrincipalDetails(private val user: User):UserDetails {

    override fun getPassword(): String {
        return user.password!!
    }

    override fun getUsername(): String {
        return user.email!!
    }

    //계정이 만료되지 않았는지를 리턴
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    //계정이 잠기지 않았는지를 리턴
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    //비밀번호가 만료되지 않았는지를 리턴
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    //계정이 활성화되지 않았는지를 리턴
    override fun isEnabled(): Boolean {
        return true
    }

    //계정의 권한 리턴
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val collectors: MutableCollection<GrantedAuthority> = ArrayList()
        collectors.add(GrantedAuthority {
            user.role.toString()
        })
        return collectors
    }
}