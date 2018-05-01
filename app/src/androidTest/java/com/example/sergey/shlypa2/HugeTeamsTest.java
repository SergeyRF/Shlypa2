package com.example.sergey.shlypa2;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.sergey.shlypa2.game.Game;
import com.example.sergey.shlypa2.testUtils.CustomActions;
import com.example.sergey.shlypa2.testUtils.Utils;
import com.example.sergey.shlypa2.ui.FirstActivity;
import com.example.sergey.shlypa2.ui.RoundActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by alex on 4/29/18.
 */

@RunWith(AndroidJUnit4.class)
public class HugeTeamsTest {

    public static final int PLAYERS_COUNT = 50;
    public static final int TEAMS_COUNT = 2;

    @Rule
    public ActivityTestRule<FirstActivity> rule = new ActivityTestRule<>(FirstActivity.class);

    @Test
    public void testTeamCreating() {
       playGame();
    }

    @Test
    public void testInfiniteGameRunning() {
        while(true) {
            playGame();
            onView(withId(R.id.btCreateNewGame)).perform(click());
        }
    }

    private void playGame() {
        onView(withId(R.id.btNewGame)).perform(click());

        for (int i = 0; i <= PLAYERS_COUNT; i++) {
            onView(withId(R.id.btAddRandomPlayer)).perform(click());
        }

        onView(withId(R.id.btGoNextPlayers)).perform(click());
        onView(withId(R.id.btNextWords)).perform(click());

        onView(withId(R.id.btNextSettings)).perform(click());

        while (!(Utils.Companion.getCurrentActivity(InstrumentationRegistry.getInstrumentation())
                instanceof RoundActivity)) {
            onView(withId(R.id.btNextWords)).perform(click());
        }

        while (!saveCheckVisible(R.id.btCreateNewGame)) {
            playRound();
        }
    }

    private void playRound() {
        onView(withId(R.id.btBeginRound)).perform(click());

        while (!saveCheckVisible(R.id.btNextRound)) {
            playTurn();
        }

        int teamCount = Game.INSTANCE.getTeams().size();
        onView(withId(R.id.rvRoundResult)).perform(CustomActions.INSTANCE.smoothScrollTo(teamCount));
        SystemClock.sleep(2000);

        onView(withId(R.id.btNextRound)).perform(click());
    }

    private void playTurn() {
        onView(withId(R.id.btTurnStart)).perform(click());

        while (!saveCheckVisible(R.id.btFinishTurn) && saveCheckVisible(R.id.containerGame)) {
            onView(withId(R.id.containerGame)).perform(swipeUp());
        }

        int answeredCount = Game.INSTANCE.getRound().getWordsAnsweredByPlayer().size();
        onView(withId(R.id.rvWordsTurnResult)).perform(CustomActions.INSTANCE.smoothScrollTo(answeredCount));
        SystemClock.sleep(2000);

        onView(withId(R.id.btFinishTurn)).perform(click());
    }

    private boolean saveCheckVisible(int viewId) {
        ViewInteraction interaction = onView(withId(viewId));
        try {
            interaction.check(matches(isDisplayed()));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}