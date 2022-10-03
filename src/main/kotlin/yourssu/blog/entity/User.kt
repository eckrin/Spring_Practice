package yourssu.blog.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
class User(

    @Email
    @NotBlank
    @Column(unique = true)
    var email: String?,

    @NotBlank
    var password: String?,

    @NotBlank
    var username: String?

) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var user_id: Long = 0

    @CreationTimestamp
    var created_at:LocalDateTime? = null

    @UpdateTimestamp
    var updated_at:LocalDateTime? = null

}