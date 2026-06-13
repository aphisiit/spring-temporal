package com.aphisiit.spring_temporal.configure

import com.aphisiit.spring_temporal.model.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @Value("\${app.show-stack-trace:false}")
    private var showStackTrace: Boolean = false

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("Bad request: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(
                httpStatus = HttpStatus.BAD_REQUEST,
                message = ex.message ?: "Bad request",
                stackTrace = ex.stackTraceString()
            ))
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParam(ex: MissingServletRequestParameterException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("Missing parameter: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(
                httpStatus = HttpStatus.BAD_REQUEST,
                message = "Missing required parameter: '${ex.parameterName}'",
                stackTrace = ex.stackTraceString()
            ))
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(ex: NoResourceFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(
                httpStatus = HttpStatus.NOT_FOUND,
                message = ex.message ?: "Resource not found"
            ))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Unhandled runtime exception: ${ex.message}", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                message = ex.message ?: "Internal server error",
                stackTrace = ex.stackTraceString()
            ))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Unexpected error: ${ex.message}", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "Internal server error",
                stackTrace = ex.stackTraceString()
            ))
    }

    private fun Throwable.stackTraceString(): String? =
        if (showStackTrace) stackTrace.take(10).joinToString("\n") { "\tat $it" } else null
}
