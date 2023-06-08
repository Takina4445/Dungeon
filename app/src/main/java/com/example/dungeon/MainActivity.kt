package com.example.dungeon

import android.annotation.SuppressLint
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
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.dungeon.MyDBHelper.Companion.DB_TABLE_MONSTER
import org.w3c.dom.Text
import java.util.Arrays
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var button_main: Button
    private lateinit var button_item: Button
    private lateinit var button_food: Button
    private lateinit var player_status:TextView
    private lateinit var battle_log:TextView
    private lateinit var button_battle:Button
    val DB_FILE = "doungeon.db"
    val DB_TABLE = "player"
    val DB_TABLE_MONSTER = "monster"
    val DB_TABLE_SKILLS="skills"
    val DB_TABLE_USE_SKILL="useskill"
    val DB_TABLE_ITEM="item"
var i:Int=0


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
        val battle_log:TextView=findViewById<TextView>(R.id.textview_battleprocess)//戰鬥紀錄
        battle_log.text= ""

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
            "<font color=${Color.GREEN}>生命:</font>"+"<font color=${Color.WHITE}>"+c.getInt(4)+"/"+c.getInt(5)+"<br></font>"
                +"<font color=${Color.GREEN}>體力:</font>"+"<font color=${Color.WHITE}>"+c.getInt(6)+"/"+c.getInt(7)+"<br></font>"
                +"<font color=${Color.GREEN}>攻擊力:</font>"+"<font color=${Color.WHITE}>"+c.getInt(8)+"<br></font>"
                +"<font color=${Color.GREEN}>防禦力:</font>"+"<font color=${Color.WHITE}>"+c.getInt(9)+"<br></font>"
        ,Html.FROM_HTML_MODE_LEGACY)

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

    private val Activity_battle=View.OnClickListener {
        val battle_log:TextView=findViewById<TextView>(R.id.textview_battleprocess)//戰鬥紀錄
//        battle_log.text= ""

        val MyDB: SQLiteDatabase
// 建立自訂的 FriendDbHelper 物件
        val friDbHp = MyDBHelper(applicationContext, DB_FILE, null, 1)

        MyDB = friDbHp.writableDatabase
        val c = friDbHp.getPlayerData()
        c.moveToFirst()
        val obj_player = ContentValues()
//        將玩家數值存入
        var columnCount = c.columnCount-1
        do{
            for (i in 0..columnCount) {
                val columnName = c.getColumnName(i)
                val columnType = c.getType(i)
                when (columnType) {
                    Cursor.FIELD_TYPE_INTEGER -> {
                        val intValue = c.getInt(i)
                        obj_player.put(columnName, intValue)
                    }

                    Cursor.FIELD_TYPE_STRING -> {
                        val stringValue = c.getString(i)
                        obj_player.put(columnName, stringValue)
                    }
                }
            }
        }while (c.moveToNext())
        c.moveToFirst()
//怪物選擇
        val selection = "id = ?"
        val selectionArgs = arrayOf(Random.nextInt(1, 3).toString())
        val cursor = MyDB.query(DB_TABLE_MONSTER,null ,selection, selectionArgs, null, null, null)
//        val columnName = "*"
//        arrayOf(columnName)

        cursor.moveToFirst()

        val obj_monster = ContentValues()
//        將怪物數值存入
        columnCount = cursor.columnCount-1
        do{
            for (i in 0..columnCount) {
                val columnName = cursor.getColumnName(i)
                val columnType = cursor.getType(i)
                when (columnType) {
                    Cursor.FIELD_TYPE_INTEGER -> {
                        val intValue = cursor.getInt(i)
                        obj_monster.put(columnName, intValue)
                    }

                    Cursor.FIELD_TYPE_STRING -> {
                        val stringValue = cursor.getString(i)
                        obj_monster.put(columnName, stringValue)
                    }
                }
            }
        }while (cursor.moveToNext())
        cursor.moveToFirst()



//        val monsterName =obj_monster.get("name")
//        battle_log.text=monsterName
        var var_totalweight:Int
        var var_battle_random:Int


//        while (cursor.getInt(5)>0||c.getInt(4)>0){
            var var_battle_damage:Int//戰鬥傷害
            var_totalweight=obj_player.getAsInteger("stamina")+obj_monster.getAsInteger("stamina")


            var_battle_random= Random.nextInt(0,var_totalweight+2)
//        if (var_battle_random <= c.getInt(6) * (c.getInt(11) / 100)) {
            if (var_battle_random <= obj_player.getAsInteger("stamina") * (obj_player.getAsInteger("speed")/ obj_monster.getAsInteger("speed"))*0.9) {
                //玩家回合
                var_battle_damage=(obj_player.getAsInteger("atk")*1.3-obj_monster.getAsInteger("def")).toInt()
                battle_log.append(Html.fromHtml(
                        "<font color=${Color.CYAN}>" + obj_player.get("name") + "&nbsp;</font>"
                                + "<font color=${Color.WHITE}>對&nbsp;</font>"
                                + "<font color=${Color.RED}>" + obj_monster.get("name") + "&nbsp;</font>"
                                + "<font color=${Color.WHITE}>使出&nbsp;</font>" + "<font color=${Color.YELLOW}>" + "攻擊" + "&nbsp;</font>"
                                + "<font color=${Color.WHITE}>造成</font>" + "<font color=${Color.WHITE}>" + var_battle_damage + "點傷害<br></font>",
                        Html.FROM_HTML_MODE_LEGACY))

            }

            else {
                //怪物回合
                battle_log.append(Html.fromHtml(
                    "<font color=${Color.RED}>" + obj_monster.get("name") + "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>對&nbsp;</font>"
                            + "<font color=${Color.CYAN}>" + obj_player.get("name") + "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>使出&nbsp;</font>" + "<font color=${Color.YELLOW}>" + "攻擊" + "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>造成</font>" + "<font color=${Color.WHITE}>" + 3 + "點傷害<br></font>",
                    Html.FROM_HTML_MODE_LEGACY))
            }

            cursor.close()
//        }


/*
        val newRow = ContentValues()
        val monsterName = "野豬"
        val columnName = "exp"
        val selection = "name = ?"
        val selectionArgs = arrayOf(monsterName)
        val cursor = MyDB.query(DB_TABLE_MONSTER, arrayOf(columnName),selection, selectionArgs, null, null, null)
        var expValue: Int?=null
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(columnName)
            expValue = cursor.getInt(columnIndex)
        }
        newRow.put("exp", c.getInt(2)+expValue!!)
// 將ContentValues中的資料，放至資料表中
        MyDB.update(DB_TABLE, newRow,
            "id='1'", null)
        player_status_change()
        cursor.close()
        */


    }

    /////////
    /////////
    override fun onBackPressed() {
//        封鎖返回鍵
        moveTaskToBack(false)
    }
}

