package com.example.anagram

import androidx.test.ext.junit.rules.activityScenarioRule
import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HelloWorldEspressoTest {

    //@get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()
    @get:Rule val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test fun listGoesOverTheFold() {
        onView(withId(R.id.textView1)).check(matches(withText(STRING_TO_BE_TYPED)))
    }

    companion object {
        val STRING_TO_BE_TYPED = "Anagram puzzle (Kotlin) ...!"
    }
}