package br.alex.contaspagar.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import br.alex.contaspagar.model.DocumentoVO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HelperDB(
    context: Context?
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {

    companion object {
        private val NOME_BANCO = "cpagar.db"
        private val VERSAO_ATUAL = 3
    }

    //Tabela Documento
    val TABLE_NAME_DOC    = "documento"
    val COLUMN_ID         = "id"
    val COLUMN_TERCEIRO   = "terceiro"
    val COLUMN_DESCRICAO  = "descricao"
    val COLUMN_VALOR      = "valor"
    val COLUMN_EMISSAO    = "emissao"
    val COLUMN_VENCIMENTO = "vencimento"
    val COLUMN_PAGAMENTO  = "pagamento"
    val DROP_TABLE_DOC    = "DROP TABLE IF EXISTS $TABLE_NAME_DOC"
    val CREATE_TABLE_DOC  = "CREATE TABLE $TABLE_NAME_DOC (" +
                            "$COLUMN_ID INTEGER NOT NULL," +
                            "$COLUMN_TERCEIRO TEXT NOT NULL," +
                            "$COLUMN_DESCRICAO TEXT NOT NULL," +
                            "$COLUMN_VALOR REAL NOT NULL," +
                            "$COLUMN_EMISSAO TEXT NOT NULL," +
                            "$COLUMN_VENCIMENTO TEXT NOT NULL," +
                            "$COLUMN_PAGAMENTO TEXT," +
                            "" +
                            "PRIMARY KEY($COLUMN_ID AUTOINCREMENT)" +
                            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_DOC)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL(DROP_TABLE_DOC)
        }
        onCreate(db)
    }

    fun obtemDocumentos(descricao: String): List<DocumentoVO> {
        val db = readableDatabase ?: return mutableListOf()
        var cursor = db.query(TABLE_NAME_DOC,
                              arrayOf("id","terceiro","descricao","valor","emissao","vencimento","pagamento"),
                              "$COLUMN_DESCRICAO LIKE ?",
                              arrayOf("%$descricao%"),"","","")
        if (cursor == null) {
            db.close()
            return mutableListOf()
        }
        var lista = mutableListOf<DocumentoVO>()
        while (cursor.moveToNext()) {
            val documento = DocumentoVO(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TERCEIRO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DESCRICAO)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_VALOR)),
                cursor.getString(cursor.getColumnIndex(COLUMN_EMISSAO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_VENCIMENTO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PAGAMENTO))
            )
            lista.add(documento)
        }
        db.close()
        return lista
    }

    fun salvaDocumento(documento: DocumentoVO) {
        val db = writableDatabase ?: return
        var content = ContentValues()
        content.put(COLUMN_TERCEIRO,documento.terceiro)
        content.put(COLUMN_DESCRICAO,documento.descricao)
        content.put(COLUMN_VALOR,documento.valor)
        content.put(COLUMN_EMISSAO,documento.emissao)
        content.put(COLUMN_VENCIMENTO,documento.vencimento)
        content.put(COLUMN_PAGAMENTO,documento.pagamento)
        if (documento.id == -1) {
            db.insert(TABLE_NAME_DOC, null, content)
        } else {
            db.update(TABLE_NAME_DOC,content,"ID = ?", arrayOf("${documento.id}"))
        }
        db.close()
    }

    fun excluiDocumento(id: Int) {
        val db = writableDatabase ?: return
        db.delete(TABLE_NAME_DOC, "ID = ?", arrayOf("$id"))
        db.close()
    }
}