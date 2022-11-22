package name.valery1707.problem.rshb.service

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.rshb.svoe.rodnoe.api.FarmersApi
import ru.rshb.svoe.rodnoe.api.ProductsApi

@OptIn(ExperimentalCoroutinesApi::class)
internal class DigitalCookServiceTest {
    @Test
    internal fun test1() = runTest {
        val log = LoggerFactory.getLogger(DigitalCookService::class.java)
        val baseUrl = "https://svoe-rodnoe.ru/fresh"
        val productsApi = ProductsApi(baseUrl)
        val actual = DigitalCookService.findBestFarmer(log, productsApi)
        assert(!actual.relevantProductIds.isEmpty())
        val expected = BestFarmer(
            farmerId = 12463 /*Ярмарка на Профсоюзной*/,
            relevantProductIds = listOf(
                30611, /*Огурцы соленые*/
                30780, /*Морковь*/
                30785, /*Свекла*/
                32773, /*Картофель синеглазка*/
                33898, /*Квашенная капуста*/
            )
        )
        assert(actual == expected)
        val farmer = FarmersApi(baseUrl).getFarmerById(12463, 55.773788, 37.65495, 77, "Москва").body()
        assert(farmer.createdAt.isNotEmpty())
    }
}