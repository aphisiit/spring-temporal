package com.aphisiit.spring_temporal.model.response

data class Pagination(
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Long
)
