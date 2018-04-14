package ru.santaev.clipboardtranslator.ui.adapter


import android.databinding.DataBindingUtil
import android.graphics.Canvas
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.santaev.domain.dto.TranslationDto
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.databinding.HistoryItemLayoutBinding
import java.util.*

class HistoryAdapter(translations: List<TranslationDto>?) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var translations: MutableList<TranslateView>? = null
    var listener: ((TranslationDto) -> Unit)? = null

    init {
        setHasStableIds(true)
        setTranslations(translations)
    }

    fun setTranslations(translations: List<TranslationDto>?) {
        this.translations = ArrayList()
        if (translations == null) {
            return
        }
        translations
                .map { TranslateView(it) }
                .forEach { this.translations?.add(it) }
    }

    fun getTranslation(pos: Int): TranslationDto {
        return translations!![pos].translation
    }

    override fun getItemId(position: Int): Long {
        return if (null == translations) 0 else translations!![position].translation.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = DataBindingUtil.inflate<HistoryItemLayoutBinding>(LayoutInflater.from(parent.context),
                R.layout.history_item_layout, parent, false)
        val viewHolder = HistoryViewHolder(binding)
        viewHolder.binding.isExpanded = false
        return viewHolder
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val model = translations?.get(position) ?: return
        holder.binding.originText.text = model.translation.sourceText
        holder.binding.targetText.text = model.translation.targetText
        holder.binding.lang.text = buildLangText(model.translation)

        holder.binding.isExpanded = model.isExpanded
        holder.binding.executePendingBindings()

        holder.binding.root.setOnClickListener {
            model.isExpanded = !model.isExpanded
            holder.binding.isExpanded = model.isExpanded
            listener?.invoke(model.translation)
        }

    }

    override fun getItemCount(): Int {
        return if (translations == null) 0 else translations!!.size
    }

    private fun buildLangText(translation: TranslationDto): String {
        return translation.run {
            "${sourceLangCode.toUpperCase()}-${targetLangCode.toUpperCase()}"
        }
    }

    inner class HistoryViewHolder(var binding: HistoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    class RecyclerViewCallback(
            private val onSwipedListener: (Int) -> Unit
    ) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            onSwipedListener(position)
        }

        override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (viewHolder != null) {
                val foregroundView = viewHolder.foreground
                ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
            }
        }

        override fun onChildDrawOver(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
        ) {
            val item = (viewHolder as HistoryViewHolder).binding
            item.icDelete.apply {
                val width = item.root.width
                rotation = if (width * 0.5 > -dX) {
                    (width * 0.5F + dX) / width * 45
                } else {
                    0F
                }
            }
            val foregroundView = viewHolder.foreground
            ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }

        override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
            val foregroundView = viewHolder.foreground
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
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
            val foregroundView = viewHolder.foreground
            ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive)
        }

        private val RecyclerView.ViewHolder.foreground: CardView
            get() = (this as HistoryViewHolder).binding.foregroundView
    }

    internal inner class TranslateView(var translation: TranslationDto) {
        var isExpanded: Boolean = false

        init {
            this.isExpanded = false
        }
    }
}
