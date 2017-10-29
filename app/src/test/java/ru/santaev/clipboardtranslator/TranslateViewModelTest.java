package ru.santaev.clipboardtranslator;

import org.junit.Test;

import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel;

public class TranslateViewModelTest {

    public static final String TEST_SRC = "Тест";

    @Test
    public void test() throws Exception {
        /*IDataModel dataModel = request -> {
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
        };*/

        TranslateViewModel viewModel = new TranslateViewModel(dataModel);
        viewModel.onOriginTextChanged(TEST_SRC);

        /*String value = LiveDataUtil.getValue(viewModel.getTranslatedText());
        assertEquals(value, "Test1");*/
    }
}