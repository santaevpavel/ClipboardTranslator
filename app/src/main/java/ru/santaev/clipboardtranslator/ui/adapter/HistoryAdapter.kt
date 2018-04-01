package ru.santaev.clipboardtranslator.ui.adapter


import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
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
        val model = translations!![position]
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

    internal inner class TranslateView(var translation: TranslationDto) {
        var isExpanded: Boolean = false

        init {
            this.isExpanded = false
        }
    }

}
