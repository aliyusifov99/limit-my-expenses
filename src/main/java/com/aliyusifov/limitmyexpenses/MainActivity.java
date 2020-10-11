package com.aliyusifov.limitmyexpenses;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView currentAmountTextView;
    EditText editTextAmountSubtract;
    Button buttonSubtract;
    Button buttonReset;
    Button buttonEditGoal;
    String textValue;
    final String SHARED_PREFERENCES = "sharedPrefences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentAmountTextView = findViewById(R.id.amountTextView);
        editTextAmountSubtract  = findViewById(R.id.editAmountSubtract);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonReset = findViewById(R.id.buttonReset);
        buttonEditGoal = findViewById(R.id.buttonEditTarget);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String text = sharedPreferences.getString("Value","0");
        currentAmountTextView.setText(text);
        int currentValue = Integer.valueOf(text);
        if (currentValue<5){
            currentAmountTextView.setTextColor(Color.rgb(255,57,51));
        }


    }


    public void editGoal(View view) {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
        myDialog.setTitle("Write your goal!");
        final EditText goalInput = new EditText(MainActivity.this);
        goalInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        myDialog.setView(goalInput);

        myDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textValue = goalInput.getText().toString();
                if (textValue.isEmpty()) {
                    Toast.makeText(MainActivity.this, "You didn't enter any value!", Toast.LENGTH_SHORT).show();
                }
                else{
                currentAmountTextView.setText(goalInput.getText().toString());
                currentAmountTextView.setTextColor(Color.rgb(0,0,0));
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Value", goalInput.getText().toString());
                editor.putString("ResetValue",goalInput.getText().toString());
                editor.commit();
                Toast.makeText(MainActivity.this, "Updated Goal:"+goalInput.getText().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        myDialog.show();

    }


    public void reset(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String text = sharedPreferences.getString("ResetValue","0");
        currentAmountTextView.setText(text);
        currentAmountTextView.setTextColor(Color.rgb(0,0,0));
        buttonSubtract.setEnabled(true);
    }

    public void subtract(View view) {
        int currentValue = Integer.valueOf(currentAmountTextView.getText().toString());
        String valueFromEditText = editTextAmountSubtract.getText().toString();
        if (valueFromEditText.isEmpty()){
            editTextAmountSubtract.setError("Please enter the value");
        }
        else {
            int valueForExtract = Integer.valueOf(valueFromEditText);
            if (currentValue-valueForExtract<0){
                Toast.makeText(this, "The value is bigger than your daily goal", Toast.LENGTH_SHORT).show();
                editTextAmountSubtract.setText("");
            }
            else {
                int result = currentValue - valueForExtract;
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Value",String.valueOf(result));
                Toast.makeText(this, "Spend:"+String.valueOf(valueForExtract), Toast.LENGTH_SHORT).show();
                editor.apply();
                currentAmountTextView.setText(String.valueOf(result));
                editTextAmountSubtract.setText("");
                if (result<5 && result!=0){
                    currentAmountTextView.setTextColor(Color.rgb(255,57,51));
                    Toast.makeText(this, "You are reaching limit!", Toast.LENGTH_SHORT).show();
                }
                else if (result == 0){
                    Toast.makeText(this, "You have spend your daily limit!", Toast.LENGTH_SHORT).show();
                    buttonSubtract.setEnabled(false);
                }
            }

        }

    }
}