package ru.iteco.fmhandroid.ui.helpers;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.pages.ClaimListPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditClaimPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.pages.LoginPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.pages.NewsListPage;
import ru.iteco.fmhandroid.ui.pages.OpenClaimPage;

public class Helper {
    private final static MainPage mainPage = new MainPage();
    private final static LoginPage loginPage = new LoginPage();
    private final static ClaimListPage claimListPage = new ClaimListPage();
    private final static CreateEditClaimPage createEditClaimPage = new CreateEditClaimPage();
    private final static OpenClaimPage openClaimPage = new OpenClaimPage();
    private final static NewsListPage newsListPage = new NewsListPage();
    private final static CreateEditNewsPage createEditNewsPage = new CreateEditNewsPage();

    public static Matcher<View> withPositionInMenuDropDownListView(int position) {
        return allOf(withParent(isMenuDropDownListView()), withParentIndex(position));
    }
    private static Matcher<View> isMenuDropDownListView() {
        return anyOf(
                withClassName(is("android.widget.MenuPopupWindow$MenuDropDownListView")),
                withClassName(is(
                        "androidx.appcompat.widget.MenuPopupWindow$MenuDropDownListView"))
        );
    }

    public static void tryShow(ViewInteraction viewInteraction) {
        try {
            viewInteraction.check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            onView(withId(android.R.id.message)).check(matches(isDisplayed()));
            ViewInteraction buttonOk = onView(withId(android.R.id.button1));
            buttonOk.check(matches(isDisplayed()));
            buttonOk.perform(click());
        }
    }

