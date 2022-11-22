package name.valery1707.problem.rshb.service

import io.ktor.util.logging.*
import kotlinx.serialization.Serializable
import ru.rshb.svoe.rodnoe.api.ProductsApi
import ru.rshb.svoe.rodnoe.model.ApiProduct

object DigitalCookService {
    //todo Inject client
    suspend fun findBestFarmer(
        log: Logger,
        api: ProductsApi,
        pref: PersonPreferences = DEFAULT_PERSON_PREFERENCES,
        ingredients: Map<String, String> = DEFAULT_INGREDIENTS,
    ): BestFarmer {
        return ingredients
            .keys.associateWith {
                api.loadProducts(1, 100, pref.latitude, pref.longitude, pref.regionId, pref.city, searchQuery = it)
            }
            .mapValues { it.value.body().items }
            .mapValues {
                val filters = it.key.split(' ').map { s -> "\\b$s\\b".toRegex(RegexOption.IGNORE_CASE) }
                it.value.filter { p -> filters.all(p.title::contains) }
            }
            .mapValues { it.value.groupBy(ApiProduct::farmerId, ApiProduct::id) }
            .flatMap { it.value.flatMap { f -> f.value.map { p -> Triple(it.key, f.key, p) } } }
            .groupBy({ it.second }, { it.first to it.third })
            .mapValues { it.value.associate { p -> p } }
            .maxByOrNull { it.value.size }
            ?.let { BestFarmer(farmerId = it.key, relevantProductIds = it.value.values.sorted()) }
            ?: DEFAULT_FARMER
    }

    private val DEFAULT_INGREDIENTS = mapOf(
        "Свекла" to "3 шт",
        "Картофель" to "3 шт",
        "Морковь" to "3 шт",
        "Квашенная капуста" to "100 г",
        "Соленые огурцы" to "3 шт",
        //Search is empty for string `Растительное`
        "Растительное масло" to "2 ст. ложки",
        "Уксус" to "1 ст. ложка",
        "Лук" to "3 шт",
    )

    private val DEFAULT_PERSON_PREFERENCES = PersonPreferences(55.773788, 37.65495, 77, "Москва")

    private val DEFAULT_FARMER = BestFarmer(
        farmerId = 1,
        relevantProductIds = listOf(11, 25, 34)
    )
}

@Serializable
data class BestFarmer(
    val farmerId: Int,
    val relevantProductIds: Collection<Int>
)

data class PersonPreferences(
    val latitude: Double,
    val longitude: Double,
    val regionId: Int,
    val city: String,
)