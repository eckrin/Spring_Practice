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

    @CreationTimestamp
    var created_at:LocalDateTime? = null

    @UpdateTimestamp
    var updated_at:LocalDateTime? = null

    constructor(email:String, password:String, username:String):this(){
        this.email = email
        this.password = password
        this.username = username
    }

}