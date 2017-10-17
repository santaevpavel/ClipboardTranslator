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
import ru.santaev.clipboardtranslator.db.entity.Language;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder>{

    public interface OnItemClickedListener{
        void onClick(Language translation);
    }

    private List<Language> languages;
    private List<Language> unsupportedLanguages;
    private OnItemClickedListener listener;

    public LanguageAdapter(List<Language> languages, List<Language> unsupportedLanguages) {
        this.languages = languages;
        this.unsupportedLanguages = unsupportedLanguages;
    }

    public OnItemClickedListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LanguageItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.language_item_layout, parent, false);
        return new LanguageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LanguageViewHolder viewHolder = (LanguageViewHolder) holder;
        Language language = getLangByPos(position);
        boolean isSupported = position < languages.size();

        viewHolder.binding.text.setText(language.getName());
        //viewHolder.binding.icon.setImageResource(languageIconProvider.getLanguageIcon(language));

        int color = isSupported ? R.color.textColorGrayDark : R.color.textColorGrayLight;
        Resources resources = TranslatorApp.getAppContext().getResources();
        viewHolder.binding.text.setTextColor(ResourcesCompat.getColor(resources, color, null));

        viewHolder.binding.getRoot().setOnClickListener(v -> {
            Language langByPos = getLangByPos(position);
            if (listener != null && null != langByPos) {
                listener.onClick(langByPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return languages != null ? languages.size() + unsupportedLanguages.size() : 0;
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
}
