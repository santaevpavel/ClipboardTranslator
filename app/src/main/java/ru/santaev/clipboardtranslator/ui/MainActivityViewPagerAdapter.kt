package ru.santaev.clipboardtranslator.ui


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

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
