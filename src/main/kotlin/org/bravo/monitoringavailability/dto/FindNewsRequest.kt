package org.bravo.monitoringavailability.dto

data class FindNewsRequest(
    val pagination: Pagination,
    val tag: String
)

data class Pagination(
    val offset: Int,
    val limit: Int
)
