package com.example.palavras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.util.DisplayMetrics
import androidx.recyclerview.widget.RecyclerView

class PalavrasAdapter(private val palavras: List<String>) :
    RecyclerView.Adapter<PalavrasAdapter.PalavraViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    class PalavraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val palavraButton: Button = itemView.findViewById(R.id.palavraButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PalavraViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_palavra, parent, false)

        // Ajustar o layout para ocupar o espaço disponível
        val layoutParams = view.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        view.layoutParams = layoutParams

        return PalavraViewHolder(view)
    }

    override fun getItemCount(): Int = palavras.size

    override fun onBindViewHolder(holder: PalavraViewHolder, position: Int) {
        val palavra = palavras[position]
        holder.palavraButton.text = palavra

        // Ajustar a altura do botão com base na quantidade de palavras
        adjustButtonHeight(holder)

        // Definir o estado do botão com base na seleção
        updateButtonState(holder, position)

        // Configurar o clique no botão
        holder.palavraButton.setOnClickListener {
            if (selectedItems.contains(position)) {
                selectedItems.remove(position)
            } else {
                selectedItems.add(position)
            }
            updateButtonState(holder, position)
        }
    }

    private fun adjustButtonHeight(holder: PalavraViewHolder) {
        val context = holder.itemView.context
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        // Calcular a altura ideal com base na quantidade de palavras
        val itemCount = palavras.size
        val idealHeight = when {
            itemCount == 1 -> screenHeight * 0.8 // 80% da tela para uma única palavra
            itemCount <= 4 -> screenHeight * 0.4 // 40% da tela para 2-4 palavras
            itemCount <= 9 -> screenHeight * 0.25 // 25% da tela para 5-9 palavras
            else -> screenHeight * 0.2 // 20% da tela para 10+ palavras
        }

        // Definir a altura do botão
        val layoutParams = holder.palavraButton.layoutParams
        layoutParams.height = idealHeight.toInt()
        holder.palavraButton.layoutParams = layoutParams
    }

    private fun updateButtonState(holder: PalavraViewHolder, position: Int) {
        if (selectedItems.contains(position)) {
            holder.palavraButton.setBackgroundResource(R.color.button_selected)
        } else {
            holder.palavraButton.setBackgroundResource(R.color.button_normal)
        }
    }
}
