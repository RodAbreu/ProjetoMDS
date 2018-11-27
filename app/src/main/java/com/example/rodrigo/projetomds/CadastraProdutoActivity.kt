package com.example.rodrigo.projetomds

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_cadastra_produto.*

class CadastraProdutoActivity : AppCompatActivity() {

    companion object {
        const val PRODUTO: String = "Produto"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastra_produto)

        val produto: Produto? = intent.getSerializableExtra(PRODUTO) as Produto?

        if(produto != null){
            carregaDados(produto)
        }
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

        val produto = Produto(cadastraNome.text.toString(),
            cadastraQuant.text.toString())

        val salvaProduto = Intent(this, ListaProdutosActivity::class.java)
        salvaProduto.putExtra(PRODUTO, produto)
        setResult(Activity.RESULT_OK, salvaProduto)
        finish()

    }

    private fun carregaDados(produto: Produto) {
        cadastraNome.setText(produto.Nome)
        cadastraQuant.setText(produto.Quant)
    }
}
