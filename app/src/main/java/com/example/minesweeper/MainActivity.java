package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnCellClickListener  {

    RecyclerView gridRecyclerView;
    MineGridRecyclerAdapter mineGridRecyclerAdapter;
    MinesweeperGame mineSweeperGame;
    TextView smiley, timer, flag, flagCount;
    CountDownTimer countDownTimer;
    int secElapse;
    boolean timeStart;

    int sizeOfGrid = 8;
    int minesHidden = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flag = findViewById(R.id.activity_main_flag);
        flagCount = findViewById(R.id.activity_main_flagsleft);
        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mineSweeperGame.toggleMode();
                if (mineSweeperGame.isFlagMode()) {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFFFF);
                    border.setStroke(1, 0xFF000000);
                    flag.setBackground(border);
                } else {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFFFF);
                    flag.setBackground(border);
                }
            }
        });

        smiley = findViewById(R.id.activity_main_smiley);
        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mineSweeperGame = new MinesweeperGame(sizeOfGrid, minesHidden);
                mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells());
                timeStart = false;
                countDownTimer.cancel();
                secElapse = 0;
                timer.setText(R.string.default_score);
                flagCount.setText(String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));
            }
        });

        timer = findViewById(R.id.activity_main_timer);
        timeStart = false;
        countDownTimer = new CountDownTimer(999000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secElapse += 1;
                timer.setText(String.format("%03d", secElapse));
            }

            @Override
            public void onFinish() {
                mineSweeperGame.outOfTime();
                Toast.makeText(getApplicationContext(), "Out of Time! Game Over...", Toast.LENGTH_SHORT).show();
                mineSweeperGame.getMineGrid().revealAllBombs();
                mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells());
            }
        };

        gridRecyclerView = findViewById(R.id.activity_main_grid);
        gridRecyclerView.setLayoutManager(new GridLayoutManager(this, sizeOfGrid));
        mineSweeperGame = new MinesweeperGame(sizeOfGrid, minesHidden);
        mineGridRecyclerAdapter = new MineGridRecyclerAdapter(mineSweeperGame.getMineGrid().getCells(), this);
        gridRecyclerView.setAdapter(mineGridRecyclerAdapter);
        flagCount.setText(String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));
    }

    @Override
    public void cellClick(Cell cell){
        mineSweeperGame.handleCellClick(cell);

        flagCount.setText(String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));

        if(!timeStart) {
            countDownTimer.start();
            timeStart = true;
        }

        if (mineSweeperGame.isGameOver()) {
            countDownTimer.cancel();
            Toast.makeText(getApplicationContext(), "Game Over!", Toast.LENGTH_SHORT).show();
            mineSweeperGame.getMineGrid().revealAllBombs();
        }else if (mineSweeperGame.isGameWon()) {
            countDownTimer.cancel();
            Toast.makeText(getApplicationContext(), "You won!!!", Toast.LENGTH_SHORT).show();
            mineSweeperGame.getMineGrid().revealAllBombs();
        }

        mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells());
    }
}






