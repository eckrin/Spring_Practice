package yourssu.blog.userControllerTest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import yourssu.blog.dto.req.SignUpRequestDTO
import yourssu.blog.service.UserService

@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest
class SignUpTest {

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Autowired
    private lateinit var mockMvc:MockMvc
    @Autowired
    private lateinit var objectMapper:ObjectMapper

    val email = "email@urssu.com"
    val password = "password"
    val username = "username"

    @Test
    @DisplayName("회원가입 성공 테스트")
    fun signUpTestSuccess() {
        //given
        var dto = SignUpRequestDTO(email, password, username)

        //when
        var result = mockMvc.perform(post("/signUp")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("email").value(email))
            .andExpect(jsonPath("username").value(username))
    }

}