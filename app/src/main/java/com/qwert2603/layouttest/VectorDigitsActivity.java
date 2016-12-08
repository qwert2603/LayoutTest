package com.qwert2603.layouttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qwert2603.layouttest.integer_view.IntegerView;

public class VectorDigitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_digits);

        final IntegerView vectorIntegerView = (IntegerView) findViewById(R.id.vector_digit_view);
        final IntegerView counterIntegerView = (IntegerView) findViewById(R.id.anim_integer_view);

        Button set = (Button) findViewById(R.id.set);
        final EditText textInputEditText = (EditText) findViewById(R.id.integer);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vectorIntegerView.setInteger(Integer.parseInt(textInputEditText.getText().toString()));
                counterIntegerView.setInteger(Integer.parseInt(textInputEditText.getText().toString()));
            }
        });


        Button plus = (Button) findViewById(R.id.plus);
        Button minus = (Button) findViewById(R.id.minus);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vectorIntegerView.setInteger(vectorIntegerView.getInteger() + 1);
                counterIntegerView.setInteger(counterIntegerView.getInteger() + 1);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vectorIntegerView.setInteger(vectorIntegerView.getInteger() - 1);
                counterIntegerView.setInteger(counterIntegerView.getInteger() - 1);
            }
        });
    }

}
