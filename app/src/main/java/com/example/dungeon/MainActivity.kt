package com.example.dungeon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var button_main: Button
    private lateinit var button_item: Button
    private lateinit var button_food: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_main:Button=findViewById<Button>(R.id.button1)
        val button_item:Button=findViewById<Button>(R.id.button2)
        val button_food:Button=findViewById<Button>(R.id.button3)
        button_main.setOnClickListener(ActivityChange_main)
        button_item.setOnClickListener(ActivityChange_item)
        button_food.setOnClickListener(ActivityChange_food)
    }
    private val ActivityChange_main = View.OnClickListener {

        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
    }
    private val ActivityChange_item= View.OnClickListener{
        //切換至主頁面(戰鬥頁面)
        val intent = Intent()
        intent.setClass(this, item::class.java)
        startActivity(intent)
    }
    private val ActivityChange_food= View.OnClickListener{
        //切換至主頁面(戰鬥頁面)
        val intent = Intent()
        intent.setClass(this, food::class.java)
        startActivity(intent)
    }
}