package br.alex.contaspagar.application

import android.app.Application
import br.alex.contaspagar.helper.HelperDB

class ContaPagarApplication: Application() {

    var helperDB: HelperDB? = null
        private set

    companion object {
        lateinit var instance: ContaPagarApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }

}