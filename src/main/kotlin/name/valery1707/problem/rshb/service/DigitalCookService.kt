package name.valery1707.problem.rshb.service

import io.ktor.util.logging.*
import kotlinx.serialization.Serializable
import ru.rshb.svoe.rodnoe.api.ProductsApi

object DigitalCookService {
    //todo Inject client
    suspend fun findBestFarmer(log: Logger, api: ProductsApi): BestFarmer {
        val products = api.loadProducts(1, 100, 55.773788, 37.65495, 77, "Москва", searchQuery = "Свекла")
        log.info(products.body().items.toString())
        return BestFarmer(
            farmerId = 1,
            relevantProductIds = listOf(11, 25, 34)
        )
    }
}

@Serializable
data class BestFarmer(
    val farmerId: Long,
    val relevantProductIds: Collection<Long>
)
