package com.example.twocatsamigurumi.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.twocatsamigurumi.R
import com.example.twocatsamigurumi.databinding.FragmentDetailBinding
import com.example.twocatsamigurumi.entities.Product
import com.example.twocatsamigurumi.product.MainAux

class DetailFragment : Fragment() {
    private var binding : FragmentDetailBinding? = null
    private var product : Product? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding?.let{
            return it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProduct()
        setupButtons()
    }

    private fun setupButtons() {
        product?.let{product ->
            binding?.let{binding->
                binding.ibRem.setOnClickListener{
                    if(product.newQuantity > 1){
                        product.newQuantity -= 1
                        setNewQuantity(product)
                    }
                }
                binding.ibSum.setOnClickListener{
                    if(product.newQuantity < product.quantity){
                        product.newQuantity += 1
                        setNewQuantity(product)
                    }
                }
                binding.efabCart.setOnClickListener {
                    product.newQuantity = binding.etQuantity.text.toString().toInt()
                    addToCart(product)
                }
            }
        }
    }

    private fun addToCart(product: Product) {
        (activity as? MainAux)?.let{
            it.addProductToCart(product)
            activity?.onBackPressed()
        }
    }

    private fun getProduct() {
        product = (activity as? MainAux)?.getProductSelected()
        product?.let{product ->
            binding?.let{
                it.tvName.text = product.name
                it.tvDescription.text = product.description
                it.tvQuantityA.text = getString(R.string.text_test_avaiables, product.quantity)
                setNewQuantity(product)
                Glide.with(this).load(product.imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_time)
                    .centerCrop().into(it.imgProduct)
            }
        }
    }

    private fun setNewQuantity(product: Product) {
        binding?.let{
            it.etQuantity.setText(product.newQuantity.toString())
            it.tvTotal.text = getString(R.string.text_test_total,
                product.totalPrice(), product.newQuantity, product.price)
        }
    }

    override fun onDestroyView() {
        (activity as? MainAux)?.showButton(true)
        super.onDestroyView()
        binding = null
    }
}