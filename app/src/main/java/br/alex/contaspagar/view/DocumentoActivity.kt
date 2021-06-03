package br.alex.contaspagar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import androidx.core.view.isVisible
import br.alex.contaspagar.R
import br.alex.contaspagar.application.ContaPagarApplication
import br.alex.contaspagar.model.DocumentoVO
import kotlinx.android.synthetic.main.documento.*
import android.icu.text.DateFormat
import android.os.Build
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.alex.contaspagar.CalendarioFragment
import java.util.*
import kotlin.Exception

class DocumentoActivity : AppCompatActivity() {

    private var documento: DocumentoVO? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.documento)

        getExtras()
        bindViews()
        setListeners()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setListeners() {
        btnSalvar.setOnClickListener() {
            onClickSalvar()
        }
        btnExcluir.setOnClickListener() {
            onClickExcluir()
        }
        txtEmissao.setOnClickListener() {
            onClickEditarData(R.id.txtEmissao,getString(R.string.TituloEdicaoDataEmissao) )
        }
        txtVencimento.setOnClickListener() {
            onClickEditarData(R.id.txtVencimento, getString(R.string.TituloEdicaoDataVencimento) )
        }
        txtPagamento.setOnClickListener() {
            onClickEditarData(R.id.txtPagamento, getString(R.string.TituloEdicaoDataPagamento) )
        }
        imgLimparEmissao.setOnClickListener() {
            txtEmissao.setText("")
        }
        imgLimparVencimento.setOnClickListener() {
            txtVencimento.setText("")
        }
        imgLimparPagamento.setOnClickListener() {
            txtPagamento.setText("")
        }
    }

    private fun onClickEditarData(editViewOrigem: Int, titulo: String) {
        val fragment = CalendarioFragment.newInstance(editViewOrigem, titulo)
        fragment.show(supportFragmentManager, "dialog")
    }

    private fun onClickExcluir() {
        Thread( Runnable {
            try {
                ContaPagarApplication.instance.helperDB?.excluiDocumento(documento?.id!!)
                finish()
            } catch (ex: Exception) {
                runOnUiThread {
                    Toast.makeText( this, getString(R.string.MsgErroExclusaoDocumento), Toast.LENGTH_SHORT).show()
                }
            }
        }).start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onClickSalvar() {
        documento?.descricao = edtDescricaoDoc.text.toString()
        documento?.terceiro = edtTerceiroDoc.text.toString()
        documento?.valor = edtValorDoc.text.toString().toDouble()
        documento?.emissao = txtEmissao.text.toString()
        documento?.vencimento = txtVencimento.text.toString()
        documento?.pagamento = txtPagamento.text.toString()
        Thread( Runnable {
            try {
                ContaPagarApplication.instance.helperDB?.salvaDocumento(documento!!)
                finish()
            } catch (ex: Exception) {
                runOnUiThread {
                    Toast.makeText( this, getString(R.string.MsgErroSalvarDocumento), Toast.LENGTH_LONG).show()
                }
            }
        }).start()
    }

    private fun bindViews() {
        if (documento?.id == -1) {
            tbrDocumento.setTitle(getString(R.string.TituloToolbarIncluirDocumento))
            btnExcluir.isVisible = false
        } else {
            tbrDocumento.setTitle(getString(R.string.TituloToolbarEditarDocumento))
            btnExcluir.isVisible = true
        }
        edtDescricaoDoc.setText(documento?.descricao)
        edtTerceiroDoc.setText(documento?.terceiro)
        edtValorDoc.setText(documento?.valor.toString())
        txtEmissao.setText(documento?.emissao)
        txtVencimento.setText(documento?.vencimento)
        txtPagamento.setText(documento?.pagamento)
    }

    private fun getExtras() {
        documento = intent.getParcelableExtra("Documento")
    }

}