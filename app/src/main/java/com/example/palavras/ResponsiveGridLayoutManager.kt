package com.example.palavras

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Layout manager que ajusta automaticamente o número de colunas com base na quantidade de itens
 * para que os itens ocupem toda a tela de forma responsiva.
 */
class ResponsiveGridLayoutManager(
    context: Context,
    private val minItemWidth: Int = 200, // Largura mínima de cada item em dp
    private val maxColumns: Int = 4 // Número máximo de colunas
) : GridLayoutManager(context, 1) {

    private val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    private var lastItemCount = -1

    init {
        spanCount = 1
    }

    override fun onLayoutChildren(recycler: androidx.recyclerview.widget.RecyclerView.Recycler?, state: androidx.recyclerview.widget.RecyclerView.State?) {
        val itemCount = state?.itemCount ?: 0
        
        if (itemCount != lastItemCount) {
            lastItemCount = itemCount
            updateSpanCount(itemCount)
        }
        
        super.onLayoutChildren(recycler, state)
    }

    private fun updateSpanCount(itemCount: Int) {
        if (itemCount <= 0) return

        // Calcular a largura disponível em dp
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        
        // Calcular o número ideal de colunas com base na quantidade de itens
        val idealColumns = when {
            itemCount == 1 -> 1
            itemCount <= 4 -> 2
            itemCount <= 9 -> 3
            else -> min(maxColumns, max(2, sqrt(itemCount.toFloat()).toInt()))
        }
        
        // Garantir que cada item tenha pelo menos a largura mínima
        val maxPossibleColumns = max(1, (screenWidthDp / minItemWidth).toInt())
        
        // Usar o menor valor entre o ideal e o máximo possível
        spanCount = min(idealColumns, maxPossibleColumns)
    }
}
