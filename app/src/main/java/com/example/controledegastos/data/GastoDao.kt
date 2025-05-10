package com.example.controledegastos.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.controledegastos.model.Gasto
import java.util.Date

/**
 * Data Access Object for managing Gasto entities in the database.
 */
class GastoDao(private val context: Context) {
    private val dbHelper = GastoDatabaseHelper(context)

    /**
     * Inserts a new Gasto into the database.
     * @return The ID of the newly inserted Gasto, or -1 if insertion failed
     */
    fun inserir(gasto: Gasto): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(GastoDatabaseHelper.COL_DESCRICAO, gasto.descricao)
            put(GastoDatabaseHelper.COL_VALOR, gasto.valor)
            put(GastoDatabaseHelper.COL_DATA, gasto.data.time)
            put(GastoDatabaseHelper.COL_CATEGORIA, gasto.categoria)
        }
        return db.insert(GastoDatabaseHelper.TABLE_GASTOS, null, values)
    }

    /**
     * Updates an existing Gasto in the database.
     * @return The number of rows affected
     */
    fun atualizar(gasto: Gasto): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(GastoDatabaseHelper.COL_DESCRICAO, gasto.descricao)
            put(GastoDatabaseHelper.COL_VALOR, gasto.valor)
            put(GastoDatabaseHelper.COL_DATA, gasto.data.time)
            put(GastoDatabaseHelper.COL_CATEGORIA, gasto.categoria)
        }
        return db.update(
            GastoDatabaseHelper.TABLE_GASTOS,
            values,
            "${GastoDatabaseHelper.COL_ID} = ?",
            arrayOf(gasto.id.toString())
        )
    }

    /**
     * Deletes a Gasto from the database.
     * @return The number of rows affected
     */
    fun deletar(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            GastoDatabaseHelper.TABLE_GASTOS,
            "${GastoDatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString())
        )
    }

    /**
     * Retrieves all Gastos from the database.
     */
    fun buscarTodos(): List<Gasto> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            GastoDatabaseHelper.TABLE_GASTOS,
            null,
            null,
            null,
            null,
            null,
            "${GastoDatabaseHelper.COL_DATA} DESC"
        )
        return cursor.use { it.toGastoList() }
    }

    /**
     * Retrieves a Gasto by its ID.
     */
    fun buscarPorId(id: Long): Gasto? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            GastoDatabaseHelper.TABLE_GASTOS,
            null,
            "${GastoDatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) it.toGasto() else null
        }
    }

    /**
     * Retrieves all Gastos for a specific date.
     */
    fun buscarPorData(data: Date): List<Gasto> {
        val db = dbHelper.readableDatabase
        val startOfDay = Date(data.year, data.month, data.date, 0, 0, 0).time
        val endOfDay = Date(data.year, data.month, data.date, 23, 59, 59).time
        
        val cursor = db.query(
            GastoDatabaseHelper.TABLE_GASTOS,
            null,
            "${GastoDatabaseHelper.COL_DATA} BETWEEN ? AND ?",
            arrayOf(startOfDay.toString(), endOfDay.toString()),
            null,
            null,
            "${GastoDatabaseHelper.COL_DATA} DESC"
        )
        return cursor.use { it.toGastoList() }
    }

    private fun Cursor.toGasto(): Gasto {
        return Gasto(
            id = getLong(getColumnIndexOrThrow(GastoDatabaseHelper.COL_ID)),
            descricao = getString(getColumnIndexOrThrow(GastoDatabaseHelper.COL_DESCRICAO)),
            valor = getDouble(getColumnIndexOrThrow(GastoDatabaseHelper.COL_VALOR)),
            data = Date(getLong(getColumnIndexOrThrow(GastoDatabaseHelper.COL_DATA))),
            categoria = getString(getColumnIndexOrThrow(GastoDatabaseHelper.COL_CATEGORIA))
        )
    }

    private fun Cursor.toGastoList(): List<Gasto> {
        val gastos = mutableListOf<Gasto>()
        while (moveToNext()) {
            gastos.add(toGasto())
        }
        return gastos
    }
} 