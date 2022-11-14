package yourssu.blog.usercontrollertest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import yourssu.blog.DefaultTest
import yourssu.blog.service.UserService

class WithdrawTest:DefaultTest() {

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


    @Test
    @DisplayName("회원탈퇴 성공")
    fun WithdrawTestSuccess() {
        //given
        val dto = WithdrawRequestDTO(email, password)

        //when
        val result = mockMvc.perform(post("/withdraw")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isOk)
    }

    @Test
    @DisplayName("회원탈퇴 실패 - 비밀번호 오류")
    fun WithdrawTestFailWrongPwd() {
        //given
        val dto = WithdrawRequestDTO(email, password+"wrong")

        //when
        val result = mockMvc.perform(post("/withdraw")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("message").value("비밀번호가 틀립니다."))
    }

}