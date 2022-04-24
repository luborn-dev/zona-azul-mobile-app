package br.com.aulaspuc.sistemazonaazul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var btnConsulta: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnConsulta = findViewById(R.id.btnVerificarVeiculo)

        btnConsulta.setOnClickListener{
            val intent = Intent(this, Consultar::class.java)
            startActivity(intent)
        }
    }
}