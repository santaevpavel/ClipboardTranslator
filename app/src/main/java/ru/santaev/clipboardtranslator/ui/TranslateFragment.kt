package ru.santaev.clipboardtranslator.ui


import android.animation.LayoutTransition
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.TranslatorApp
import ru.santaev.clipboardtranslator.databinding.FragmentTranslateBinding
import ru.santaev.clipboardtranslator.db.entity.Language
import ru.santaev.clipboardtranslator.model.TranslateDirectionProvider
import ru.santaev.clipboardtranslator.service.TranslateService
import ru.santaev.clipboardtranslator.util.Analytics
import ru.santaev.clipboardtranslator.util.Analytics.*
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel

class TranslateFragment : Fragment() {

    private lateinit var viewModel: TranslateViewModel
    private lateinit var binding: FragmentTranslateBinding
    private lateinit var translateDirectionProvider: TranslateDirectionProvider

    private lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Analytics(activity)
        translateDirectionProvider = TranslateDirectionProvider()
        val factory = ViewModelFactory(TranslatorApp.getInstance().dataModel)
        viewModel = ViewModelProviders.of(this, factory).get(TranslateViewModel::class.java)
        observeModel()

        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_translate, container, false)

        binding.originTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                viewModel.onOriginTextChanged(s.toString())
                binding.clear.visibility = if (s.isEmpty()) View.INVISIBLE else View.VISIBLE
            }
        })

        binding.clear.setOnClickListener {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_CLEAR_TEXT)
            binding.originTextView.setText("")
        }

        binding.originLangText.setOnClickListener { chooseOriginLang() }

        binding.targetLangText.setOnClickListener { chooseTargetLang() }

        binding.startService.setOnClickListener {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_START_SERVICE)
            activity.startService(Intent(context, TranslateService::class.java))
        }

        binding.stopService.setOnClickListener {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_STOP_SERVICE)
            activity.stopService(Intent(context, TranslateService::class.java))
        }

        binding.retry.setOnClickListener {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_RETRY)
            viewModel.onClickRetry()
        }

        binding.translatedByYandex.setOnClickListener({ openYandexTranslate() })

        enableAnimation()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.translate_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_swap_languages -> {
                viewModel.onClickSwipeLanguages()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun chooseOriginLang() {
        analytics.logClickEvent(EVENT_ID_NAME_CLICK_SOURCE_LANG)

        val intent = ChooseLanguageActivity.getIntent(context,
                viewModel.originLang.value,
                viewModel.targetLang.value, true)

        startActivityForResult(intent, REQUEST_CODE_ORIGIN_LANG)
    }

    private fun chooseTargetLang() {
        analytics.logClickEvent(EVENT_ID_NAME_CLICK_TARGET_LANG)

        val intent = ChooseLanguageActivity.getIntent(context,
                viewModel.originLang.value,
                viewModel.targetLang.value, false)
        startActivityForResult(intent, REQUEST_CODE_TARGET_LANG)
    }

    private fun observeModel() {
        viewModel.translatedText.observe(this, Observer<String?> { s ->
            if (s?.isEmpty() != false) {
                binding.translateLayout.visibility = View.GONE
            } else {
                analytics.logClickEvent(EVENT_ID_NAME_TRANSLATED)
                binding.translateLayout.visibility = View.VISIBLE
                binding.translatedTextView.text = s
            }
        })

        viewModel.progress.observe(this, Observer { progress ->
            progress?.let {
                if (progress == true) {
                    binding.translateLayout.visibility = View.VISIBLE
                }
                binding.translateProgress.visibility = if (progress == true) View.VISIBLE else View.INVISIBLE
            }

        })

        viewModel.originLang.observe(this, Observer { language ->
            language?.let {
                binding.originLangText.text = language.name
            }
        })

        viewModel.targetLang.observe(this, Observer { language ->
            language?.let {
                binding.targetLangText.text = language.name
            }
        })

        viewModel.failed.observe(this, Observer { isFailed ->
            isFailed?.let {
                analytics.logClickEvent(EVENT_ID_NAME_TRANSLATE_FAILED)
            }
            binding.retry.visibility = if (isFailed == true) View.VISIBLE else View.GONE
            binding.translatedByYandex.visibility = if (isFailed == true) View.GONE else View.VISIBLE
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_ORIGIN_LANG -> {
                val lang = (data?.getSerializableExtra(ChooseLanguageActivity.RESULT_KEY_LANG)) as? Language
                lang?.let {
                    analytics.logSelectEvent(EVENT_ID_SELECT_SOURCE_LANG + lang.code, lang.name)
                    viewModel.onOriginLangSelected(lang)
                }
            }
            REQUEST_CODE_TARGET_LANG -> {
                val lang = data?.getSerializableExtra(ChooseLanguageActivity.RESULT_KEY_LANG) as? Language
                lang?.let {
                    analytics.logSelectEvent(EVENT_ID_SELECT_TARGET_LANG + lang.code, lang.name)
                    viewModel.onTargetLangSelected(lang)
                }
            }
        }
    }

    private fun openYandexTranslate() {
        analytics.logClickEvent(EVENT_ID_NAME_CLICK_OPEN_YANDEX)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yandex_translate_url)))
        startActivity(browserIntent)
    }

    private fun enableAnimation() {
        binding.rootLinear.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    companion object {

        private val REQUEST_CODE_ORIGIN_LANG = 0
        private val REQUEST_CODE_TARGET_LANG = 1

        fun newInstance(): TranslateFragment {
            val fragment = TranslateFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
