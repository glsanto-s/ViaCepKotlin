package com.example.buscarcep

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.buscarcep.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lblCep = findViewById<EditText>(R.id.editTextCEP)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val txtViewRes = findViewById<TextView>(R.id.txtViewRes)

        val groupOne = findViewById<LinearLayout>(R.id.groupOne)
        val groupTwo = findViewById<LinearLayout>(R.id.groupTwo)

        btnBuscar.setOnClickListener{
            val cep = lblCep.text.toString()
            if(cep.isEmpty()) {
                Toast.makeText(this,
                    "Por favor, digite um CEP válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitClient.instance.buscarEndereco(cep).enqueue(object : Callback<Endereco> {
                override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                    if(response.isSuccessful){
                        val endereco = response.body()
                        if(endereco?.cep != null){
                            txtViewRes.text = "CEP: ${endereco.cep}" +
                                    "\nLogradouro: ${endereco.logradouro} | Bairro: ${endereco.bairro}" +
                                    "\nLocalidade: ${endereco.localidade} | UF: ${endereco.uf}"
                            groupOne.visibility = LinearLayout.GONE
                            groupTwo.visibility = LinearLayout.VISIBLE
                        }
                        else {
                            txtViewRes.text = "CEP não encontrado!"
                            groupOne.visibility = LinearLayout.GONE
                            groupTwo.visibility = LinearLayout.VISIBLE
                        }
                    } else {
                        txtViewRes.text = "Erro ao buscar o CEP!"
                        groupOne.visibility = LinearLayout.GONE
                        groupTwo.visibility = LinearLayout.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Endereco>, t: Throwable) {
                    txtViewRes.text = "Erro de conexão: ${t.message}"
                    groupOne.visibility = LinearLayout.GONE
                    groupTwo.visibility = LinearLayout.VISIBLE
                }
            })
        }
        btnBack.setOnClickListener{
            groupOne.visibility = LinearLayout.VISIBLE
            groupTwo.visibility = LinearLayout.GONE
        }
    }
}