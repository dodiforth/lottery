package com.example.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val addButton: Button by lazy {
        findViewById<Button>(R.id.addButton)
    }

    private val clearButton: Button by lazy {
        findViewById<Button>(R.id.clearButton)
    }

    private  val runButton: Button by lazy {
        findViewById<Button>(R.id.runButton)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.firstNumberTextView),
            findViewById<TextView>(R.id.secondNumberTextView),
            findViewById<TextView>(R.id.thirdNumberTextView),
            findViewById<TextView>(R.id.fourthNumberTextView),
            findViewById<TextView>(R.id.fifthNumberTextView),
            findViewById<TextView>(R.id.sixthNumberTextView)
        )
    }

    private var didRun = false

    private val pickNumberSet = hashSetOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set range for the number picker
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    //init runButton at starting
    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number, textView)
            }

            Log.d("MainActivity", list.toString())
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {

            // EXCEPTION CONDITIONS :
            if (didRun) {
                Toast.makeText(this, "Try again after clear the numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "You can only choose up to 5 numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "You already choose this number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //-- ends

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            pickNumberSet.add(numberPicker.value)

            setNumberBackground(numberPicker.value, textView)

        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach{
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)){
                        continue
                    }
                    this.add(i)
                }
            }
        numberList.shuffle()
        val finalList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
        return finalList.sorted()
    }

    private fun setNumberBackground(number:Int, textView: TextView) {
        // Colours depending in the range of the numbers
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_grey)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }

        // Code : Apply colour ball look to the numbers
        //textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
    }
}