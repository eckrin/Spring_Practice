package yourssu.blog.articlecontrollertest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import yourssu.blog.DefaultTest
import yourssu.blog.dto.req.DeleteArticleRequestDTO
import yourssu.blog.dto.req.UpdateArticleRequestDTO
import yourssu.blog.service.ArticleService
import yourssu.blog.service.UserService

class DeleteArticleTest: DefaultTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var articleService: ArticleService

    var articleId:Long = 0

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        //회원정보 등록
        userService.signUp(email, password, username)
        userService.signUp(email2, password2, username2)
        //삭제할 게시글 생성
        articleId = articleService.createArticle(email, password, title, content).articleId
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    fun deleteArticleTestSuccess() {
        //given
        var dto = DeleteArticleRequestDTO(email, password)

        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${articleId}")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 존재하지 않는 유저")
    fun deleteArticleTestFailWrongEmail() {
        //given
        var dto = DeleteArticleRequestDTO(email+"wrong", password)

        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${articleId}")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("유저 정보를 찾을 수 없습니다."))
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 틀린 비밀번호")
    fun deleteArticleTestFailWrongPwd() {
        //given
        var dto = DeleteArticleRequestDTO(email, password+"wrong")

        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${articleId}")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("유효하지 않은 비밀번호입니다."))
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 삭제 권한 없음")
    fun deleteArticleTestFailNoPermission() {
        //given
        var dto = DeleteArticleRequestDTO(email2, password2)

        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${articleId}")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("게시글 삭제 권한이 없습니다."))
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 게시글 정보 없음")
    fun deleteArticleTestFailWrongArticleId() {
        //given
        var dto = DeleteArticleRequestDTO(email, password)

        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${-0x3f3f3f3f}")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("게시글 정보를 가져올 수 없습니다."))
    }
}