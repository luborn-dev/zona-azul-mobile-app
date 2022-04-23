package br.com.aulaspuc.sistemazonaazul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.w3c.dom.Text

class Consultar : AppCompatActivity() {

    private lateinit var btnBuscar: AppCompatButton
    private lateinit var btnPlacaInvalida: AppCompatButton
    private lateinit var functions: FirebaseFunctions
    private lateinit var etPlaca: TextInputEditText
    private lateinit var resultado: TextView
    private val logEntry = "CADASTRO_PRODUTO";
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar)

        functions        = Firebase.functions("southamerica-east1")
        btnBuscar        = findViewById(R.id.btnBuscarVeiculo)
        btnPlacaInvalida = findViewById(R.id.btnPlacaInválida)
        resultado        = findViewById(R.id.res)
        etPlaca          = findViewById(R.id.entryPlaca)

        btnBuscar.setOnClickListener {
            val search = Product(etPlaca.text.toString())
            buscarVeiculo(search)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        val e = it.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                            println(code)
                            println(details)
                        }
                    } else {
                        val genericResp =
                            gson.fromJson(it.result, FunctionsGenericResponse::class.java)
                        val payload = genericResp.payload.toString()
                        if (payload == "NÃO PAGO") {
                            val message = "Veiculo Irregular\n\nDeseja prosseguir com o registro da irregularidade?"
                            resultado.text = message
                            resultado.visibility = View.VISIBLE
                            btnPlacaInvalida.visibility = View.VISIBLE

                        } else {
                            val message = "Veiculo Regular"
                            resultado.text = message
                            resultado.visibility = View.VISIBLE
                            btnPlacaInvalida.visibility = View.INVISIBLE

                        }
                    }
                }
        }
        btnPlacaInvalida.setOnClickListener{
            val intent = Intent(this, Irregularidades::class.java)
            intent.putExtra("placa",etPlaca.text.toString())
            startActivity(intent)
        }
    }

    private fun buscarVeiculo(p: Product): Task<String> {
        val data = hashMapOf(
            "placa" to p.placa
        )
        return functions
            .getHttpsCallable("checarRegularidade")
            .call(data)
            .continueWith { task ->
                // se faz necessario transformar a string de retorno como uma string Json valida.
                val res = gson.toJson(task.result?.data)
                println(res)
                res
            }
    }

}