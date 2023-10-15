package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class Calculator extends AppCompatActivity {
    private EditText e1;
    int operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        e1 = (EditText) findViewById(R.id.editText1);
        ((Button)findViewById(R.id.num0)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "0");
            }
        });
        ((Button)findViewById(R.id.num1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "1");
            }
        });
        ((Button)findViewById(R.id.num2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "2");
            }
        });
        ((Button)findViewById(R.id.num3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "3");
            }
        });
        ((Button)findViewById(R.id.num4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "4");
            }
        });
        ((Button)findViewById(R.id.num5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "5");
            }
        });
        ((Button)findViewById(R.id.num6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "6");
            }
        });
        ((Button)findViewById(R.id.num7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "7");
            }
        });
        ((Button)findViewById(R.id.num8)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "8");
            }
        });
        ((Button)findViewById(R.id.num9)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "9");
            }
        });
        ((Button)findViewById(R.id.c)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText("");
            }
        });
        ((Button)findViewById(R.id.plus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "+");
            }
        });
        ((Button)findViewById(R.id.minus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "-");
            }
        });
        ((Button)findViewById(R.id.multiply)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "*");
            }
        });
        ((Button)findViewById(R.id.divide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(e1.getText() + "/");
            }
        });
        ((Button)findViewById(R.id.equal)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setText(evaluateExpression(e1.getText().toString()+"")+"");
            }
        });

    }
    private double evaluateExpression(String expression) {
        double result = 0;

        // Split the expression based on operators
        String[] parts = expression.split("[+\\-*/]");

        if (parts.length == 2) {
            double num1 = Double.parseDouble(parts[0]);
            double num2 = Double.parseDouble(parts[1]);

            if (expression.contains("+")) {
                result = num1 + num2;
            } else if (expression.contains("-")) {
                result = num1 - num2;
            } else if (expression.contains("*")) {
                result = num1 * num2;
            } else if (expression.contains("/")) {
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    result = Double.POSITIVE_INFINITY; // Division by zero
                }
            }
        }

        return result;
    }

}