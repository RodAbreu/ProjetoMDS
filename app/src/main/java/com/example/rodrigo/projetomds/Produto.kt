package com.example.rodrigo.projetomds
import java.io.Serializable

data class Produto (
    val Nome: String,
    val Quant:String,
    val Tel:String,
    val camImagem:String) : Serializable
