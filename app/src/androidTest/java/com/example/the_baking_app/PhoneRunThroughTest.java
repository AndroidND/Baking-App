package com.example.the_baking_app;


import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PhoneRunThroughTest {
    private MainActivity mActivity;
    private boolean mIsScreenSw600dp;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void phoneRunThroughTest() {
        if (!mIsScreenSw600dp) {
            // skip phone test because tablet detected
            return;
        }

        ViewInteraction textView = onView(
                allOf(withText("Baking Time"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Baking Time")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rv_recipe_list),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction cardView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.rv_recipe_list),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1)),
                        0),
                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("Nutella Pie"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.cookingsteps_parent),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Nutella Pie")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.tv_ingredients), withText("Ingredients:\n ~ 2 cup Graham Cracker crumbs \n ~ 6 tblsp unsalted butter, melted \n ~ 0 cup granulated sugar \n ~ 1 tsp salt \n ~ 5 tblsp vanilla \n ~ 1 k Nutella or other chocolate-hazelnut spread \n ~ 500 g Mascapone Cheese(room temperature) \n ~ 1 cup heavy cream(cold) \n ~ 4 oz cream cheese(softened) "),
                        childAtPosition(
                                allOf(withId(R.id.cookingsteps_parent),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                                0)),
                                1),
                        isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.tv_cookingstep_short_description), withText("Recipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_cookingsteps_list),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Recipe Introduction")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tv_cookingstep_short_description), withText("Start filling prep"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_cookingsteps_list),
                                        4),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Start filling prep")));

        ViewInteraction linearLayout = onView(
                childAtPosition(
                        allOf(withId(R.id.rv_cookingsteps_list),
                                childAtPosition(
                                        withId(R.id.cookingsteps_parent),
                                        2)),
                        0));
        linearLayout.perform(scrollTo(), click());

        ViewInteraction textView6 = onView(
                allOf(withText("Nutella Pie"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("Nutella Pie")));

        ViewInteraction imageView = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));


        ViewInteraction textView7 = onView(
                allOf(withId(R.id.tv_cookingstep_description), withText("Recipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.player_view), withContentDescription("Hide player controls"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

        ViewInteraction frameLayout2 = onView(
                allOf(withId(R.id.player_view), withContentDescription("Hide player controls"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        frameLayout2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.next_step_button), withText("next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.tv_cookingstep_description), withText("1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.previous_step_button), withText("previous"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.previous_step_button), withText("previous"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.tv_cookingstep_description), withText("Recipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public boolean isScreenSw600dp() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float screenSw = Math.min(widthDp, heightDp);
        return screenSw >= 600;
    }
}
