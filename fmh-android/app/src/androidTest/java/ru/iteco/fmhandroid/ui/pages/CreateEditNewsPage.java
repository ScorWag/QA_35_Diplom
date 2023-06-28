package ru.iteco.fmhandroid.ui.pages;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static ru.iteco.fmhandroid.ui.helpers.Helper.clickOnViewInteraction;
import static ru.iteco.fmhandroid.ui.helpers.Helper.fillField;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.helpers.Helper;

public class CreateEditNewsPage {
    private final Helper helper = new Helper();
    private final ViewInteraction categoryField = onView(withContentDescription(
            "Show dropdown menu"));
    private final ViewInteraction titleField = onView(withId(
            R.id.news_item_title_text_input_edit_text));
    private final ViewInteraction publishDateField = onView(
                withId(R.id.news_item_publish_date_text_input_edit_text));
    private final ViewInteraction publishTimeField = onView(
                withId(R.id.news_item_publish_time_text_input_edit_text));
    private final ViewInteraction descriptionField = onView(withId(
            R.id.news_item_description_text_input_edit_text));
    private final ViewInteraction saveButton = onView(withId(R.id.save_button));
    private final ViewInteraction cancelButton = onView(withId(R.id.cancel_button));
    private final ViewInteraction message = onView(withId(android.R.id.message));
    private final ViewInteraction buttonOk = onView(withId(android.R.id.button1));
    private final ViewInteraction switcher = onView(withId(R.id.switcher));

    public ViewInteraction getDescriptionField() {
        return descriptionField;
    }

    public ViewInteraction getCategoryField() {
        return categoryField;
    }

    public ViewInteraction getMessage() {
        return message;
    }

    public ViewInteraction getButtonOk() {
        return buttonOk;
    }

    public void chooseCategoryInCreateEditNewsScreen(
            String categoryText, int categoryIndex) {
        if (!categoryText.equals("")) {
            clickOnViewInteraction(categoryField);
            ViewInteraction category = onView(allOf(withParent(withClassName(is(
                            "android.widget.DropDownListView"))),
                    withParentIndex(categoryIndex))).inRoot(isPlatformPopup());
            clickOnViewInteraction(category);

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .check(matches(withText(categoryText)));
        }
    }

    public ViewInteraction getTitleField() {
        return titleField;
    }

    public ViewInteraction getPublishDateField() {
        return publishDateField;
    }

    public ViewInteraction getPublishTimeField() {
        return publishTimeField;
    }

    public ViewInteraction getSaveButton() {
        return saveButton;
    }

    public ViewInteraction getCancelButton() {
        return cancelButton;
    }

    public void setPublishDate(String date) {
        publishDateField.check(matches(isDisplayed()));
        helper.setDate(date, publishDateField);
    }

    public void setPublishTime(String time) {
        publishTimeField.check(matches(isDisplayed()));
        helper.setTime(time, publishTimeField);
    }

    public void fillCreateEditNewsScreen(String categoryText, int categoryIndex, String title,
                                         String date, String time, String description) {
        chooseCategoryInCreateEditNewsScreen(categoryText, categoryIndex);
        fillField(titleField, title);
        setPublishDate(date);
        setPublishTime(time);
        fillField(descriptionField, description);
    }

    public ViewInteraction getSwitcher() {
        return switcher;
    }
}
