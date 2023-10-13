package com.example.twocatsamigurumi.order

import com.example.twocatsamigurumi.entities.Order

interface OnOrderListener {
    fun onTrack(order: Order)
    fun onStartChat(order: Order)
}