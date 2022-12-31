package yourssu.blog.usercontrollertest

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
import yourssu.blog.dto.req.SignUpRequestDTO
import yourssu.blog.service.UserService

class SignUpTest:DefaultTest() {

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Autowired
    private lateinit var mockMvc:MockMvc
    @Autowired
    private lateinit var objectMapper:ObjectMapper
    @Autowired
    private lateinit var userService: UserService


    @Test
    @DisplayName("회원가입 성공")
    fun signUpTestSuccess() {
        //given
        val dto = SignUpRequestDTO(email, password, username, role)

        //when
        val result = mockMvc.perform(post("/signUp")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("email").value(email))
            .andExpect(jsonPath("username").value(username))
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    fun signUpTestFailDuplicateEmail() {
        //given
        userService.signUp(email, password, username, role)
        val dto = SignUpRequestDTO(email, password, username, role)

        //when
        val result = mockMvc.perform(post("/signUp")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("message").value("중복된 이메일입니다."))
    }

}
