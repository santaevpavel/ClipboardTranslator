package ru.santaev.clipboardtranslator.ui


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainActivityViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TranslateFragment.newInstance()
            1 -> HistoryFragment.newInstance()
            else -> throw IllegalArgumentException("Not found item for position " + position)
        }

    }

    override fun getCount(): Int {
        return 2
    }
}
