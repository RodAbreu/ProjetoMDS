package com.example.rodrigo.projetomds

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lista_produtos.*

class ListaProdutosActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CADASTRO: Int = 1
        private const val LISTA: String = "ListaProdutos"
    }

    var produtosList: MutableList<Produto> = mutableListOf()
    //indice para verificar se algum produto foi clicado
    var indexProdutoClicado: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produtos)

        btnAddProduto.setOnClickListener(){
            val cadastrarProduto = Intent(this, CadastraProdutoActivity::class.java)
            startActivityForResult(cadastrarProduto, REQUEST_CADASTRO)
        }

    }

    override fun onResume() {
        super.onResume()
        carregaLista()
    }

    //recebe o produto da tela de cadastro e o adiciona na lista
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CADASTRO && resultCode == Activity.RESULT_OK){
            val produto: Produto? = data?.getSerializableExtra(CadastraProdutoActivity.PRODUTO) as Produto
            //caso algum item tenha sido clicado seus dados são alterados, caso não adiciona um novo
            if (produto != null) {
                if(indexProdutoClicado>= 0){
                    produtosList.set(indexProdutoClicado, produto)
                    indexProdutoClicado = -1
                }else {
                    produtosList.add(produto)
                }
            }
        }

    }

    //salva a lista caso o Android venha a destruir a activity
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable(LISTA, produtosList as ArrayList<String>)
    }

    //restaura a lista caso o Android venha a destruir a activity
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if(savedInstanceState != null)
            produtosList = savedInstanceState.getSerializable(LISTA) as MutableList<Produto>
    }

    fun carregaLista() {
        val adapter = ProdutoAdapter(this, produtosList)

        adapter.setOnItemClickListener(){ produto, indexProdutoClicado->
            this.indexProdutoClicado = indexProdutoClicado
            val editaProduto = Intent(this, CadastraProdutoActivity::class.java)
            editaProduto.putExtra(CadastraProdutoActivity.PRODUTO, produto)
            this.startActivityForResult(editaProduto, REQUEST_CADASTRO)
        }

        val layoutManager = LinearLayoutManager(this)

        rvProdutos.adapter = adapter
        rvProdutos.layoutManager = layoutManager
    }

}
