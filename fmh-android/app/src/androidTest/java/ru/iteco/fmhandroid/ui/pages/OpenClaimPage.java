package ru.iteco.fmhandroid.ui.pages;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

public class OpenClaimPage {
    private static final ViewInteraction commentField = onView(withId(R.id.editText));
    private final ViewInteraction buttonOk = onView(withId(android.R.id.button1));
    private final ViewInteraction commentsList = onView(withId(
            R.id.claim_comments_list_recycler_view));
    private final ViewInteraction changeStatusButton = onView(withId(
            R.id.status_processing_image_button));
    private final ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
    private final ViewInteraction saveButton = onView(withId(R.id.save_button));
    private final ViewInteraction cancelButton = onView(withId(R.id.cancel_button));

    public ViewInteraction getCommentField() {
        return commentField;
    }

    public ViewInteraction getButtonOk() {
        return buttonOk;
    }

    public ViewInteraction getCommentsList() {
        return commentsList;
    }

    public ViewInteraction getChangeStatusButton() {
        return changeStatusButton;
    }

    public ViewInteraction getEditButton() {
        return editButton;
    }

    public ViewInteraction getSaveButton() {
        return saveButton;
    }

    public ViewInteraction getCancelButton() {
        return cancelButton;
    }

    public void chooseStatusInDropDownListByIndex(int index) {
        ViewInteraction dropDownList = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(index)));
        dropDownList.check(matches(isDisplayed()));
        dropDownList.perform(click());
    }
}
