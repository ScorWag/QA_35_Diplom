package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;
import static ru.iteco.fmhandroid.ui.helpers.Helper.clickOnViewInteraction;
import static ru.iteco.fmhandroid.ui.helpers.Helper.fillField;
import static ru.iteco.fmhandroid.ui.helpers.Helper.hasItem;
import static ru.iteco.fmhandroid.ui.helpers.Helper.login;
import static ru.iteco.fmhandroid.ui.helpers.Helper.logout;
import static ru.iteco.fmhandroid.ui.helpers.Helper.tryShow;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
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
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Owner;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.helpers.DataHelper;
import ru.iteco.fmhandroid.ui.helpers.Helper;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.pages.NewsListPage;

@RunWith(AllureAndroidJUnit4.class)
public class CreateEditNewsAndroidTest {

    Helper helper = new Helper();
    CreateEditNewsPage createEditNewsPage = new CreateEditNewsPage();
    NewsListPage newsListPage = new NewsListPage();
    public static ActivityTestRule<AppActivity> mActivityScenarioRule =
            new ActivityTestRule<>(AppActivity.class);
    static Intent intent = new Intent(Intent.CATEGORY_LAUNCHER);

    static String login = DataHelper.getLogin();
    static String password = DataHelper.getPassword();
    String dateFormat = "dd.MM.yyyy";
    String timeFormat = "HH:mm";
    LocalDate date = LocalDate.now().plusDays(1);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
    String dateTest = dateFormatter.format(date);
    String timeTest = "10:35";
    int claimExecutorIndexTest = 0;
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
    @Description(value = "Создание новости из раздела \"Новости\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void addNews() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;

        helper.openCreateNewsScreen();

        createEditNewsPage.fillCreateEditNewsScreen(newsCategoryTextTest, 0, expectedNewsTitle,
                dateTest, timeTest, expectedNewsTitle);

        clickOnViewInteraction(createEditNewsPage.getSaveButton());
        tryShow(newsListPage.getNewsList());
        newsListPage.getNewsList()
                .check(matches(hasItem(hasDescendant(withText(expectedNewsTitle)))));
    }

