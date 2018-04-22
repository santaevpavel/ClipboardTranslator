package ru.santaev.clipboardtranslator.util

fun makeViewPagerFragmentTag(viewPagerId: Int, index: Int): String {
    return "android:switcher:$viewPagerId:$index"
}