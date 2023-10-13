package com.example.twocatsamigurumi.cart

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twocatsamigurumi.R
import com.example.twocatsamigurumi.databinding.FragmentCartBinding
import com.example.twocatsamigurumi.entities.Product
import com.example.twocatsamigurumi.order.OrderActivity
import com.example.twocatsamigurumi.product.MainAux
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CartFragment : BottomSheetDialogFragment(), OnCartListener {
    private var binding : FragmentCartBinding? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var  adapter : ProductCartAdapter
    private var totalPrice = 0.0
    override fun onCreateDialog(savedInstanceState : Bundle?) : Dialog {
        binding = FragmentCartBinding.inflate(LayoutInflater.from(activity))
        binding?.let {
            val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            bottomSheetDialog.setContentView(it.root)
            bottomSheetBehavior = BottomSheetBehavior.from(it.root.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            setupRecyclerView()
            setupButtons()
            getProducts()
            return bottomSheetDialog
        }
        return super.onCreateDialog(savedInstanceState)
    }
    private fun setupButtons() {
        binding?.let {
            it.iBtnCloseCart.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            it.efab.setOnClickListener {
                requestOrder()
            }
        }
    }

    private fun requestOrder() {
        dismiss()
        (activity as? MainAux)?.clearCart()
        startActivity(Intent(context, OrderActivity::class.java))
    }

    private fun getProducts(){
        (activity as? MainAux)?.getProductsCart()?.forEach {
            adapter.addProduct(it)
        }
    }
    private fun setupRecyclerView() {
        binding?.let {
            adapter = ProductCartAdapter(mutableListOf(), this)
            it.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@CartFragment.adapter
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun setQuantity(product: Product) {
        adapter.updateProduct(product)
    }
    override fun showTotal(total: Double) {
        totalPrice = total
        binding?.let {
            it.tvTotal.text = getString(R.string.product_full_cart, total)
        }
    }
}
