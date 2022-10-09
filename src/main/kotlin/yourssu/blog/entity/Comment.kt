package yourssu.blog.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Comment() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var comment_id:Long = 0

    @NotBlank
    @Column(nullable = false, length = 255)
    var content:String? = null

    @CreationTimestamp
    var created_at:LocalDateTime? = null

    @UpdateTimestamp
    var updated_at:LocalDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    var article:Article? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user:User? = null

    constructor(content:String, article:Article, user:User):this() {
        this.content = content
        this.article = article
        this.user = user
    }

    fun updateContent(content:String) {
        this.content = content
    }
}