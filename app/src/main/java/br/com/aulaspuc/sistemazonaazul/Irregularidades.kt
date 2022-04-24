package br.com.aulaspuc.sistemazonaazul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class Irregularidades : AppCompatActivity() {

    private lateinit var btnEnviarPlaca: MaterialButton
    private lateinit var etPlaca: TextView
    private lateinit var functions: FirebaseFunctions

    private val logEntry = "CADASTRO_PRODUTO"

    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_irregularidades)

        functions = Firebase.functions("southamerica-east1")

        btnEnviarPlaca = findViewById(R.id.btnBuscarPlacaIrregularidade)
        etPlaca = findViewById(R.id.carPlate)

        val ss:String = intent.getStringExtra("placa").toString()
        etPlaca.text = ss

        btnEnviarPlaca.setOnClickListener{
            val p = Product(etPlaca.text.toString())
            cadastrarProduto(p)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {

                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }

                    }else{

                        val genericResp = gson.fromJson(task.result, FunctionsGenericResponse::class.java)

                        Log.i(logEntry, genericResp.status.toString())
                        Log.i(logEntry, genericResp.message.toString())

                        Log.i(logEntry, genericResp.payload.toString())

                        val insertInfo = gson.fromJson(genericResp.payload.toString(), GenericInsertResponse::class.java)

                        Snackbar.make(btnEnviarPlaca, "Produto cadastrado: " + insertInfo.docId,
                            Snackbar.LENGTH_LONG).show();
                    }
                })
        }
        }

    private fun cadastrarProduto(p: Product): Task<String> {
        val data = hashMapOf(
            "placa" to p.placa
        )
        return functions
            .getHttpsCallable("addNewProduct")
            .call(data)
            .continueWith { task ->
                // se faz necessario transformar a string de retorno como uma string Json valida.
                val res = gson.toJson(task.result?.data)
                res
            }
    }
}
