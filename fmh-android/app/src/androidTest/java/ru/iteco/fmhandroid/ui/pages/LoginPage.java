package ru.iteco.fmhandroid.ui.pages;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

public class LoginPage {
    private final ViewInteraction loginInput = onView(withInputType(1));
    private final ViewInteraction passwordInput = onView(withInputType(129));
    private final ViewInteraction enterButton = onView(withId(R.id.enter_button));

    public ViewInteraction getLoginInput() {
        return loginInput;
    }

    public ViewInteraction getPasswordInput() {
        return passwordInput;
    }

    public ViewInteraction getEnterButton() {
        return enterButton;
    }

    public void enterLogin(String login) {
        loginInput.check(matches(isDisplayed()));
        loginInput.perform(typeText(login)).perform(closeSoftKeyboard());
    }

    public void enterPassword(String password) {
        passwordInput.check(matches(isDisplayed()));
        passwordInput.perform(typeText(password)).perform(closeSoftKeyboard());
    }

    public void clickEnterButton() {
        enterButton.perform(click());
    }
}
