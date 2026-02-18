package pe.edu.idat.app_componentes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.edu.idat.app_componentes.databinding.ActivityMainBinding
import pe.edu.idat.app_componentes.utils.AppMensaje
import pe.edu.idat.app_componentes.utils.TipoMensaje

class MainActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val listPreferencias = ArrayList<String>()
    private val listUsuario = ArrayList<String>()
    private var estadoCivil = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ArrayAdapter.createFromResource(
            this, R.array.estado_civil_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
            binding.spestadocivil.adapter = adapter
        }

        binding.cbfutbol.setOnClickListener(this)
        binding.cbmusica.setOnClickListener(this)
        binding.cbotros.setOnClickListener(this)
        binding.btnregistrar.setOnClickListener(this)
        binding.btnveriusuarios.setOnClickListener(this)
        binding.spestadocivil.onItemSelectedListener = this
    }

    fun getGenero():String{
        var genero = ""
        when(binding.rggenero.checkedRadioButtonId){
            R.id.rbmasculino -> genero = binding.rbmasculino.text.toString()
            R.id.rbfemenino -> genero = binding.rbfemenino.text.toString()
            R.id.rbotros -> genero = binding.rbotros.text.toString()
        }
        return genero
    }

    override fun onClick(p0: View) {
        if(p0 is CheckBox){
            getHobbies(p0)
        }else{
            when(p0.id){
                R.id.btnregistrar -> registrarUsuario()
                R.id.btnveriusuarios -> verUsuarios()
            }
        }
    }
    fun getHobbies(vista: View){
        val checkBox = vista as CheckBox
        if(checkBox.isChecked){
            listPreferencias.add(checkBox.text.toString())
        }else{
            listPreferencias.remove(checkBox.text.toString())
        }
    }
    fun verUsuarios(){
        val intentLista = Intent(this,
            ListaActivity::class.java).apply {
                putStringArrayListExtra("listausuarios", listUsuario)
        }
        startActivity(intentLista)
    }
    fun registrarUsuario(){
        if(validarFomulario()){
            val infoUsuario = binding.etnombres.text.toString()+"-"+
                    binding.etapellidos.text.toString()+"-"+
                    getGenero()+"-"+
                    listPreferencias.toTypedArray().contentToString()+"-"+
                    estadoCivil+"-"+
                    binding.swnotificacion.isChecked
            listUsuario.add(infoUsuario)
            AppMensaje.enviarMensaje(binding.root, "Usuario Registrado",
                TipoMensaje.SUCCESSFULL)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?,
                                position: Int, p3: Long) {
        estadoCivil = if(position > 0){
            p0!!.getItemAtPosition(position).toString()
        }else ""
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    //Validar formulario
    fun validarNombreApellido():Boolean{
        var respuesta = true
        if(binding.etnombres.text.toString().trim().isEmpty()){
            binding.etnombres.isFocusableInTouchMode = true
            binding.etnombres.requestFocus()
            respuesta = false
        }else if(binding.etapellidos.text.toString().trim().isEmpty()){
            binding.etapellidos.isFocusableInTouchMode = true
            binding.etapellidos.requestFocus()
            respuesta = false
        }
        return respuesta
    }
    fun validarGenero():Boolean{
        var respuesta = true
        if(binding.rggenero.checkedRadioButtonId == -1){
            respuesta = false
        }
        return respuesta
    }
    fun validarEstadoCivil(): Boolean{
        var respuesta = true
        if(estadoCivil == ""){
            respuesta = false
        }
        return respuesta
    }
    fun validarHobbies(): Boolean {
        var respuesta = false
        if(binding.cbfutbol.isChecked || binding.cbmusica.isChecked
            || binding.cbotros.isChecked){
            respuesta = true
        }
        return respuesta
    }
    fun validarFomulario(): Boolean{
        var respuesta = false
        var mensaje = ""
        if(!validarNombreApellido()){
            mensaje = "Nombres y apellidos obligatorios"
            mensajeError(mensaje)
        }else if(!validarGenero()){
            mensaje = "Seleccione su género"
            mensajeError(mensaje)
        }else if(!validarEstadoCivil()){
            mensaje = "Seleccione su estado civil"
            mensajeError(mensaje)
        }else if(!validarHobbies()){
            mensaje = "Seleccione al menos un hobbie"
            mensajeError(mensaje)
        }else {
            respuesta = true
        }
        return respuesta
    }
    fun mensajeError(mensaje: String){
        AppMensaje.enviarMensaje(binding.root, mensaje, TipoMensaje.ERROR)
    }
}