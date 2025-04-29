package com.example.palavras

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private val palavrasList = mutableListOf<String>()
    private lateinit var adapter: PalavrasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addButton)

        // Configurar o RecyclerView
        adapter = PalavrasAdapter(palavrasList)
        recyclerView.layoutManager = ResponsiveGridLayoutManager(this)
        recyclerView.adapter = adapter

        // Configurar o swipe para deletar
        setupSwipeToDelete()

        // Configurar o botão de adicionar
        addButton.setOnClickListener {
            showAddPalavraDialog()
        }
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            private val deleteIcon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_delete)!!
            private val background = ColorDrawable(Color.RED)
            private val clearPaint = Paint().apply { xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR) }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCanceled = dX == 0f && !isCurrentlyActive

                if (isCanceled) {
                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }

                // Desenhar o fundo vermelho
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)

                // Calcular posição do ícone
                val iconMargin = (itemHeight - deleteIcon.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemHeight - deleteIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + deleteIcon.intrinsicHeight
                val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                val iconRight = itemView.right - iconMargin

                // Desenhar o ícone de lixeira
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
                c?.drawRect(left, top, right, bottom, clearPaint)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val palavraRemovida = palavrasList[position]

                // Remover a palavra da lista
                palavrasList.removeAt(position)
                // Notificar o adaptador que os dados mudaram completamente
                adapter.notifyDataSetChanged()

                // Mostrar Snackbar com opção de desfazer
                Snackbar.make(
                    recyclerView,
                    "Palavra '${palavraRemovida}' removida",
                    Snackbar.LENGTH_LONG
                ).setAction("Desfazer") {
                    // Restaurar a palavra se o usuário clicar em "Desfazer"
                    palavrasList.add(position, palavraRemovida)
                    // Notificar o adaptador que os dados mudaram completamente
                    adapter.notifyDataSetChanged()
                }.show()
            }
        }

        // Anexar o ItemTouchHelper ao RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showAddPalavraDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_palavra, null)
        val editTextPalavra = dialogView.findViewById<EditText>(R.id.editTextPalavra)

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.add_palavra)
            .setView(dialogView)
            .setPositiveButton(R.string.btn_adicionar) { _, _ ->
                val novaPalavra = editTextPalavra.text.toString().trim()
                if (novaPalavra.isNotEmpty()) {
                    palavrasList.add(novaPalavra)
                    // Notificar o adaptador que os dados mudaram completamente
                    // para recalcular o layout com base na nova quantidade
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, R.string.hint_palavra, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.btn_cancelar, null)
            .create()

        dialog.show()
    }
}