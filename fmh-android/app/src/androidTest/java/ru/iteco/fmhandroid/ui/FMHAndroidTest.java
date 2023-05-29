package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static ru.iteco.fmhandroid.ui.DataGenerator.generateSymbols;
import static ru.iteco.fmhandroid.ui.Helper.hasItem;
import static ru.iteco.fmhandroid.ui.Helper.login;
import static ru.iteco.fmhandroid.ui.Helper.logout;
import static ru.iteco.fmhandroid.ui.Helper.tryShow;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.R;

@RunWith(AllureAndroidJUnit4.class)
public class FMHAndroidTest {

    Helper helper = new Helper();
    public static ActivityTestRule<AppActivity> mActivityScenarioRule =
            new ActivityTestRule<>(AppActivity.class);
    static Intent intent = new Intent(Intent.CATEGORY_LAUNCHER);

    static String login = "login2";
    static String password = "password2";
    String dateFormat = "dd.MM.yyyy";
    String timeFormat = "HH:mm";
    LocalDate date = LocalDate.now().plusDays(1);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
    String dateTest = dateFormatter.format(date);
    String timeTest = "10:35";
    String claimTitleTest = "Тест ";
    String claimExecutorTextTest = "Ivanov Ivan Ivanovich";
    int claimExecutorIndexTest = 0;
    String claimDescriptionTest = "Требуется массаж";
    String claimCommentTest = "Комментарий";
    String newsCategoryTextTest = "Объявление";
    int newsCategoryIndexTest = 0;
    String newsTitleTest = "Тест ";
    String newsDescriptionTest = "Индексация заработной платы";

    String cyrillicValue = "ru";
    String latinValue = "en";
//    String specSymbolsOnlyValue = "special";
//    String specSymbolsAndCyrillicValue = "specialRu";
//    String specSymbolsAndLatinValue = "specialEn";

    @BeforeClass
    public static void authorizationAndSetUp() {
        mActivityScenarioRule.launchActivity(intent);
        IdlingRegistry.getInstance().register(FMHAndroidIdlingResources.idlingResource);
        login(login,password);
        onView(withId(R.id.container_list_news_include_on_fragment_main))
                .check(matches(isDisplayed()));
        onView(withId(R.id.container_list_claim_include_on_fragment_main))
                .check(matches(isDisplayed()));
        mActivityScenarioRule.finishActivity();
        IdlingRegistry.getInstance().unregister(FMHAndroidIdlingResources.idlingResource);
    }

    @AfterClass
    public static void unAuthorization() {
        IdlingRegistry.getInstance().register(FMHAndroidIdlingResources.idlingResource);
        mActivityScenarioRule.launchActivity(intent);
        logout();
        mActivityScenarioRule.finishActivity();
        IdlingRegistry.getInstance().unregister(FMHAndroidIdlingResources.idlingResource);
    }

