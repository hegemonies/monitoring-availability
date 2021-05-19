package org.bravo.monitoringavailability.service

import kotlinx.coroutines.reactive.awaitFirst
import org.bravo.monitoringavailability.configuration.properties.HttpPingProperties
import org.bravo.monitoringavailability.dto.FindNewsRequest
import org.bravo.monitoringavailability.dto.Pagination
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.function.component1
import reactor.util.function.component2

@Service
class HttpPingService(
    private val webClient: WebClient,
    private val httpPingProperties: HttpPingProperties
) {

    suspend fun ping() =
        pingTo(httpPingProperties.url)

    suspend fun pingTo(url: String): Pair<Long, Boolean> {
        return executePing(url)
    }

    /**
     * @return pair - first is elapsed time, second is available.
     */
    private suspend fun executePing(url: String): Pair<Long, Boolean> {
        repeat(httpPingProperties.countRepeat) {
            val (elapsed, isOk) = runCatching {
                webClient.post()
                    .uri(url)
                    .bodyValue(request)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchangeToMono { response ->
                        Mono.just(response.statusCode().is2xxSuccessful)
                    }
                    .elapsed()
                    .retry(httpPingProperties.countRepeat.toLong())
                    .map { (elapsed, isOk) -> elapsed to isOk }
                    .defaultIfEmpty(0L to false)
                    .awaitFirst()
            }.getOrElse { error ->
                logger.warn("Can not http ping to $url: ${error.message}")
                return 0L to false
            }

            if (isOk) {
                return elapsed to isOk
            }
        }

        return 0L to false
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
