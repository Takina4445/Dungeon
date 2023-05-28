package com.example.dungeon

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        const val DB_TABLE = "player"
    }

    // 建立應用程式需要的表格
    override fun onCreate(db: SQLiteDatabase) {
        createPlayerTable(db)
        insertPlayerData(db)
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
            newRow.put("stamina", 10)
            newRow.put("atk", 1)
            newRow.put("def", 0)
            newRow.put("money", 0)
            db.insert(DB_TABLE, null, newRow)
        }
    }
}
