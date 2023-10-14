package com.example.twocatsamigurumi.chat

import com.example.twocatsamigurumi.entities.Message

interface OnChatListener {
    fun deleteMessage(message: Message)
}