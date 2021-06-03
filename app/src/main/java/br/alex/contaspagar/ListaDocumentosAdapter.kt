package br.alex.contaspagar

import android.icu.text.NumberFormat
import android.icu.text.NumberFormat.getNumberInstance
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import br.alex.contaspagar.model.DocumentoVO

class ListaDocumentosAdapter(var listener: ClickItemListaDocumentosListener) : RecyclerView.Adapter<ListaDocumentosAdapter.ListaDocumentosAdapterViewHolder>() {

    private val lista: MutableList<DocumentoVO> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaDocumentosAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listadocumentos_item, parent, false)
        return ListaDocumentosAdapterViewHolder(view, lista, listener)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ListaDocumentosAdapterViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun updateList(lista: List<DocumentoVO>) {
        this.lista.clear()
        this.lista.addAll(lista)
        notifyDataSetChanged()
    }

    class ListaDocumentosAdapterViewHolder(itemView: View, var lista: List<DocumentoVO>, var listener: ClickItemListaDocumentosListener) : RecyclerView.ViewHolder(itemView) {
        private val txtDescricao: TextView = itemView.findViewById(R.id.txtDescricao)
        private val txtVencimento: TextView = itemView.findViewById(R.id.txtVencimento)
        private val txtValor: TextView = itemView.findViewById(R.id.txtValor)

        init {
            itemView.setOnClickListener {
                listener.onClickItemListaDocumentos(lista[adapterPosition])
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(Documento: DocumentoVO) {
            txtDescricao.text = Documento.descricao
            txtVencimento.text = Documento.vencimento
            val nf = NumberFormat.getNumberInstance()
            nf.minimumFractionDigits = 2
            txtValor.text = nf.format(Documento.valor)
        }
    }

}