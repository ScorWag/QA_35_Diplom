package ru.iteco.fmhandroid.ui.pages;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.helpers.Helper;

public class ClaimListPage {
    private final ViewInteraction claimList = onView(withId(R.id.claim_list_recycler_view));
    private final ViewInteraction addClaimButton = onView(withId(
            R.id.add_new_claim_material_button));
    private final ViewInteraction claimDescription = onView(withId(R.id.description_text_view));

    public ViewInteraction getClaimList() {
        return claimList;
    }

    public ViewInteraction getAddClaimButton() {
        return addClaimButton;
    }

    public ViewInteraction getOpenClaimButtonOnClaimWithTitle(String title) {
        return onView(allOf(
                withId(R.id.claim_list_card),
                hasDescendant(withText(title))));
    }

    public void clickOnClaimOnClaimListWithTitle (String claimTitle) {
        claimList.check(matches(isDisplayed()));
        claimList.perform(RecyclerViewActions
                .scrollTo(hasDescendant(withText(claimTitle))));
        Helper.clickOnViewInteraction(getOpenClaimButtonOnClaimWithTitle(claimTitle));
//        ViewInteraction openClaimButton = onView(allOf(
//                withId(R.id.claim_list_card),
//                hasDescendant(withText(claimTitle))));
//        openClaimButton.check(matches(isDisplayed()));
//        openClaimButton.perform(click());
    }

    public ViewInteraction getClaimDescription() {
        return claimDescription;
    }
}
