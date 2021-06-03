package br.alex.contaspagar.view

import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.alex.contaspagar.ClickItemListaDocumentosListener
import br.alex.contaspagar.ListaDocumentosAdapter
import br.alex.contaspagar.R
import br.alex.contaspagar.application.ContaPagarApplication
import br.alex.contaspagar.model.DocumentoVO
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ClickItemListaDocumentosListener  {

    private val rcvListaDocumentos: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.rcvListaDocumentos)
    }
    private val adapter = ListaDocumentosAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setClickListeners()
        bindViews()
        updateList()
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun setClickListeners() {
        fabIncluir.setOnClickListener { onClickIncluir() }
    }

    private fun onClickIncluir() {
        val documento: DocumentoVO = DocumentoVO(-1, "", "", 0.0, "", "", "")
        editaDocumento( documento )
    }

    private fun bindViews() {
        rcvListaDocumentos.adapter = adapter
        rcvListaDocumentos.layoutManager = LinearLayoutManager(this)
    }

    private fun updateList() {
        var lista: List<DocumentoVO> = mutableListOf()
        Thread(Runnable {
            try {
                lista = ContaPagarApplication.instance.helperDB?.obtemDocumentos("") ?: mutableListOf()
            } catch (ex: Exception) {
                ex.printStackTrace()
                runOnUiThread {
                    Toast.makeText(baseContext, getString(R.string.MsgErroObterDados), Toast.LENGTH_LONG).show()
                }
            }
            runOnUiThread {
                adapter.updateList(lista)
            }
        }).start()
    }

    override fun onClickItemListaDocumentos(documento: DocumentoVO) {
        editaDocumento( documento )
    }

    fun editaDocumento(documento: DocumentoVO) {
        val intent = Intent(this, DocumentoActivity::class.java)
        intent.putExtra( "Documento", documento )
        startActivity(intent)
    }

}