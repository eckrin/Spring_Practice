package yourssu.blog.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Article (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var article_id:Long,

    @Column(nullable = false, length = 255)
    var title:String,

    @Column(nullable = false, length = 255)
    var content:String,

    @CreationTimestamp
    var created_at:LocalDateTime,

    @CreationTimestamp
    var updated_at:LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user_id:User

)