    @Test
    @Description(value = "Удаление созданной новости")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void deleteNews() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);
        helper.openNewsControlPanel();

        newsListPage.clickOnDeleteNewsButtonOnCreatedNewsWithTitle(expectedNewsTitle);
        newsListPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(newsListPage.getButtonOk());

        newsListPage.getNewsList().check(matches(isDisplayed()))
                .check(matches(Matchers.not(hasItem(hasDescendant(withText(expectedNewsTitle))))));
    }

    @Test
    @Description(value = "Редактирование существующей новости с изменением всех полей")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
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

        createEditNewsPage.fillCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex,
                modifiedNewsTitle, modifiedDate, modifiedTime, modifiedNewsDescription);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

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
    @Description(value = "Редактирование существующей новости с изменением только поля " +
            "\"Категория\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editNewsWithChangeCategoryFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        String modifiedCategoryText = "Праздник";
        int modifiedCategoryIndex = 4;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                dateTest, timeTest, newsDescriptionTest);
        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);

        createEditNewsPage.chooseCategoryInCreateEditNewsScreen(modifiedCategoryText,
                modifiedCategoryIndex);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .check(matches(withText(modifiedCategoryText)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    @Description(value = "Редактирование существующей новости с изменением только поля " +
            "\"Заголовок\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editNewsWithChangeTitleFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        String modifiedNewsTitle = "Измененный " + dateForId;

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);
        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);

        fillField(createEditNewsPage.getTitleField(), modifiedNewsTitle);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        helper.openEditNewsScreenByTitle(modifiedNewsTitle);
        onView(withId(R.id.news_item_title_text_input_edit_text))
                .check(matches(withText(modifiedNewsTitle)));

        pressBack();
        helper.deleteCreatedNewsByTitle(modifiedNewsTitle);
    }

    @Test
    @Description(value = "Редактирование существующей новости с изменением только поля " +
            "\"Дата публикации\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editNewsWithChangeDateFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        LocalDate date = LocalDate.now().plusDays(20);
        String modifiedDate = dateFormatter.format(date);

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);
        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);

        createEditNewsPage.setPublishDate(modifiedDate);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                .check(matches(withText(modifiedDate)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    @Description(value = "Редактирование существующей новости с изменением только поля \"Время\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editNewsWithChangeTimeFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        String modifiedTime = "08:00";

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);
        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);

        createEditNewsPage.setPublishTime(modifiedTime);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        onView(withId(R.id.news_item_publish_time_text_input_edit_text))
                .check(matches(withText(modifiedTime)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    @Description(value = "Редактирование существующей новости с изменением только поля " +
            "\"Описание\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void editNewsWithChangeDescriptionFieldOnly() {
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        String modifiedNewsDescription = "Рост продуктивности персонала";

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);
        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);

        fillField(createEditNewsPage.getDescriptionField(), modifiedNewsDescription);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        onView(withId(R.id.news_item_description_text_input_edit_text))
                .check(matches(withText(modifiedNewsDescription)));

        pressBack();
        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    @Description(value = "Запрет создания новости с пустыми полями")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateNewsWithEmptyFields() {
        helper.openCreateNewsScreen();
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

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
    @Description(value = "Запрет создания новости с пустым полем \"Категория\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateNewsWithEmptyCategoryFieldOnly() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        createEditNewsPage.fillCreateEditNewsScreen("", claimExecutorIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_start_icon),
                isDescendantOfA(withId(R.id.news_item_category_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет создания новости с пустым полем \"Заголовок\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateNewsWithEmptyTitleFieldOnly() {
        helper.openCreateNewsScreen();
        createEditNewsPage.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest, "",
                dateTest, timeTest, newsDescriptionTest);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_title_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет создания новости с пустым полем \"Дата публикации\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateNewsWithEmptyDateFieldOnly() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        createEditNewsPage.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle,"", timeTest, newsDescriptionTest);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_create_date_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет создания новости с пустым полем \"Время\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateNewsWithEmptyTimeFieldOnly() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        createEditNewsPage.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, "", newsDescriptionTest);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_publish_time_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Запрет создания новости с пустым полем \"Описание\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void notAllowCreateNewsWithEmptyDescriptionFieldOnly() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;
        createEditNewsPage.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, "");
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        onView(withResourceName("message"))
                .inRoot(withDecorView(not(mActivityScenarioRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.news_item_description_text_input_layout))))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    @Description(value = "Отмена создания новости после заполнения полей в " +
            "\"Создание новости\" и нажатии \"Отмена\" ")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void cancelCreateNews() {
        helper.openCreateNewsScreen();
        Date dateForId = new Date();
        String expectedNewsTitle = newsTitleTest + dateForId;

        createEditNewsPage.fillCreateEditNewsScreen(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, dateTest, timeTest, newsDescriptionTest);
        clickOnViewInteraction(createEditNewsPage.getCancelButton());
        createEditNewsPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditNewsPage.getButtonOk());

        newsListPage.getNewsList()
                .check(matches(isDisplayed()))
                .check(matches(Matchers.not(hasItem(hasDescendant(withText(expectedNewsTitle))))));
    }

    @Test
    @Description(value = "Запрет сохранения отредактированной новости с  пустым полем " +
            "\"Заголовок\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
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

        createEditNewsPage.fillCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex,
                "", modifiedDate, modifiedTime, modifiedNewsDescription);
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

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
    @Description(value = "Запрет сохранения отредактированной новости с  пустым полем \"Описание\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
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
        createEditNewsPage.fillCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex,
                modifiedNewsTitle, modifiedDate, modifiedTime, "");
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

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
    @Description(value = "Изменение статуса существующей новости на \"Не активна\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void switchStatusNewsToNoActive() {
        LocalDateTime date = LocalDateTime.now();
        String expectedNewsTitle = newsTitleTest + date;
        String currentDate = dateFormatter.format(date);
        String currentTime = timeFormatter.format(date);

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest, expectedNewsTitle,
                currentDate, currentTime, newsDescriptionTest);
        helper.openNewsList();
        newsListPage.getNewsList()
                .check(matches(hasItem(hasDescendant(withText(expectedNewsTitle)))));

        clickOnViewInteraction(newsListPage.getEditNewsButton());
        newsListPage.getAddNewsButton().check(matches(isDisplayed()));
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        clickOnViewInteraction(createEditNewsPage.getSwitcher());
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        helper.goToTheMainScreen();
        helper.openNewsList();
        newsListPage.getNewsList().check(matches(isDisplayed()))
                .check(matches(Matchers.not(hasItem(hasDescendant(withText(expectedNewsTitle))))));

        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    @Description(value = "Изменение статуса существующей новости на \"Активна\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void switchStatusNewsToActive() {
        LocalDateTime date = LocalDateTime.now();
        String expectedNewsTitle = newsTitleTest + date;
        String currentDate = dateFormatter.format(date);
        String currentTime = timeFormatter.format(date);

        helper.createNews(newsCategoryTextTest, newsCategoryIndexTest,
                expectedNewsTitle, currentDate, currentTime, newsDescriptionTest);

        helper.openNewsControlPanel();
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        clickOnViewInteraction(createEditNewsPage.getSwitcher());
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        helper.goToTheMainScreen();
        helper.openNewsList();
        newsListPage.getNewsList().check(matches(isDisplayed()))
                .check(matches(Matchers.not(hasItem(hasDescendant(withText(expectedNewsTitle))))));

        clickOnViewInteraction(newsListPage.getEditNewsButton());
        newsListPage.getAddNewsButton().check(matches(isDisplayed()));
        helper.openEditNewsScreenByTitle(expectedNewsTitle);
        clickOnViewInteraction(createEditNewsPage.getSwitcher());
        clickOnViewInteraction(createEditNewsPage.getSaveButton());

        helper.goToTheMainScreen();
        helper.openNewsList();
        newsListPage.getNewsList().check(matches(isDisplayed()))
                .check(matches(hasItem(hasDescendant(withText(expectedNewsTitle)))));

        helper.deleteCreatedNewsByTitle(expectedNewsTitle);
    }

    @Test
    @Description(value = "Отмена сохранения внесенных изменений в существующую новость по " +
            "\"Отмена\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
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
        createEditNewsPage.fillCreateEditNewsScreen(modifiedCategoryText, modifiedCategoryIndex,
                modifiedNewsTitle, modifiedDate, modifiedTime, modifiedNewsDescription);
        clickOnViewInteraction(createEditNewsPage.getCancelButton());
        createEditNewsPage.getMessage().check(matches(isDisplayed()));
        clickOnViewInteraction(createEditNewsPage.getButtonOk());

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








