package com.example.twocatsamigurumi.product

import com.example.twocatsamigurumi.entities.Product

interface OnProductListener {
    fun onClick(product: Product)
}