package ru.santaev.clipboardtranslator.ui.settings

import android.app.AlertDialog
import android.content.Context

typealias ISettingsPropertySetter = (Int) -> Unit
typealias ISettingsPropertyGetter = () -> Int

class ListSettingsItem(
        title: Int,
        arrayValue: Int,
        private val context: Context,
        private val textArray: Int,
        private val setter: ISettingsPropertySetter,
        private val getter: ISettingsPropertyGetter
) : SettingsItem(context.getString(title), "") {

    private val valueArray: IntArray
    private val titleString: String
    private val textArrayRes: Array<String>


    init {

        val resources = context.resources
        valueArray = resources.getIntArray(arrayValue)
        textArrayRes = context.resources.getStringArray(textArray)
        titleString = resources.getString(title)

        update()
    }

    fun onClick() {
        AlertDialog.Builder(context)
                .setTitle(titleString)
                .setItems(textArray) { dialogInterface, i ->
                    this.subtitle.set(textArrayRes[i])
                    setter.invoke(valueArray[i])
                    dialogInterface.dismiss()
                }
                .show()
    }

    private fun update() {
        val text = textArrayRes[getIdx(getter.invoke())]
        this.subtitle.set(text)
    }

    private fun getIdx(value: Int): Int {
        return valueArray.indices.firstOrNull { valueArray[it] == value } ?: 0
    }
}
