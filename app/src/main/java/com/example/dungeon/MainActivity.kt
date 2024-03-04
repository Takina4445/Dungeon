package com.example.dungeon

//import android.annotation.SuppressLint
//import android.app.NotificationManager
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
//import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Build.VERSION_CODES.N
//import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.provider.Settings
import android.text.Html
//import android.text.method.ScrollingMovementMethod
//import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
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
    private lateinit var button_place_1:Button
    private lateinit var button_place_2:Button
    private lateinit var button_place_3:Button
    private lateinit var button_next_level:Button
    lateinit var imageview_EnemyPic:ImageView
    val DB_FILE = "doungeon.db"
    val DB_TABLE = "player"
    val DB_TABLE_MONSTER = "monster"
    val DB_TABLE_SKILLS="skills"
    val DB_TABLE_USE_SKILL="useskill"
    val DB_TABLE_ITEM="item"
    var public_var_dungeon_place:Int=1
    var public_var_dungeon_level:Int=1
    var public_var_has_won:Boolean=false//是否戰鬥勝利過


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
        val button_place_1:Button=findViewById<Button>(R.id.button_place1)
        val button_place_2:Button=findViewById<Button>(R.id.button_place2)
        val button_place_3:Button=findViewById<Button>(R.id.button_place3)
        val button_next_level:Button=findViewById<Button>(R.id.button_next_level)
        imageview_EnemyPic=findViewById<ImageView>(R.id.imageview_EnemyPic)
        Image_Place_Change()
        Text_Place_Change()
        ///
        button_main.setOnClickListener(ActivityChange_main)
        button_item.setOnClickListener(ActivityChange_item)
        button_food.setOnClickListener(ActivityChange_food)
        button_battle.setOnClickListener(Activity_battle)
        button_rest.setOnClickListener(Activity_rest)
        button_place_1.setOnClickListener(ActivityChange_Place_1)
        button_place_2.setOnClickListener(ActivityChange_Place_2)
        button_place_3.setOnClickListener(ActivityChange_Place_3)
        button_next_level.setOnClickListener(ActivityChange_next_level)
        player_status_change()//刷新玩家資料
        val battle_log:TextView=findViewById<TextView>(R.id.textview_battleprocess)//戰鬥紀錄
        battle_log.text= ""
        public_var_dungeon_place=1
        public_var_dungeon_level=1
        public_var_has_won=false//是否戰鬥勝利過

    }
    ////////////////////
    private fun Image_Place_Change(){
        when(public_var_dungeon_place){
            1->{
                imageview_EnemyPic.setImageResource(R.drawable.place_1)
            }
            2->{
                imageview_EnemyPic.setImageResource(R.drawable.place_2)
            }
            3->{
                imageview_EnemyPic.setImageResource(R.drawable.place_3)
            }
        }
    }
    private val ActivityChange_Place_1= View.OnClickListener{
        //切換至主place1
        public_var_dungeon_place=1
        public_var_dungeon_level=1
        public_var_has_won=false
        Image_Place_Change()
        Text_Place_Change()
    }

    private val ActivityChange_Place_2= View.OnClickListener{
        //切換至主place1
        public_var_dungeon_place=2
        public_var_dungeon_level=1
        public_var_has_won=false
        Image_Place_Change()
        Text_Place_Change()
    }
    private val ActivityChange_Place_3= View.OnClickListener{
        //切換至主place1
        public_var_dungeon_place=3
        public_var_dungeon_level=1
        public_var_has_won=false
        Image_Place_Change()
        Text_Place_Change()

    }
    private val ActivityChange_next_level=View.OnClickListener {
        if (public_var_has_won==true){
            public_var_dungeon_level+=1
            public_var_has_won=false
            Text_Place_Change()
            Image_Place_Change()
        }
        else{
            AlertDialog.Builder(this)  //參數放要傳入的 MainActivity Context
                .setMessage("打贏當前層數才能去下一關")
                .setPositiveButton("確認",null)
                .create()
                .show()
        }
    }
    private fun Text_Place_Change(){
        var place_status=findViewById<TextView>(R.id.textview_EnemyStatus)
        var place_name:String=""
        when(public_var_dungeon_place){
            1->{
                place_name="巴別塔"
            }
            2->{
                place_name="精靈樹"
            }
            3->{
                place_name="龍之巢穴"
            }
        }
        place_status.text=Html.fromHtml(
            "<font color=${Color.CYAN}>"+place_name+"&nbsp;</font>"
                    +"<font color=${Color.CYAN}>第"+public_var_dungeon_level+"層</font>"
            ,Html.FROM_HTML_MODE_LEGACY)
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
                    Cursor.FIELD_TYPE_FLOAT -> {
                        val floatValue = c.getDouble(i)
                        obj_player.put(columnName, floatValue)
                    }
                }
            }
        }while (c.moveToNext())
        return obj_player
    }
    ///////////////////
    fun Cursor_to_HashMap(cursor: Cursor): HashMap<String, Double> {
        val hashMap = HashMap<String, Double>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
//            val columnCount = cursor.columnCount
//            for (i in 0 until columnCount) {
                val columnName = cursor.getString(0)
                hashMap.put(columnName,cursor.getDouble(1))
//            }
            cursor.moveToNext()
        }
        return hashMap
    }
    ///////////////////
    fun monster_level_count(cursor:ContentValues,obj_level:Int):ContentValues{
        var var_level_cursor:ContentValues=cursor
        var level:Double=obj_level.toDouble()
        var_level_cursor.put("hp",monster_level_count_put("hp",var_level_cursor,level))
        var_level_cursor.put("maxhp",monster_level_count_put("maxhp",var_level_cursor,level))
        var_level_cursor.put("stamina",monster_level_count_put("stamina",var_level_cursor,level))
        var_level_cursor.put("maxstamina",monster_level_count_put("maxstamina",var_level_cursor,level))
        var_level_cursor.put("atk",monster_level_count_put("atk",var_level_cursor,level))
        var_level_cursor.put("def",monster_level_count_put("def",var_level_cursor,level))
        var_level_cursor.put("speed",monster_level_count_put("speed",var_level_cursor,level))
        var_level_cursor.put("exp",monster_level_count_put("exp",var_level_cursor,level))
        var_level_cursor.put("money",monster_level_count_put("money",var_level_cursor,level))
        return var_level_cursor
    }
    ///////////////////

    private fun monster_level_count_put(key: String, cursor: ContentValues, level: Double): Int{
        var var_pow_per_level:Double=1.1
        return ((cursor.getAsInteger(key)+1)*Math.pow(var_pow_per_level, (level-1.0)) + level-2).toInt()
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
    private fun monster_status_change(obj_monster:ContentValues,level:Int){
        val textview_monster_status:TextView=findViewById<TextView>(R.id.textview_EnemyStatus)
        textview_monster_status.text=Html.fromHtml(
            "<font color=${Color.RED}>"+obj_monster.getAsString("name")+"<br></font>"
                    +"<font color=${Color.RED}>Lv.</font>"
                    +"<font color=${Color.WHITE}>"+level+"<br></font>"
                    +"<font color=${Color.RED}>生命:</font>"
                    +"<font color=${Color.WHITE}>"+obj_monster.getAsInteger("hp")+"/"+obj_monster.getAsInteger("maxhp")+"<br></font>"
                    +"<font color=${Color.RED}>體力:</font>"
                    +"<font color=${Color.WHITE}>"+obj_monster.getAsInteger("stamina")+"/"+obj_monster.getAsInteger("maxstamina")+"<br></font>"
                    +"<font color=${Color.RED}>攻擊力:</font>"
                    +"<font color=${Color.WHITE}>"+obj_monster.getAsInteger("atk")+"<br></font>"
                    +"<font color=${Color.RED}>防禦力:</font>"
                    +"<font color=${Color.WHITE}>"+obj_monster.getAsInteger("def")+"<br></font>"
                    +"<font color=${Color.RED}>速度:</font>"
                    +"<font color=${Color.WHITE}>"+obj_monster.getAsInteger("speed")+"<br></font>"
                    +"<font color=${Color.RED}>掉落exp:</font>"
                    +"<font color=${Color.WHITE}>"+obj_monster.getAsInteger("exp")+"<br></font>"
                    +"<font color=${Color.RED}>掉落金錢:</font>"
                    +"<font color=${Color.WHITE}>"+obj_monster.getAsInteger("money")+"<br></font>"
            ,Html.FROM_HTML_MODE_LEGACY)


    }
    private fun monster_level_select():Int{
        //怪物等級
        return (Math.pow(3.5,(public_var_dungeon_place.toDouble()-1.0))*(public_var_dungeon_place)+((public_var_dungeon_level/3 ).toInt()*2.0)).toInt()
    }

    fun Check_Player_Level_Up(obj_player:ContentValues):ContentValues{
        var exp:Int
        var level:Int
        var level_up_need_exp:Double
        var obj_player_new:ContentValues
        obj_player_new=obj_player
        do{

            exp=obj_player_new.getAsInteger("exp")
            level=obj_player_new.getAsInteger("level")
            level_up_need_exp=(20*Math.pow(1.2,(level-1.0)))
            if(exp>=level_up_need_exp){

                obj_player_new.put("level",obj_player_new.getAsInteger("level")+1)
                obj_player_new.put("exp",obj_player_new.getAsInteger("exp")-level_up_need_exp)
                obj_player_new=Player_Level_Up(obj_player_new,level+1)
            }
        }while(exp>=level_up_need_exp)
        return  obj_player_new

    }
    fun Player_Level_Up(obj_player: ContentValues,level: Int):ContentValues{
        var var_level_cursor:ContentValues=obj_player
        var level:Double=level.toDouble()
        var maxhp=player_level_count_put("maxhp",var_level_cursor,level)
        var maxstamina=player_level_count_put("maxstamina",var_level_cursor,level)
        /*
        //數值崩壞
        var_level_cursor.put("maxhp",maxhp)
        var_level_cursor.put("hp",maxhp)//血量同時恢復到最大值
        var_level_cursor.put("maxstamina",maxstamina)
        var_level_cursor.put("stamina",maxstamina)//體力同時恢復到最大值
        var_level_cursor.put("atk",player_level_count_put("atk",var_level_cursor,level))
        var_level_cursor.put("def",player_level_count_put("def",var_level_cursor,level))
        var_level_cursor.put("speed",player_level_count_put("speed",var_level_cursor,level))
        */
        var_level_cursor.put("maxhp",var_level_cursor.getAsInteger("maxhp")+5)
        var_level_cursor.put("hp",var_level_cursor.getAsInteger("maxhp"))//血量同時恢復到最大值
        var_level_cursor.put("maxstamina",var_level_cursor.getAsInteger("maxstamina")+5)
        var_level_cursor.put("stamina",var_level_cursor.getAsInteger("maxstamina"))//體力同時恢復到最大值
        var_level_cursor.put("atk",var_level_cursor.getAsInteger("atk")+2)
        var_level_cursor.put("def",var_level_cursor.getAsInteger("def")+1)
        var_level_cursor.put("speed",var_level_cursor.getAsInteger("speed")+2)
        return var_level_cursor
    }

    ///////////////////

    private fun player_level_count_put(key: String, cursor: ContentValues, level: Double): Int{
        var var_pow_per_level:Double=1.1
        return ((cursor.getAsInteger(key)+1)*Math.pow(var_pow_per_level, (level-1.0)) + level-2).toInt()
    }
    fun Is_Die(){
        val button_battle:Button=findViewById<Button>(R.id.button_battle)
        button_battle.isEnabled=false
    }
    fun Is_Heal(){
        val button_battle:Button=findViewById<Button>(R.id.button_battle)
        button_battle.isEnabled=true
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
        if(obj_player.getAsInteger("hp")<=0){
            AlertDialog.Builder(this)  //參數放要傳入的 MainActivity Context
                .setMessage("老哥，你已經死了")
                .setPositiveButton("確認",null)
                .create()
                .show()
            Is_Die()
        }else{
//怪物選擇
        val selection = "id = ?"
        val selectionArgs = arrayOf(Random.nextInt(1, 3).toString())
        val cursor = MyDB.query(DB_TABLE_MONSTER,null ,selection, selectionArgs, null, null, null)
//        val columnName = "*"
//        arrayOf(columnName)

        cursor.moveToFirst()
//        val imageview_EnemyPic:ImageView=findViewById<ImageView>(R.id.imageview_EnemyPic)
        var obj_monster :ContentValues = Cursor_To_ContentValues(cursor)
        val resourceId = resources.getIdentifier(obj_monster.getAsString("picture"), "drawable", packageName)
        imageview_EnemyPic.setImageResource(resourceId)
        ////
        //怪物等級
        var var_monster_level=monster_level_select()

        obj_monster=monster_level_count(obj_monster,var_monster_level)
        monster_status_change(obj_monster,var_monster_level)
        ////
        var db_table="monster LEFT JOIN useskill ON monster.name = useskill.user_name LEFT JOIN skills ON useskill.skill_name = skills.skill_name"
        var db_columns= arrayOf("skills.skill_name","skills.skill_damage_multiplier")

        val skill_cursor = MyDB.query(db_table,db_columns ,selection, selectionArgs, null, null, null)
//        var obj_skill_list:ContentValues=Cursor_To_ContentValues(skill_cursor)
        var obj_skill_list=Cursor_to_HashMap(skill_cursor)
        obj_skill_list.put("攻擊",1.3)
//        val monsterName =obj_monster.get("name")
//        battle_log.text=monsterName
        var var_battle_damage:Int//戰鬥傷害
        var var_battle_skill_multiplier:Double
        var var_totalweight:Int
        var var_battle_random:Int
        var var_use_stamina:Int//體力消耗
        var var_battle_reward:String=""//戰鬥獎勵
        var var_battle_round:Int=0
        var var_monster_weaken:Double=1.0//怪獸傷害削弱

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
                val keys = obj_skill_list.keys.toList() // 取得所有鍵集合並轉為列表
                val randomKey = keys[Random.nextInt(keys.size)] // 隨機選取一個鍵
                var_battle_damage=(obj_monster.getAsInteger("atk")* obj_skill_list.get(randomKey)!!).toInt()
                var_battle_damage=(var_battle_damage/var_monster_weaken-obj_player.getAsInteger("def")).toInt()
                var_battle_damage*=((obj_monster.getAsInteger("maxstamina")-obj_monster.getAsInteger("stamina"))/20.0+1).toInt()
                if(var_battle_damage<=0){
                    var_battle_damage=1
                }
                var_use_stamina=(var_battle_damage/30).toInt()+1
                if (obj_monster.getAsInteger("stamina")>=var_use_stamina){
                    obj_monster.put("stamina",obj_monster.getAsInteger("stamina")-var_use_stamina)//攻擊扣除體力
                }
                else{
                    obj_monster.put("stamina",0)
                }
                if(obj_player.getAsInteger("hp")>=var_battle_damage){
//                    傷害結算
                    obj_player.put("hp",obj_player.getAsInteger("hp")-var_battle_damage)
                }
                else{
                    obj_player.put("hp",0)
                }

                battle_log.append(Html.fromHtml("<font color=${Color.WHITE}>"+var_battle_round+".&nbsp;</font>"
                            +"<font color=${Color.RED}>" + obj_monster.get("name") + "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>對&nbsp;</font>"
                            + "<font color=${Color.CYAN}>" + obj_player.get("name") + "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>使出&nbsp;</font>" + "<font color=${Color.YELLOW}>" + randomKey+ "&nbsp;</font>"
                            + "<font color=${Color.WHITE}>造成</font>" + "<font color=${Color.WHITE}>" + var_battle_damage + "點傷害<br></font>",
                    Html.FROM_HTML_MODE_LEGACY))
            }


        }
        kotlin.run {
            if (obj_player.getAsInteger("hp") > 0) {
//                玩家獲勝
                obj_player.put(
                    "exp",
                    obj_player.getAsInteger("exp") + obj_monster.getAsInteger("exp")
                )//戰鬥經驗
                obj_player.put(
                    "money",
                    obj_player.getAsInteger("money") + obj_monster.getAsInteger("money")
                )//戰鬥金幣
                battle_log.append(
                    Html.fromHtml(
                        "<font color=${Color.WHITE}>戰鬥勝利，獲得&nbsp;</font>"
                                + "<font color=${Color.GREEN}>exp+" + obj_monster.getAsInteger("exp") + "</font>"
                                + "<font color=${Color.WHITE}>,</font>"
                                + "<font color=${Color.YELLOW}>金幣+" + obj_monster.getAsInteger("money") + "</font>"
//                            + "<font color=${Color.WHITE}>,"+var_battle_reward+"</font>"
                        //戰鬥掉落物
                        , Html.FROM_HTML_MODE_LEGACY
                    )
                )
                obj_player=Check_Player_Level_Up(obj_player)
                public_var_has_won=true//勝利過了
            } else {
                battle_log.append(
                    Html.fromHtml(
                        "<font color=${Color.RED}>戰鬥失敗，失去...</font>",
                        Html.FROM_HTML_MODE_LEGACY
                    )
                )
                Is_Die()
            }
            val newRow = ContentValues()
            newRow.put("exp", obj_player.getAsInteger("exp"))
            newRow.put("level", obj_player.getAsInteger("level"))
            newRow.put("hp", obj_player.getAsInteger("hp"))
            newRow.put("maxhp", obj_player.getAsInteger("maxhp"))
            newRow.put("stamina", obj_player.getAsInteger("stamina"))
            newRow.put("maxstamina", obj_player.getAsInteger("maxstamina"))
            newRow.put("atk", obj_player.getAsInteger("atk"))
            newRow.put("def", obj_player.getAsInteger("def"))
            newRow.put("speed", obj_player.getAsInteger("speed"))
            newRow.put("money", obj_player.getAsInteger("money"))
            MyDB.update(DB_TABLE, newRow, "id='1'", null)
            player_status_change()
            newRow.clear()
            obj_player.clear()
            obj_monster.clear()
            c.close()
            cursor.close()
        }
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
        val obj_player:ContentValues=Cursor_To_ContentValues(c)

//        將玩家數值存入

        c.moveToFirst()
        val newRow=ContentValues()
        var var_heal_hp_add:Int
        var var_heal_hp:Int
        var var_heal_stamina_add:Int
        var var_heal_stamina:Int
        var var_rest_need_money=0//休息消耗金幣 改比率
        if(obj_player.getAsInteger("money")>=var_rest_need_money){
            var_heal_hp_add=((obj_player.getAsInteger("maxhp")-obj_player.getAsInteger("hp"))*0.4+1).toInt()
            var_heal_stamina_add=((obj_player.getAsInteger("maxstamina")-obj_player.getAsInteger("stamina"))*0.4+1).toInt()
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
            newRow.put("money",obj_player.getAsInteger("money")-var_rest_need_money)
            MyDB.update(DB_TABLE,newRow,"id='1'",null)

            Is_Heal()
        }
        else{
            AlertDialog.Builder(this)  //參數放要傳入的 MainActivity Context
                .setMessage("您的金幣不足"+var_rest_need_money+"")
                .setPositiveButton("確認",null)
                .create()
                .show()
        }
        c.close()
        player_status_change()
    }

    /////////
    override fun onBackPressed() {
//        封鎖返回鍵
        moveTaskToBack(false)
    }
}

