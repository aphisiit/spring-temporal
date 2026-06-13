package com.aphisiit.spring_temporal.controller.leave

import com.aphisiit.spring_temporal.model.request.leave.LeaveRequestApproveRequest
import com.aphisiit.spring_temporal.model.request.leave.LeaveRequestCreateRequest
import com.aphisiit.spring_temporal.model.request.leave.LeaveRequestRejectRequest
import com.aphisiit.spring_temporal.model.response.ApiResponse
import com.aphisiit.spring_temporal.model.response.leave.LeaveCommandAcceptedResponse
import com.aphisiit.spring_temporal.model.response.leave.LeaveRequestAcceptedResponse
import com.aphisiit.spring_temporal.model.response.leave.LeaveRequestResponse
import com.aphisiit.spring_temporal.service.leave.LeaveRequestService
import com.aphisiit.spring_temporal.service.leave.LeaveWorkflowStarterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leave/requests")
class LeaveRequestController(
    private val leaveRequestService: LeaveRequestService,
    private val leaveWorkflowStarterService: LeaveWorkflowStarterService
) {

    @PostMapping
    fun submit(@RequestBody request: LeaveRequestCreateRequest): ResponseEntity<ApiResponse<LeaveRequestAcceptedResponse>> {
        val accepted = leaveWorkflowStarterService.submit(request)
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(ApiResponse.of(HttpStatus.ACCEPTED, accepted, "Leave request accepted"))
    }

    @GetMapping("/{requestId}")
    fun getByRequestId(@PathVariable requestId: String): ResponseEntity<ApiResponse<LeaveRequestResponse>> {
        return ResponseEntity.ok(ApiResponse.ok(leaveRequestService.getByRequestId(requestId)))
    }

    @PatchMapping("/{requestId}/approve")
    fun approve(
        @PathVariable requestId: String,
        @RequestBody request: LeaveRequestApproveRequest
    ): ResponseEntity<ApiResponse<LeaveCommandAcceptedResponse>> {
        val accepted = leaveWorkflowStarterService.approve(requestId, request)
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(ApiResponse.of(HttpStatus.ACCEPTED, accepted, "Approval signal accepted"))
    }

    @PatchMapping("/{requestId}/reject")
    fun reject(
        @PathVariable requestId: String,
        @RequestBody request: LeaveRequestRejectRequest
    ): ResponseEntity<ApiResponse<LeaveCommandAcceptedResponse>> {
        val accepted = leaveWorkflowStarterService.reject(requestId, request)
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(ApiResponse.of(HttpStatus.ACCEPTED, accepted, "Rejection signal accepted"))
    }
}
