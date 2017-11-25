package ru.santaev.clipboardtranslator.ui.adapter


import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.databinding.HistoryItemLayoutBinding
import ru.santaev.clipboardtranslator.db.entity.Translation
import java.util.*

class HistoryAdapter(translations: List<Translation>?) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var translations: MutableList<TranslateView>? = null
    var listener: ((Translation) -> Unit)? = null

    init {
        setHasStableIds(true)
        setTranslations(translations)
    }

    fun setTranslations(translations: List<Translation>?) {
        this.translations = ArrayList()
        if (translations == null) {
            return
        }
        translations
                .map { TranslateView(it) }
                .forEach { this.translations?.add(it) }
    }

    fun getTranslation(pos: Int): Translation {
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
        val model = translations!![position]
        holder.binding.originText.text = model.translation.textSource
        holder.binding.targetText.text = model.translation.textTarget
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

    private fun buildLangText(translation: Translation): String {
        return translation.langSource.toUpperCase() +
                "-" + translation.langTarget.toUpperCase()
    }

    inner class HistoryViewHolder(var binding: HistoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    internal inner class TranslateView(var translation: Translation) {
        var isExpanded: Boolean = false

        init {
            this.isExpanded = false
        }
    }

}
