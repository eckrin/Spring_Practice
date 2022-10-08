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
import yourssu.blog.dto.req.CreateArticleRequestDTO

@AutoConfigureMockMvc
@SpringBootTest
class CreateArticleTest {

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Autowired
    private lateinit var mockMvc:MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    val email = "email1@urssu.com"
    val password = "password1"
    val title = "title1"
    val content = "content1"

    @Test
    @DisplayName("게시글 작성 테스트")
    fun createArticleTestSuccess() {
        //given
        var dto = CreateArticleRequestDTO(email, password, title, content)

        //when
        var result = mockMvc.perform(post("/article/create")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("email").value(email))
            .andExpect(jsonPath("title").value(title))
            .andExpect(jsonPath("content").value(content))

    }
}