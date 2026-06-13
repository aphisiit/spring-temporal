package com.aphisiit.spring_temporal.controller.leave

import com.aphisiit.spring_temporal.model.request.leave.RequestGeneratorRequest
import com.aphisiit.spring_temporal.model.response.ApiResponse
import com.aphisiit.spring_temporal.model.response.leave.RequestGeneratorResponse
import com.aphisiit.spring_temporal.service.leave.RequestGeneratorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("request-number")
class RequestGeneratorController(
    private val requestGeneratorService: RequestGeneratorService
) {

    @GetMapping
    fun findWithPrefix(@RequestParam prefix: String): ResponseEntity<ApiResponse<RequestGeneratorResponse?>> {
        return ResponseEntity.ok(ApiResponse.ok(requestGeneratorService.findByPrefix(prefix)))
    }

    @PostMapping
    fun generate(@RequestBody body: RequestGeneratorRequest): ResponseEntity<ApiResponse<RequestGeneratorResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(requestGeneratorService.generateRequestId(body.prefix)))
    }
}