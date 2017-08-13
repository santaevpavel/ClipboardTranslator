package ru.santaev.clipboardtranslator.ui.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.databinding.LanguageItemLayoutBinding;
import ru.santaev.clipboardtranslator.databinding.LanguageSectionItemLayoutBinding;
import ru.santaev.clipboardtranslator.model.Language;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder>{

    private static final int TYPE_LANG = 0;
    private static final int TYPE_SECTION = 1;

    public interface OnItemClickedListener{
        void onClick(Language translation);
    }

    private List<Language> languages;
    private List<Language> unsupportedLanguages;
    private OnItemClickedListener listener;
    private LanguageIconProvider languageIconProvider;

    public LanguageAdapter(List<Language> languages, List<Language> unsupportedLanguages) {
        this.languages = languages;
        this.unsupportedLanguages = unsupportedLanguages;
        languageIconProvider = new LanguageIconProvider();
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<Language> getUnsupportedLanguages() {
        return unsupportedLanguages;
    }

    public void setUnsupportedLanguages(List<Language> unsupportedLanguages) {
        this.unsupportedLanguages = unsupportedLanguages;
    }

    public OnItemClickedListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position || languages.size() + 1 == position){
            return TYPE_SECTION;
        } else {
            return TYPE_LANG;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LANG) {
            LanguageItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.language_item_layout, parent, false);
            return new LanguageViewHolder(binding);
        } else {
            LanguageSectionItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.language_section_item_layout, parent, false);
            return new SectionViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_LANG) {
            LanguageViewHolder viewHolder = (LanguageViewHolder) holder;
            Language language = position < languages.size() + 2
                    ? languages.get(position - 1)
                    : unsupportedLanguages.get(position - languages.size() - 2);

            viewHolder.binding.text.setText(TranslatorApp.getAppContext().getString(language.getTextRes()));
            viewHolder.binding.icon.setImageResource(languageIconProvider.getLanguageIcon(language));

            viewHolder.binding.getRoot().setOnClickListener(v -> {
                Language langByPos = getLangByPos(position);
                if (listener != null && null != langByPos) {
                    listener.onClick(langByPos);
                }
            });
        } else {
            SectionViewHolder viewHolder = (SectionViewHolder) holder;
            viewHolder.binding.text.setText(0 == position ? "Все языки" : "Неподдерживаемые языки");
            viewHolder.binding.getRoot().setOnClickListener(v -> {});
        }
    }

    private Language getLangByPos(int position){
        if (position == 0 || position == languages.size() + 1){
            return null;
        }
        return position < languages.size() + 2
                ? languages.get(position - 1)
                : unsupportedLanguages.get(position - languages.size() - 2);
    }

    @Override
    public int getItemCount() {
        int countLang = languages == null ? 0 : languages.size();
        int countUnsupportedLang = unsupportedLanguages == null ? 0 : unsupportedLanguages.size();
        return countLang + countUnsupportedLang + (countUnsupportedLang == 0 ? 1 : 2);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class SectionViewHolder extends ViewHolder{

        LanguageSectionItemLayoutBinding binding;

        SectionViewHolder(LanguageSectionItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class LanguageViewHolder extends ViewHolder{

        LanguageItemLayoutBinding binding;

        LanguageViewHolder(LanguageItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
