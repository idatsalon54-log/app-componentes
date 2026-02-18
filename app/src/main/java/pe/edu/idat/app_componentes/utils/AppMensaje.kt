package pe.edu.idat.app_componentes.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import pe.edu.idat.app_componentes.R

object AppMensaje {

    fun enviarMensaje(vista: View,
                      mensaje: String,
                      tipo: TipoMensaje){
        val snackbar = Snackbar.make(vista,
            mensaje, Snackbar.LENGTH_LONG)
        if(tipo == TipoMensaje.ERROR){
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    MiApp.instance, R.color.error))
        }else{
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    MiApp.instance, R.color.success))
        }
        snackbar.show()
    }

}