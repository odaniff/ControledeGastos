package com.example.controledegastos.ui.gasto

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controledegastos.model.Gasto
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoFormScreen(
    gasto: Gasto? = null,
    onSave: (Gasto) -> Unit,
    onCancel: () -> Unit
) {
    var descricao by remember { mutableStateOf(gasto?.descricao ?: "") }
    var valor by remember { mutableStateOf(gasto?.valor?.toString() ?: "") }
    var categoria by remember { mutableStateOf(gasto?.categoria ?: Gasto.CATEGORIAS[0]) }
    var data by remember { mutableStateOf(gasto?.data ?: Date()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (gasto == null) "Novo Gasto" else "Editar Gasto") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth()
            )

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Categoria") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Gasto.CATEGORIAS.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoria = cat
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val novoGasto = Gasto(
                        id = gasto?.id ?: 0,
                        descricao = descricao,
                        valor = valor.toDoubleOrNull() ?: 0.0,
                        data = data,
                        categoria = categoria
                    )
                    onSave(novoGasto)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = descricao.isNotBlank() && valor.toDoubleOrNull() != null
            ) {
                Text("Salvar")
            }
        }
    }
}