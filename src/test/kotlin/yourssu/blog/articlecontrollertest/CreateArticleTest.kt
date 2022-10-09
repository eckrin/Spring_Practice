package yourssu.blog.articlecontrollertest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import yourssu.blog.DefaultTest
import yourssu.blog.dto.req.CreateArticleRequestDTO
import yourssu.blog.dto.req.SignUpRequestDTO
import yourssu.blog.service.UserService

class CreateArticleTest: DefaultTest() {

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userService.signUp(email, password, username)
    }

    @Autowired
    private lateinit var mockMvc:MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @Autowired
    private lateinit var userService: UserService

    @Test
    @DisplayName("게시글 작성 성공")
    fun createArticleTestSuccess() {
        //given
        val dto = CreateArticleRequestDTO(email, password, title, content)

        //when
        val result = mockMvc.perform(post("/article/create")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("email").value(email))
            .andExpect(jsonPath("title").value(title))
            .andExpect(jsonPath("content").value(content))
    }

    @Test
    @DisplayName("게시글 작성 실패 - 존재하지 않는 유저 정보")
    fun createArticleTestFailWrongEmail() {
        //given
        val dto = CreateArticleRequestDTO(email+"wrong", password, title, content)

        //when
        val result = mockMvc.perform(post("/article/create")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("message").value("유저 정보를 찾을 수 없습니다."))
    }

    @Test
    @DisplayName("게시글 작성 실패 - 틀린 비밀번호")
    fun createArticleTestFailWrongPwd() {
        //given
        val dto = CreateArticleRequestDTO(email, password+"wrong", title, content)

        //when
        val result = mockMvc.perform(post("/article/create")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("message").value("유효하지 않은 비밀번호입니다."))
    }
}