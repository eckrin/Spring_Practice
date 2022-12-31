package yourssu.blog.articlecontrollertest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import yourssu.blog.DefaultTest
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
        userService.signUp(email, password, username, role)
        userService.signUp(email2, password2, username2, role)
		//로그인-token 받아오기
		accessToken = userService.signIn(email, password).accessToken
		accessToken2 = userService.signIn(email2, password2).accessToken
        //삭제할 게시글 생성
        articleId = articleService.createArticle(email, title, content).articleId
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    fun deleteArticleTestSuccess() {
        //given
        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${articleId}")
            .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 존재하지 않는 유저")
    fun deleteArticleTestFailWrongEmail() {
        //given
        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${articleId}")
				.header(HttpHeaders.AUTHORIZATION, "qowepfhioshdafav4897hf9qhekauhew9qf")
                .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("유저 정보를 찾을 수 없습니다."))
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 삭제 권한 없음")
    fun deleteArticleTestFailNoPermission() {
        //given
        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${articleId}")
				.header(HttpHeaders.AUTHORIZATION, accessToken2)
                .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("게시글 삭제 권한이 없습니다."))
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 게시글 정보 없음")
    fun deleteArticleTestFailWrongArticleId() {
        //given
        //when
        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/article/delete/${-0x3f3f3f3f}")
				.header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("게시글 정보를 가져올 수 없습니다."))
    }
}
