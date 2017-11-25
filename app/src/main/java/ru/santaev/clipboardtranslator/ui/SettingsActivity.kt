package ru.santaev.clipboardtranslator.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.databinding.ActivitySettingsBinding
import ru.santaev.clipboardtranslator.ui.settings.ListSettingsItem
import ru.santaev.clipboardtranslator.ui.settings.SettingsItem
import ru.santaev.clipboardtranslator.util.Analytics
import ru.santaev.clipboardtranslator.util.AnalyticsConstants.EVENT_ID_NAME_CLICK_SETTINGS_COPY_BUTTON
import ru.santaev.clipboardtranslator.util.AnalyticsConstants.EVENT_ID_NAME_CLICK_SETTINGS_FEEDBACK
import ru.santaev.clipboardtranslator.util.AnalyticsConstants.EVENT_ID_NAME_CLICK_SETTINGS_NOTIFICATION_TYPE
import ru.santaev.clipboardtranslator.util.AnalyticsConstants.EVENT_ID_NAME_CLICK_SETTINGS_RATE
import ru.santaev.clipboardtranslator.util.settings.AppPreference
import ru.santaev.clipboardtranslator.util.settings.ISettings
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var settings: ISettings
    private lateinit var binding: ActivitySettingsBinding

    private lateinit var settingsItemNotificationType: ListSettingsItem
    private lateinit var settingsItemNotificationCopyButton: ListSettingsItem
    private lateinit var settingsItemFeedback: SettingsItem
    private lateinit var settingsItemRate: SettingsItem
    private lateinit var settingsItemBuildVersion: SettingsItem

    private lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Analytics(this)
        settings = AppPreference.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setTitle(R.string.settings_activity_title)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initSettings()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSettings() {
        // Notification type
        settingsItemNotificationType = ListSettingsItem(this, R.string.settings_item_notif_title,
                R.array.settings_item_notification_type_text, R.array.settings_item_notification_type_value,
                ListSettingsItem.ISettingsPropertySetter { settings.notificationType = it },
                ListSettingsItem.ISettingsPropertyGetter { settings.notificationType })
        binding.itemNotif?.item = settingsItemNotificationType
        binding.itemNotif?.root?.setOnClickListener { v ->
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS_NOTIFICATION_TYPE)
            settingsItemNotificationType.onClick()
        }

        // Copy button
        settingsItemNotificationCopyButton = ListSettingsItem(this, R.string.settings_item_notif_btn_title,
                R.array.settings_item_notification_btn_text, R.array.settings_item_notification_btn_value,
                { value -> settings.isNotificationButtonEnabled = value != 0 }) { if (settings.isNotificationButtonEnabled) 1 else 0 }
        binding.itemNotifCopyButton?.item = settingsItemNotificationCopyButton
        binding.itemNotifCopyButton?.root?.setOnClickListener { v ->
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS_COPY_BUTTON)
            settingsItemNotificationCopyButton.onClick()
        }

        // Feedback
        settingsItemFeedback = SettingsItem(getString(R.string.settings_item_feedback_title),
                getString(R.string.settings_item_feedback_subtitle))
        binding.itemFeedback?.item = settingsItemFeedback
        binding.itemFeedback?.root?.setOnClickListener { v ->
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS_FEEDBACK)
            email()
        }

        // Rate
        settingsItemRate = SettingsItem(getString(R.string.settings_item_rate_title),
                getString(R.string.settings_item_rate_subtitle))
        binding.itemRate?.item = settingsItemRate
        binding.itemRate?.root?.setOnClickListener { v ->
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS_RATE)
            launchGooglePLay()
        }

        // App version
        settingsItemBuildVersion = SettingsItem(getString(R.string.settings_item_build_title),
                getBuildVersion())
        binding.itemBuildVersion?.item = settingsItemBuildVersion
    }

    private fun launchGooglePLay() {
        val uri = Uri.parse("market://details?id=" + packageName)
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
        }

    }

    private fun email() {
        val mailTo = "mailto:santaevp@gmail.com"

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailTo)

        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Unable to find mail app!", Toast.LENGTH_LONG).show()
        }

    }

    private fun getBuildVersion(): String {
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            val versionName = pInfo.versionName
            return String.format(Locale.ENGLISH, "%s (%d)", versionName, pInfo.versionCode)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "-"
    }

}
