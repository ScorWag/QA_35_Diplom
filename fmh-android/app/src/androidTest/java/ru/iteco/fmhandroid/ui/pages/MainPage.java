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

public class MainPage {
    private final ViewInteraction addClaimButton = onView(withId(
            R.id.add_new_claim_material_button));
    private final ViewInteraction authorizationImage = onView(withId(
            R.id.authorization_image_button));
    private final ViewInteraction logoutButton = onView(withId(android.R.id.content));
    private final ViewInteraction listNewsOnMainScreen = onView(
            withId(R.id.container_list_news_include_on_fragment_main));
    private final ViewInteraction listClaimOnMainScreen = onView(
            withId(R.id.container_list_claim_include_on_fragment_main));
    private final ViewInteraction mainMenuButton = onView(withId(R.id.main_menu_image_button));
    private final ViewInteraction mainScreenMenuButton = onView(allOf(withParent(withClassName(is
                    ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
            withParentIndex(0)));
    private final ViewInteraction claimsInMaimMenuButton = onView(allOf(withParent(withClassName(is
                    ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
            withParentIndex(1)));
    private final ViewInteraction newsInMaimMenuButton = onView(allOf(withParent(withClassName(is
                    ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
            withParentIndex(2)));
    private final ViewInteraction aboutScreenInMaimMenuButton = onView(allOf(withParent(withClassName(is
                        ("android.widget.MenuPopupWindow$MenuDropDownListView"))),
                withParentIndex(3)));

    public ViewInteraction getAddClaimButton() {
        return addClaimButton;
    }

    public ViewInteraction getAuthorizationImage() {
        return authorizationImage;
    }

    public ViewInteraction getLogoutButton() {
        return logoutButton;
    }

    public ViewInteraction getListNewsOnMainScreen() {
        return listNewsOnMainScreen;
    }

    public ViewInteraction getListClaimOnMainScreen() {
        return listClaimOnMainScreen;
    }

    public ViewInteraction getMainMenuButton() {
        return mainMenuButton;
    }

    public ViewInteraction getMainScreenMenuButton() {
        return mainScreenMenuButton;
    }

    public ViewInteraction getClaimsInMaimMenuButton() {
        return claimsInMaimMenuButton;
    }

    public ViewInteraction getNewsInMaimMenuButton() {
        return newsInMaimMenuButton;
    }

    public ViewInteraction getAboutScreenInMaimMenuButton() {
        return aboutScreenInMaimMenuButton;
    }
}


