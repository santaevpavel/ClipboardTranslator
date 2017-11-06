package ru.santaev.clipboardtranslator.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.databinding.ActivityMainBinding;
import ru.santaev.clipboardtranslator.util.Analytics;

import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_SETTINGS;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private boolean isTablet;
    private ActivityMainBinding binding;
    private Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        isTablet = binding.viewPager == null;
        if (isTablet){
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(binding.containerTranslate.getId(), TranslateFragment.Companion.newInstance())
                        .add(binding.containerHistory.getId(), HistoryFragment.newInstance())
                        .commit();
            }
        } else {
            binding.toolbar.setTitle(R.string.app_name);
            setSupportActionBar(binding.toolbar);

            binding.viewPager.setAdapter(new MainActivityViewPagerAdapter(getSupportFragmentManager()));
            binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
            binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                @Override
                public void onPageSelected(int position) {
                    switch (position){
                        case 0:
                            binding.bottomNavigation.getMenu().findItem(R.id.item_translate).setChecked(true);
                            break;
                        case 1:
                            binding.bottomNavigation.getMenu().findItem(R.id.item_history).setChecked(true);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {}
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_translate:
                binding.viewPager.setCurrentItem(0, true);
                break;
            case R.id.item_history:
                binding.viewPager.setCurrentItem(1, true);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS);
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
        }
        return false;
    }
}
