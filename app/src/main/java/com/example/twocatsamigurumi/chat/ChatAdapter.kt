package com.example.twocatsamigurumi.chat

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.twocatsamigurumi.R
import com.example.twocatsamigurumi.databinding.ItemChatBinding
import com.example.twocatsamigurumi.entities.Message

class ChatAdapter (private val messageList : MutableList<Message>,
                   private val listener : OnChatListener) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    private lateinit var context : Context
    fun addMessage(message: Message) {
        if(!messageList.contains(message)) {
            messageList.add(message)
            notifyItemInserted(messageList.size - 1)
        }
    }
    fun updateMessage(message: Message) {
        val index = messageList.indexOf(message)
        if (index != - 1) {
            messageList.set(index, message)
            notifyItemChanged(index)
        }
    }
    fun deleteMessage(message: Message) {
        val index = messageList.indexOf(message)
        if (index != - 1) {
            messageList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemChatBinding.bind(view)
        fun setListener(message: Message) {
            binding.tvChat.setOnLongClickListener {
                listener.deleteMessage(message)
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = messageList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        holder.setListener(message)
        var gravity = Gravity.END
        var background = ContextCompat.getDrawable(context, R.drawable.background_chat_client)
        var textColor = ContextCompat.getColor(context, R.color.blue_light)
        val marginHorizontal = context.resources.getDimensionPixelSize(R.dimen.chat_margin_horizontal)
        val params = holder.binding.tvChat.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = marginHorizontal
        params.marginEnd = 0
        params.topMargin = 0
        if(position > 0 && message.isSentByMe() != messageList[position-1].isSentByMe()){
            params.topMargin = context.resources.getDimensionPixelSize(R.dimen.common_padding_default)
        }
        if(!message.isSentByMe()){
            gravity = Gravity.START
            background = ContextCompat.getDrawable(context, R.drawable.background_chat_support)
            textColor = ContextCompat.getColor(context, R.color.pink_light)
            params.marginStart = 0
            params.marginEnd = marginHorizontal
        }
        holder.binding.root.gravity = gravity
        holder.binding.tvChat.layoutParams = params
        holder.binding.tvChat.setBackground(background)
        holder.binding.tvChat.setTextColor(textColor)
        holder.binding.tvChat.text = message.message

    }
}