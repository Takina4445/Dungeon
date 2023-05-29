package com.example.dungeon

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        const val DB_TABLE = "player"
        const val DB_TABLE_MONSTER = "monster"
        const val DB_TABLE_ITEM="item"
    }

    // 建立應用程式需要的表格
    override fun onCreate(db: SQLiteDatabase) {
        createPlayerTable(db)
        insertPlayerData(db)
        createMonsterTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVer: Int, newVer: Int) {
        // TODO Auto-generated method stub
    }

    private fun createPlayerTable(db: SQLiteDatabase) {
        val createTableCommand = "CREATE TABLE IF NOT EXISTS $DB_TABLE (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "exp INT," +
                "hp INT," +
                "stamina INT," +
                "atk INT," +
                "def INT," +
                "money INT)"

        db.execSQL(createTableCommand)
        //get(0):id
        //get(1):"name"
        //get(2):"exp"
        //get(3):"hp"
        //get(4):"stamina"
        //get(5):"atk"
        //get(6):"def"
        //get(7):"money"
    }
    private fun createMonsterTable(db: SQLiteDatabase) {
        val createTableCommand = "CREATE TABLE IF NOT EXISTS $DB_TABLE (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "exp INT," +
                "hp INT," +
                "stamina INT," +
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
            newRow.put("hp", 10)
            newRow.put("stamina", 11)
            newRow.put("atk", 1)
            newRow.put("def", 0)
            newRow.put("money", 0)
            db.insert(DB_TABLE, null, newRow)
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