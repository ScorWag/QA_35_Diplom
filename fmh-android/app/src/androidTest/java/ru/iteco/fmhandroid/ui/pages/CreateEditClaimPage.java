package ru.iteco.fmhandroid.ui.pages;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static ru.iteco.fmhandroid.ui.helpers.Helper.clickOnViewInteraction;
import static ru.iteco.fmhandroid.ui.helpers.Helper.fillField;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.helpers.Helper;

public class CreateEditClaimPage {
    private final Helper helper = new Helper();
    private final ViewInteraction executorField = onView(withId(
            R.id.executor_drop_menu_auto_complete_text_view));
    private final ViewInteraction titleField = onView(withId(R.id.title_edit_text));
    private final ViewInteraction dateInPlan = onView(withId(
            R.id.date_in_plan_text_input_edit_text));
    private final ViewInteraction timeInput = onView(withId(
            R.id.time_in_plan_text_input_edit_text));
    private final ViewInteraction descriptionField = onView(withId(R.id.description_edit_text));
    private final ViewInteraction saveButton = onView(withId(R.id.save_button));
    private final ViewInteraction cancelButton = onView(withId(R.id.cancel_button));
    private final ViewInteraction message = onView(withId(android.R.id.message));
    private final ViewInteraction buttonOk = onView(withId(android.R.id.button1));

    public ViewInteraction getExecutorField() {
        return executorField;
    }

    public ViewInteraction getTitleField() {
        return titleField;
    }

    public ViewInteraction getDateInPlan() {
        return dateInPlan;
    }

    public ViewInteraction getTimeInput() {
        return timeInput;
    }

    public ViewInteraction getDescriptionField() {
        return descriptionField;
    }

    public ViewInteraction getSaveButton() {
        return saveButton;
    }

    public ViewInteraction getCancelButton() {
        return cancelButton;
    }

    public ViewInteraction getMessage() {
        return message;
    }

    public ViewInteraction getButtonOk() {
        return buttonOk;
    }

    public void chooseExecutorOnDropDownListByIndex(int executorIndex) {
        ViewInteraction dropDownList = onView(allOf(withParent(withClassName(is(
                        "android.widget.DropDownListView"))),
                withParentIndex(executorIndex))).inRoot(isPlatformPopup());
        dropDownList.check(matches(isDisplayed()));
        dropDownList.perform(click());
    }

    public void setDateInPlan(String date) {
        dateInPlan.check(matches(isDisplayed()));
        helper.setDate(date, dateInPlan);
    }

    public void setTime(String time) {
        timeInput.check(matches(isDisplayed()));
        helper.setTime(time, timeInput);
    }

    public void fillCreateEditClaimScreen (String title, String executorText, int executorIndex,
                                           String date, String time, String description) {
        fillField(titleField, title);
        titleField.check(matches(withText(title)));
        if (!executorText.equals("")) {
            clickOnViewInteraction(executorField);
            chooseExecutorOnDropDownListByIndex(executorIndex);
            executorField.check(matches(withText(executorText)));
        }
        setDateInPlan(date);
        setTime(time);
        fillField(descriptionField, description);
    }
}
