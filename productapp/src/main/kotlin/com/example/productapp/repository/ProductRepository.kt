package com.example.productapp.repository

import com.example.productapp.model.Product
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
class ProductRepository(private val jdbcClient: JdbcClient) {

    fun save(product: Product): Int {
        // Prepare the createdAt value. If product.createdAt is null, use now.
        // This makes sure you always insert a value for createdAt.
        val createdAtValue = product.createdAt ?: LocalDateTime.now()

        return jdbcClient.sql("INSERT INTO products (title, vendor, price, variants, created_at) VALUES (:title, :vendor, :price, :variants, :createdAt)")
            .param("title", product.title)
            .param("vendor", product.vendor.orEmpty())
            .param("price", product.price)
            .param("variants", product.variants, java.sql.Types.OTHER)
            .param("createdAt", createdAtValue) // <--- ADD THIS LINE
            .update()
    }

    fun findAll(): List<Product> {
        return jdbcClient.sql("SELECT * FROM products ORDER BY created_at DESC LIMIT 50")
            .query { rs, _ ->
                Product(
                    id = rs.getInt("id"),
                    title = rs.getString("title"),
                    vendor = rs.getString("vendor"),
                    price = rs.getBigDecimal("price"),
                    variants = rs.getString("variants"),
                    createdAt = rs.getTimestamp("created_at").toLocalDateTime()
                )
            }
            .list()
    }

    fun deleteById(id: Int) {
        jdbcClient.sql("DELETE FROM products WHERE id = :id")
            .param("id", id)
            .update()
    }

    fun findById(id: Int): Product? {
        return jdbcClient.sql("SELECT * FROM products WHERE id = :id")
            .param("id", id)
            .query { rs, _ ->
                Product(
                    id = rs.getInt("id"),
                    title = rs.getString("title"),
                    vendor = rs.getString("vendor"),
                    price = rs.getBigDecimal("price"),
                    variants = rs.getString("variants"),
                    createdAt = rs.getTimestamp("created_at").toLocalDateTime()
                )
            }
            .optional()  // returns Optional<Product>
            .orElse(null) // convert Optional to nullable
    }

    fun update(product: Product): Int {
        return jdbcClient.sql("UPDATE products SET title = :title, vendor = :vendor, price = :price WHERE id = :id")
            .param("id", product.id)
            .param("title", product.title)
            .param("vendor", product.vendor.orEmpty())
            .param("price", product.price)
            .update()
    }

    fun findByTitleContainingIgnoreCase(title: String): List<Product> {
        return jdbcClient.sql("SELECT * FROM products WHERE LOWER(title) LIKE LOWER(:title)")
            .param("title", "%$title%")
            .query(Product::class.java)
            .list()
    }

    fun searchByTitleOrVendor(query: String?): List<Product> {
        val safeQuery = "%${query?.lowercase() ?: ""}%"
        return jdbcClient.sql("""
        SELECT * FROM products
        WHERE LOWER(title) LIKE :q OR LOWER(vendor) LIKE :q
        ORDER BY created_at DESC
    """.trimIndent())
            .param("q", safeQuery)
            .query(Product::class.java)
            .list()
    }


}
