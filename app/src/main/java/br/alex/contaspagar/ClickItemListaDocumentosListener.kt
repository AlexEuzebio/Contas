package br.alex.contaspagar

import br.alex.contaspagar.model.DocumentoVO

interface ClickItemListaDocumentosListener {
    fun onClickItemListaDocumentos(documento: DocumentoVO)
}