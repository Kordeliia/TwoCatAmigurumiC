package com.example.twocatsamigurumi.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.twocatsamigurumi.Constants
import com.example.twocatsamigurumi.R
import com.example.twocatsamigurumi.cart.CartFragment
import com.example.twocatsamigurumi.databinding.ActivityMainBinding
import com.example.twocatsamigurumi.entities.Product
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MainActivity : AppCompatActivity(),OnProductListener, MainAux {
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it ->
        val response = IdpResponse.fromResultIntent(it.data)
        if(it.resultCode == RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser
            if(user != null){
                Toast.makeText(this,
                    getString(R.string.mssg_bienvenida),
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            if(response == null){
                Toast.makeText(this,
                    getString(R.string.mssg_despedida),
                    Toast.LENGTH_SHORT).show()
                finish()
            } else {
                response.error?.let{
                    if(it.errorCode == ErrorCodes.NO_NETWORK){
                        Toast.makeText(this,
                            getString(R.string.mssg_error_no_network),
                            Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(this,
                            getString(R.string.mssg_error_codigo_error),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private lateinit var binding : ActivityMainBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener
    private lateinit var adapter : ProductAdapter
    private lateinit var firestoreListener : ListenerRegistration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configAuth()
        configRecyclerView()
        configButtons()
    }



    private fun configRecyclerView() {
        adapter = ProductAdapter(mutableListOf(), this)
        binding.recyclerView.apply{
            layoutManager = GridLayoutManager(
                this@MainActivity,
                3,
                GridLayoutManager.HORIZONTAL,
                false)
            adapter = this@MainActivity.adapter
        }
    }

    private fun configButtons() {
        binding.mBtnCart.setOnClickListener{
            val fragment = CartFragment()
            fragment.show(supportFragmentManager.beginTransaction(), CartFragment::class.java.simpleName)
        }
    }
    private fun configAuth(){
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if(auth.currentUser != null){
                supportActionBar?.title = auth.currentUser?.displayName
                binding.nsvProducts.visibility = View.VISIBLE
                binding.llProgress.visibility = View.GONE
            } else{
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())
                resultLauncher.launch(AuthUI
                    .getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .build())
            }
        }
    }
    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
        configFirestoreRealtime()
    }
    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
        firestoreListener.remove()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_sign_out -> {
                AuthUI.getInstance().signOut(this)
                    .addOnSuccessListener {
                        Toast.makeText(this,
                            getString(R.string.mssg_sign_out_success),
                            Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            binding.nsvProducts.visibility = View.GONE
                            binding.llProgress.visibility = View.VISIBLE
                        } else{
                            Toast.makeText(this,
                                getString(R.string.mssg_sign_out_failure),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun configFirestoreRealtime() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_PRODUCTS)
        firestoreListener = productRef.addSnapshotListener { snapshots, error ->
            if(error != null){
                Toast.makeText(this,
                    getString(R.string.mssg_error_consultar_datos),
                    Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            for (snapshot in snapshots!!.documentChanges){
                val product = snapshot.document.toObject(Product::class.java)
                product.id = snapshot.document.id
                when(snapshot.type){
                    DocumentChange.Type.ADDED -> adapter.addProduct(product)
                    DocumentChange.Type.MODIFIED -> adapter.updateProduct(product)
                    DocumentChange.Type.REMOVED -> adapter.deleteProduct(product)
                }
            }
        }
    }

    override fun onClick(product: Product) {
        TODO("Not yet implemented")
    }

    override fun getProductsCart(): MutableList<Product> {
        val productCartList = mutableListOf<Product>()
        (1..7).forEach {
            val product = Product(it.toString(), "Producto $it",
                "This producto is $it", "", it, 2.0*it)
            productCartList.add(product)
        }
        return productCartList
    }

}