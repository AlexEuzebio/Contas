package br.alex.contaspagar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DocumentoVO(
    var id: Int,
    var terceiro: String,
    var descricao: String,
    var valor: Double,
    var emissao: String,
    var vencimento: String,
    var pagamento: String
): Parcelable
