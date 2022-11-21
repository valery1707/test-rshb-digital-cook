package name.valery1707.problem.rshb.service

import kotlinx.serialization.Serializable

object DigitalCookService {
    fun findBestFarmer(): BestFarmer {
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
