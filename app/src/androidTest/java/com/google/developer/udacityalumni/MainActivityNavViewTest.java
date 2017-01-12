package com.google.developer.udacityalumni;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import com.google.developer.udacityalumni.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityNavViewTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void openNavDrawer() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        String navigateUpDesc = mActivityTestRule.getActivity()
                .getString(android.support.v7.appcompat.R.string.abc_action_bar_up_description);
        onView(withContentDescription(navigateUpDesc)).perform(click());

        // Check if drawer is open
        onView(withId(R.id.drawer))
                .check(matches(isOpen(Gravity.START))); // Left drawer is open open.

        //Check if navigation drawer contains profile pic
        onView(withContentDescription(R.string.profile_pic))
                .check(matches(isDisplayed()));

        //Check if navigation drawer contains the word 'Udacity'
        onView(withText("Udacity"))
                .check(matches(isDisplayed()));

    }







}
