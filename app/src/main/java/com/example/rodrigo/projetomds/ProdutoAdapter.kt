package com.example.rodrigo.projetomds

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_holder.view.*

class ProdutoAdapter(val context: Context,val produtos: List<Produto>)
    : RecyclerView.Adapter<ProdutoAdapter.ViewHolder>() {

    //salva a função do clique no item
    var clickListener: ((produto:Produto, index: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return produtos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(context, produtos[position], clickListener)
    }

    //configuração a função de clique nos itens
    fun setOnItemClickListener(clique: ((produto:Produto, index: Int) -> Unit)){
        this.clickListener = clique
    }

    //referência para a view de cada item da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(context: Context, produto: Produto, clickListener: ((produto:Produto, index: Int) -> Unit)?) {
            itemView.vhNome.text = produto.Nome
            itemView.vhQuant.text = produto.Quant


            if(clickListener != null) {
                itemView.setOnClickListener {
                    clickListener.invoke(produto, adapterPosition)
                }
            }

        }

    }


}