package yourssu.blog.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Article() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var article_id:Long = 0

    @NotBlank
    @Column(length = 255)
    var title:String? = null

    @NotBlank
    @Column(length = 255)
    var content:String? = null

    @CreationTimestamp
    var created_at:LocalDateTime? = null

    @CreationTimestamp
    var updated_at:LocalDateTime? = null

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user_id:User? = null

    @OneToMany(mappedBy = "article_id", orphanRemoval = true)
    var comments:List<Comment> = ArrayList()

    constructor(title:String, content:String, user:User):this() {
        this.title = title
        this.content = content
        this.user_id = user
    }

}