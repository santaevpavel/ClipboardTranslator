package ru.santaev.clipboardtranslator.ui.adapter;


import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.databinding.LanguageItemLayoutBinding;
import ru.santaev.clipboardtranslator.databinding.LanguageSectionItemLayoutBinding;
import ru.santaev.clipboardtranslator.db.entity.Language;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder>{

    private static final int VIEW_TYPE_LANGUAGE = 0;
    private static final int VIEW_TYPE_SECTION = 1;

    public interface OnItemClickedListener{
        void onClick(Language translation);
    }

    private List<Language> recentLanguages;
    private List<Language> languages;
    private List<Language> unsupportedLanguages;
    private OnItemClickedListener listener;
    private boolean hasHeaders = false;

    public LanguageAdapter(List<Language> recentLanguages, List<Language> languages,
                           List<Language> unsupportedLanguages) {
        this.recentLanguages = recentLanguages;
        this.languages = languages;
        this.unsupportedLanguages = unsupportedLanguages;
        hasHeaders = !recentLanguages.isEmpty();
    }

    public OnItemClickedListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LANGUAGE) {
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
        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_LANGUAGE) {
            boolean isRecentLang = hasHeaders && position - 1 < recentLanguages.size();
            int langPosition = position - (hasHeaders ? recentLanguages.size() + 2 : 0);
            boolean isSupported = langPosition < languages.size();

            LanguageViewHolder viewHolder = (LanguageViewHolder) holder;
            Language language = isRecentLang
                    ? recentLanguages.get(position - 1)
                    : getLangByPos(langPosition);


            viewHolder.binding.text.setText(language.getName());

            int color = isSupported ? R.color.textColorGrayDark : R.color.textColorGrayLight;
            Resources resources = TranslatorApp.Companion.getAppContext().getResources();
            viewHolder.binding.text.setTextColor(ResourcesCompat.getColor(resources, color, null));

            viewHolder.binding.getRoot().setOnClickListener(v -> {
                Language langByPos = isRecentLang
                        ? recentLanguages.get(position - 1)
                        : getLangByPos(langPosition);
                if (listener != null && null != langByPos) {
                    listener.onClick(langByPos);
                }
            });
        } else {
            SectionViewHolder viewHolder = (SectionViewHolder) holder;
            viewHolder.binding.text.setText(0 == position
                    ? R.string.choose_lang_activity_subheader_recent
                    : R.string.choose_lang_activity_subheader_langs);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeaders) {
            if (position == 0) {
                return VIEW_TYPE_SECTION;
            } else if (position < recentLanguages.size() + 1) {
                return VIEW_TYPE_LANGUAGE;
            } else if (position == recentLanguages.size() + 1) {
                return VIEW_TYPE_SECTION;
            } else {
                return VIEW_TYPE_LANGUAGE;
            }
        } else {
            return VIEW_TYPE_LANGUAGE;
        }
    }

    @Override
    public int getItemCount() {
        return languages != null
                ? languages.size() + unsupportedLanguages.size() + recentLanguages.size()
                + (hasHeaders ? 2 : 0)
                : 0;
    }

    private Language getLangByPos(int position){
        return position < languages.size()
                ? languages.get(position)
                : unsupportedLanguages.get(position - languages.size());
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class LanguageViewHolder extends ViewHolder{

        LanguageItemLayoutBinding binding;

        LanguageViewHolder(LanguageItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class SectionViewHolder extends ViewHolder {

        LanguageSectionItemLayoutBinding binding;

        SectionViewHolder(LanguageSectionItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
