package ru.santaev.clipboardtranslator;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("UnusedAssignment")
@RunWith(AndroidJUnit4.class)
public class TranslateTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    private static final String TEST_SRC = "Тест";

    @UiThreadTest
    @Test
    public void test() throws Exception {
       /* IDataModel dataModel = request -> {
            TranslateResponse item = new TranslateResponse();
            item.setLang("ru");
            item.setCode(200);

            ArrayList<String> text = new ArrayList<>();
            if (request.originText.equals(TEST_SRC)) {
                text.add("Test1");
            } else {
                text.add("Test2");
            }

            item.setText(text);

            return Single.just(item);
        };

        TranslateViewModel viewModel = new TranslateViewModel(dataModel);
        viewModel.onOriginTextChanged(TEST_SRC);

        Thread.sleep(1000);

        String value = LiveDataUtil.getValue(viewModel.getTranslatedText());
        Thread.sleep(20000);
        value = LiveDataUtil.getValue(viewModel.getTranslatedText());
        assertEquals(value, "Test1");*/
    }
}
