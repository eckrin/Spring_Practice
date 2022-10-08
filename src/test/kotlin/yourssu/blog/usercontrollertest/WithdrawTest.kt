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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import yourssu.blog.dto.req.WithdrawRequestDTO
import yourssu.blog.service.UserService

@AutoConfigureMockMvc
@SpringBootTest
class WithdrawTest {

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this) //initmocks deprecated
        userService.signUp(email, password, "username")
    }

    @Autowired
    private lateinit var userService:UserService
    @Autowired
    private lateinit var mockMvc:MockMvc
    @Autowired
    private lateinit var objectMapper:ObjectMapper

    val email = "email@urssu.com"
    val password = "password"

    @Test
    @DisplayName("회원탈퇴 성공 테스트")
    fun signUpTestSuccess() {
        //given
        var dto = WithdrawRequestDTO(email, password)

        //when
        var result = mockMvc.perform(post("/withdraw")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isOk)
    }

}