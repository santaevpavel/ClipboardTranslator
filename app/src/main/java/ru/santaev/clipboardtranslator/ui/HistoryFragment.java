package ru.santaev.clipboardtranslator.ui;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.databinding.FragmentHistoryBinding;
import ru.santaev.clipboardtranslator.ui.adapter.HistoryAdapter;
import ru.santaev.clipboardtranslator.viewmodel.HistoryViewModel;


public class HistoryFragment extends LifecycleFragment {

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    private HistoryViewModel viewModel;
    private FragmentHistoryBinding binding;
    private HistoryAdapter adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        viewModel.getHistory().observe(this, translations -> {
            if (adapter != null){
                if (translations != null && !translations.isEmpty()) {
                    adapter.setTranslations(translations);
                    adapter.notifyDataSetChanged();

                    binding.historyList.setVisibility(View.VISIBLE);
                    binding.emptyLayout.setVisibility(View.INVISIBLE);
                } else {
                    binding.historyList.setVisibility(View.INVISIBLE);
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        adapter = new HistoryAdapter(null);
        binding.historyList.setAdapter(adapter);
        binding.historyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setListener(translation -> viewModel.onClickedItem(translation));
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                viewModel.removeItem(adapter.getTranslations().get(position));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.historyList);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_history_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete_all:
                viewModel.clearHistory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
