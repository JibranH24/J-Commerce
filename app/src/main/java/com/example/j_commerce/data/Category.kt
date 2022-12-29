package com.example.j_commerce.data

sealed class Category( val category : String){
    object Chair: Category("Chairs")
    object Wardrobes: Category("Wardrobes")
    object Tables: Category("Tables")
    object Lights: Category("Lights")
    object Beds: Category("Beds")
}
