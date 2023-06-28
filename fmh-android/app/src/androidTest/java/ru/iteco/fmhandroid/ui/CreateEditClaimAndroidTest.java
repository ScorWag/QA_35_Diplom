package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static ru.iteco.fmhandroid.ui.helpers.DataHelper.generateSymbols;
import static ru.iteco.fmhandroid.ui.helpers.Helper.clickOnViewInteraction;
import static ru.iteco.fmhandroid.ui.helpers.Helper.fillField;
import static ru.iteco.fmhandroid.ui.helpers.Helper.hasItem;
import static ru.iteco.fmhandroid.ui.helpers.Helper.login;
import static ru.iteco.fmhandroid.ui.helpers.Helper.logout;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Owner;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.helpers.DataHelper;
import ru.iteco.fmhandroid.ui.helpers.Helper;
import ru.iteco.fmhandroid.ui.pages.ClaimListPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditClaimPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.pages.OpenClaimPage;

@RunWith(AllureAndroidJUnit4.class)
public class CreateEditClaimAndroidTest {

    Helper helper = new Helper();
    MainPage mainPage = new MainPage();
    CreateEditClaimPage createEditClaimPage = new CreateEditClaimPage();
    OpenClaimPage openClaimPage = new OpenClaimPage();
    ClaimListPage claimListPage = new ClaimListPage();
    public static ActivityTestRule<AppActivity> mActivityScenarioRule =
            new ActivityTestRule<>(AppActivity.class);
    static Intent intent = new Intent(Intent.CATEGORY_LAUNCHER);

    static String login = DataHelper.getLogin();
    static String password = DataHelper.getPassword();
    String dateFormat = "dd.MM.yyyy";
    LocalDate date = LocalDate.now().plusDays(1);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
    String dateTest = dateFormatter.format(date);
    String timeTest = "10:35";
    String claimTitleTest = "Тест ";
    String claimExecutorTextTest = "Ivanov Ivan Ivanovich";
    int claimExecutorIndexTest = 0;
    String claimDescriptionTest = "Требуется массаж";
    String claimCommentTest = "Комментарий";
    String cyrillicValue = "ru";
    String latinValue = "en";
//    String specSymbolsOnlyValue = "special";
//    String specSymbolsAndCyrillicValue = "specialRu";
//    String specSymbolsAndLatinValue = "specialEn";

