package ru.santaev.clipboardtranslator.ui;


import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.santaev.clipboardtranslator.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SimpleLanguageListTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void simpleLanguageListTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.origin_lang_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0)));
        appCompatTextView.perform(scrollTo(), click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.text), withText("джава"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("джава")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.text), withText("котлин"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("котлин")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
