package com.example.navegacao1.ui.telas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.navegacao1.model.dados.RetrofitClient
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TelaPrincipal(modifier: Modifier = Modifier) {
    var scope = rememberCoroutineScope()
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var nomeUsuario by remember { mutableStateOf("") }
    var idUsuario by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text(text = "Tela Principal")

        // Inserção de Usuário
        OutlinedTextField(
            value = nomeUsuario,
            onValueChange = { nomeUsuario = it },
            label = { Text(text = "Nome do Usuário") }
        )
        Button(onClick = {
            scope.launch {
                val novoUsuario = Usuario(nome = nomeUsuario, senha = "default" )
                RetrofitClient.usuarioService.inserir(novoUsuario)
                usuarios = getUsuarios() // Atualizar lista
            }
        }) {
            Text("Adicionar Usuário")
        }

        // Remoção de Usuário por ID
        OutlinedTextField(
            value = idUsuario,
            onValueChange = { idUsuario = it },
            label = { Text(text = "ID do Usuário para Remover") }
        )
        Button(onClick = {
            scope.launch {
                RetrofitClient.usuarioService.remover(idUsuario)
                usuarios = getUsuarios() // Atualizar lista
            }
        }) {
            Text("Remover Usuário")
        }

        // Exibir Lista de Usuários
        LazyColumn {
            items(usuarios) { usuario ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = usuario.nome)
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            scope.launch {
                usuarios = getUsuarios()
            }
        }
    }
}

suspend fun getUsuarios(): List<Usuario> {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.listar()
    }
}