    @BeforeClass
    public static void authorization() {
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
    public void registerIdlingResources() {
        IdlingRegistry.getInstance().register(FMHAndroidIdlingResources.idlingResource);
        mActivityScenarioRule.launchActivity(intent);
        onView(withId(R.id.container_list_news_include_on_fragment_main))
                .check(matches(isDisplayed()));
        onView(withId(R.id.container_list_claim_include_on_fragment_main))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResources() {
        mActivityScenarioRule.finishActivity();
        IdlingRegistry.getInstance().unregister(FMHAndroidIdlingResources.idlingResource);
    }

    @Test
    @Description(value = "Создание заявки из раздела \"Заявки\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void createClaimFromClaimList() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        claimListPage.getClaimList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(expectedClaimTitle)))));
    }

    @Test
    @Description(value = "Создание заявки из главного экрана")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void createClaimFromMainScreen() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        clickOnViewInteraction(mainPage.getAddClaimButton());
        createEditClaimPage.fillCreateEditClaimScreen(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        clickOnViewInteraction(mainPage.getMainMenuButton());
        clickOnViewInteraction(mainPage.getClaimsInMaimMenuButton());
        claimListPage.getClaimList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(expectedClaimTitle)))));
    }

    @Test
    @Description(value = "Валидация поля \"Тема\" в форме \"Создание заявки\" " +
            "со значением из одного символа")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.NORMAL)
    public void validationClaimTitleInputWithOneSymbol() {
        String oneSymbol = generateSymbols(1, cyrillicValue);

        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(oneSymbol, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        claimListPage.getClaimList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(oneSymbol)))));
    }

    @Test
    @Description(value = "Валидация поля \"Тема\" в форме \"Создание заявки\" " +
            "со значением из 49 символов")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.NORMAL)
    public void validationClaimTitleInputWithFortyNineSymbols() {
        String fortyNineSymbols = generateSymbols(49, cyrillicValue);

        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(fortyNineSymbols, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        claimListPage.getClaimList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(fortyNineSymbols)))));
    }

    @Test
    @Description(value = "Валидация поля \"Тема\" в форме \"Создание заявки\" " +
            "со значением из 50 символов")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.NORMAL)
    public void validationClaimTitleInputWithFiftySymbols() {
        String fiftySymbols = generateSymbols(50, cyrillicValue);

        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(fiftySymbols, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        claimListPage.getClaimList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(fiftySymbols)))));
    }

    @Test
    @Description(value = "Валидация поля \"Тема\" в форме \"Создание заявки\" " +
            "со значением на латинице")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.NORMAL)
    public void validationClaimTitleInputWithLatinSymbols() {
        String latinSymbols = generateSymbols(17, latinValue);

        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(latinSymbols, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        claimListPage.getClaimList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(latinSymbols)))));
    }

    @Test
    @Description(value = "Ограничение длины в поле \"Тема\" формы \"Создание заявки\" " +
            "со значением из 51 символа")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.NORMAL)
    public void limitLengthClaimTitleInputWithFiftyOneSymbols() {
        String fiftyOneSymbols = generateSymbols(51, cyrillicValue);
        String expectedTitle = fiftyOneSymbols.substring(0, fiftyOneSymbols.length() - 1);

        helper.openCreateClaimScreen();
        createEditClaimPage.getTitleField().check(matches(isDisplayed()))
                .perform(replaceText(fiftyOneSymbols)).perform(closeSoftKeyboard());

        createEditClaimPage.getTitleField().check(matches(withText(expectedTitle)));
    }

    @Test
    @Description(value = "Ограничение длины в поле \"Тема\" формы \"Создание заявки\" " +
            "со значением из 55 символов")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.NORMAL)
    public void limitLengthClaimTitleInputWithFiftyFiveSymbols() {
        String fiftyFiveSymbols = generateSymbols(55, cyrillicValue);
        String expectedTitle = fiftyFiveSymbols.substring(0, fiftyFiveSymbols.length() - 5);

        helper.openCreateClaimScreen();
        createEditClaimPage.getTitleField().check(matches(isDisplayed()))
                .perform(replaceText(fiftyFiveSymbols)).perform(closeSoftKeyboard());

        createEditClaimPage.getTitleField().check(matches(withText(expectedTitle)));
    }

    @Test
    @Description(value = "Обрезание пробелов по краям строки в поле \"Тема\" в форме " +
            "\"Создание заявки\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.NORMAL)
    public void validationClaimTitleInputTrim() {
        String expectedTitle = generateSymbols(20, cyrillicValue);
        String titleWithSpaces = " " + expectedTitle + " ";

        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(titleWithSpaces, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        claimListPage.getClaimList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(expectedTitle)))));
    }

    @Test
    @Description(value = "Запрет создания заявки с пустым полем \"Тема\" в \"Создание заявки\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateClaimWithEmptyTitleInput() {
        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen("", claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());

        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет создания заявки с пустым полем \"Дата\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateClaimWithEmptyDateInput() {
        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(claimTitleTest, claimExecutorTextTest,
                claimExecutorIndexTest, "", timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());

        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.date_in_plan_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет создания заявки с пустым полем \"Время\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateClaimWithEmptyTimeInput() {
        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(claimTitleTest, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, "", claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());

        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.time_in_plan_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет создания заявки с пустым полем \"Описание\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateClaimWithEmptyDescriptionInput() {
        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(claimTitleTest, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, "");
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());

        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет создания заявки с пустыми полями")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateClaimWithEmptyFields() {
        helper.openCreateClaimScreen();
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());

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
    @Description(value = "Отмена создания заявки после заполнения полей в \"Создание заявки\" " +
            "и нажатии \"Отмена\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void cancelCreateClaim() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.openCreateClaimScreen();
        createEditClaimPage.fillCreateEditClaimScreen(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        clickOnViewInteraction(createEditClaimPage.getCancelButton());
        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());
        claimListPage.getClaimList().check(matches(isDisplayed()))
                .check(matches(not(hasItem(hasDescendant(withText(expectedClaimTitle))))));
    }

    @Test
    @Description(value = "Изменение статуса существующей заявки на \"Выполнена\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void changeStatusClaimToExecute() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        clickOnViewInteraction(openClaimPage.getChangeStatusButton());
        openClaimPage.chooseStatusInDropDownListByIndex(1);
        helper.fillCommentFieldAndConfirm(claimCommentTest);
        clickOnViewInteraction(openClaimPage.getEditButton());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        openClaimPage.getChangeStatusButton().inRoot(withDecorView(is(mActivityScenarioRule
                .getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        clickOnViewInteraction(openClaimPage.getChangeStatusButton());
        onView(withClassName(is("android.widget.MenuPopupWindow$MenuDropDownListView")))
                .check(matches(not(isDisplayed())));
    }

    @Test
    @Description(value = "Изменение статуса существующей заявки на \"Открыта\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void changeStatusClaimToOpen() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);

        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        clickOnViewInteraction(openClaimPage.getChangeStatusButton());
        openClaimPage.chooseStatusInDropDownListByIndex(0);
        helper.fillCommentFieldAndConfirm(claimCommentTest);

        clickOnViewInteraction(openClaimPage.getEditButton());
        onView(withId(R.id.save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()));
    }

    @Test
    @Description(value = "Изменение статуса существующей заявки со статусом \"Открыта\" на " +
            "\"В работе\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void changeStatusClaimToInProgress() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);

        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        clickOnViewInteraction(openClaimPage.getChangeStatusButton());
        openClaimPage.chooseStatusInDropDownListByIndex(0);

        clickOnViewInteraction(openClaimPage.getEditButton());
        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    @Description(value = "Изменение статуса существующей заявки со статусом \"Открыта\" на " +
            "\"Отменена\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void changeStatusClaimToCancel() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);

        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);
        clickOnViewInteraction(openClaimPage.getChangeStatusButton());
        openClaimPage.chooseStatusInDropDownListByIndex(1);

        clickOnViewInteraction(openClaimPage.getEditButton());
        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        openClaimPage.getChangeStatusButton().inRoot(withDecorView(is(mActivityScenarioRule
                .getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()))
                .perform(click());
        onView(withClassName(is("android.widget.MenuPopupWindow$MenuDropDownListView")))
                .check(matches(not(isDisplayed())));
    }

    @Test
    @Description(value = "Редактирование существующей заявки с внесением изменений во все поля")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
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
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);
        clickOnViewInteraction(openClaimPage.getEditButton());
        createEditClaimPage.fillCreateEditClaimScreen(modifiedTitle, claimExecutorTextTest,
                claimExecutorIndexTest, modifiedDate, modifiedTime, modifiedClaimDescription);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

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
    @Description(value = "Редактирование существующей заявки с внесением изменений только в " +
            "поле \"Тема\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editClaimWithChangeTitleFieldOnly() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        String modifiedTitle = "Измененный " + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);
        clickOnViewInteraction(openClaimPage.getEditButton());
        fillField(createEditClaimPage.getTitleField(), modifiedTitle);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        onView(withId(R.id.title_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedTitle)));
    }

    @Test
    @Description(value = "Редактирование существующей заявки с внесением изменений только в " +
            "поле \"Дата\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editClaimWithChangeDateFieldOnly() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedDate = dateFormatter.format(date);

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);
        clickOnViewInteraction(openClaimPage.getEditButton());
        createEditClaimPage.setDateInPlan(modifiedDate);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        onView(withId(R.id.plane_date_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedDate)));
    }

    @Test
    @Description(value = "Редактирование существующей заявки с внесением изменений только в " +
            "поле \"Время\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editClaimWithChangeTimeFieldOnly() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        String modifiedTime = "17:19";

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);
        clickOnViewInteraction(openClaimPage.getEditButton());
        createEditClaimPage.setTime(modifiedTime);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        onView(withId(R.id.plan_time_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedTime)));
    }

    @Test
    @Description(value = "Редактирование существующей заявки с внесением изменений только в " +
            "поле \"Описание\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editClaimWithChangeDescriptionFieldOnly() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;
        String modifiedClaimDescription = "Требуется осмотр";

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);
        clickOnViewInteraction(openClaimPage.getEditButton());
        fillField(createEditClaimPage.getDescriptionField(), modifiedClaimDescription);
        clickOnViewInteraction(createEditClaimPage.getSaveButton());

        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(modifiedClaimDescription)));
    }

    @Test
    @Description(value = "Запрет сохранения изменений в заявке с пустым полем \"Тема\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowSaveEditClaimWithEmptyTitleField() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        clickOnViewInteraction(openClaimPage.getEditButton());
        helper.clearField(createEditClaimPage.getTitleField());
        clickOnViewInteraction(createEditClaimPage.getSaveButton());
        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());

        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет сохранения изменений в заявке с пустым полем \"Описание\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowSaveEditClaimWithEmptyDescriptionField() {
        Date dateForId = new Date();
        String expectedClaimTitle = claimTitleTest + dateForId;

        helper.createClaim(expectedClaimTitle, claimExecutorTextTest,
                claimExecutorIndexTest, dateTest, timeTest, claimDescriptionTest);
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        clickOnViewInteraction(openClaimPage.getEditButton());
        helper.clearField(createEditClaimPage.getDescriptionField());
        clickOnViewInteraction(createEditClaimPage.getSaveButton());
        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());

        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Отмена сохранения внесенных изменений в существующую заявку " +
            "по \"Отмена\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
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
        helper.openCreatedClaimOnClaimListWithTitle(expectedClaimTitle);
        helper.changeStatusClaimInProgressToOpen(claimCommentTest);

        clickOnViewInteraction(openClaimPage.getEditButton());
        createEditClaimPage.fillCreateEditClaimScreen(modifiedTitle, claimExecutorTextTest,
                claimExecutorIndexTest, modifiedDate, modifiedTime, modifiedClaimDescription);
        clickOnViewInteraction(createEditClaimPage.getCancelButton());
        createEditClaimPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditClaimPage.getButtonOk());

        onView(withId(R.id.title_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(expectedClaimTitle)));
        onView(withId(R.id.plane_date_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(dateTest)));
        onView(withId(R.id.plan_time_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(timeTest)));
        onView(withId(R.id.description_text_view)).check(matches(isDisplayed()))
                .check(matches(withText(claimDescriptionTest)));
    }
}








