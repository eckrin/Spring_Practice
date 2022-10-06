package yourssu.blog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import yourssu.blog.entity.Article

@Repository
interface ArticleRepository:JpaRepository<Article, Long> {
}