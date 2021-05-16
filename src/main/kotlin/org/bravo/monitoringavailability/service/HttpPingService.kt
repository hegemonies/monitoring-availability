package org.bravo.monitoringavailability.service

import kotlinx.coroutines.reactive.awaitFirst
import org.bravo.monitoringavailability.configuration.properties.HttpPingProperties
import org.bravo.monitoringavailability.dto.FindNewsRequest
import org.bravo.monitoringavailability.dto.Pagination
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class HttpPingService(
    private val webClient: WebClient,
    private val httpPingProperties: HttpPingProperties
) {

    suspend fun ping(): Boolean =
        pingTo(httpPingProperties.url)

    suspend fun pingTo(url: String): Boolean {
        return executePing(url)
    }

    private suspend fun executePing(url: String): Boolean {
        repeat(httpPingProperties.countRepeat) {
            val isOk = runCatching {
                webClient.post()
                    .uri(url)
                    .bodyValue(request)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchangeToMono { response ->
                        Mono.just(response.statusCode() == HttpStatus.OK)
                    }
            }.getOrElse { error ->
                logger.warn("Can not http ping to $url: ${error.message}")
                return false
            }.awaitFirst()

            if (isOk) {
                return true
            }
        }

        return false
    }

    companion object {
        val request = FindNewsRequest(
            pagination = Pagination(
                offset = 0,
                limit = 10
            ),
            tag = "intc"
        )

        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
