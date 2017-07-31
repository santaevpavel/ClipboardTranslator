package ru.santaev.clipboardtranslator.ui.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.databinding.HistoryItemLayoutBinding;
import ru.santaev.clipboardtranslator.db.entity.Translation;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    public interface OnHistoryItemClickedListener{
        void onClick(Translation translation);
    }

    private List<Translation> translations;
    private OnHistoryItemClickedListener listener;

    public HistoryAdapter(List<Translation> translations) {
        this.translations = translations;
        setHasStableIds(true);
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public OnHistoryItemClickedListener getListener() {
        return listener;
    }

    public void setListener(OnHistoryItemClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return null == translations ? 0 : translations.get(position).getId();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HistoryItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.history_item_layout, parent, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        Translation translation = translations.get(position);
        holder.binding.originText.setText(translation.getTextSource());
        holder.binding.targetText.setText(translation.getTextTarget());
        holder.binding.lang.setText(buildLangText(translation));
        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null){
                listener.onClick(translation);
            }
        });

        /*int colorRes = 0 != holder.getLayoutPosition() % 2
                ? R.color.history_item_bg
                : android.R.color.transparent;
        holder.binding.getRoot().setBackgroundColor(TranslatorApp.getAppContext().getResources().getColor(colorRes));*/
    }

    @Override
    public int getItemCount() {
        return translations == null ? 0 : translations.size();
    }

    private String buildLangText(Translation translation){
        return translation.getLangSource() + "-" + translation.getLangTarget();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{

        HistoryItemLayoutBinding binding;

        public HistoryViewHolder(HistoryItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
