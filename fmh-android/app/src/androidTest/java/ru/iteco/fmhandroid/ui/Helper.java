package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
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
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import ru.iteco.fmhandroid.R;

public class Helper {
    Helper() {
    }

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

    public static void  login(String login, String password) {
        ViewInteraction loginInput = onView(withInputType(1));
        loginInput.check(matches(isDisplayed()));
        loginInput.perform(typeText(login));
        ViewInteraction passwordInput = onView(withInputType(129));
        passwordInput.check(matches(isDisplayed()));
        passwordInput.perform(typeText(password)).perform(closeSoftKeyboard());
        ViewInteraction enterButton = onView(withId(R.id.enter_button));
        enterButton.perform(click());
        onView(withId(R.id.trademark_image_view)).check(matches(isDisplayed()));
    }

    public static void logout() {
        boolean error = false;
        try {
            onView(withId(R.id.authorization_image_button)).check(matches(isDisplayed()));
        } catch (Error e) {
            error = true;
        }
        if (error == true) {
            pressBack();
        }
        ViewInteraction authorizationImage = onView(withId(R.id.authorization_image_button));
        authorizationImage.check(matches(isDisplayed()));
        authorizationImage.perform(click());
        ViewInteraction logoutButton = onView(withId(android.R.id.content));
        logoutButton.check(matches(isDisplayed()));
        logoutButton.perform(click());
        ViewInteraction loginInput = onView(withInputType(1));
        loginInput.check(matches(isDisplayed()));
        ViewInteraction passwordInput = onView(withInputType(129));
        passwordInput.check(matches(isDisplayed()));
    }

