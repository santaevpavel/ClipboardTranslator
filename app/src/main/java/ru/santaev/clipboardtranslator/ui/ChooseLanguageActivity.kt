package ru.santaev.clipboardtranslator.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.santaev.domain.dto.LanguageDto
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.databinding.ActivityChooseLanguageBinding
import ru.santaev.clipboardtranslator.ui.adapter.LanguageAdapter
import ru.santaev.clipboardtranslator.util.settings.AppPreference
import ru.santaev.clipboardtranslator.viewmodel.ChooseLanguageViewModel
import java.util.*

class ChooseLanguageActivity : AppCompatActivity() {

    private lateinit var viewModel: ChooseLanguageViewModel
    private lateinit var binding: ActivityChooseLanguageBinding
    private lateinit var adapter: LanguageAdapter
    private lateinit var appPreference: AppPreference
    private var languages: List<LanguageDto>? = null
    private var langOrigin: LanguageDto? = null
    private var langTarget: LanguageDto? = null
    private var chooseOrigin: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extractArguments()

        viewModel = ViewModelProviders.of(this).get(ChooseLanguageViewModel::class.java)
        appPreference = AppPreference.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.let {
            it.setTitle(R.string.choose_lang_activity_title)
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.languages.observe(this, Observer { languages ->
            this.languages = languages
            if (languages != null) {
                initAdapter()
            }
        })

        setResult(Activity.RESULT_CANCELED)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun extractArguments() {
        langOrigin = intent.getSerializableExtra(ARG_KEY_LANG_ORIGIN) as LanguageDto
        langTarget = intent.getSerializableExtra(ARG_KEY_LANG_TARGET) as LanguageDto
        chooseOrigin = intent.getBooleanExtra(ARG_KEY_CHOOSE_ORIGIN, true)
    }

    private fun initAdapter() {
        val supportedList: List<LanguageDto>?
        val unsupportedList: MutableList<LanguageDto>

        /*TranslateDirectionProvider translateDirectionProvider = new TranslateDirectionProvider();

        if (chooseOrigin){
            supportedList = translateDirectionProvider.getSupportedOriginLanguages(langTarget);
        } else {
            supportedList = translateDirectionProvider.getSupportedTargetLanguages(langOrigin);
        }*/
        supportedList = languages

        unsupportedList = ArrayList()
        unsupportedList.removeAll(supportedList!!)

        val recentLanguages = arrayListOf<LanguageDto>()//appPreference.lastUsedLanguages TODO

        adapter = LanguageAdapter(recentLanguages, supportedList, unsupportedList)

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter

        adapter.listener = { translation ->
            addLangToRecent(translation)
            val data = Intent()
            data.putExtra(RESULT_KEY_LANG, translation)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun addLangToRecent(language: LanguageDto) {
        // TODO
        /*val recentLanguages = appPreference.lastUsedLanguages
        if (!recentLanguages.contains(language)) {
            if (RECENT_LANGUAGES_MAX_SIZE <= recentLanguages.size) {
                recentLanguages.removeAt(recentLanguages.size - 1)
            }
            recentLanguages.add(0, language)
            appPreference.lastUsedLanguages = recentLanguages
        }*/
    }

    companion object {

        val ARG_KEY_LANG_ORIGIN = "ARG_KEY_LANG_ORIGIN"
        val ARG_KEY_LANG_TARGET = "ARG_KEY_LANG_TARGET"
        val ARG_KEY_CHOOSE_ORIGIN = "ARG_KEY_CHOOSE_ORIGIN"
        val RESULT_KEY_LANG = "RESULT_KEY_LANG"

        val RECENT_LANGUAGES_MAX_SIZE = 3

        fun getIntent(context: Context, origin: LanguageDto, target: LanguageDto,
                      chooseOrigin: Boolean): Intent {
            val intent = Intent(context, ChooseLanguageActivity::class.java)
            intent.putExtra(ARG_KEY_LANG_ORIGIN, origin)
            intent.putExtra(ARG_KEY_LANG_TARGET, target)
            intent.putExtra(ARG_KEY_CHOOSE_ORIGIN, chooseOrigin)
            return intent
        }
    }
}
