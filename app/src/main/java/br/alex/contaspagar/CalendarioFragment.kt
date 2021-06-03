package br.alex.contaspagar

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarioFragment: DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var cldData: CalendarView
    private lateinit var txtTitulo: TextView
    private var viewOrigem: Int = 0
    private var titulo: String = ""
    private var dataSelecionada: String = ""

    companion object {
        private const val EXTRA_VIEWORIGEM = "viewOrigem"
        private const val EXTRA_TITULO     = "titulo"
        fun newInstance(editViewOrigem: Int, titulo: String): CalendarioFragment {
            val bundle = Bundle()
            bundle.putInt(EXTRA_VIEWORIGEM, editViewOrigem)
            bundle.putString(EXTRA_TITULO, titulo)
            val calendarioFragment = CalendarioFragment()
            calendarioFragment.arguments = bundle
            return calendarioFragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity?.layoutInflater?.inflate(R.layout.calendario, null)
        cldData = view?.findViewById(R.id.cldCalendario) as CalendarView
        txtTitulo = view.findViewById(R.id.txtTituloCalendario) as TextView

        if (arguments != null) {
            viewOrigem = arguments?.getInt(EXTRA_VIEWORIGEM) as Int
            titulo = arguments?.getString(EXTRA_TITULO) as String
            txtTitulo.setText(titulo)
            var textViewOrigem = activity?.findViewById<TextView>(viewOrigem) as TextView

            setDataSelecionada(textViewOrigem.text.toString())
        }

        cldData.setOnDateChangeListener { view, year, month, dayOfMonth ->

            setDataSelecionada(dataDiaMesAno(dayOfMonth,month+1,year))
        }

        return AlertDialog.Builder(activity as Activity)
                .setView(view)
                .setPositiveButton(getString(R.string.TextoBotaoOKCalendario), this)
                .setNegativeButton(getString(R.string.TextoBotaoCancelarCalendario), this)
                .create()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDataSelecionada(data: String) {

        try {
            setDataSelecionadaConversao(data)
        } catch (ex: Exception ) {
            //Em caso de valor inválido no campo, assume data de hoje
            val dia = LocalDate.now().dayOfMonth
            val mes = LocalDate.now().monthValue
            val ano = LocalDate.now().year
            setDataSelecionadaConversao(dataDiaMesAno(dia,mes,ano))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDataSelecionadaConversao(data: String) {
        val simpleDf = SimpleDateFormat("dd/MM/yyyy")
        val dataLong = simpleDf.parse(data).time
        cldData.setDate(dataLong)

        val df = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dataDate = LocalDate.parse(data, df)
        dataSelecionada = dataDate.format(df)
    }

    private fun dataDiaMesAno(dia: Int, mes: Int, ano: Int): String {
        return dia.toString().trimStart().padStart(2, '0') + "/" +
               mes.toString().trimStart().padStart(2, '0') + "/" +
               ano.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(dialog: DialogInterface?, which: Int) {
        var view: View?
        if (which == BUTTON_POSITIVE) {
            view = activity?.findViewById<TextView>(viewOrigem)
            view?.setText(dataSelecionada)
        }
    }

}