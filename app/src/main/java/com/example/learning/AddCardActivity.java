package com.example.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

public class AddCardActivity extends AppCompatActivity {
    EditText title;
    TextView tap_front;
    TextView tap_back;
    TextView save;
    TextView addAnotherCard;
    View frontCard;
    View backCard;
    View lastCardFront;
    View lastCardEnd;
    TextView lastQuestion;
    TextView lastTapFront;
    TextView lastTapEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        title = findViewById(R.id.card_input_area);
        tap_front = findViewById(R.id.tap_to_filp);
        save = findViewById(R.id.save_deck);
        addAnotherCard = findViewById(R.id.add_another_card);
        frontCard = findViewById(R.id.front_card);
        backCard = findViewById(R.id.back_card);
        lastCardEnd = findViewById(R.id.last_card_end);
        lastCardFront = findViewById(R.id.last_card_front);
        lastQuestion = findViewById(R.id.last_qeustion_front);
        lastTapFront = findViewById(R.id.tap_to_filp_last_front);
        lastTapEnd = findViewById(R.id.tap_to_filp_last_end);
        tap_back = findViewById(R.id.tap_to_filp_back);
        backCard.setRotationY(-90f);
        lastCardEnd.setRotationY(-90f);
        tap_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipAnimation(0);
            }
        });
        tap_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipAnimation(0);
            }
        });
        lastTapFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipAnimation(1);
            }
        });
        lastTapEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipAnimation(1);
            }
        });
        addAnotherCard.setOnClickListener(new View.OnClickListener() { //todo: add the question and deck into the file
            @Override
            public void onClick(View view) {
                lastQuestion.setText(title.getText());
                title.getText().clear();
                lastCardFront.setVisibility(View.VISIBLE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void flipAnimation(int question) {
        View frontCardView;
        View endCardView;
        if (question == 0){
            frontCardView = frontCard;
            endCardView = backCard;
        }
        else{
            frontCardView = lastCardFront;
            endCardView = lastCardEnd;
        }

        final View visibletext;
        final View invisibletext;
        if (frontCardView.getVisibility() == View.GONE) {
            visibletext = endCardView;
            invisibletext = frontCardView;
        } else {
            invisibletext = endCardView;
            visibletext = frontCardView;
        }
        //LinearInterpolator()     其变化速率恒定
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(visibletext, "rotationY", 0f, 90f);
        visToInvis.setDuration(400);
        //AccelerateInterpolator()    其变化开始速率较慢，后面加速
        visToInvis.setInterpolator(new AccelerateInterpolator());
        final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(invisibletext, "rotationY",
                -90f, 0f);
        invisToVis.setDuration(400);
        //DecelerateInterpolator()   其变化开始速率较快，后面减速
        invisToVis.setInterpolator(new DecelerateInterpolator());
        visToInvis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                visibletext.setVisibility(View.GONE);
                invisToVis.start();
                invisibletext.setVisibility(View.VISIBLE);
            }
        });
        visToInvis.start();
    }


}
