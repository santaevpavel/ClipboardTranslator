package ru.santaev.clipboardtranslator.ui.adapter


import android.databinding.DataBindingUtil
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.santaev.domain.dto.LanguageDto
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.TranslatorApp
import ru.santaev.clipboardtranslator.databinding.LanguageItemLayoutBinding
import ru.santaev.clipboardtranslator.databinding.LanguageSectionItemLayoutBinding

class LanguageAdapter(
        private val recentLanguages: List<LanguageDto>,
        private val languages: List<LanguageDto>?,
        private val unsupportedLanguages: List<LanguageDto>
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    var listener: ((LanguageDto) -> Unit)? = null
    private var hasHeaders = false

    init {
        hasHeaders = !recentLanguages.isEmpty()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == VIEW_TYPE_LANGUAGE) {
            val binding = DataBindingUtil.inflate<LanguageItemLayoutBinding>(LayoutInflater.from(parent.context),
                    R.layout.language_item_layout, parent, false)
            return LanguageViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<LanguageSectionItemLayoutBinding>(LayoutInflater.from(parent.context),
                    R.layout.language_section_item_layout, parent, false)
            return SectionViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        if (viewType == VIEW_TYPE_LANGUAGE) {
            val isRecentLang = hasHeaders && position - 1 < recentLanguages.size
            val langPosition = position - if (hasHeaders) recentLanguages.size + 2 else 0
            val isSupported = langPosition < languages!!.size

            val viewHolder = holder as LanguageViewHolder
            val language = if (isRecentLang)
                recentLanguages[position - 1]
            else
                getLangByPos(langPosition)


            viewHolder.binding.text.text = language.name

            val color = if (isSupported) R.color.textColorGrayDark else R.color.textColorGrayLight
            val resources = TranslatorApp.appContext.resources
            viewHolder.binding.text.setTextColor(ResourcesCompat.getColor(resources, color, null))

            viewHolder.binding.root.setOnClickListener { v ->
                val langByPos = if (isRecentLang)
                    recentLanguages[position - 1]
                else
                    getLangByPos(langPosition)
                listener?.invoke(langByPos)
            }
        } else {
            val viewHolder = holder as SectionViewHolder
            viewHolder.binding.text.setText(if (0 == position)
                R.string.choose_lang_activity_subheader_recent
            else
                R.string.choose_lang_activity_subheader_langs)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (hasHeaders) {
            when {
                position == 0 -> VIEW_TYPE_SECTION
                position < recentLanguages.size + 1 -> VIEW_TYPE_LANGUAGE
                position == recentLanguages.size + 1 -> VIEW_TYPE_SECTION
                else -> VIEW_TYPE_LANGUAGE
            }
        } else {
            VIEW_TYPE_LANGUAGE
        }
    }

    override fun getItemCount(): Int {
        return if (languages != null) {
            languages.size + unsupportedLanguages.size + recentLanguages.size + if (hasHeaders) 2 else 0
        } else {
            0
        }
    }

    private fun getLangByPos(position: Int): LanguageDto {
        return if (position < languages!!.size)
            languages[position]
        else
            unsupportedLanguages[position - languages.size]
    }


    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private inner class LanguageViewHolder
    internal constructor(internal var binding: LanguageItemLayoutBinding) : ViewHolder(binding.root)

    private inner class SectionViewHolder
    internal constructor(internal var binding: LanguageSectionItemLayoutBinding) : ViewHolder(binding.root)

    companion object {

        private val VIEW_TYPE_LANGUAGE = 0
        private val VIEW_TYPE_SECTION = 1
    }
}
