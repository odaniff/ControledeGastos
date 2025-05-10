package com.example.controledegastos.model

import java.util.Date

/**
 * Data class representing an expense (Gasto) in the application.
 * 
 * @property id Unique identifier for the expense (auto-incremented in database)
 * @property descricao Description of the expense (required)
 * @property valor Amount of the expense (must be greater than 0)
 * @property data Date when the expense occurred
 * @property categoria Category of the expense (e.g., Alimentação, Transporte)
 */
data class Gasto(
    val id: Long = 0,
    val descricao: String,
    val valor: Double,
    val data: Date,
    val categoria: String
) {
    companion object {
        // List of predefined expense categories
        val CATEGORIAS = listOf(
            "Alimentação",
            "Transporte",
            "Moradia",
            "Lazer",
            "Saúde",
            "Educação",
            "Outros"
        )
    }
} 