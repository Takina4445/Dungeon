package com.example.dungeon

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var button_main: Button
    private lateinit var button_item: Button
    private lateinit var button_food: Button
    private lateinit var player_status:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_main:Button=findViewById<Button>(R.id.button1)
        val button_item:Button=findViewById<Button>(R.id.button2)
        val button_food:Button=findViewById<Button>(R.id.button3)
        val textview_player_status:TextView=findViewById<TextView>(R.id.textview_status)
        button_main.setOnClickListener(ActivityChange_main)
        button_item.setOnClickListener(ActivityChange_item)
        button_food.setOnClickListener(ActivityChange_food)

        val DB_FILE = "doungeon.db"
        val DB_TABLE = "player"
        val MyDB: SQLiteDatabase

// 建立自訂的 FriendDbHelper 物件
        val friDbHp = MyDBHelper(applicationContext, DB_FILE, null, 1)
// 設定建立 table 的指令
        friDbHp.sCreateTableCommand = "CREATE TABLE " + DB_TABLE + "(" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "exp INT," +
                "hp INT," +
                "stamina INT," +
                "atk INT," +
                "def INT," +
                "money INT)"
// 取得上面指定的檔名資料庫，如果該檔名不存在就會自動建立一個資料庫檔案
        MyDB = friDbHp.writableDatabase
        val c = MyDB.query(
            true, DB_TABLE, arrayOf("name", "exp", "hp", "stamina","atk","def","money"),
            null, null, null, null, null, null
        )
        if (c.count === 0) {
            val newRow = ContentValues()
// 初始化玩家資料
            newRow.put("name", "無名的旅人")
            newRow.put("exp", 0)
            newRow.put("hp", 10)
            newRow.put("stamina", 10)
            newRow.put("atk", 1)
            newRow.put("def", 0)
            newRow.put("money", 0)
// 玩家資料放至資料表中
            MyDB.insert(DB_TABLE, null, newRow)
        }
        else{
            c.moveToFirst();
            textview_player_status.text="生命:"+c.getInt(2)+"\n體力:"+c.getInt(3)+"\n攻擊力:"+c.getInt(4)+"\n防禦力:"+c.getInt(5)+"\n"
        }








    }
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
    override fun onBackPressed() {
//        封鎖返回鍵
        moveTaskToBack(false)
    }
}

