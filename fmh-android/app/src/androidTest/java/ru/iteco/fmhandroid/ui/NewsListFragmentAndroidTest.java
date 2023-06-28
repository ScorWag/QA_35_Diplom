package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.helpers.Helper.clickOnViewInteraction;
import static ru.iteco.fmhandroid.ui.helpers.Helper.login;
import static ru.iteco.fmhandroid.ui.helpers.Helper.logout;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
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
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.pages.NewsListPage;

@RunWith(AllureAndroidJUnit4.class)
public class NewsListFragmentAndroidTest {

    Helper helper = new Helper();
    MainPage mainPage = new MainPage();
    NewsListPage newsListPage = new NewsListPage();
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
    String newsCategoryTextTest = "Объявление";
    int newsCategoryIndexTest = 0;
    String newsTitleTest = "Тест ";
    String newsDescriptionTest = "Индексация заработной платы";

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
        mainPage.getListClaimOnMainScreen().check(matches(isDisplayed()));
        mainPage.getListNewsOnMainScreen().check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResources() {
        mActivityScenarioRule.finishActivity();
        IdlingRegistry.getInstance().unregister(FMHAndroidIdlingResources.idlingResource);
    }

    @Test
    @Description(value = "Навигация по меню - переход на главный экран по \"Главная\" " +
            "из раздела \"Новости\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void goToTheMainScreenFromNewsScreen() {
        helper.openNewsList();
        clickOnViewInteraction(mainPage.getMainMenuButton());
        clickOnViewInteraction(mainPage.getMainScreenMenuButton());

        mainPage.getListClaimOnMainScreen().check(matches(isDisplayed()));
        mainPage.getListNewsOnMainScreen().check(matches(isDisplayed()));
    }

    @Test
    @Description(value = "Отображение элементов панели управления создания/редактирования " +
            "новости в разделе \"Новости\" по соответствующему значку")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void showNewsControlPanel() {
        helper.openNewsList();
        clickOnViewInteraction(newsListPage.getEditNewsButton());

        newsListPage.getAddNewsButton().check(matches(isDisplayed()));
    }

    @Test
    @Description(value = "Открытие информации о новости в разделе \"Новости\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void openCreatedNewsInfoInNewsControlPanel() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);
        helper.openNewsControlPanel();
        newsListPage.getNewsList()
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(expectedNewsTitle))));
        clickOnViewInteraction(newsListPage.getCreatedNewsCardWithTitle(expectedNewsTitle));

        newsListPage.getNewsList()
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(expectedNewsTitle))));
        newsListPage.getCreatedNewsDescriptionWithTitle(expectedNewsTitle)
                .check(matches(isDisplayed()));

        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }
}








