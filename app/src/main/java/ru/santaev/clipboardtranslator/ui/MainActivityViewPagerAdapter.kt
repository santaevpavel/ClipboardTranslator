package ru.santaev.clipboardtranslator.ui


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainActivityViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            PAGE_TRANSLATE -> return TranslateFragment.newInstance()
            PAGE_HISTORY -> return HistoryFragment.newInstance()
            else -> throw IllegalArgumentException("Not found item for position " + position)
        }

    }

    override fun getCount(): Int {
        return 2
    }

    companion object {
        val PAGE_TRANSLATE: Int = 0
        val PAGE_HISTORY: Int = 1
    }
}
