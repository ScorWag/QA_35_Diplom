package ru.iteco.fmhandroid.ui.pages;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import static ru.iteco.fmhandroid.ui.helpers.Helper.clickOnViewInteraction;
import static ru.iteco.fmhandroid.ui.helpers.Helper.tryShow;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import ru.iteco.fmhandroid.R;

public class NewsListPage {
    private final ViewInteraction newsList = onView(withId(R.id.news_list_recycler_view));
    private final ViewInteraction editNewsButton = onView(withId(R.id.edit_news_material_button));
    private final ViewInteraction addNewsButton = onView(withId(R.id.add_news_image_view));
    private final ViewInteraction message = onView(withId(android.R.id.message));
    private final ViewInteraction buttonOk = onView(withId(android.R.id.button1));

    public ViewInteraction getNewsList() {
        return newsList;
    }

    public ViewInteraction getEditNewsButton() { return editNewsButton; }

    public ViewInteraction getAddNewsButton() {
        return addNewsButton;
    }

    public ViewInteraction getMessage() {
        return message;
    }

    public ViewInteraction getButtonOk() {
        return buttonOk;
    }

    public ViewInteraction getCreatedNewsCardWithTitle(String title) {
        return onView(allOf(withId(R.id.news_item_material_card_view),
                hasDescendant(withText(title))));
    }

    public ViewInteraction getCreatedNewsDescriptionWithTitle(String title) {
        return onView(allOf(withId(R.id.news_item_description_text_view),
                hasSibling(withText(title))));
    }

//
    public void clickOnEditNewsButtonOnCreatedNewsWithTitle (String title) {
        tryShow(newsList);
        newsList.perform(RecyclerViewActions.scrollTo(hasDescendant(withText(title))));
        ViewInteraction editNewsButton = onView(allOf(withId(
                R.id.edit_news_item_image_view), hasSibling(withText(title))));
        clickOnViewInteraction(editNewsButton);
    }

    public void clickOnDeleteNewsButtonOnCreatedNewsWithTitle (String title) {
        tryShow(newsList);
        newsList.perform(RecyclerViewActions.scrollTo(hasDescendant(withText(title))));
        ViewInteraction deleteNewsButton = onView(allOf(withId(
                R.id.delete_news_item_image_view), hasSibling(withText(title))));
        clickOnViewInteraction(deleteNewsButton);
    }



//        claimList.check(matches(isDisplayed()));
//        claimList.perform(RecyclerViewActions
//                .scrollTo(hasDescendant(withText(claimTitle))));
//        ViewInteraction openClaimButton = onView(allOf(
//                withId(R.id.claim_list_card),
//                hasDescendant(withText(claimTitle))));
//        openClaimButton.check(matches(isDisplayed()));
//        openClaimButton.perform(click());

}
