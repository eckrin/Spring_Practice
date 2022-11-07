package yourssu.blog.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
class User() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var user_id: Long = 0

    @Email
    @NotBlank
    @Column(unique = true)
    var email: String? = null

    @NotBlank
    var password: String? = null

    @NotBlank
    var username: String? = null

    @Column
    var refresh_token: String? = null

    @Enumerated(EnumType.STRING)
    var role: Role? = null

    @CreationTimestamp
    var created_at:LocalDateTime? = null

    @UpdateTimestamp
    var updated_at:LocalDateTime? = null

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    var articles:List<Article> = ArrayList()

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    var comments:List<Comment> = ArrayList()

    constructor(email:String, password:String, username:String, role:String):this(){
        this.email = email
        this.password = password
        this.username = username
        this.role = Role.valueOf(role)
    }

    constructor(email:String, role:String):this() {
        this.email = email
        this.role = Role.valueOf(role)
    }

    fun updateRefreshToken(token:String) {
        this.refresh_token = token
    }

}