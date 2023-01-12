package pt.ipca.smartcanteen.views.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.RetroPaymentMethod
import pt.ipca.smartcanteen.models.RetroTrade
import pt.ipca.smartcanteen.models.adapters.OrdersAdapterRec
import pt.ipca.smartcanteen.models.helpers.LoadingDialogManager
import pt.ipca.smartcanteen.models.helpers.SharedPreferencesHelper
import pt.ipca.smartcanteen.models.helpers.SmartCanteenRequests
import pt.ipca.smartcanteen.services.OrdersService
import pt.ipca.smartcanteen.services.TradesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumerTradeActivity : AppCompatActivity() {

    private val checkBox1: CheckBox by lazy {findViewById<View>(R.id.trade_general_checkbox) as CheckBox};
    private val checkBox2: CheckBox by lazy {findViewById<View>(R.id.trade_direct_checkbox) as CheckBox};
    private val editText: EditText by lazy {findViewById<View>(R.id.trade_email_edittext) as EditText}
    private val spinner_general: Spinner by lazy {findViewById<View>(R.id.trade_general_spinner) as Spinner}
    private val spinner_direct: Spinner by lazy {findViewById<View>(R.id.trade_direct_spinner) as Spinner}
    private val cancelButton: Button by lazy {findViewById<View>(R.id.trade_cancel) as Button}
    private val confirmButton: Button by lazy {findViewById<View>(R.id.trade_confirm) as Button}
    private lateinit var loadingDialogManager: LoadingDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)

        loadingDialogManager = LoadingDialogManager(layoutInflater, this)
        loadingDialogManager.createLoadingAlertDialog()

        getPaymentMethods()

        spinner_general.visibility = View.INVISIBLE
        spinner_direct.visibility = View.INVISIBLE
        editText.visibility = View.INVISIBLE

        if (spinner_general != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("MBWAY", "Multibanco", "MBWAY", "Multibanco", "MBWAY", "Multibanco",)
            )
            spinner_general.adapter = adapter
        }

        if (spinner_direct != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOf("MBWAY", "Multibanco", "MBWAY", "Multibanco", "MBWAY", "Multibanco",)
            )
            spinner_direct.adapter = adapter
        }

        cancelButton.setOnClickListener {
            finish()
        }

        confirmButton.setOnClickListener {

            // TODO: verificar se os spinners foram preenchidos

            finish()
            Toast.makeText(this@ConsumerTradeActivity, "Encomenda colocada para troca com sucesso!", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun checkBox1Clicked(view: View) {
        checkBox1.setOnClickListener {
            if (checkBox1.isChecked) {
                spinner_general.visibility = View.VISIBLE
                checkBox2.isChecked = false
                editText.visibility = View.GONE
                spinner_direct.visibility = View.GONE
            } else {
                spinner_general.visibility = View.GONE
            }
        }
    }

    fun checkBox2Clicked(view: View) {
        checkBox2.setOnClickListener {
            if (checkBox2.isChecked) {
                editText.visibility = View.VISIBLE
                spinner_direct.visibility = View.VISIBLE
                checkBox1.isChecked = false
                spinner_general.visibility = View.GONE
            } else {
                editText.visibility = View.GONE
                spinner_direct.visibility = View.GONE
            }
        }
    }

    fun getPaymentMethods() {
        val retrofit = SmartCanteenRequests().retrofit

        val service = retrofit.create(TradesService::class.java)

        val sp = SharedPreferencesHelper.getSharedPreferences(this@ConsumerTradeActivity)
        val token = sp.getString("token", null)

        loadingDialogManager.dialog.show()

        service.getPaymentMethods("Bearer $token").enqueue(object :
            Callback<List<RetroPaymentMethod>> {
            override fun onResponse(
                call: Call<List<RetroPaymentMethod>>,
                response: Response<List<RetroPaymentMethod>>
            ) {
                if (response.code() == 200) {

                    loadingDialogManager.dialog.dismiss()


                    Toast.makeText(this@ConsumerTradeActivity, "Métodos de pagamento obtidos com sucesso!.", Toast.LENGTH_LONG)
                        .show()

                    val retroFit2 = response.body()
                }
            }

            override fun onFailure(calll: Call<List<RetroPaymentMethod>>, t: Throwable) {
                loadingDialogManager.dialog.dismiss()
                Toast.makeText(this@ConsumerTradeActivity, "Erro! Tente novamente.", Toast.LENGTH_LONG)
                    .show()
            }

            })

    }

    //fun doCancel(view: View){
    //    println("aqui")
    //    var intent = Intent(this@ConsumerExchangeActivity, MyOrdersActivity::class.java)
    //    startActivity(intent)
    //}
}