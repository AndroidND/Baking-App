package com.example.the_baking_app;


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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TabletEspressoTest {

    private MainActivity mActivity;
    private boolean mIsScreenSw600dp;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void tabletEspressoTest() {

        if (mIsScreenSw600dp) {
            // skip tablet test because phone detected
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
                                allOf(withId(R.id.left_group),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.tv_cookingstep_short_description), withText("Recipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_cookingsteps_list),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.rv_cookingsteps_list),
                                childAtPosition(
                                        withId(R.id.left_group),
                                        1)),
                        0),
                        isDisplayed()));
        linearLayout.perform(click());


        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tv_cookingstep_description), withText("Recipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cookingsteps_container),
                                        0),
                                1),
                        isDisplayed()));
//        textView5.check(matches(withText("Recipe Introduction")));
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
}
