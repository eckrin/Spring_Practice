package yourssu.blog.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Article() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var article_id:Long = 0

    @Column(nullable = false, length = 255)
    var title:String? = null

    @Column(nullable = false, length = 255)
    var content:String? = null

    @CreationTimestamp
    var created_at:LocalDateTime? = null

    @CreationTimestamp
    var updated_at:LocalDateTime? = null

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user_id:User? = null

}