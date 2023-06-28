package ru.iteco.fmhandroid.ui.pages;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

public class AboutPage {
    private final ViewInteraction versionTitle = onView(withId(R.id.about_version_title_text_view));
    private final ViewInteraction backButton = onView(withId(R.id.about_back_image_button));
    private final ViewInteraction linkForPrivacyPolicy = onView(withId(
            R.id.about_privacy_policy_value_text_view));
    private final ViewInteraction linkForTermsOfUse = onView(
            withId(R.id.about_terms_of_use_value_text_view));

    public ViewInteraction getVersionTitle() {
        return versionTitle;
    }

    public ViewInteraction getBackButton() {
        return backButton;
    }

    public ViewInteraction getLinkForPrivacyPolicy() {
        return linkForPrivacyPolicy;
    }

    public ViewInteraction getLinkForTermsOfUse() {
        return linkForTermsOfUse;
    }
}


