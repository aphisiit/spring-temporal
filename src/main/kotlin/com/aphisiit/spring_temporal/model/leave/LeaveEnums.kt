package com.aphisiit.spring_temporal.model.leave

enum class LeaveRequestStatus {
    SUBMITTED,
    MANAGER_APPROVED,
    HR_APPROVED,
    REJECTED
}

enum class ApprovalRole {
    MANAGER,
    HR
}

enum class LeaveEventType {
    SUBMITTED,
    MANAGER_APPROVED,
    HR_APPROVED,
    REJECTED
}
