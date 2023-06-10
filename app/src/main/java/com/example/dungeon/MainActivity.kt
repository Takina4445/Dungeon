package com.example.dungeon

//import android.annotation.SuppressLint
//import android.app.NotificationManager
import android.content.ContentValues
//import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
//import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.provider.Settings
import android.text.Html
//import android.text.method.ScrollingMovementMethod
//import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
//import android.widget.Toast
//import com.example.dungeon.MyDBHelper.Companion.DB_TABLE_MONSTER
//import com.google.android.material.snackbar.Snackbar
//import org.w3c.dom.Text
//import java.util.Arrays
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var button_main: Button
    private lateinit var button_item: Button
    private lateinit var button_food: Button
    private lateinit var player_status:TextView
    private lateinit var battle_log:TextView
    private lateinit var button_battle:Button
    private lateinit var button_rest:Button
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
        val button_rest:Button=findViewById<Button>(R.id.button_rest)
        button_main.setOnClickListener(ActivityChange_main)
        button_item.setOnClickListener(ActivityChange_item)
        button_food.setOnClickListener(ActivityChange_food)
        button_battle.setOnClickListener(Activity_battle)
        button_rest.setOnClickListener(Activity_rest)
        player_status_change()//刷新玩家資料
        val battle_log:TextView=findViewById<TextView>(R.id.textview_battleprocess)//戰鬥紀錄
        battle_log.text= ""

    }

    ////////////////////
    private fun Cursor_To_ContentValues(c:Cursor):ContentValues{
        c.moveToFirst()
        val obj_player = ContentValues()
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
        return obj_player
    }
    ///////////////////
    private fun player_status_change(){

        val MyDB: SQLiteDatabase
// 建立自訂的 FriendDbHelper 物件
        val friDbHp = MyDBHelper(applicationContext, DB_FILE, null, 1)

        MyDB = friDbHp.writableDatabase
        val c = friDbHp.getPlayerData()
        c.moveToFirst()
        //        將玩家數值存入
        var obj_player :ContentValues = Cursor_To_ContentValues(c)


        val textview_player_status:TextView=findViewById<TextView>(R.id.textview_status)
        textview_player_status.text=Html.fromHtml(
        "<font color=${Color.GREEN}>Lv.</font>"
                +"<font color=${Color.WHITE}>"+obj_player.getAsInteger("level")+"<br></font>"
                +"<font color=${Color.GREEN}>exp:</font>"
                +"<font color=${Color.WHITE}>"+obj_player.getAsInteger("exp")+"<br></font>"
                +"<font color=${Color.GREEN}>生命:</font>"
                +"<font color=${Color.WHITE}>"+obj_player.getAsInteger("hp")+"/"+obj_player.getAsInteger("maxhp")+"<br></font>"
                +"<font color=${Color.GREEN}>體力:</font>"
                +"<font color=${Color.WHITE}>"+obj_player.getAsInteger("stamina")+"/"+obj_player.getAsInteger("maxstamina")+"<br></font>"
                +"<font color=${Color.GREEN}>攻擊力:</font>"
                +"<font color=${Color.WHITE}>"+obj_player.getAsInteger("atk")+"<br></font>"
                +"<font color=${Color.GREEN}>防禦力:</font>"
                +"<font color=${Color.WHITE}>"+obj_player.getAsInteger("def")+"<br></font>"
                +"<font color=${Color.GREEN}>速度:</font>"
                +"<font color=${Color.WHITE}>"+obj_player.getAsInteger("speed")+"<br></font>"
                +"<font color=${Color.GREEN}>金錢:</font>"
                +"<font color=${Color.WHITE}>"+obj_player.getAsInteger("money")+"<br></font>"
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
        battle_log.text= ""

        val MyDB: SQLiteDatabase
// 建立自訂的 FriendDbHelper 物件
        val friDbHp = MyDBHelper(applicationContext, DB_FILE, null, 1)

        MyDB = friDbHp.writableDatabase
        val c = friDbHp.getPlayerData()
        c.moveToFirst()
        var obj_player :ContentValues = Cursor_To_ContentValues(c)
//怪物選擇
        val selection = "id = ?"
        val selectionArgs = arrayOf(Random.nextInt(1, 3).toString())
        val cursor = MyDB.query(DB_TABLE_MONSTER,null ,selection, selectionArgs, null, null, null)
//        val columnName = "*"
//        arrayOf(columnName)

        cursor.moveToFirst()

        var obj_monster :ContentValues = Cursor_To_ContentValues(cursor)



//        val monsterName =obj_monster.get("name")
//        battle_log.text=monsterName
            var var_battle_damage:Int//戰鬥傷害
        var var_totalweight:Int
        var var_battle_random:Int
        var var_use_stamina:Int//體力消耗
        var var_battle_reward:String=""//戰鬥獎勵
        var var_battle_round:Int=0
        while (obj_player.getAsInteger("hp")>0&&obj_monster.getAsInteger("hp")>0){
            var_battle_round+=1
            var_totalweight=obj_player.getAsInteger("stamina")+obj_monster.getAsInteger("stamina")


            var_battle_random= Random.nextInt(0,var_totalweight+2)
//        if (var_battle_random <= c.getInt(6) * (c.getInt(11) / 100)) {
            if (var_battle_random <= (obj_player.getAsInteger("stamina")+1) * (obj_player.getAsInteger("speed")/ obj_monster.getAsInteger("speed"))*0.9) {
                //玩家回合
//                var_battle_damage=(obj_player.getAsInteger("atk")*1.3-obj_monster.getAsInteger("def")).toInt()

                var_battle_damage=(obj_player.getAsInteger("atk")*2-obj_monster.getAsInteger("def"))
                var_battle_damage-=((obj_player.getAsInteger("maxstamina")-obj_player.getAsInteger("stamina"))/10).toInt()
                if(var_battle_damage<=0){
                    var_battle_damage=1
                }
                battle_log.append(Html.fromHtml(
                    "<font color=${Color.WHITE}>"+var_battle_round+".&nbsp;</font>"
                                +"<font color=${Color.CYAN}>" + obj_player.get("name") + "&nbsp;</font>"
                                + "<font color=${Color.WHITE}>對&nbsp;</font>"
                                + "<font color=${Color.RED}>" + obj_monster.get("name") + "&nbsp;</font>"
                                + "<font color=${Color.WHITE}>使出&nbsp;</font>" + "<font color=${Color.YELLOW}>" + "攻擊" + "&nbsp;</font>"
                                + "<font color=${Color.WHITE}>造成</font>" + "<font color=${Color.WHITE}>" + var_battle_damage + "點傷害<br></font>",
                        Html.FROM_HTML_MODE_LEGACY))
                var_use_stamina=(var_battle_damage/30).toInt()+1
                if (obj_player.getAsInteger("stamina")>=var_use_stamina){
                    obj_player.put("stamina",obj_player.getAsInteger("stamina")-var_use_stamina)//攻擊扣除體力
                }
                else{
                    obj_player.put("stamina",0)
                }

                if(obj_monster.getAsInteger("hp")>=var_battle_damage){
//                    傷害結算
                    obj_monster.put("hp",obj_monster.getAsInteger("hp")-var_battle_damage)
                }
                else{
                    obj_monster.put("hp",0)
                }



//                    val snackbar = Snackbar.make(findViewById(R.id.battle_layout), "123", Snackbar.LENGTH_SHORT)
//                    snackbar.show()
//                    Snackbar.make(findViewById(R.id.battle_layout), "戰鬥勝利 "+var_battle_reward, Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(applicationContext, "戰鬥勝利 "+var_battle_reward, Toast.LENGTH_SHORT).show()
//                    toast部分裝置無法顯示



            }

            else {
                //怪物回合
                battle_log.append(Html.fromHtml("<font color=${Color.WHITE}>"+var_battle_round+".&nbsp;</font>"
                            +"<font color=${Color.RED}>" + obj_monster.get("name") + "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>對&nbsp;</font>"
                            + "<font color=${Color.CYAN}>" + obj_player.get("name") + "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>使出&nbsp;</font>" + "<font color=${Color.YELLOW}>" + "攻擊" + "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>造成</font>" + "<font color=${Color.WHITE}>" + 3 + "點傷害<br></font>",
                    Html.FROM_HTML_MODE_LEGACY))
            }


        }
        kotlin.run {
            if(obj_player.getAsInteger("hp")>0){
//                玩家獲勝
                obj_player.put("exp",obj_player.getAsInteger("exp")+obj_monster.getAsInteger("exp"))//戰鬥經驗
                battle_log.append(Html.fromHtml(
                    "<font color=${Color.WHITE}>戰鬥勝利，獲得&nbsp;</font>"
                            + "<font color=${Color.GREEN}>exp+"+obj_monster.getAsInteger("exp")+"</font>"
                            + "<font color=${Color.WHITE}>,"+var_battle_reward+"</font>",
                    Html.FROM_HTML_MODE_LEGACY))
            }
            val newRow=ContentValues()
            newRow.put("hp",obj_player.getAsInteger("hp"))
            newRow.put("stamina",obj_player.getAsInteger("stamina"))
            newRow.put("exp",obj_player.getAsInteger("exp"))
            MyDB.update(DB_TABLE,newRow,"id='1'",null)
            player_status_change()
            newRow.clear()
            obj_player.clear()
            obj_monster.clear()
            c.close()
            cursor.close()
        }


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
    private val Activity_rest=View.OnClickListener {
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
        val newRow=ContentValues()
        var var_heal_hp_add:Int=3
        var var_heal_hp:Int
        var var_heal_stamina_add:Int=5
        var var_heal_stamina:Int
        if(obj_player.getAsInteger("hp")+var_heal_hp_add<=obj_player.getAsInteger("maxhp")){
            var_heal_hp=obj_player.getAsInteger("hp")+var_heal_hp_add
        }
        else{
            var_heal_hp=obj_player.getAsInteger("maxhp")
        }
        if(obj_player.getAsInteger("stamina")+var_heal_stamina_add<=obj_player.getAsInteger("maxstamina")){
            var_heal_stamina=obj_player.getAsInteger("stamina")+var_heal_stamina_add
        }
        else{
            var_heal_stamina=obj_player.getAsInteger("maxstamina")
        }
        newRow.put("hp",var_heal_hp)
        newRow.put("stamina",var_heal_stamina)
        MyDB.update(DB_TABLE,newRow,"id='1'",null)
        player_status_change()
        c.close()
    }

    /////////
    override fun onBackPressed() {
//        封鎖返回鍵
        moveTaskToBack(false)
    }
}

