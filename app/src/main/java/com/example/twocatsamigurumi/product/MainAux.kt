package com.example.twocatsamigurumi.product

import com.example.twocatsamigurumi.entities.Product

interface MainAux {
    fun getProductsCart() : MutableList<Product>
    fun getProductSelected() : Product?
    fun showButton(isVisible : Boolean)
}