    @Before
    public void registerIdlingResourcesAndAuthorization() {
        IdlingRegistry.getInstance().register(FMHAndroidIdlingResources.idlingResource);
        mActivityScenarioRule.launchActivity(intent);
        onView(withId(R.id.container_list_news_include_on_fragment_main))
                .check(matches(isDisplayed()));
        onView(withId(R.id.container_list_claim_include_on_fragment_main))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResourcesAndLogout() {
        mActivityScenarioRule.finishActivity();
        IdlingRegistry.getInstance().unregister(FMHAndroidIdlingResources.idlingResource);
    }

    @Test
    public void createClaimFromClaimList() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(expectedClaimTitle)))));
    }

    @Test
    public void createClaimFromMainScreen() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        ViewInteraction addClaimButton = onView(withId(R.id.add_new_claim_material_button));
        addClaimButton.check(matches(isDisplayed()));
        addClaimButton.perform(click());
        helper.fillCreateEditClaimScreen(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.openClaimsList();
        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(expectedClaimTitle)))));
    }

    @Test
    public void validationClaimTitleInputWithOneSymbol() {
        String oneSymbol = generateSymbols(1, cyrillicValue);

        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(oneSymbol, claimExecutorTextTest, claimExecutorIndexTest,
                dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(oneSymbol)))));
    }

    @Test
    public void validationClaimTitleInputWithFortyNineSymbols() {
        String fortyNineSymbols = generateSymbols(49, cyrillicValue);

        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(fortyNineSymbols, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(fortyNineSymbols)))));
    }

    @Test
    public void validationClaimTitleInputWithFiftySymbols() {
        String fiftySymbols = generateSymbols(50, cyrillicValue);

        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(fiftySymbols, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(fiftySymbols)))));
    }

    @Test
    public void validationClaimTitleInputWithLatinSymbols() {
        String latinSymbols = generateSymbols(17, latinValue);

        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(latinSymbols, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(latinSymbols)))));
    }

    @Test
    public void limitLengthClaimTitleInputWithFiftyOneSymbols() {
        String fiftyOneSymbols = generateSymbols(51, cyrillicValue);
        String expectedTitle = fiftyOneSymbols.substring(0, fiftyOneSymbols.length() - 1);

        helper.openCreateClaimScreen();
        ViewInteraction titleField = onView(withId(R.id.title_edit_text));
        titleField.check(matches(isDisplayed()));
        titleField.perform(replaceText(fiftyOneSymbols));
        titleField.check(matches(withText(expectedTitle)));
    }

    @Test
    public void limitLengthClaimTitleInputWithFiftyFiveSymbols() {
        String fiftyOneSymbols = generateSymbols(55, cyrillicValue);
        String expectedTitle = fiftyOneSymbols.substring(0, fiftyOneSymbols.length() - 5);

        helper.openCreateClaimScreen();
        ViewInteraction titleField = onView(withId(R.id.title_edit_text));
        titleField.check(matches(isDisplayed()));
        titleField.perform(replaceText(fiftyOneSymbols));
        titleField.check(matches(withText(expectedTitle)));
    }

    @Test
    public void validationClaimTitleInputTrim() {
        String expectedTitle = generateSymbols(20, cyrillicValue);
        String titleWithSpaces = " " + expectedTitle + " ";

        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(titleWithSpaces, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.claim_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(expectedTitle)))));
    }

    @Test
    public void goToTheMainScreenFromNewsScreen() {
        ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
        ViewInteraction listNewsOnMainScreen = onView(
                withId(R.id.container_list_news_include_on_fragment_main))
                .check(matches(isDisplayed()));
        ViewInteraction listClaimOnMainScreen = onView(
                withId(R.id.container_list_claim_include_on_fragment_main))
                .check(matches(isDisplayed()));
        mainMenuButton.check(matches(isDisplayed()));
        mainMenuButton.perform(click());
        ViewInteraction newsInMaimMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(2)));
        newsInMaimMenuButton.check(matches(isDisplayed()));
        newsInMaimMenuButton.perform(click());
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()));
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

    @Test
    public void goToTheMainScreenFromClaimsScreen() {
        ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
        ViewInteraction listNewsOnMainScreen = onView(
                withId(R.id.container_list_news_include_on_fragment_main))
                .check(matches(isDisplayed()));
        ViewInteraction listClaimOnMainScreen = onView(
                withId(R.id.container_list_claim_include_on_fragment_main))
                .check(matches(isDisplayed()));
        mainMenuButton.check(matches(isDisplayed()));
        mainMenuButton.perform(click());
        ViewInteraction claimsInMaimMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(1)));
        claimsInMaimMenuButton.check(matches(isDisplayed()));
        claimsInMaimMenuButton.perform(click());
        ViewInteraction claimList = onView(withId(R.id.claim_list_recycler_view));
        claimList.check(matches(isDisplayed()));
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

    @Test
    public void backToThePreviousScreenFromAboutScreen() {
        ViewInteraction listNewsOnMainScreen = onView(
                withId(R.id.container_list_news_include_on_fragment_main));
        ViewInteraction listClaimOnMainScreen = onView(
                withId(R.id.container_list_claim_include_on_fragment_main));
        ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
        mainMenuButton.check(matches(isDisplayed()));
        mainMenuButton.perform(click());
        ViewInteraction aboutScreenInMaimMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(3)));
        aboutScreenInMaimMenuButton.check(matches(isDisplayed()));
        aboutScreenInMaimMenuButton.perform(click());
        onView(withId(R.id.about_version_title_text_view)).check(matches(isDisplayed()));
        ViewInteraction backButton = onView(withId(R.id.about_back_image_button));
        backButton.check(matches(isDisplayed()));
        backButton.perform(click());

        listClaimOnMainScreen.check(matches(isDisplayed()));
        listNewsOnMainScreen.check(matches(isDisplayed()));
    }

    @Test
    public void goToThePrivacyPolicyPageFromAboutScreen() {
        ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
        mainMenuButton.check(matches(isDisplayed()));
        mainMenuButton.perform(click());
        ViewInteraction aboutScreenInMaimMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(3)));
        aboutScreenInMaimMenuButton.check(matches(isDisplayed()));
        aboutScreenInMaimMenuButton.perform(click());
        ViewInteraction linkForPrivacyPolicy = onView(
                withId(R.id.about_privacy_policy_value_text_view));
        linkForPrivacyPolicy.check(matches(isDisplayed()));
        Intents.init();
        linkForPrivacyPolicy.perform(click());

        Intents.intended(hasData("https://vhospice.org/#/privacy-policy/"));
        Intents.intended(hasAction(Intent.ACTION_VIEW));
        Intents.release();
    }

    @Test
    public void goToTheTermsOfUsePageFromAboutScreen() {
        ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
        mainMenuButton.check(matches(isDisplayed()));
        mainMenuButton.perform(click());
        ViewInteraction aboutScreenInMaimMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(3)));
        aboutScreenInMaimMenuButton.check(matches(isDisplayed()));
        aboutScreenInMaimMenuButton.perform(click());
        ViewInteraction linkForPrivacyPolicy = onView(
                withId(R.id.about_terms_of_use_value_text_view));
        linkForPrivacyPolicy.check(matches(isDisplayed()));
        Intents.init();
        linkForPrivacyPolicy.perform(click());

        Intents.intended(hasData("https://vhospice.org/#/terms-of-use"));
        Intents.intended(hasAction(Intent.ACTION_VIEW));
        Intents.release();
    }

    @Test
    public void openCreatedClaimScreen() {
        Date dateForId = new Date();
        String createdClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(createdClaimTitle, claimExecutorTextTest, claimExecutorIndexTest,
                dateTest, timeTest, claimDescriptionTest);

        helper.openClaimsList();
        onView(withId(R.id.claim_list_recycler_view)).perform(RecyclerViewActions
                .scrollTo(hasDescendant(withText(createdClaimTitle))));
        ViewInteraction openClaimButton = onView(allOf(
                withId(R.id.claim_list_card),
                hasDescendant(withText(createdClaimTitle))));
        openClaimButton.check(matches(isDisplayed()));
        openClaimButton.perform(click());

        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(claimDescriptionTest)));
    }

    @Test
    public void notAllowCreateClaimWithEmptyTitleInput() {
        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen("", claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateClaimWithEmptyDateInput() {
        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(claimTitleTest, claimExecutorTextTest,
                claimExecutorIndexTest, "", timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.date_in_plan_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateClaimWithEmptyTimeInput() {
        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(claimTitleTest, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, "", claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.time_in_plan_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateClaimWithEmptyDescriptionInput() {
        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(claimTitleTest, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, "");
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateClaimWithEmptyFields() {
        helper.openCreateClaimScreen();
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.date_in_plan_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.time_in_plan_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void cancelCreateClaim() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.openCreateClaimScreen();
        helper.fillCreateEditClaimScreen(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.cancel_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());

        ViewInteraction allClaimsList = onView(withId(
                R.id.claim_list_recycler_view));
        allClaimsList.check(matches(isDisplayed()));
        allClaimsList.check(matches(not(hasItem(hasDescendant(withText(expectedClaimTitle))))));
    }

    @Test
    public void changeStatusClaimToExecute() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        ViewInteraction changeStatusButton = onView(withId(R.id.status_processing_image_button));
        changeStatusButton.check(matches(isDisplayed()));
        changeStatusButton.perform(click());
        ViewInteraction statusToExecute = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(1)));
        statusToExecute.check(matches(isDisplayed()));
        statusToExecute.perform(click());
        helper.fillCommentFieldAndConfirm(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        changeStatusButton.inRoot(withDecorView(is(mActivityScenarioRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));
        changeStatusButton.perform(click());
        onView(withClassName(is("android.widget.MenuPopupWindow$MenuDropDownListView")))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void changeStatusClaimToOpen() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);

        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        ViewInteraction changeStatusButton = onView(withId(R.id.status_processing_image_button));
        changeStatusButton.check(matches(isDisplayed()));
        changeStatusButton.perform(click());
        ViewInteraction statusToOpen = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(0)));
        statusToOpen.check(matches(isDisplayed()));
        statusToOpen.perform(click());
        helper.fillCommentFieldAndConfirm(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        onView(withId(R.id.save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()));
    }

    @Test
    public void changeStatusClaimToInProgress() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);

        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction changeStatusButton = onView(withId(R.id.status_processing_image_button));
        changeStatusButton.check(matches(isDisplayed()));
        changeStatusButton.perform(click());
        ViewInteraction statusToInProgress = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(0)));
        statusToInProgress.check(matches(isDisplayed()));
        statusToInProgress.perform(click());

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void changeStatusClaimToCancel() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);

        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction changeStatusButton = onView(withId(R.id.status_processing_image_button));
        changeStatusButton.check(matches(isDisplayed()));
        changeStatusButton.perform(click());
        ViewInteraction changeStatusToCanceled = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(1)));
        changeStatusToCanceled.check(matches(isDisplayed()));
        changeStatusToCanceled.perform(click());

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        changeStatusButton.inRoot(withDecorView(is(mActivityScenarioRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));
        changeStatusButton.perform(click());
        onView(withClassName(is("android.widget.MenuPopupWindow$MenuDropDownListView")))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void editClaimWithChangeAllFields() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        String modifiedTitle = "Измененный " + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedDate = dateFormatter.format(date);
        String modifiedTime = "17:19";
        String modifiedClaimDescription = "Требуется осмотр";

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        helper.fillCreateEditClaimScreen(modifiedTitle, claimExecutorTextTest,
                claimExecutorIndexTest, modifiedDate, modifiedTime, modifiedClaimDescription);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.title_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedTitle)));
        onView(withId(R.id.plane_date_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedDate)));
        onView(withId(R.id.plan_time_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedTime)));
        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedClaimDescription)));
    }

    @Test
    public void editClaimWithChangeTitleFieldOnly() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        String modifiedTitle = "Измененный " + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        helper.fillField(onView(withId(R.id.title_edit_text)), modifiedTitle);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.title_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedTitle)));
    }

    @Test
    public void editClaimWithChangeDateFieldOnly() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedDate = dateFormatter.format(date);

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        ViewInteraction dateInPlan = onView(withId(R.id.date_in_plan_text_input_edit_text));
        helper.setDate(modifiedDate, dateInPlan);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.plane_date_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedDate)));
    }

    @Test
    public void editClaimWithChangeTimeFieldOnly() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        String modifiedTime = "17:19";

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        ViewInteraction timeInput = onView(withId(R.id.time_in_plan_text_input_edit_text));
        helper.setTime(modifiedTime, timeInput);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.plan_time_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedTime)));
    }

    @Test
    public void editClaimWithChangeDescriptionFieldOnly() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        String modifiedClaimDescription = "Требуется осмотр";

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        helper.fillField(onView(withId(R.id.description_edit_text)), modifiedClaimDescription);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedClaimDescription)));
    }

    @Test
    public void notAllowSaveEditClaimWithEmptyTitleField() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        ViewInteraction titleField = onView(withId(R.id.title_edit_text));
        titleField.check(matches(isDisplayed()));
        titleField.perform(clearText());
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());

        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowSaveEditClaimWithEmptyDescriptionField() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        ViewInteraction titleField = onView(withId(R.id.description_edit_text));
        titleField.check(matches(isDisplayed()));
        titleField.perform(clearText());
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());

        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void cancelEditClaim() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        String modifiedTitle = "Измененный " + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedDate = dateFormatter.format(date);
        String modifiedTime = "17:19";
        String modifiedClaimDescription = "Требуется осмотр";

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimByTitleOnClaimList(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        ViewInteraction editButton = onView(withId(R.id.edit_processing_image_button));
        editButton.check(matches(isDisplayed()));
        editButton.perform(click());
        helper.fillCreateEditClaimScreen(modifiedTitle, claimExecutorTextTest,
                claimExecutorIndexTest, modifiedDate, modifiedTime, modifiedClaimDescription);
        ViewInteraction cancelButton = onView(withId(R.id.cancel_button));
        cancelButton.check(matches(isDisplayed()));
        cancelButton.perform(click());
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());

        onView(withId(R.id.title_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(expectedClaimTitle)));
        onView(withId(R.id.plane_date_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(dateTest)));
        onView(withId(R.id.plan_time_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(timeTest)));
        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(claimDescriptionTest)));
    }

    @Test
    public void showNewsControlPanel() {
        helper.openNewsList();
        ViewInteraction editNewsButton = onView(withId(R.id.edit_news_material_button));
        editNewsButton.check(matches(isDisplayed()));
        editNewsButton.perform(click());
        onView(withId(R.id.add_news_image_view)).check(matches(isDisplayed()));
    }

    @Test
    public void addNews() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;

        helper.openCreateNewsScreen();

        helper.fillCreateEditNewsScreen(newsCategoryTextTest, 0, expectedNewsTitle,
                dateTest, timeTest, expectedNewsTitle);

        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());
        tryShow(onView(withId(R.id.news_list_recycler_view)));
        onView(withId(R.id.news_list_recycler_view))
                .check(matches(hasItem(hasDescendant(withText(expectedNewsTitle)))));
    }

    @Test
    public void deleteNews() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        ViewInteraction newsList = onView(withId(R.id.news_list_recycler_view));
        newsList.check(matches(isDisplayed()));
        newsList.perform(RecyclerViewActions.scrollTo(hasDescendant(withText(expectedNewsTitle))));
        ViewInteraction deleteNewsButton = onView(allOf(withId(R.id.delete_news_item_image_view),
                hasSibling(withText(expectedNewsTitle))));
        deleteNewsButton.check(matches(isDisplayed()));
        deleteNewsButton.perform(click());
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());

        newsList.check(matches(isDisplayed()))
                .check(matches(Matchers.not(hasItem(hasDescendant(withText(expectedNewsTitle))))));
    }

    @Test
    public void openCreatedNewsInfoInNewsControlPanel() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);
        helper.openNewsControlPanel();
        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(expectedNewsTitle))));
        ViewInteraction newsCreatedCard = onView(allOf(withId(R.id.news_item_material_card_view),
        hasDescendant(withText(expectedNewsTitle))));
        newsCreatedCard.check(matches(isDisplayed()));
        newsCreatedCard.perform(click());

        onView(withId(R.id.news_list_recycler_view))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(expectedNewsTitle))));
        onView(allOf(withId(R.id.news_item_description_text_view),
                hasSibling(withText(expectedNewsTitle)))).check(matches(isDisplayed()));

        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void editNewsWithChangeAllFields() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedNewsTitle = "Измененный " + dateForId;
        String modifiedDate = dateFormatter.format(date);
        String modifiedTime = "08:00";
        String modifiedCategoryText = "Праздник";
        int modifiedCategoryIndex = 4;
        String modifiedNewsDescription = "Рост продуктивности персонала";

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        helper.fillCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex, modifiedNewsTitle,
                modifiedDate, modifiedTime, modifiedNewsDescription);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.openEditNewsScreenByTitle(modifiedNewsTitle);
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .check(matches(withText(modifiedCategoryText)));
        onView(withId(R.id.news_item_title_text_input_edit_text))
                .check(matches(withText(modifiedNewsTitle)));
        onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                .check(matches(withText(modifiedDate)));
        onView(withId(R.id.news_item_publish_time_text_input_edit_text))
                .check(matches(withText(modifiedTime)));
        onView(withId(R.id.news_item_description_text_input_edit_text))
                .check(matches(withText(modifiedNewsDescription)));

        pressBack();
        helper.deleteCreatedNewsByTitle(modifiedNewsTitle);
    }

    @Test
    public void editNewsWithChangeCategoryFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        String modifiedCategoryText = "Праздник";
        int modifiedCategoryIndex = 4;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        helper.chooseCategoryInCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .check(matches(withText(modifiedCategoryText)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void editNewsWithChangeTitleFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        String modifiedNewsTitle = "Измененный " + dateForId;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        helper.fillField(onView(withId(R.id.news_item_title_text_input_edit_text)),
                modifiedNewsTitle);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.openEditNewsScreenByTitle(modifiedNewsTitle);
        onView(withId(R.id.news_item_title_text_input_edit_text))
                .check(matches(withText(modifiedNewsTitle)));

        pressBack();
        helper.deleteCreatedNewsByTitle(modifiedNewsTitle);
    }

    @Test
    public void editNewsWithChangeDateFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedDate = dateFormatter.format(date);

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        ViewInteraction dateField = onView(withId
                (R.id.news_item_publish_date_text_input_edit_text));
        helper.setDate(modifiedDate, dateField);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());
        helper.openEditNewsScreenByTitle(expectedNewsTitle);

        onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                .check(matches(withText(modifiedDate)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void editNewsWithChangeTimeFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        String modifiedTime = "08:00";

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        ViewInteraction timeField = onView(withId
                (R.id.news_item_publish_time_text_input_edit_text));
        helper.setTime(modifiedTime, timeField);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        onView(withId(R.id.news_item_publish_time_text_input_edit_text))
                .check(matches(withText(modifiedTime)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void editNewsWithChangeDescriptionFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        String modifiedNewsDescription = "Рост продуктивности персонала";

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        helper.fillField(onView(
                withId(R.id.news_item_description_text_input_edit_text)), modifiedNewsDescription);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        onView(withId(R.id.news_item_description_text_input_edit_text))
                .check(matches(withText(modifiedNewsDescription)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void notAllowCreateNewsWithEmptyFields() {
        helper.openCreateNewsScreen();
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_start_icon),
                isDescendantOfA(withId(R.id.news_item_category_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_create_date_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_publish_time_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateNewsWithEmptyCategoryFieldOnly() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        helper.fillCreateEditNewsScreen("", claimExecutorIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_start_icon),
                isDescendantOfA(withId(R.id.news_item_category_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateNewsWithEmptyTitleFieldOnly() {
        helper.openCreateNewsScreen();
        helper.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest, "",
                dateTest, timeTest, newsDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateNewsWithEmptyDateFieldOnly() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        helper.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle,"", timeTest, newsDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_create_date_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateNewsWithEmptyTimeFieldOnly() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        helper.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, "", newsDescriptionTest);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_publish_time_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void notAllowCreateNewsWithEmptyDescriptionFieldOnly() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        helper.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, "");
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void cancelCreateNews() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        helper.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);
        ViewInteraction cancelButton = onView(withId(R.id.cancel_button));
        cancelButton.check(matches(isDisplayed()));
        cancelButton.perform(click());
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());

        onView(withId(R.id.news_list_recycler_view))
                .check(matches(isDisplayed()))
                .check(matches(Matchers.not(hasItem(hasDescendant(withText(expectedNewsTitle))))));
    }

    @Test
    public void notAllowSaveEditNewsWithEmptyTitleField() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedDate = dateFormatter.format(date);
        String modifiedTime = "08:00";
        String modifiedCategoryText = "Праздник";
        int modifiedCategoryIndex = 4;
        String modifiedNewsDescription = "Рост продуктивности персонала";

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        helper.fillCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex,
                "", modifiedDate, modifiedTime, modifiedNewsDescription);
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void notAllowSaveEditNewsWithEmptyDescriptionField() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedDate = dateFormatter.format(date);
        String modifiedNewsTitle = "Измененный " + dateForId;
        String modifiedTime = "08:00";
        String modifiedCategoryText = "Праздник";
        int modifiedCategoryIndex = 4;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        helper.fillCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex,
                modifiedNewsTitle, modifiedDate, modifiedTime, "");
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void switchStatusNewsToNoActive() {
        LocalDateTime date = LocalDateTime.now();
        String expectedNewsTitle = newsTitleTest + date;
        String currentDate = dateFormatter.format(date);
        String currentTime = timeFormatter.format(date);

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                currentDate, currentTime, newsDescriptionTest);
        helper.openNewsList();
        onView(withId(R.id.news_list_recycler_view))
                .check(matches(hasItem(hasDescendant(withText(expectedNewsTitle)))));

        ViewInteraction editNewsButton = onView(withId(R.id.edit_news_material_button));
        editNewsButton.check(matches(isDisplayed()));
        editNewsButton.perform(click());
        onView(withId(R.id.add_news_image_view)).check(matches(isDisplayed()));
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        ViewInteraction switcher = onView(withId(R.id.switcher));
        switcher.check(matches(isDisplayed()));
        switcher.perform(click());
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.goToTheMainScreen();
        helper.openNewsList();
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(Matchers.not(hasItem(hasDescendant(withText(expectedNewsTitle))))));

        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void switchStatusNewsToActive() {
        LocalDateTime date = LocalDateTime.now();
        String expectedNewsTitle = newsTitleTest + date;
        String currentDate = dateFormatter.format(date);
        String currentTime = timeFormatter.format(date);

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, currentDate, currentTime, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        ViewInteraction switcher = onView(withId(R.id.switcher));
        switcher.check(matches(isDisplayed()));
        switcher.perform(click());
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.goToTheMainScreen();
        helper.openNewsList();
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(Matchers.not(hasItem(hasDescendant(withText(expectedNewsTitle))))));

        ViewInteraction editNewsButton = onView(withId(R.id.edit_news_material_button));
        editNewsButton.check(matches(isDisplayed()));
        editNewsButton.perform(click());
        onView(withId(R.id.add_news_image_view)).check(matches(isDisplayed()));
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        switcher.check(matches(isDisplayed()));
        switcher.perform(click());
        saveButton.check(matches(isDisplayed()));
        saveButton.perform(click());

        helper.goToTheMainScreen();
        helper.openNewsList();
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(expectedNewsTitle)))));

        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    public void cancelSaveEditNews() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedNewsTitle = "Измененный " + dateForId;
        String modifiedDate = dateFormatter.format(date);
        String modifiedTime = "08:00";
        String modifiedCategoryText = "Праздник";
        int modifiedCategoryIndex = 4;
        String modifiedNewsDescription = "Рост продуктивности персонала";

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        helper.fillCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex, modifiedNewsTitle,
                modifiedDate, modifiedTime, modifiedNewsDescription);
        ViewInteraction cancelButton = onView(withId(R.id.cancel_button));
        cancelButton.check(matches(isDisplayed()));
        cancelButton.perform(click());
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));
        ViewInteraction buttonOk = onView(withId(android.R.id.button1));
        buttonOk.check(matches(isDisplayed()));
        buttonOk.perform(click());

        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .check(matches(withText(newsCategoryTextTest)));
        onView(withId(R.id.news_item_title_text_input_edit_text))
                .check(matches(withText(expectedNewsTitle)));
        onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                .check(matches(withText(dateTest)));
        onView(withId(R.id.news_item_publish_time_text_input_edit_text))
                .check(matches(withText(timeTest)));
        onView(withId(R.id.news_item_description_text_input_edit_text))
                .check(matches(withText(newsDescriptionTest)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }
}








