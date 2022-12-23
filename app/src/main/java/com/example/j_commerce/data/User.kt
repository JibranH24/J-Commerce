package com.example.j_commerce.data

data class User(
     val firstName: String,
     val lastName: String,
     val email: String,
     var imagePath: String = "") {

constructor(): this("","","","")
}