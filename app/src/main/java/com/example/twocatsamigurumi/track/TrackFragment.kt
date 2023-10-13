package com.example.twocatsamigurumi.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.twocatsamigurumi.Constants
import com.example.twocatsamigurumi.R
import com.example.twocatsamigurumi.databinding.FragmentTrackBinding
import com.example.twocatsamigurumi.entities.Order
import com.example.twocatsamigurumi.order.OrderAux
import com.google.firebase.firestore.FirebaseFirestore

class TrackFragment : Fragment(){
    private var binding : FragmentTrackBinding? = null
    private var order : Order? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackBinding.inflate(inflater, container, false)
        binding?.let{
            return it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    private fun getOrder(){
        order = (activity as? OrderAux)?.getOrderSelected()
        order?.let{
            updateUI(it)
            getOrderInRealtime(it.id)
           setupActionBar()
        }
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.let{
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.title =getString(R.string.track_title)
            setHasOptionsMenu(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getOrderInRealtime(orderId : String) {
        val db = FirebaseFirestore.getInstance()
        val orderRef = db.collection(Constants.COLL_REQUESTS).document(orderId)
        orderRef.addSnapshotListener{snapshot, error ->
            if(error != null){
                Toast.makeText(activity,
                    getString(R.string.toast_error_consult_location),
                    Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if(snapshot != null && snapshot.exists()){
                val order = snapshot.toObject(Order::class.java)
                order?.let{
                    it.id = snapshot.id
                    updateUI(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun updateUI(order: Order) {
        binding?.let{
            it.progressBar.progress = order.status *(100 / 4) - 15
            it.cbOrdered.isChecked= order.status > 0
            it.cbPrepared.isChecked= order.status > 1
            it.cbSent.isChecked= order.status > 2
            it.cbInDistribution.isChecked= order.status > 3
            it.cbDelivered.isChecked= order.status > 4
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrder()
    }

    override fun onDestroy() {
        (activity as? AppCompatActivity)?.let{
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            it.supportActionBar?.title =getString(R.string.order_title)
            setHasOptionsMenu(false)
        }
        super.onDestroy()
    }
}