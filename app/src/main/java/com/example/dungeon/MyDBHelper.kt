package com.example.dungeon

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.Types.NULL

class MyDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        val DB_TABLE = "player"
        const val DB_TABLE_MONSTER = "monster"
        const val DB_TABLE_SKILLS="skills"
        const val DB_TABLE_USE_SKILL="useskill"
        const val DB_TABLE_ITEM="item"
    }

    // 建立應用程式需要的表格
    override fun onCreate(db: SQLiteDatabase) {
        createPlayerTable(db)
        insertPlayerData(db)
        createMonsterTable(db)
        insertMonsterData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVer: Int, newVer: Int) {
        // TODO Auto-generated method stub
    }

    private fun createPlayerTable(db: SQLiteDatabase) {
        val createTableCommand = "CREATE TABLE IF NOT EXISTS $DB_TABLE (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "exp INT," +
                "level INT," +
                "hp INT," +
                "maxhp INT," +
                "stamina INT," +
                "maxstamina INT," +
                "atk INT," +
                "def INT," +
                "money INT)"

        db.execSQL(createTableCommand)

    }
    private fun createMonsterTable(db: SQLiteDatabase) {
        val createTableCommand = "CREATE TABLE IF NOT EXISTS $DB_TABLE_MONSTER (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "picture TEXT NOT NULL,"+
                "exp INT," +
                "level INT," +
                "hp INT," +
                "maxhp INT," +
                "stamina INT," +
                "maxstamina INT," +
                "atk INT," +
                "def INT," +
                "money INT)"

        db.execSQL(createTableCommand)
        //get(0):id
        //get(1):"name"
        //get(2):"exp" 掉落經驗
        //get(3):"hp" 基礎血量
        //get(4):"stamina" 基礎體力
        //get(5):"atk" 基礎攻擊力
        //get(6):"def" 基礎防禦力
        //get(7):"money" 掉落金錢
    }
    private fun insertPlayerData(db: SQLiteDatabase) {
        val playerDataExistQuery = "SELECT COUNT(*) FROM $DB_TABLE"
        val cursor = db.rawQuery(playerDataExistQuery, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        if (count == 0) {
            val newRow = ContentValues()
            newRow.put("name", "無名的旅人")
            newRow.put("exp", 0)
            newRow.put("level",1)
            newRow.put("hp", 10)
            newRow.put("maxhp", 10)
            newRow.put("stamina", 11)
            newRow.put("maxstamina",11)
            newRow.put("atk", 1)
            newRow.put("def", 0)
            newRow.put("money", 0)
            db.insert(DB_TABLE, null, newRow)
        }
    }
    private fun insertMonsterData(db: SQLiteDatabase) {
//        怪物初始化
        val monster = "SELECT COUNT(*) FROM $DB_TABLE_MONSTER"
        val cursor = db.rawQuery(monster, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        if (count == 0) {
            //            老鼠戰士
            val newRow = ContentValues()
            newRow.put("name", "老鼠戰士")
            newRow.put("picture","mouse")
            newRow.put("exp", 1)
            newRow.put("level",1)
            newRow.put("hp", 10)
            newRow.put("maxhp", 10)
            newRow.put("stamina", 11)
            newRow.put("maxstamina",11)
            newRow.put("atk", 1)
            newRow.put("def", 0)
            newRow.put("money", 0)
            db.insert(DB_TABLE_MONSTER, null, newRow)
//野豬
            newRow.clear()
            newRow.put("name", "野豬")
            newRow.put("picture","wildboar")
            newRow.put("exp", 3)
            newRow.put("level",3)
            newRow.put("hp", 20)
            newRow.put("maxhp", 20)
            newRow.put("stamina", 11)
            newRow.put("maxstamina",11)
            newRow.put("atk", 3)
            newRow.put("def", 0)
            newRow.put("money", 0)
            db.insert(DB_TABLE_MONSTER, null, newRow)
        }
    }
    fun getPlayerData(): Cursor {
        val db = readableDatabase
        return db.query(DB_TABLE, null, null, null, null, null, null)
    }
    fun getMonsterData(): Cursor {
        val db = readableDatabase
        return db.query(DB_TABLE_MONSTER, null, null, null, null, null, null)
    }

}
