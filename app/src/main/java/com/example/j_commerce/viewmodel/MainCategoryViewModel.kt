package com.example.j_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.j_commerce.data.Product
import com.example.j_commerce.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
):ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealsProduct =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProduct: StateFlow<Resource<List<Product>>> = _bestDealsProduct

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    private val pagingInfo = PagingInfo()
    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProducts()
    }

    fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Special Products").get()
            .addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDealsProduct.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Best Deals").get()
            .addOnSuccessListener { result ->
                val bestDealsProducts = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealsProduct.emit(Resource.Success(bestDealsProducts))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDealsProduct.emit(Resource.Error(it.message.toString()))
                }
            }
    }
    fun fetchBestProducts(){
        if(!pagingInfo.isPagingEnd){
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
            // if you want to make multiple queries you have to create an index on firestore and then write it like this:
        //firestore.collection("Products").whereEqualTo("categegory","Chairs").orderBy("id",Query.Direction.ASCENDING).limit()
        firestore.collection("Products").limit(pagingInfo.bestProductsPage * 10).get()
            .addOnSuccessListener { result ->
                val bestProducts = result.toObjects(Product::class.java)
                pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
                pagingInfo.oldBestProducts  = bestProducts
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProducts))
                }
                pagingInfo.bestProductsPage ++
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}
}
internal data class PagingInfo(
    var bestProductsPage : Long =1,
    var oldBestProducts: List<Product> = emptyList(),
    var isPagingEnd : Boolean = false

)