package yourssu.blog.repository

import yourssu.blog.entity.User

interface UserCustomRepository {

    fun searchAll():List<User>
}
