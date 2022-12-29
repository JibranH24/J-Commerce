package com.example.j_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.j_commerce.data.Category
import com.example.j_commerce.data.Product
import com.example.j_commerce.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val  firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {

    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()

    private val pagingInfo = PagingInfo()
    init {
        fetchOfferProducts()
        fetchBestProducts()
    }

    fun fetchOfferProducts(){
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category).whereNotEqualTo("offerPercentage",null).limit(pagingInfo.pageNumber * 10).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
                pagingInfo.oldBestProducts  = products
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(products))
                }
                pagingInfo.pageNumber ++
            }.addOnFailureListener{
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
    fun fetchBestProducts(){
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category",category.category).whereEqualTo("offerPercentage",null).limit(pagingInfo.pageNumber * 10).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
                pagingInfo.oldBestProducts  = products
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(products))
                }
                pagingInfo.pageNumber ++
            }.addOnFailureListener{
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

}
