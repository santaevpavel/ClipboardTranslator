package ru.santaev.clipboardtranslator.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.databinding.ActivityMainBinding
import ru.santaev.clipboardtranslator.util.Analytics
import ru.santaev.clipboardtranslator.util.AnalyticsConstants.EVENT_ID_NAME_CLICK_SETTINGS
import ru.santaev.clipboardtranslator.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var analytics: Analytics
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        analytics = Analytics(this)

        initUi()
        handleIncomingText(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_translate -> binding.viewPager.setCurrentItem(0, true)
            R.id.item_history -> binding.viewPager.setCurrentItem(1, true)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS)
                startActivity(Intent(applicationContext, SettingsActivity::class.java))
                return true
            }
        }
        return false
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingText(intent, true)
    }

    private fun initUi() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.toolbar.setTitle(R.string.app_name)
        setSupportActionBar(binding.toolbar)

        binding.viewPager.adapter = MainActivityViewPagerAdapter(supportFragmentManager)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigation.menu.findItem(R.id.item_translate)?.isChecked = true
                    1 -> binding.bottomNavigation.menu.findItem(R.id.item_history)?.isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.isLoading = true
        binding.executePendingBindings()

        observeViewModel()
    }

    private fun handleIncomingText(intent: Intent, isNewIntent: Boolean = false) {
        if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val textFromIntent = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return
            viewModel.sharedText = textFromIntent
            if (isNewIntent) {
                val translateFragment = supportFragmentManager.findFragmentByTag(
                        makeFragmentName(binding.viewPager.id, 0)) as? TranslateFragment
                translateFragment?.onIncomingText(textFromIntent)
            }
        }

    }

    private fun makeFragmentName(viewPagerId: Int, index: Int): String {
        return "android:switcher:$viewPagerId:$index"
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this, Observer { isLoading ->
            binding.isLoading = isLoading
        })
    }

}
