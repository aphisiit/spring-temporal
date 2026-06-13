package com.aphisiit.spring_temporal.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val status: String,
    val statusCode: Int,
    val message: String,
    val data: Any = emptyMap<String, Any>(),
    val pagination: Pagination? = null,
    val stackTrace: String? = null
) {
    companion object {
        fun <T> ok(data: T?, message: String = "Success"): ApiResponse<T> =
            ApiResponse(
                status = HttpStatus.OK.reasonPhrase,
                statusCode = HttpStatus.OK.value(),
                message = message,
                data = data ?: emptyMap<String, Any>()
            )

        fun <T> okList(data: List<T>?, message: String = "Success"): ApiResponse<List<T>> =
            ApiResponse(
                status = HttpStatus.OK.reasonPhrase,
                statusCode = HttpStatus.OK.value(),
                message = message,
                data = data ?: emptyList<T>()
            )

        fun <T> created(data: T?, message: String = "Created"): ApiResponse<T> =
            ApiResponse(
                status = HttpStatus.CREATED.reasonPhrase,
                statusCode = HttpStatus.CREATED.value(),
                message = message,
                data = data ?: emptyMap<String, Any>()
            )

        fun <T> createdList(data: List<T>?, message: String = "Created"): ApiResponse<List<T>> =
            ApiResponse(
                status = HttpStatus.CREATED.reasonPhrase,
                statusCode = HttpStatus.CREATED.value(),
                message = message,
                data = data ?: emptyList<T>()
            )

        fun <T> of(httpStatus: HttpStatus, data: T? = null, message: String, pagination: Pagination? = null): ApiResponse<T> =
            ApiResponse(
                status = httpStatus.reasonPhrase,
                statusCode = httpStatus.value(),
                message = message,
                data = data ?: emptyMap<String, Any>(),
                pagination = pagination
            )

        fun <T> ofList(httpStatus: HttpStatus, data: List<T>? = null, message: String, pagination: Pagination? = null): ApiResponse<List<T>> =
            ApiResponse(
                status = httpStatus.reasonPhrase,
                statusCode = httpStatus.value(),
                message = message,
                data = data ?: emptyList<T>(),
                pagination = pagination
            )

        fun error(httpStatus: HttpStatus, message: String, stackTrace: String? = null): ApiResponse<Nothing> =
            ApiResponse(
                status = httpStatus.reasonPhrase,
                statusCode = httpStatus.value(),
                message = message,
                stackTrace = stackTrace
            )
    }
}
