package yourssu.blog.exception

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime


class ErrorResponse(
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd HH:mm:ss",
        timezone = "Asia/Seoul"
    )
    var timestamp: LocalDateTime?,
    var httpStatus: Int,
    var errorCode: String?,
    var message: String?,
    var path: String?
)
