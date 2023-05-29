package com.example.dungeon

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text
import java.util.Arrays

class MainActivity : AppCompatActivity() {
    private lateinit var button_main: Button
    private lateinit var button_item: Button
    private lateinit var button_food: Button
    private lateinit var player_status:TextView
    private lateinit var button_battle:Button
    val DB_FILE = "doungeon.db"
    val DB_TABLE = "player"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_main:Button=findViewById<Button>(R.id.button1)
        val button_item:Button=findViewById<Button>(R.id.button2)
        val button_food:Button=findViewById<Button>(R.id.button3)
//        val textview_player_status:TextView=findViewById<TextView>(R.id.textview_status)
        //在fun player_status_change()中
        val button_battle:Button=findViewById<Button>(R.id.button_battle)
        button_main.setOnClickListener(ActivityChange_main)
        button_item.setOnClickListener(ActivityChange_item)
        button_food.setOnClickListener(ActivityChange_food)
        button_battle.setOnClickListener(Activity_battle)
        player_status_change()//刷新玩家資料





    }

    ////////////////////
    ///////////////////
    private fun player_status_change(){
        val MyDB: SQLiteDatabase
// 建立自訂的 FriendDbHelper 物件
        val friDbHp = MyDBHelper(applicationContext, DB_FILE, null, 1)

        MyDB = friDbHp.writableDatabase
        val c = friDbHp.getPlayerData()
        c.moveToFirst();
        val textview_player_status:TextView=findViewById<TextView>(R.id.textview_status)
        textview_player_status.text=Html.fromHtml(
            "<font color=${Color.GREEN}>生命:</font>"+"<font color=${Color.WHITE}>"+c.getInt(3)+"<br></font>"
                +"<font color=${Color.GREEN}>體力:</font>"+"<font color=${Color.WHITE}>"+c.getInt(4)+"<br></font>"
                +"<font color=${Color.GREEN}>攻擊力:</font>"+"<font color=${Color.WHITE}>"+c.getInt(5)+"<br></font>"
                +"<font color=${Color.GREEN}>防禦力:</font>"+"<font color=${Color.WHITE}>"+c.getInt(6)+"<br></font>"
        )

    }
//    private fun player_query(): Cursor? {
//        val MyDB: SQLiteDatabase
//// 建立自訂的 FriendDbHelper 物件
//        val friDbHp = MyDBHelper(applicationContext, DB_FILE, null, 1)
//
//        MyDB = friDbHp.writableDatabase
//        return MyDB.query(
//            true, DB_TABLE, arrayOf("id","name","exp","hp","syamina","atk","def","money"),
//            null, null, null, null, null, null
//        )
//    }
    private val ActivityChange_main = View.OnClickListener {

        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
    }
    private val ActivityChange_item= View.OnClickListener{
        //切換至主頁面(戰鬥頁面)
        val intent = Intent()
        intent.setClass(this, Item::class.java)
        startActivity(intent)
    }
    private val ActivityChange_food= View.OnClickListener{
        //切換至主頁面(戰鬥頁面)
        val intent = Intent()
        intent.setClass(this, Food::class.java)
        startActivity(intent)
    }
    private val Activity_battle=View.OnClickListener {
        val MyDB: SQLiteDatabase
// 建立自訂的 FriendDbHelper 物件
        val friDbHp = MyDBHelper(applicationContext, DB_FILE, null, 1)

        MyDB = friDbHp.writableDatabase
        val c = friDbHp.getPlayerData()
        c.moveToFirst()
        val newRow = ContentValues()
        newRow.put("name", "無名的旅人")
        newRow.put("exp", 0)
        newRow.put("hp", c.getInt(3)+1)
        newRow.put("stamina", 11)
        newRow.put("atk", 1)
        newRow.put("def", 0)
        newRow.put("money", 0)
// 將ContentValues中的資料，放至資料表中
        MyDB.update(DB_TABLE, newRow,
            "id='1'", null)
        player_status_change()
    }

    /////////
    /////////
    override fun onBackPressed() {
//        封鎖返回鍵
        moveTaskToBack(false)
    }
}

