package br.com.aulaspuc.sistemazonaazul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var btnIrregularidades: MaterialButton
    private lateinit var btnConsulta: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnIrregularidades = findViewById(R.id.btnRegistrarIrregularidade)
        btnConsulta = findViewById(R.id.btnVerificarVeiculo)

        btnIrregularidades.setOnClickListener{
            val intent = Intent(this, Irregularidades::class.java)
            startActivity(intent)
        }
        btnConsulta.setOnClickListener{
            val intent = Intent(this, Consultar::class.java)
            startActivity(intent)
        }
    }
}