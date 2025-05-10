package com.example.controledegastos.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Helper class to manage database creation and version management.
 */
class GastoDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_GASTOS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DESCRICAO TEXT NOT NULL,
                $COL_VALOR REAL NOT NULL,
                $COL_DATA INTEGER NOT NULL,
                $COL_CATEGORIA TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // In a production app, you should implement proper migration logic here
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GASTOS")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "gastos.db"
        const val DATABASE_VERSION = 1
        
        // Table name and column names
        const val TABLE_GASTOS = "gastos"
        const val COL_ID = "id"
        const val COL_DESCRICAO = "descricao"
        const val COL_VALOR = "valor"
        const val COL_DATA = "data"
        const val COL_CATEGORIA = "categoria"
    }
} 