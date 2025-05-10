package com.example.controledegastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.controledegastos.ui.theme.ControledeGastosTheme
import com.example.controledegastos.ui.gasto.GastoDetailScreen
import com.example.controledegastos.ui.gasto.GastoFormScreen
import com.example.controledegastos.ui.gasto.GastoListScreen
import com.example.controledegastos.ui.viewmodel.GastoViewModel
import com.example.controledegastos.model.Gasto
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControledeGastosTheme {
                val viewModel: GastoViewModel = viewModel()
                val gastos by viewModel.gastos.collectAsState()
                val gastoSelecionado by viewModel.gastoSelecionado.collectAsState()
                val erro by viewModel.erro.collectAsState()

                var showForm by remember { mutableStateOf(false) }
                var showDetail by remember { mutableStateOf(false) }

                when {
                    showForm -> {
                        GastoFormScreen(
                            gasto = gastoSelecionado,
                            onSave = { gasto ->
                                if (gasto.id == 0L) {
                                    viewModel.adicionarGasto(gasto)
                                } else {
                                    viewModel.atualizarGasto(gasto)
                                }
                                showForm = false
                                viewModel.selecionarGasto(null)
                            },
                            onCancel = {
                                showForm = false
                                viewModel.selecionarGasto(null)
                            }
                        )
                    }
                    showDetail && gastoSelecionado != null -> {
                        GastoDetailScreen(
                            gasto = gastoSelecionado!!,
                            onEdit = {
                                showDetail = false
                                showForm = true
                            },
                            onDelete = {
                                viewModel.deletarGasto(gastoSelecionado!!.id)
                                showDetail = false
                                viewModel.selecionarGasto(null)
                            },
                            onBack = {
                                showDetail = false
                                viewModel.selecionarGasto(null)
                            }
                        )
                    }
                    else -> {
                        GastoListScreen(
                            gastos = gastos,
                            onAddClick = { showForm = true },
                            onGastoClick = { gasto ->
                                viewModel.selecionarGasto(gasto)
                                showDetail = true
                            }
                        )
                    }
                }

                // Show error dialog if there's an error
                erro?.let { errorMessage ->
                    AlertDialog(
                        onDismissRequest = { viewModel.limparErro() },
                        title = { Text("Erro") },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            TextButton(onClick = { viewModel.limparErro() }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}