    public void goToTheMainScreen() {
        ViewInteraction listNewsOnMainScreen = onView(
                withId(R.id.container_list_news_include_on_fragment_main));
        ViewInteraction listClaimOnMainScreen = onView(
                withId(R.id.container_list_claim_include_on_fragment_main));
        ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
        mainMenuButton.check(matches(isDisplayed()));
        mainMenuButton.perform(click());
        ViewInteraction mainScreenMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(0)));
        mainScreenMenuButton.check(matches(isDisplayed()));
        mainScreenMenuButton.perform(click());

        listClaimOnMainScreen.check(matches(isDisplayed()));
        listNewsOnMainScreen.check(matches(isDisplayed()));
    }

    public void fillField(ViewInteraction viewInteractionField, String value) {
        ViewInteraction field = viewInteractionField;
        field.check(matches(isDisplayed()));
        field.perform(replaceText(value)).perform(closeSoftKeyboard());
        field.check(matches(withText(value)));
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

    public void openClaimsList() {
        ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
        mainMenuButton.check(matches(isDisplayed()));
        mainMenuButton.perform(click());
        ViewInteraction claimsInMaimMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(1)));
        claimsInMaimMenuButton.check(matches(isDisplayed()));
        claimsInMaimMenuButton.perform(click());
        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()));
    }

    public void openCreateClaimScreen () {
        openClaimsList();
        ViewInteraction addClaimButton = onView(withId(R.id.add_new_claim_material_button));
        addClaimButton.check(matches(isDisplayed()));
        addClaimButton.perform(click());
    }

    public void chooseExecutorInCreateEditClaimScreen(String executorText, int executorIndex) {
            ViewInteraction executorField = onView(withId(
                    R.id.executor_drop_menu_auto_complete_text_view));
            executorField.check(matches(isDisplayed()));
            executorField.perform(click());
            ViewInteraction dropDownList = onView(allOf(withParent(withClassName(is(
                            "android.widget.DropDownListView"))),
                    withParentIndex(executorIndex))).inRoot(isPlatformPopup());
            dropDownList.check(matches(isDisplayed()));
            dropDownList.perform(click());
            executorField.check(matches(withText(executorText)));
    }

    public void fillCreateEditClaimScreen (String title, String executorText, int executorIndex,
                                           String date, String time, String description) {

        fillField(onView(withId(R.id.title_edit_text)), title);
        if (!executorText.equals("")) {
            chooseExecutorInCreateEditClaimScreen(executorText, executorIndex);
        }
        ViewInteraction dateInPlan = onView(withId(R.id.date_in_plan_text_input_edit_text));
        setDate(date, dateInPlan);
        ViewInteraction timeInput = onView(withId(R.id.time_in_plan_text_input_edit_text));
        setTime(time, timeInput);
        fillField(onView(withId(R.id.description_edit_text)), description);
    }

    public void createClaim (String title, String executorText, int executorIndex,
                             String date, String time, String description) {
        openCreateClaimScreen();
        fillCreateEditClaimScreen(title, executorText, executorIndex, date,
                time, description);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());
        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()));

        goToTheMainScreen();
    }

    public void fillCommentFieldAndConfirm (String comment) {
        fillField(onView(withId(R.id.editText)), comment);
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());
        ViewInteraction commentsList = onView(withId(R.id.claim_comments_list_recycler_view));
        commentsList.check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(comment)))));
    }

    public void changeStatusClaimInProgressToOpen(
            String comment) {
        ViewInteraction changeStatusButton = onView(withId(R.id.status_processing_image_button));
        changeStatusButton.check(matches(isDisplayed()));
        changeStatusButton.perform(click());

        ViewInteraction statusToOpen = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(0)));
        statusToOpen.check(matches(isDisplayed()));
        statusToOpen.perform(click());

        fillCommentFieldAndConfirm(comment);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        onView(withId(R.id.save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()));
        pressBack();
    }

    public void openCreatedClaimByTitleOnClaimList (String claimTitle) {
        openClaimsList();
        onView(withId(R.id.claim_list_recycler_view)).perform(RecyclerViewActions
                .scrollTo(hasDescendant(withText(claimTitle))));
        ViewInteraction openClaimButton = onView(allOf(
                withId(R.id.claim_list_card),
                hasDescendant(withText(claimTitle))));
        openClaimButton.check(matches(isDisplayed()));
        openClaimButton.perform(click());

        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()));
    }

    public void openNewsList() {
        ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
        mainMenuButton.check(matches(isDisplayed()));
        mainMenuButton.perform(click());
        ViewInteraction claimsInMaimMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(2)));
        claimsInMaimMenuButton.check(matches(isDisplayed()));
        claimsInMaimMenuButton.perform(click());
        tryShow(onView(withId(R.id.news_list_recycler_view)));
    }

    public void openNewsControlPanel() {
        openNewsList();
        ViewInteraction editNewsButton = onView(withId(R.id.edit_news_material_button));
        editNewsButton.check(matches(isDisplayed()));
        editNewsButton.perform(click());
        onView(withId(R.id.add_news_image_view)).check(matches(isDisplayed()));
    }

    public void openCreateNewsScreen() {
        openNewsControlPanel();
        ViewInteraction addNewsButton = onView(withId(R.id.add_news_image_view));
        addNewsButton.check(matches(isDisplayed()));
        addNewsButton.perform(click());
        onView(withId(R.id.news_item_description_text_input_edit_text))
                .check(matches(isDisplayed()));
    }

    public void openEditNewsScreenByTitle(String title) {
        tryShow(onView(withId(R.id.news_list_recycler_view)));
        ViewInteraction newsList = onView(withId(R.id.news_list_recycler_view));
        newsList.perform(RecyclerViewActions.scrollTo(hasDescendant(withText(title))));
        ViewInteraction editNewsButtonForChangeNews = onView(allOf(withId(
                R.id.edit_news_item_image_view),
                hasSibling(withText(title))));
        editNewsButtonForChangeNews.check(matches(isDisplayed()));
        editNewsButtonForChangeNews.perform(click());
        onView(withId(R.id.custom_app_bar_title_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.custom_app_bar_sub_title_text_view)).check(matches(isDisplayed()));
    }

    public void deleteCreatedNewsByTitle (String title) {
        goToTheMainScreen();
        openNewsControlPanel();

        tryShow(onView(withId(R.id.news_list_recycler_view)));
        ViewInteraction newsList = onView(withId(R.id.news_list_recycler_view));
        newsList.perform(RecyclerViewActions.scrollTo(hasDescendant(withText(title))));
        ViewInteraction deleteNewsButton = onView(allOf(withId(R.id.delete_news_item_image_view),
                hasSibling(withText(title))));
        deleteNewsButton.check(matches(isDisplayed()));
        deleteNewsButton.perform(click());
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());
        newsList.check(matches(isDisplayed()))
                .check(matches(not(hasItem(hasDescendant(withText(title))))));
    }

    public void chooseCategoryInCreateEditNewsScreen(
            String categoryText, int categoryIndex) {
        if (!categoryText.equals("")) {
            ViewInteraction categoryField = onView(withContentDescription("Show dropdown menu"));
            categoryField.check(matches(isDisplayed()));
            categoryField.perform(click());
            ViewInteraction dropDownList = onView(allOf(withParent(withClassName(is(
                            "android.widget.DropDownListView"))),
                    withParentIndex(categoryIndex))).inRoot(isPlatformPopup());
            dropDownList.check(matches(isDisplayed()));
            dropDownList.perform(click());
            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .check(matches(withText(categoryText)));
        }
    }

    public void fillCreateEditNewsScreen(String categoryText, int categoryIndex, String title,
                                         String date, String time, String description) {
        chooseCategoryInCreateEditNewsScreen(categoryText, categoryIndex);
        fillField(onView(withId(R.id.news_item_title_text_input_edit_text)), title);
        ViewInteraction publishDateInput = onView(
                withId(R.id.news_item_publish_date_text_input_edit_text));
        setDate(date, publishDateInput);
        ViewInteraction timeInput = onView(
                withId(R.id.news_item_publish_time_text_input_edit_text));
        setTime(time, timeInput);
        fillField(onView(
                withId(R.id.news_item_description_text_input_edit_text)), description);
    }

    public void createNews (String categoryText, int categoryIndex, String title,
            String date, String time, String description) {
        openCreateNewsScreen();
        fillCreateEditNewsScreen(categoryText,categoryIndex, title, date, time, description);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        tryShow(onView(withId(R.id.news_list_recycler_view)));

        onView(withId(R.id.news_list_recycler_view))
                .check(matches(hasItem(hasDescendant(withText(title)))));

        goToTheMainScreen();
    }
}