    public static Matcher<View> hasItem(Matcher<View> matcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override public void describeTo(Description description) {
                description.appendText("has item: ");
                matcher.describeTo(description);
            }

            @Override protected boolean matchesSafely(RecyclerView view) {
                RecyclerView.Adapter adapter = view.getAdapter();
                for (int position = 0; position < adapter.getItemCount(); position++) {
                    int type = adapter.getItemViewType(position);
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(view, type);
                    adapter.onBindViewHolder(holder, position);
                    if (matcher.matches(holder.itemView)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static String getText(final ViewInteraction view) {
        final String[] stringHolder = { null };
        view.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    public static void clickOnViewInteraction(ViewInteraction viewInteraction) {
        viewInteraction.check(matches(isDisplayed()));
        viewInteraction.perform(click());
    }

    public void clearField(ViewInteraction viewInteraction) {
        viewInteraction.check(matches(isDisplayed()));
        viewInteraction.perform(clearText());
    }

    public static void fillField(ViewInteraction viewInteractionField, String value) {
        ViewInteraction field = viewInteractionField;
        field.check(matches(isDisplayed()));
        field.perform(replaceText(value)).perform(closeSoftKeyboard());
        field.check(matches(withText(value)));
    }

    public static void login(String login, String password) {
        loginPage.enterLogin(login);
        loginPage.enterPassword(password);
        loginPage.clickEnterButton();
        onView(withId(R.id.trademark_image_view)).check(matches(isDisplayed()));
    }

    public static void logout() {
        boolean error = false;
        try {
            mainPage.getAuthorizationImage().check(matches(isDisplayed()));
        } catch (Error e) {
            error = true;
        }
        if (error == true) {
            pressBack();
        }
        clickOnViewInteraction(mainPage.getAuthorizationImage());
        clickOnViewInteraction(mainPage.getLogoutButton());

        loginPage.getLoginInput().check(matches(isDisplayed()));
        loginPage.getPasswordInput().check(matches(isDisplayed()));
    }

    public void goToTheMainScreen() {
        clickOnViewInteraction(mainPage.getMainMenuButton());
        clickOnViewInteraction(mainPage.getMainScreenMenuButton());
        mainPage.getListClaimOnMainScreen().check(matches(isDisplayed()));
        mainPage.getListNewsOnMainScreen().check(matches(isDisplayed()));
    }

    private int[] splitterDate (String date) {
        String[] dateSplit = date.split("\\.");
        int[] resultDate = new int[3];
        for (int i = 0; i < resultDate.length; i++) {
            resultDate[i] = Integer.parseInt(dateSplit[i]);
        }
        return resultDate;
    }

    public void setDate(String date, ViewInteraction dateInput) {
        int[] splitDate = new int[3];
        boolean exceptionForSplitterDate = false;
        try {
            splitDate = splitterDate(date);
        } catch (Exception e) {
            exceptionForSplitterDate = true;
        }
        if (!exceptionForSplitterDate) {
            int day = splitDate[0];
            int month = splitDate[1];
            int year = splitDate[2];
            dateInput.check(matches(isDisplayed()));
            dateInput.perform(click());
            ViewInteraction datePicker = onView(withClassName(is("android.widget.DatePicker")));
            datePicker.check(matches(isDisplayed()));
            datePicker.perform(PickerActions.setDate(year, month, day));
            ViewInteraction buttonOk = onView(withId(android.R.id.button1));
            buttonOk.check(matches(isDisplayed()));
            buttonOk.perform(click());
            dateInput.check(matches(isDisplayed())).check(matches(withText(date)));
        }
    }

    private int[] splitterTime (String time) {
        String[] timeSplit = time.split(":");
        int[] resultTime = new int[2];
        for (int i = 0; i < resultTime.length; i++) {
            resultTime[i] = Integer.parseInt(timeSplit[i]);
        }
        return resultTime;
    }

    public void setTime(String time, ViewInteraction timeInput) {
        int[] splitTime = new int[2];
        boolean exceptionForSplitterTime = false;
        try {
            splitTime = splitterTime(time);
        } catch (Exception e) {
            exceptionForSplitterTime = true;
        }
        if (!exceptionForSplitterTime) {
            int hour = splitTime[0];
            int minute = splitTime[1];

            timeInput.check(matches(isDisplayed()));
            timeInput.perform(click());
            ViewInteraction timePicker = onView(withClassName(is("android.widget.TimePicker")));
            timePicker.check(matches(isDisplayed()));
            timePicker.perform(PickerActions.setTime(hour, minute));
            ViewInteraction buttonOk = onView(withId(android.R.id.button1));
            buttonOk.check(matches(isDisplayed()));
            buttonOk.perform(click());
            timeInput.check(matches(isDisplayed())).check(matches(withText(time)));
        }
    }

    public void openCreateClaimScreen () {
        clickOnViewInteraction(mainPage.getMainMenuButton());
        clickOnViewInteraction(mainPage.getClaimsInMaimMenuButton());
        claimListPage.getClaimList().check(matches(isDisplayed()));
        clickOnViewInteraction(claimListPage.getAddClaimButton());
    }

    public void createClaim (String title, String executorText, int executorIndex,
                             String date, String time, String description) {
        openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(title, executorText, executorIndex, date,
                time, description);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());
        claimListPage.getClaimList().check(matches(isDisplayed()));
        goToTheMainScreen();
    }

    public void openNewsList() {
        clickOnViewInteraction(mainPage.getMainMenuButton());
        clickOnViewInteraction(mainPage.getNewsInMaimMenuButton());

        tryShow(newsListPage.getNewsList());
    }

    public void openNewsControlPanel() {
        openNewsList();
        clickOnViewInteraction(newsListPage.getEditNewsButton());

        newsListPage.getAddNewsButton().check(matches(isDisplayed()));
    }

    public void openCreateNewsScreen() {
        openNewsControlPanel();
        clickOnViewInteraction(newsListPage.getAddNewsButton());

        createEditNewsPage.getDescriptionField().check(matches(isDisplayed()));
    }

    public void openEditNewsScreenByTitle(String title) {
        newsListPage.clickOnEditNewsButtonOnCreatedNewsWithTitle(title);

        onView(withId(R.id.custom_app_bar_title_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.custom_app_bar_sub_title_text_view)).check(matches(isDisplayed()));
    }

    public void deleteCreatedNewsByTitle (String title) {
        goToTheMainScreen();
        openNewsControlPanel();
        newsListPage.clickOnDeleteNewsButtonOnCreatedNewsWithTitle(title);
        newsListPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(newsListPage.getButtonOk());

        newsListPage.getNewsList().check(matches(isDisplayed()))
                .check(matches(not(hasItem(hasDescendant(withText(title))))));
    }

    public void createNews (String categoryText, int categoryIndex, String title,
            String date, String time, String description) {
        openCreateNewsScreen();
        createEditNewsPage.fillCreateEditNewsScreen(categoryText,categoryIndex,
                title, date, time, description);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        tryShow(newsListPage.getNewsList());
        newsListPage.getNewsList()
                .check(matches(hasItem(hasDescendant(withText(title)))));

        goToTheMainScreen();
    }

    public void openCreatedClaimOnClaimListWithTitle(String claimTitle) {
        clickOnViewInteraction(mainPage.getMainMenuButton());
        clickOnViewInteraction(mainPage.getClaimsInMaimMenuButton());
        claimListPage.clickOnClaimOnClaimListWithTitle(claimTitle);

        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()));
    }

    public void fillCommentFieldAndConfirm (String comment) {
        fillField(openClaimPage.getCommentField(), comment);
        clickOnViewInteraction(openClaimPage.getButtonOk());
        openClaimPage.getCommentsList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(comment)))));
    }

    public void changeStatusClaimInProgressToOpen(String comment) {
        clickOnViewInteraction(openClaimPage.getChangeStatusButton());
        openClaimPage.chooseStatusInDropDownListByIndex(0);
        fillCommentFieldAndConfirm(comment);
        clickOnViewInteraction(openClaimPage.getEditButton());
        openClaimPage.getSaveButton().check(matches(isDisplayed()));
        openClaimPage.getCancelButton().check(matches(isDisplayed()));
        pressBack();
    }
}
