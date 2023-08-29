package com.example.autostock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

    public class fragment_calculator extends Fragment implements View.OnClickListener {

        private TextView displayTextView;
        private StringBuilder expressionBuilder;

        public fragment_calculator() {
            // Constructor vacío requerido
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);

            displayTextView = rootView.findViewById(R.id.result_tv);
            expressionBuilder = new StringBuilder();

            // Inicializar los botones y establecer los listeners
            Button button0 = rootView.findViewById(R.id.button_0);
            Button button1 = rootView.findViewById(R.id.button_1);
            Button button2 = rootView.findViewById(R.id.button_2);
            Button button3 = rootView.findViewById(R.id.button_3);
            Button button4 = rootView.findViewById(R.id.button_4);
            Button button5 = rootView.findViewById(R.id.button_5);
            Button button6 = rootView.findViewById(R.id.button_6);
            Button button7 = rootView.findViewById(R.id.button_7);
            Button button8 = rootView.findViewById(R.id.button_8);
            Button button9 = rootView.findViewById(R.id.button_9);
            Button buttonDot = rootView.findViewById(R.id.button_dot);
            Button buttonPlus = rootView.findViewById(R.id.button_plus);
            Button buttonMinus = rootView.findViewById(R.id.button_minus);
            Button buttonMultiply = rootView.findViewById(R.id.button_multiply);
            Button buttonDivide = rootView.findViewById(R.id.button_divide);
            Button buttonEquals = rootView.findViewById(R.id.button_equals);
            Button buttonClear = rootView.findViewById(R.id.button_c);
            Button buttonAllClear = rootView.findViewById(R.id.button_ac);
            Button buttonOpenBracket = rootView.findViewById(R.id.button_open_bracket);
            Button buttonCloseBracket = rootView.findViewById(R.id.button_close_bracket);

            button0.setOnClickListener(this);
            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);
            button4.setOnClickListener(this);
            button5.setOnClickListener(this);
            button6.setOnClickListener(this);
            button7.setOnClickListener(this);
            button8.setOnClickListener(this);
            button9.setOnClickListener(this);
            buttonDot.setOnClickListener(this);
            buttonPlus.setOnClickListener(this);
            buttonMinus.setOnClickListener(this);
            buttonMultiply.setOnClickListener(this);
            buttonDivide.setOnClickListener(this);
            buttonEquals.setOnClickListener(this);
            buttonClear.setOnClickListener(this);
            buttonAllClear.setOnClickListener(this);
            buttonOpenBracket.setOnClickListener(this);
            buttonCloseBracket.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_0:
                    appendToExpression("0");
                    break;
                case R.id.button_1:
                    appendToExpression("1");
                    break;
                case R.id.button_2:
                    appendToExpression("2");
                    break;
                case R.id.button_3:
                    appendToExpression("3");
                    break;
                case R.id.button_4:
                    appendToExpression("4");
                    break;
                case R.id.button_5:
                    appendToExpression("5");
                    break;
                case R.id.button_6:
                    appendToExpression("6");
                    break;
                case R.id.button_7:
                    appendToExpression("7");
                    break;
                case R.id.button_8:
                    appendToExpression("8");
                    break;
                case R.id.button_9:
                    appendToExpression("9");
                    break;
                case R.id.button_dot:
                    appendToExpression(".");
                    break;
                case R.id.button_plus:
                    appendToExpression("+");
                    break;
                case R.id.button_minus:
                    appendToExpression("-");
                    break;
                case R.id.button_multiply:
                    appendToExpression("*");
                    break;
                case R.id.button_divide:
                    appendToExpression("/");
                    break;
                case R.id.button_open_bracket:
                    appendToExpression("(");
                    break;
                case R.id.button_close_bracket:
                    appendToExpression(")");
                    break;
                case R.id.button_equals:
                    evaluateExpression();
                    break;
                case R.id.button_c:
                    clearLastCharacter();
                    break;
                case R.id.button_ac:
                    clearExpression();
                    break;
            }
        }

        private void appendToExpression(String value) {
            expressionBuilder.append(value);
            displayTextView.setText(expressionBuilder.toString());
        }

        private void evaluateExpression() {
            try {
                double result = eval(expressionBuilder.toString());
                displayTextView.setText(String.valueOf(result));
            } catch (Exception e) {
                displayTextView.setText("Error");
            }
        }

        private void clearLastCharacter() {
            if (expressionBuilder.length() > 0) {
                expressionBuilder.deleteCharAt(expressionBuilder.length() - 1);
                displayTextView.setText(expressionBuilder.toString());
            }
        }

        private void clearExpression() {
            expressionBuilder = new StringBuilder();
            displayTextView.setText("");
        }

        private double eval(final String str) {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < str.length()) ? str.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    while (true) {
                        if (eat('+')) x += parseTerm(); // Suma
                        else if (eat('-')) x -= parseTerm(); // Resta
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    while (true) {
                        if (eat('*')) x *= parseFactor(); // Multiplicación
                        else if (eat('/')) x /= parseFactor(); // División
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor(); // Positivo
                    if (eat('-')) return -parseFactor(); // Negativo

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) { // Paréntesis
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') { // Número
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = Double.parseDouble(str.substring(startPos, this.pos));
                    } else {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }

                    return x;
                }
            }.parse();
        }
    }
