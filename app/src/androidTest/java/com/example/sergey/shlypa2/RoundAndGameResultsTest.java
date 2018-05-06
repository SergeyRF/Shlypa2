package com.example.sergey.shlypa2;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.sergey.shlypa2.beans.Player;
import com.example.sergey.shlypa2.beans.Word;
import com.example.sergey.shlypa2.game.Game;
import com.example.sergey.shlypa2.game.Round;
import com.example.sergey.shlypa2.game.TeamWithScores;
import com.example.sergey.shlypa2.testUtils.Utils;
import com.example.sergey.shlypa2.ui.FirstActivity;
import com.example.sergey.shlypa2.ui.RoundActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.sergey.shlypa2.testUtils.RvActions.withRecyclerView;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by alex on 5/6/18.
 */

@RunWith(AndroidJUnit4.class)
public class RoundAndGameResultsTest {

    public static final int PLAYERS_COUNT = 4;

    @Rule
    public ActivityTestRule<FirstActivity> rule = new ActivityTestRule<>(FirstActivity.class);

    @Test
    public void testTurnResult() {
        startGame();

        Game.INSTANCE.getSettings().setTime(10);
        onView(withId(R.id.btBeginRound)).perform(click());

        playTurn(4, 3);

        RecyclerView view =
                Utils.Companion.getCurrentActivity(InstrumentationRegistry.getInstrumentation())
                        .findViewById(R.id.rvWordsTurnResult);

        assertEquals(7, view.getAdapter().getItemCount());

        onView(withId(R.id.btFinishTurn)).perform(click());

        List<TeamWithScores> list = Game.INSTANCE.getRoundResults();

        Collections.sort(list, new Comparator<TeamWithScores>() {
            @Override
            public int compare(TeamWithScores o1, TeamWithScores o2) {
                return o2.getScores() - o1.getScores();
            }
        });

        assertEquals(4, list.get(0).getScores());
    }

    //Run round, run turn, answers words then checks correctness of results
    @Test
    public void testWordsAnsweredCorrect() {
        startGame();

        //Answers order
        List<Boolean> listOfAnswers = new ArrayList<>();
        Collections.addAll(listOfAnswers, true, true, false, false, true, false);

        Game.INSTANCE.getSettings().setTime(5);
        onView(withId(R.id.btBeginRound)).perform(click());
        onView(withId(R.id.btTurnStart)).perform(click());

        //Get current player
        Player player = Game.INSTANCE.getRound().getPlayer();

        List<String> answeredWords = new ArrayList<>();

        //answer words
        for (boolean b : listOfAnswers) {
            TextView tvWord = Utils.Companion
                    .getCurrentActivity(InstrumentationRegistry.getInstrumentation())
                    .findViewById(R.id.tv_word);

            answeredWords.add(tvWord.getText().toString());

            if (b) {
                onView(withId(R.id.containerGame)).perform(swipeUp());
            } else {
                onView(withId(R.id.containerGame)).perform(swipeDown());
            }
        }

        while (!saveCheckVisible(R.id.btFinishTurn)) {
            SystemClock.sleep(500);
        }

        Round round = Game.INSTANCE.getRound();
        List<Word> wordList = round.getWordsAnsweredByPlayer();

        //Check answers in the game and state of holders
        for (int i = 0; i < listOfAnswers.size(); i++) {
            assertTrue(listOfAnswers.get(i) == wordList.get(i).getRight());

            int id = listOfAnswers.get(i) ? R.id.radioCorrect : R.id.radioWrong;
            onView(withRecyclerView(R.id.rvWordsTurnResult).atPositionOnView(i, id))
                    .check(matches(isChecked()));

            onView(withRecyclerView(R.id.rvWordsTurnResult).atPositionOnView(i, R.id.word_out))
                    .check(matches(withText(answeredWords.get(i))));
        }

        //Finish turn and check player scores
        onView(withId(R.id.btFinishTurn)).perform(click());
        int expectedScores = 0;
        for (boolean answer : listOfAnswers) {
            if (answer) expectedScores++;
            else if (Game.INSTANCE.getSettings().getMinusBal()) {
                expectedScores -= Game.INSTANCE.getSettings().getNumberMinusBal();
            }
        }

        List<TeamWithScores> teamsWithScores = Game.INSTANCE.getRoundResults();
        int playerScores = 0;
        for (TeamWithScores team : teamsWithScores) {
            if (team.getScoresMap().containsKey(player.getId())) {
                playerScores = team.getScoresMap().get(player.getId());
            }
        }
        assertEquals(expectedScores, playerScores);
    }

    public void playTurn(int right, int wrong) {
        onView(withId(R.id.btTurnStart)).perform(click());
        for (int i = 0; i < right; i++) {
            onView(withId(R.id.containerGame)).perform(swipeUp());
        }

        for (int i = 0; i < wrong; i++) {
            onView(withId(R.id.containerGame)).perform(swipeDown());
        }

        while (!saveCheckVisible(R.id.btFinishTurn)) {
            SystemClock.sleep(500);
        }
    }

    public void startGame() {
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
