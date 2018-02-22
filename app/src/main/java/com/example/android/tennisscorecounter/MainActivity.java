package com.example.android.tennisscorecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final int GAMES_TO_WIN = 6;
    private final int SHORT_GAME_SETS = 3;
    private final int LONG_GAME_SETS = 5;

    private int setsToWin = SHORT_GAME_SETS;
    private int playerAScore;
    private int playerBScore;
    private int playerAGames;
    private int playerBGames;
    private int playerASets;
    private int playerBSets;
    private boolean playerAAdvantage;
    private boolean playerBAdvantage;
    private boolean deuceScore;
    private boolean tieBreak;
    private boolean win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Restores state of needed variables ans set its values to views.
     * @param savedInstanceState contains all variables to restore.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        playerAScore = savedInstanceState.getInt("playerAScore");
        playerBScore = savedInstanceState.getInt("playerBScore");
        playerAGames = savedInstanceState.getInt("playerAGames");
        playerBGames = savedInstanceState.getInt("playerBGames");
        playerASets = savedInstanceState.getInt("playerASets");
        playerBSets = savedInstanceState.getInt("playerBSets");
        setsToWin = savedInstanceState.getInt("setsToWin");
        playerAAdvantage = savedInstanceState.getBoolean("playerAAdvantage");
        playerBAdvantage = savedInstanceState.getBoolean("playerBAdvantage");
        deuceScore = savedInstanceState.getBoolean("deuceScore");
        tieBreak = savedInstanceState.getBoolean("tieBreak");

        updateButtonsText();
        updateGamesText();
        updateSetsText();
        if (playerAScore > 0 || playerBScore > 0 || playerAGames > 0 ||
                playerBGames > 0 || playerASets > 0 || playerBSets > 0 ||
                playerAAdvantage || playerBAdvantage || deuceScore) {
            RadioGroup radioGroup = findViewById(R.id.set_type_radio_buttons_group);
            radioGroup.setVisibility(View.GONE);
            TextView textView = findViewById(R.id.info_text_view);
            textView.setVisibility(View.VISIBLE);
            updateInfoText();
        }
    }

    /**
     * Saves all variables to restore them after screen will be recreated after rotation.
     * @param outState bundle to save variables.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("playerAScore", playerAScore);
        outState.putInt("playerBScore", playerBScore);
        outState.putInt("playerAGames", playerAGames);
        outState.putInt("playerBGames", playerBGames);
        outState.putInt("playerASets", playerASets);
        outState.putInt("playerBSets", playerBSets);
        outState.putInt("setsToWin", setsToWin);
        outState.putBoolean("playerAAdvantage", playerAAdvantage);
        outState.putBoolean("playerBAdvantage", playerBAdvantage);
        outState.putBoolean("deuceScore", deuceScore);
        outState.putBoolean("tieBreak", tieBreak);

        super.onSaveInstanceState(outState);
    }

    /**
     * Adds one point to player A.
     * @param view button pressed.
     */
    public void addScorePlayerA(View view) {
        if (win) {
            return;
        }

        if (deuceScore) {
            playerAAdvantage = true;
            playerBAdvantage = false;
            deuceScore = false;
            updateButtonsText();
            return;
        }

        if (tieBreak) {
            addScorePlayerATieBreak();
            return;
        }

        if (playerAScore == 0 || playerAScore == 15) {
            playerAScore += 15;
        } else if (playerAScore == 30 || playerAScore == 40) {
            playerAScore += 10;
        }

        if (playerAScore == 15 && playerBScore == 0) {
            RadioGroup radioGroup = findViewById(R.id.set_type_radio_buttons_group);
            radioGroup.setVisibility(View.GONE);
            TextView textView = findViewById(R.id.info_text_view);
            textView.setVisibility(View.VISIBLE);
            textView.setText("");
        }

        if ((playerAScore == 50 && playerBScore <= 30) || playerAAdvantage) {
            addGamePlayerA();
            playerAScore = 0;
            playerBScore = 0;
            playerAAdvantage = false;
        } else if ((playerAScore == 40 && playerBScore == 40) || playerBAdvantage) {
            deuceScore = true;
            playerBAdvantage = false;
        }

        updateButtonsText();
    }

    /**
     * Adds one point to player A in tie break mode.
     */
    private void addScorePlayerATieBreak() {
        playerAScore++;
        if ((playerAScore == 7 && playerBScore < 6) ||
                (playerAScore == 6 && playerBScore < 5) || playerAAdvantage) {
            addGamePlayerA();
            playerAScore = 0;
            playerBScore = 0;
            playerAAdvantage = false;
        } else if ((playerAScore == 6 && playerBScore == 6) || playerBAdvantage) {
            deuceScore = true;
            playerBAdvantage = false;
        }

        updateButtonsText();
    }

    /**
     * Adds one game to player A.
     */
    private void addGamePlayerA() {
        playerAGames++;
        if (tieBreak || (playerAGames == GAMES_TO_WIN && playerBGames < GAMES_TO_WIN - 1)) {
            tieBreak = false;
            playerAGames = 0;
            playerBGames = 0;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText("");
            addSetPlayerA();
        } else if (playerAGames == GAMES_TO_WIN - 1 && playerBGames == GAMES_TO_WIN - 1) {
            tieBreak = true;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText(R.string.tie_break);
        }

        updateGamesText();
    }

    /**
     * Adds one set to player A.
     */
    private void addSetPlayerA() {
        playerASets++;
        updateSetsText();
        updateInfoText();
    }

    /**
     * Adds one point to player B.
     * @param view button pressed.
     */
    public void addScorePlayerB(View view) {
        if (win) {
            return;
        }

        if (deuceScore) {
            playerAAdvantage = false;
            playerBAdvantage = true;
            deuceScore = false;
            updateButtonsText();
            return;
        }

        if (tieBreak) {
            addScorePlayerBTieBreak();
            return;
        }

        if (playerBScore == 15 || playerBScore == 0) {
            playerBScore += 15;
        } else if (playerBScore == 30 || playerBScore == 40) {
            playerBScore += 10;
        }

        if (playerAScore == 0 && playerBScore == 15) {
            RadioGroup radioGroup = findViewById(R.id.set_type_radio_buttons_group);
            radioGroup.setVisibility(View.GONE);
            TextView textView = findViewById(R.id.info_text_view);
            textView.setVisibility(View.VISIBLE);
            textView.setText("");
        }

        if ((playerBScore == 50 && playerAScore <= 30) || playerBAdvantage) {
            addGamePlayerB();
            playerAScore = 0;
            playerBScore = 0;
            playerBAdvantage = false;
        } else if ((playerAScore == 40 && playerBScore == 40) || playerAAdvantage) {
            deuceScore = true;
            playerAAdvantage = false;
        }

        updateButtonsText();
    }

    /**
     * Adds one point in tie-break mode to player B.
     */
    private void addScorePlayerBTieBreak() {
        playerBScore++;
        if ((playerBScore == 7 && playerAScore < 6) ||
                (playerBScore == 6 && playerAScore < 5) || playerBAdvantage) {
            addGamePlayerB();
            playerAScore = 0;
            playerBScore = 0;
            playerBAdvantage = false;
        } else if ((playerAScore == 6 && playerBScore == 6) || playerAAdvantage) {
            deuceScore = true;
            playerAAdvantage = false;
        }

        updateButtonsText();
    }

    /**
     * Add one game to player B.
     */
    private void addGamePlayerB() {
        playerBGames++;
        if (tieBreak || (playerBGames == GAMES_TO_WIN && playerAGames < GAMES_TO_WIN - 1)) {
            tieBreak = false;
            playerAGames = 0;
            playerBGames = 0;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText("");
            addSetPlayerB();
        } else if (playerAGames == GAMES_TO_WIN - 1 && playerBGames == GAMES_TO_WIN - 1) {
            tieBreak = true;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText(R.string.tie_break);
        }

        updateGamesText();
    }

    /**
     * Adds one set to player B.
     */
    private void addSetPlayerB() {
        playerBSets++;
        updateSetsText();
        updateInfoText();
    }

    /**
     * Updates text on both buttons.
     */
    private void updateButtonsText() {
        Button scoreAButton = findViewById(R.id.player_A_score_button);
        Button scoreBButton = findViewById(R.id.player_B_score_button);

        if (deuceScore) {
            scoreAButton.setText(R.string.deuce);
            scoreBButton.setText(R.string.deuce);
        } else if (playerAAdvantage) {
            scoreAButton.setText(R.string.more);
            scoreBButton.setText(R.string.less);
        } else if (playerBAdvantage) {
            scoreBButton.setText(R.string.more);
            scoreAButton.setText(R.string.less);
        } else {
            scoreAButton.setText(String.valueOf(playerAScore));
            scoreBButton.setText(String.valueOf(playerBScore));
        }
    }

    /**
     * Updates text views with games score.
     */
    private void updateGamesText() {
        TextView gamesATextView = findViewById(R.id.player_A_games_text_view);
        TextView gamesBTextView = findViewById(R.id.player_B_games_text_view);
        gamesATextView.setText(String.valueOf(playerAGames));
        gamesBTextView.setText(String.valueOf(playerBGames));
    }

    /**
     * Updates text views sets score.
     */
    private void updateSetsText() {
        TextView setsATextView = findViewById(R.id.player_A_sets_text_view);
        TextView setsBTextView = findViewById(R.id.player_B_sets_text_view);

        setsATextView.setText(String.valueOf(playerASets));
        setsBTextView.setText(String.valueOf(playerBSets));
    }

    /**
     * Updates info text view.
     */
    private void updateInfoText() {
        if (playerASets == setsToWin) {
            win = true;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText(R.string.player_A_wins);
        } else if (playerBSets == setsToWin) {
            win = true;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText(R.string.player_B_wins);
        }
    }

    /**
     * Starts new match, sets all global variables to zeroes and false.
     * @param view new match button.
     */
    public void newMatch(View view) {
        playerAScore = 0;
        playerBScore = 0;
        playerAGames = 0;
        playerBGames = 0;
        playerASets = 0;
        playerBSets = 0;
        playerAAdvantage = false;
        playerBAdvantage = false;
        deuceScore = false;
        tieBreak = false;
        win = false;

        TextView setsATextView = findViewById(R.id.player_A_sets_text_view);
        TextView setsBTextView = findViewById(R.id.player_B_sets_text_view);
        setsATextView.setText(String.valueOf(playerASets));
        setsBTextView.setText(String.valueOf(playerBSets));

        TextView gameATextView = findViewById(R.id.player_A_games_text_view);
        TextView gameBTextView = findViewById(R.id.player_B_games_text_view);
        gameATextView.setText(String.valueOf(playerAGames));
        gameBTextView.setText(String.valueOf(playerBGames));

        Button scoreAButton = findViewById(R.id.player_A_score_button);
        Button scoreBButton = findViewById(R.id.player_B_score_button);
        scoreAButton.setText(String.valueOf(playerAScore));
        scoreBButton.setText(String.valueOf(playerBScore));

        TextView infoTextView = findViewById(R.id.info_text_view);
        infoTextView.setVisibility(View.GONE);

        RadioGroup radioGroup = findViewById(R.id.set_type_radio_buttons_group);
        radioGroup.setVisibility(View.VISIBLE);
    }

    /**
     * Sets long match.
     * @param view radio button long match chosen.
     */
    public void setLongMatch(View view) {
        setsToWin = LONG_GAME_SETS;
    }

    /**
     * Sets short match.
     * @param view radio button short match chosen.
     */
    public void setShortMatch(View view) {
        setsToWin = SHORT_GAME_SETS;
    }
}