package com.example.currencyconverter

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val TAG = "MainActivity"

    private val americanDollar = "American Dollar"
    private val turkishLira = "Turkish Lira"
    private val syrianLira = "Syrian Lira"
    private val riyalSaudi = "Riyal Saudi"
    private val egyptianPound = "Egyptian Pound"
    private val euro = "Euro"

    val values = mapOf(
        americanDollar to 1.0,
        turkishLira to 18.09,
        syrianLira to 4500.0,
        riyalSaudi to 3.76,
        egyptianPound to 19.15,
        euro to 0.99
    )

    fun vibratePhone() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        populateDropDownMenu()



        toolbar.inflateMenu(R.menu.options_menu)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.miShare -> {
                    val message = "${etAmount.text.toString()} ${tvFromCurrency.text}" +
                            " is equal to ${etConvertedAmount.text.toString()} ${tvToCurrency.text}"

                    Intent(Intent.ACTION_SEND).also {
                        it.type = "text/plain"
                        it.putExtra(Intent.EXTRA_TEXT, message)

                        if (it.resolveActivity(packageManager) != null) {
                            startActivity(it)
                        } else {
                            Toast.makeText(
                                this,
                                "No App to handle with this action",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            true

        }


        binding.btnConvert.setOnClickListener {
            etAmount.requestFocus()
            calculateResult()
        }

    }

    private fun calculateResult() {
        if (etAmount.text.toString().isNotEmpty()) {
            val fromValue: Double = values[tvFromCurrency.text.toString()] as Double
            val toValue: Double = values[tvToCurrency.text.toString()] as Double
            val amount = etAmount.text.toString().toDouble()
            val result = amount.times(toValue.div(fromValue))
            val formattedResult = String.format("%.2f", result)
            etConvertedAmount.setText(formattedResult)
        } else {
            val snackbar = Snackbar.make(etAmount, "Amount required!!", Snackbar.LENGTH_SHORT)
            snackbar.show()
            snackbar.setAction("OK") {}
            vibratePhone()
        }
    }

    private fun populateDropDownMenu() {
        val listOfCountry =
            listOf(americanDollar, turkishLira, syrianLira, riyalSaudi, egyptianPound, euro)
        val adapter = ArrayAdapter(this, R.layout.drop_down_list_item, listOfCountry)
        tvToCurrency.setAdapter(adapter)
        tvFromCurrency.setAdapter(adapter)
    }


}