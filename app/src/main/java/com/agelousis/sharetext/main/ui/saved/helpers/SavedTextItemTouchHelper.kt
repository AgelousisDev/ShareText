package com.agelousis.sharetext.main.ui.saved.helpers

import android.content.Context
import android.graphics.*
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.sharetext.R
import com.agelousis.sharetext.contact.view_holders.HeaderViewHolder
import com.agelousis.sharetext.main.ui.saved.enums.SwipeAction
import com.agelousis.sharetext.main.ui.share_text.view_holders.EmptyViewHolder
import com.agelousis.sharetext.utilities.extensions.fromVector
import com.agelousis.sharetext.utilities.extensions.secondOrNull

typealias SwipeActionBlock = (swipeAction: SwipeAction, position: Int) -> Unit
class SavedTextItemTouchHelper(context: Context, private val swipeActionBlock: SwipeActionBlock): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {

    private val paint = Paint()

    //Colors & Icons
    @ColorRes private var colors = arrayOf(ContextCompat.getColor(context, R.color.colorAccent), ContextCompat.getColor(context, R.color.red))
    private var icons = arrayOf(ContextCompat.getDrawable(context, R.drawable.ic_share)?.fromVector(padding = 0), ContextCompat.getDrawable(context, R.drawable.ic_delete)?.fromVector(padding = 0))

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeActionBlock(if (direction == ItemTouchHelper.END) SwipeAction.SHARE else SwipeAction.DELETE, viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val icon: Bitmap
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val height = itemView.bottom.toFloat() - itemView.top.toFloat()
            val width = height / 3
            if (dX > 0) {
                paint.color = colors.first()
                val background =
                    RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                c.drawRect(background, paint)
                icon = icons.first()!!
                val iconDest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                c.drawBitmap(icon, null, iconDest, paint)
            } else {
                paint.color = colors.secondOrNull() ?: Color.WHITE
                val background = RectF(
                    itemView.right.toFloat() + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                c.drawRect(background, paint)
                icon = icons.secondOrNull()!!
                val iconDest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                c.drawBitmap(icon, null, iconDest, paint)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder is EmptyViewHolder || viewHolder is HeaderViewHolder) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }

}