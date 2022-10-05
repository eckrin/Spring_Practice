package yourssu.blog.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime


class ErrorResponse(
//    @JsonFormat(
//        shape = JsonFormat.Shape.STRING,
//        pattern = "yyyy-MM-dd'T'HH:mm:ss",
//        timezone = "Asia/Seoul"
//    )
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var timestamp: LocalDateTime?,
    var status: String?,
    var message: String?,
    var requestURI: String?
)
