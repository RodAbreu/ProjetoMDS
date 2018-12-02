package com.example.rodrigo.projetomds

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DatabaseHandler.DB_NAME, null, DatabaseHandler.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                ID + " INTEGER PRIMARY KEY," +
                NOME + " TEXT," +
                QUANT + " TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "Estoque"
        private val TABLE_NAME = "Produto"
        private val ID = "Id"
        private val NOME = "Nome"
        private val QUANT = "Quantidade"
    }
    fun addProduto(produto: ProdutoClass): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOME, produto.nome)
        values.put(QUANT, produto.quantidade)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }
    fun getProduto(_id: Int): ProdutoClass{
        val produto = ProdutoClass()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                produto.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                produto.nome = cursor.getString(cursor.getColumnIndex(NOME))
                produto.quantidade = Integer.parseInt(cursor.getString(cursor.getColumnIndex(QUANT)))
            }
        }
        cursor.close()
        return produto
    }

}
