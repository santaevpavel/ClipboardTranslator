package ru.santaev.clipboardtranslator.ui.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.databinding.HistoryItemLayoutBinding;
import ru.santaev.clipboardtranslator.db.entity.Translation;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    public interface OnHistoryItemClickedListener{
        void onClick(Translation translation);
    }

    private List<TranslateView> translations;
    private OnHistoryItemClickedListener listener;

    public HistoryAdapter(List<Translation> translations) {
        setHasStableIds(true);
        setTranslations(translations);
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = new ArrayList<>();
        if (translations == null){
            return;
        }
        for (Translation translation : translations) {
            TranslateView translateView = new TranslateView(translation);
            this.translations.add(translateView);
        }
    }

    public Translation getTranslation(int pos) {
        return translations.get(pos).translation;
    }

    public OnHistoryItemClickedListener getListener() {
        return listener;
    }

    public void setListener(OnHistoryItemClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return null == translations ? 0 : translations.get(position).translation.getId();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HistoryItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.history_item_layout, parent, false);
        HistoryViewHolder viewHolder = new HistoryViewHolder(binding);
        viewHolder.binding.setIsExpanded(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        TranslateView model = translations.get(position);
        holder.binding.originText.setText(model.translation.getTextSource());
        holder.binding.targetText.setText(model.translation.getTextTarget());
        holder.binding.lang.setText(buildLangText(model.translation));

        // TODO: 20.09.2017 broke childs size in recycler view
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            ((ViewGroup) holder.binding.getRoot()).getLayoutTransition()
                    .disableTransitionType(LayoutTransition.CHANGING);
        }*/

        holder.binding.setIsExpanded(model.isExpanded);
        holder.binding.executePendingBindings();

        holder.binding.getRoot().setOnClickListener(v -> {
            model.isExpanded = !model.isExpanded;
            holder.binding.setIsExpanded(model.isExpanded);

            if (listener != null){
                listener.onClick(model.translation);
            }
        });

    }

    @Override
    public int getItemCount() {
        return translations == null ? 0 : translations.size();
    }

    private String buildLangText(Translation translation){
        return translation.getLangSource().toUpperCase() +
                "-" + translation.getLangTarget().toUpperCase();
    }

    class TranslateView {

        public Translation translation;
        public boolean isExpanded;

        public TranslateView(Translation translation) {
            this.translation = translation;
            this.isExpanded = false;
        }
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{

        public HistoryItemLayoutBinding binding;

        public HistoryViewHolder(HistoryItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
