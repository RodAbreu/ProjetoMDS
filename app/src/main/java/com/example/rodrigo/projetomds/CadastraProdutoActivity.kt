package com.example.rodrigo.projetomds

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_cadastra_produto.*
import org.jetbrains.annotations.Nullable
import java.io.File
import java.io.IOException
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap



class CadastraProdutoActivity : AppCompatActivity() {

    var dbHandler : DatabaseHandler? = null
    var mCurrentPhotoPath: String = ""

    companion object {
        const val PRODUTO: String = "Produto"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastra_produto)

        val produto: Produto? = intent.getSerializableExtra(PRODUTO) as Produto?
        initDB()

        if(produto != null){
            carregaDados(produto)
            val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            imgFoto.setImageBitmap(bitmap)
            if(cadastraTelefone.text.toString() == "" || cadastraTelefone.text.isEmpty()){
                button_ligar.setBackgroundColor(Color.rgb(160,160,160))
            }else{
                button_ligar.setBackgroundColor(Color.rgb(0,153,0))
            }
        }else{
            button_ligar.setBackgroundColor(Color.rgb(160,160,160))
        }

        button_ligar.setOnClickListener(){
            if(cadastraTelefone.text.toString() == "" || cadastraTelefone.text.isEmpty()){
                cadastraTelefone.requestFocus()
                cadastraTelefone.setError("Campo obrigatorio para ligacao")
            }else{
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:"+cadastraTelefone.text.toString())
                startActivity(callIntent)
            }
        }
        btnFoto.setOnClickListener(){
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, 1)

                    }
                }
            }



        }

    }

    public override fun onResume() {
        super.onResume()
        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
        imgFoto.setImageBitmap(bitmap)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menuSalvar -> salvarProduto()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun salvarProduto() {

        var success: Boolean = false
        var tel : String = ""

        if(cadastraNome.text.isEmpty()){
            cadastraNome.requestFocus()
            cadastraNome.setError(getString(R.string.campo_obrigatorio))
            return
        }

        if(cadastraQuant.text.isEmpty()){
            cadastraQuant.requestFocus()
            cadastraQuant.setError(getString(R.string.campo_obrigatorio))
            return
        }
        if(!cadastraTelefone.text.isEmpty()){
            tel = cadastraTelefone.text.toString()
        }

        val produto = Produto(cadastraNome.text.toString(),
            cadastraQuant.text.toString(),tel, mCurrentPhotoPath)

        val salvaProduto = Intent(this, ListaProdutosActivity::class.java)
        salvaProduto.putExtra(PRODUTO, produto)
        setResult(Activity.RESULT_OK, salvaProduto)

        val prod =  ProdutoClass()

        prod.nome = cadastraNome.text.toString()
        prod.quantidade = Integer.parseInt(cadastraQuant.text.toString())

        success = dbHandler?.addProduto(prod) as Boolean

        if(success){
            Log.d("MainActivity", "Cadastrou: " + success.toString())
        }

        finish()

    }

    private fun initDB(){
        dbHandler = DatabaseHandler(this)
    }

    private fun carregaDados(produto: Produto) {
        cadastraNome.setText(produto.Nome)
        cadastraQuant.setText(produto.Quant)
        cadastraTelefone.setText(produto.Tel)
        mCurrentPhotoPath = produto.camImagem
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }
}
