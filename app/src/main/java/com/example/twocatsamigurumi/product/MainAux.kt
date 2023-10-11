package com.example.twocatsamigurumi.product

import com.example.twocatsamigurumi.entities.Product

interface MainAux {
    fun getProductsCart() : MutableList<Product>
}