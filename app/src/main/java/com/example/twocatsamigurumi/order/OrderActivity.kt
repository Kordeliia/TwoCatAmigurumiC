package com.example.twocatsamigurumi.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twocatsamigurumi.Constants
import com.example.twocatsamigurumi.R
import com.example.twocatsamigurumi.chat.ChatFragment
import com.example.twocatsamigurumi.databinding.ActivityOrderBinding
import com.example.twocatsamigurumi.entities.Order
import com.example.twocatsamigurumi.track.TrackFragment
import com.google.firebase.firestore.FirebaseFirestore

class OrderActivity : AppCompatActivity(), OnOrderListener, OrderAux {
    private lateinit var binding : ActivityOrderBinding
    private lateinit var adapter : OrderAdapter
    private lateinit var orderSelected : Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerVIew()
        setupFirestore()
    }
    private fun setupFirestore(){
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.COLL_REQUESTS)
            .get()
            .addOnSuccessListener {
                for(document in it){
                    val order = document.toObject(Order::class.java)
                    order.id = document.id
                    adapter.add(order)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,
                    "Ha fallado conexión con Firestore",
                    Toast.LENGTH_SHORT).show()
            }
    }
    private fun setupRecyclerVIew() {
        adapter = OrderAdapter(mutableListOf(), this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderActivity)
            adapter = this@OrderActivity.adapter
        }
    }

    override fun onTrack(order: Order) {
        orderSelected = order
        var fragment = TrackFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.containerMain, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onStartChat(order: Order) {
        orderSelected = order
        val fragment = ChatFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerMain, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun getOrderSelected(): Order = orderSelected

}