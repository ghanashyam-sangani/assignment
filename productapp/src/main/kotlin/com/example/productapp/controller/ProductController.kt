package com.example.productapp.controller

import com.example.productapp.model.Product
import com.example.productapp.repository.ProductRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal


@Controller
@RequestMapping("/")
class ProductController(private val repo: ProductRepository) {

    @GetMapping
    fun index(): String = "index"

    /** Returns the table fragment */
    @GetMapping("/products")
    fun listProducts(model: Model): String {
        val products = repo.findAll()
        model.addAttribute("products", products)
        return "fragments :: productTable"
    }

    /** Handles HTMX form submit to add a product */
    @PostMapping("/products")
    fun addProduct(
        @RequestParam title: String,
        @RequestParam vendor: String?,
        @RequestParam price: BigDecimal?,
        model: Model
    ): String {
        val product = Product(title = title, vendor = vendor, price = price, variants = null)
        repo.save(product)
        // Return only the newly added row
        val latest = repo.findAll().firstOrNull() ?: product
        model.addAttribute("product", latest)
        return "fragments :: productRow"
    }

    @GetMapping("/products/edit/{id}")
    fun editProduct(@PathVariable id: Int, model: Model): String {
        val product = repo.findById(id)
        return if (product != null) {
            model.addAttribute("product", product)
            "fragments :: editProductForm"
        } else {
            "fragments :: productNotFoundRow"
        }
    }

    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Int,
        @RequestParam title: String,
        @RequestParam vendor: String?,
        @RequestParam price: BigDecimal?,
        model: Model
    ): String {

        val existing = repo.findById(id)

        return if (existing != null) {
            val updated = existing.copy(
                title = title,
                vendor = vendor,
                price = price
            )

            repo.update(updated)
            model.addAttribute("product", updated)
            "fragments :: productRow"
        } else {
            "fragments :: productNotFoundRow"
        }
    }



    @DeleteMapping("/products/{id}")
    @ResponseBody
    fun deleteProduct(@PathVariable id: Int) {
        repo.deleteById(id)
    }

    @GetMapping("/products/{id}")
    fun getProductRow(@PathVariable id: Int, model: Model): String {
        val product = repo.findById(id)
        return if (product != null) {
            model.addAttribute("product", product)
            "fragments :: productRow"
        } else {
            "fragments :: productNotFoundRow"
        }
    }

    @GetMapping("/search")
    fun showSearchPage(model: Model): String {
        model.addAttribute("products", repo.findAll())
        return "search"
    }

    @GetMapping("/products/search")
    fun searchProducts(@RequestParam("query") query: String?, model: Model): String {
        val products = if (query.isNullOrBlank()) {
            repo.findAll()
        } else {
            repo.searchByTitleOrVendor(query)
        }
        model.addAttribute("products", products)
        return "fragments :: productTableRows"
    }

}
