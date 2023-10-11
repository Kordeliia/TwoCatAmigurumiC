package com.example.twocatsamigurumi.cart

import com.example.twocatsamigurumi.entities.Product

interface OnCartListener {
    fun setQuantity(product : Product)
    fun showTotal(total : Double)
}