package ru.santaev.clipboardtranslator.ui

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

import ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_SETTINGS

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var isTablet: Boolean = false
    private lateinit var analytics: Analytics
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Analytics(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        isTablet = binding.viewPager == null
        if (isTablet) {
            if (savedInstanceState == null) {
                supportFragmentManager
                        .beginTransaction()
                        .add(binding.containerTranslate!!.id, TranslateFragment.newInstance())
                        .add(binding.containerHistory!!.id, HistoryFragment.newInstance())
                        .commit()
            }
        } else {
            binding.toolbar.setTitle(R.string.app_name)
            setSupportActionBar(binding.toolbar)

            binding.viewPager?.adapter = MainActivityViewPagerAdapter(supportFragmentManager)
            binding.bottomNavigation?.setOnNavigationItemSelectedListener(this)
            binding.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> binding.bottomNavigation?.menu?.findItem(R.id.item_translate)?.isChecked = true
                        1 -> binding.bottomNavigation?.menu?.findItem(R.id.item_history)?.isChecked = true
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_translate -> binding.viewPager?.setCurrentItem(0, true)
            R.id.item_history -> binding.viewPager?.setCurrentItem(1, true)
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
}
