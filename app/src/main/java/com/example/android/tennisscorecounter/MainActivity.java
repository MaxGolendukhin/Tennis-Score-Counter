package com.example.android.tennisscorecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final int gamesToWin = 6;

    private int playerAScore;
    private int playerBScore;
    private int playerAGames;
    private int playerBGames;
    private int playerASets;
    private int playerBSets;
    private int setsToWin = 3;
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

    public void addScorePlayerA(View view) {
        //if (win) return;

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
        if (playerAScore == 0 || playerAScore == 15) playerAScore += 15;
        else if (playerAScore == 30 || playerAScore == 40) playerAScore += 10;

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

    private void addScorePlayerATieBreak() {
        playerAScore++;
        if ((playerAScore == 7 && playerBScore < 6) || (playerAScore == 6 && playerBScore < 5) || playerAAdvantage) {
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

    private void addGamePlayerA() {
        playerAGames++;
        if (tieBreak || (playerAGames == gamesToWin && playerBGames < gamesToWin - 1)) {
            tieBreak = false;
            playerAGames = 0;
            playerBGames = 0;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText("");
            addSetPlayerA();
        } else if (playerAGames == gamesToWin - 1 && playerBGames == gamesToWin - 1) {
            tieBreak = true;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText(R.string.tie_break);
        }
        updateGamesText();
    }

    private void addSetPlayerA() {
        playerASets++;
        TextView textView = findViewById(R.id.player_A_sets_text_view);
        textView.setText(String.valueOf(playerASets));

        if (playerASets == setsToWin) {
            win = true;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText(R.string.player_A_wins);
        }
    }

    public void addScorePlayerB(View view) {
        if (win) return;

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
        if (playerBScore == 15 || playerBScore == 0) playerBScore += 15;
        else if (playerBScore == 30 || playerBScore == 40) playerBScore += 10;

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

    private void addScorePlayerBTieBreak() {
        playerBScore++;
        if ((playerBScore == 7 && playerAScore < 6) || (playerBScore == 6 && playerAScore < 5) || playerBAdvantage) {
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

    private void addGamePlayerB() {
        playerBGames++;
        if (tieBreak || (playerBGames == gamesToWin && playerAGames < gamesToWin - 1)) {
            tieBreak = false;
            playerAGames = 0;
            playerBGames = 0;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText("");
            addSetPlayerB();
        } else if (playerAGames == gamesToWin - 1 && playerBGames == gamesToWin - 1) {
            tieBreak = true;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText(R.string.tie_break);
        }
        updateGamesText();
    }

    private void addSetPlayerB() {
        playerBSets++;
        TextView textView = findViewById(R.id.player_B_sets_text_view);
        textView.setText(String.valueOf(playerBSets));

        if (playerBSets == setsToWin) {
            win = true;
            TextView infoTextView = findViewById(R.id.info_text_view);
            infoTextView.setText(R.string.player_B_wins);
        }
    }

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

    private void updateGamesText() {
        TextView gamesATextView = findViewById(R.id.player_A_games_text_view);
        TextView gamesBTextView = findViewById(R.id.player_B_games_text_view);
        gamesATextView.setText(String.valueOf(playerAGames));
        gamesBTextView.setText(String.valueOf(playerBGames));
    }

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

    public void setLongMatch(View view) {
        setsToWin = 5;
    }

    public void setShortMatch(View view) {
        setsToWin = 3;
    }


}