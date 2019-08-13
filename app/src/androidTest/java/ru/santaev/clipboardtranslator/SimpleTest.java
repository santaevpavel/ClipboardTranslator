package ru.santaev.clipboardtranslator;


import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.santaev.clipboardtranslator.ui.MainActivity;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SimpleTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testApplicationName() throws Exception {
        assertEquals("TestTranslatorApp", mActivityRule.getActivity().getApplication().getClass().getSimpleName());
    }
}