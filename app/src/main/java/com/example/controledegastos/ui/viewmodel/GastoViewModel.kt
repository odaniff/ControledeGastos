package com.example.controledegastos.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.controledegastos.data.GastoDao
import com.example.controledegastos.model.Gasto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel for managing Gasto data and business logic.
 */
class GastoViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = GastoDao(application)
    private val _gastos = MutableStateFlow<List<Gasto>>(emptyList())
    val gastos: StateFlow<List<Gasto>> = _gastos.asStateFlow()

    private val _gastoSelecionado = MutableStateFlow<Gasto?>(null)
    val gastoSelecionado: StateFlow<Gasto?> = _gastoSelecionado.asStateFlow()

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro.asStateFlow()

    init {
        carregarGastos()
    }

    fun carregarGastos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _gastos.value = dao.buscarTodos()
            } catch (e: Exception) {
                _erro.value = "Erro ao carregar gastos: ${e.message}"
            }
        }
    }

    fun carregarGastosPorData(data: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _gastos.value = dao.buscarPorData(data)
            } catch (e: Exception) {
                _erro.value = "Erro ao carregar gastos: ${e.message}"
            }
        }
    }

    fun selecionarGasto(gasto: Gasto?) {
        _gastoSelecionado.value = gasto
    }

    fun adicionarGasto(gasto: Gasto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val id = dao.inserir(gasto)
                if (id != -1L) {
                    carregarGastos()
                } else {
                    _erro.value = "Erro ao adicionar gasto"
                }
            } catch (e: Exception) {
                _erro.value = "Erro ao adicionar gasto: ${e.message}"
            }
        }
    }

    fun atualizarGasto(gasto: Gasto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rowsAffected = dao.atualizar(gasto)
                if (rowsAffected > 0) {
                    carregarGastos()
                } else {
                    _erro.value = "Erro ao atualizar gasto"
                }
            } catch (e: Exception) {
                _erro.value = "Erro ao atualizar gasto: ${e.message}"
            }
        }
    }

    fun deletarGasto(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rowsAffected = dao.deletar(id)
                if (rowsAffected > 0) {
                    carregarGastos()
                } else {
                    _erro.value = "Erro ao deletar gasto"
                }
            } catch (e: Exception) {
                _erro.value = "Erro ao deletar gasto: ${e.message}"
            }
        }
    }

    fun limparErro() {
        _erro.value = null
    }
} 