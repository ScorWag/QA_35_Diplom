package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ru.iteco.fmhandroid.ui.helpers.Helper.clickOnViewInteraction;
import static ru.iteco.fmhandroid.ui.helpers.Helper.login;
import static ru.iteco.fmhandroid.ui.helpers.Helper.logout;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Owner;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.helpers.DataHelper;
import ru.iteco.fmhandroid.ui.pages.AboutPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

@RunWith(AllureAndroidJUnit4.class)
public class AboutFragmentAndroidTest {
    public static ActivityTestRule<AppActivity> mActivityScenarioRule =
            new ActivityTestRule<>(AppActivity.class);
    static Intent intent = new Intent(Intent.CATEGORY_LAUNCHER);

    static String login = DataHelper.getLogin();
    static String password = DataHelper.getPassword();
    MainPage mainPage = new MainPage();
    AboutPage aboutPage = new AboutPage();

    @BeforeClass
    public static void authorization() {
        mActivityScenarioRule.launchActivity(intent);
        IdlingRegistry.getInstance().register(FMHAndroidIdlingResources.idlingResource);
        login(login, password);
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
    public void registerIdlingResourcesAndOpenAboutScreen() {
        IdlingRegistry.getInstance().register(FMHAndroidIdlingResources.idlingResource);
        mActivityScenarioRule.launchActivity(intent);
        mainPage.getListClaimOnMainScreen().check(matches(isDisplayed()));
        mainPage.getListNewsOnMainScreen().check(matches(isDisplayed()));
        clickOnViewInteraction(mainPage.getMainMenuButton());
        clickOnViewInteraction(mainPage.getAboutScreenInMaimMenuButton());
    }

    @After
    public void unregisterIdlingResources() {
        mActivityScenarioRule.finishActivity();
        IdlingRegistry.getInstance().unregister(FMHAndroidIdlingResources.idlingResource);
    }

    @Test
    @Description(value = "Возврат из раздела \"О приложении\" на предыдущий экран")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void backToThePreviousScreenFromAboutScreen() {
        aboutPage.getVersionTitle().check(matches(isDisplayed()));
        clickOnViewInteraction(aboutPage.getBackButton());

        mainPage.getListClaimOnMainScreen().check(matches(isDisplayed()));
        mainPage.getListNewsOnMainScreen().check(matches(isDisplayed()));
    }

    @Test
    @Description(value = "Открытие политики конфиденциальности по ссылке из " +
            "раздела \"О приложении\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void goToThePrivacyPolicyPageFromAboutScreen() {
        Intents.init();
        clickOnViewInteraction(aboutPage.getLinkForPrivacyPolicy());

        Intents.intended(hasData("https://vhospice.org/#/privacy-policy/"));
        Intents.intended(hasAction(Intent.ACTION_VIEW));
        Intents.release();
    }

    @Test
    @Description(value = "Открытие пользовательского соглашения по ссылке из " +
            "раздела \"О приложении\"")
    @Owner(value = "Николаев Игорь Вячеславович")
    @Severity(value = SeverityLevel.CRITICAL)
    public void goToTheTermsOfUsePageFromAboutScreen() {
        Intents.init();
        clickOnViewInteraction(aboutPage.getLinkForTermsOfUse());

        Intents.intended(hasData("https://vhospice.org/#/terms-of-use"));
        Intents.intended(hasAction(Intent.ACTION_VIEW));
        Intents.release();
    }
}








