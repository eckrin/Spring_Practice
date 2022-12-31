package yourssu.blog.articlecontrollertest

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import yourssu.blog.DefaultTest
import yourssu.blog.dto.req.CreateArticleRequestDTO
import yourssu.blog.dto.req.SignUpRequestDTO
import yourssu.blog.dto.req.UpdateArticleRequestDTO
import yourssu.blog.dto.res.UpdateArticleResponseDTO
import yourssu.blog.service.ArticleService
import yourssu.blog.service.UserService
import javax.transaction.Transactional

class UpdateArticleTest: DefaultTest() {

    @Autowired
    private lateinit var mockMvc:MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @Autowired
    private lateinit var userService:UserService
    @Autowired
    private lateinit var articleService: ArticleService

    val uTitle = "new title"
    val uContent = "new content"
    var articleId:Long = 0

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        //회원정보 등록
        userService.signUp(email, password, username, role)
        userService.signUp(email2, password2, username2, role)

        //수정할 게시글 생성 (user1)
//        val dto = CreateArticleRequestDTO(email, password, title, content)
//        val result = mockMvc.perform(post("/article/create")
//            .content(objectMapper.writeValueAsString(dto))
//            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk).andReturn()
//        articleId = JsonPath.read(result.response.contentAsString, "$.articleId")
        articleId = articleService.createArticle(email, title, content).articleId
    }

    @Test
    @DisplayName("게시글 수정 성공")
    fun updateArticleTestSuccess() {
        //given
        var newDto = UpdateArticleRequestDTO(uTitle, uContent)

        //when
        var result = mockMvc.perform(post("/article/update/${articleId}")
            .content(objectMapper.writeValueAsString(newDto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("title").value(uTitle))
            .andExpect(jsonPath("content").value(uContent))
    }

    @Test
    @DisplayName("게시글 수정 실패 - 존재하지 않는 유저")
    fun updateArticleTestFailWrongEmail() {
        //given
        var newDto = UpdateArticleRequestDTO(uTitle, uContent)

        //when
        var result = mockMvc.perform(post("/article/update/${articleId}")
            .content(objectMapper.writeValueAsString(newDto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("message").value("유저 정보를 찾을 수 없습니다."))
    }

    @Test
    @DisplayName("게시글 수정 실패 - 틀린 비밀번호")
    fun updateArticleTestFailWrongPwd() {
        //given
        var newDto = UpdateArticleRequestDTO(uTitle, uContent)

        //when
        var result = mockMvc.perform(post("/article/update/${articleId}")
            .content(objectMapper.writeValueAsString(newDto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("message").value("유효하지 않은 비밀번호입니다."))
    }

    @Test
    @DisplayName("게시글 수정 실패 - 게시글 정보 없음")
    fun updateArticleTestFailWrongArticleId() {
        //given
        var newDto = UpdateArticleRequestDTO(uTitle, uContent)

        //when
        var result = mockMvc.perform(post("/article/update/${-0x3f3f3f3f}")
            .content(objectMapper.writeValueAsString(newDto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("message").value("게시글 정보를 가져올 수 없습니다."))
    }

    @Test
    @DisplayName("게시글 수정 실패 - 수정 권한 없음")
    fun updateArticleTestFailNoPermission() {
        //given
        var newDto = UpdateArticleRequestDTO(uTitle, uContent)

        //when
        var result = mockMvc.perform(post("/article/update/${articleId}")
            .content(objectMapper.writeValueAsString(newDto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("message").value("게시글 수정 권한이 없습니다."))
    }
}
