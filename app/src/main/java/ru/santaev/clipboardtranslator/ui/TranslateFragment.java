package ru.santaev.clipboardtranslator.ui;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.databinding.FragmentTranslateBinding;
import ru.santaev.clipboardtranslator.model.Language;
import ru.santaev.clipboardtranslator.service.TranslateService;
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel;

public class TranslateFragment extends LifecycleFragment {

    private TranslateViewModel viewModel;
    private FragmentTranslateBinding binding;

    public TranslateFragment() {
        // Required empty public constructor
    }

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TranslateViewModel.class);
        observeModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translate, container, false);

        binding.originTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onOriginTextChanged(s.toString());
                binding.clear.setVisibility(s.length() == 0 ? View.INVISIBLE : View.VISIBLE);
            }
        });

        binding.clear.setOnClickListener(v -> binding.originTextView.setText(""));

        Language[] languages = Language.values();
        String[] languagesString = new String[languages.length];
        for (int i = 0; i < languages.length; i++) {
            languagesString[i] = languages[i].toString();
        }
        binding.originLangText.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                .setTitle("Выберите язык")
                .setItems(languagesString, (dialog, which) -> viewModel.onOriginLangSelected(languages[which]))
                .show());

        binding.targetLangText.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                .setTitle("Выберите язык")
                .setItems(languagesString, (dialog, which) -> viewModel.onTargetLangSelected(languages[which]))
                .show());

        binding.startService.setOnClickListener(v -> getActivity().startService(new Intent(getContext(), TranslateService.class)));

        binding.stopService.setOnClickListener(v -> getActivity().stopService(new Intent(getContext(), TranslateService.class)));

        binding.retry.setOnClickListener(v -> viewModel.onClickRetry());

        return binding.getRoot();
    }

    private void observeModel(){
        viewModel.getTranslatedText().observe(this, s -> {
            if (null != binding) {
                if (s == null || s.isEmpty()){
                    binding.translateLayout.setVisibility(View.GONE);
                } else {
                    binding.translateLayout.setVisibility(View.VISIBLE);
                    binding.translatedTextView.setText(s);
                }
            }
        });

        viewModel.getProgress().observe(this, progress -> {
            if (null != binding) {
                if (progress) {
                    binding.translateLayout.setVisibility(View.VISIBLE);
                }
                binding.translateProgress.setVisibility(progress ? View.VISIBLE : View.INVISIBLE);
            }
        });

        viewModel.getOriginLang().observe(this, language -> {
            if (null != binding) {
                binding.originLangText.setText(language.toString());
            }
        });

        viewModel.getTargetLang().observe(this, language -> {
            if (null != binding) {
                binding.targetLangText.setText(language.toString());
            }
        });

        viewModel.getFailed().observe(this, isFailed -> {
            if (null != binding) {
                binding.retry.setVisibility(isFailed ? View.VISIBLE : View.GONE);
            }
        });
    }

}
