package yourssu.blog.config.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import yourssu.blog.entity.User

class PrincipalDetails(private val user: User):UserDetails {

    override fun getPassword(): String {
        return user.password!!
    }

    override fun getUsername(): String {
        return user.email!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val collectors: MutableCollection<GrantedAuthority> = ArrayList()
        collectors.add(GrantedAuthority {
            user.role.toString() //ROLE_을 붙여서 리턴해주어야함
        })
        return collectors
    }
}