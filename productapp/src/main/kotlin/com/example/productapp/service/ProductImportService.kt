package com.example.productapp.service

import com.example.productapp.model.Product
import com.example.productapp.repository.ProductRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigDecimal
import org.springframework.web.client.RestTemplate
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory

@Service
class ProductImportService(private val repository: ProductRepository) {

    private val logger = LoggerFactory.getLogger(ProductImportService::class.java)
    private val restTemplate = RestTemplate()
    private val mapper = jacksonObjectMapper()

    data class ProductJson(
        val title: String,
        val vendor: String?,
        val price: String?,
        val variants: List<Map<String, Any>> = emptyList()
    )

    @Scheduled(initialDelay = 0, fixedRate = Long.MAX_VALUE)
    fun fetchProductsFromApi() {
        val url = "https://famme.no/products.json"
        val response = restTemplate.getForObject(url, String::class.java)

        val products = try {
            val rawData = mapper.readTree(response)["products"]
            rawData.take(50).map {
                Product(
                    title = it["title"].asText(),
                    vendor = it["vendor"]?.asText(),
                    price = it["variants"]?.get(0)?.get("price")?.asText()?.toBigDecimalOrNull(),
                    variants = it["variants"].toString()
                )
            }
        } catch (e: Exception) {
            logger.error("Failed to parse JSON: ${e.message}")
            return
        }

        products.forEach {
            try {
                repository.save(it)
            } catch (ex: Exception) {
                logger.warn("Could not save product: ${ex.message}")
            }
        }

        logger.info("✅ Imported ${products.size} products from API")
    }
}
