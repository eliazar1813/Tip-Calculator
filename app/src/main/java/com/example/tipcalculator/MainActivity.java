package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.NumberFormat;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity
        implements TextWatcher, AdapterView.OnItemSelectedListener{

    public static final String TAG = "MainActivity";

    //declare your variables for the widgets
    private EditText editTextBillAmount;
    private TextView textViewBillAmount;
    private TextView textViewPercent;
    private TextView tipTextView;
    private TextView totalTextView;
    private TextView PerpersonView;
    private androidx.appcompat.widget.Toolbar Toolbar;



    //declare the variables for the calculations
    private double billAmount = 0.0;
    private double percent;
    private double RoundedMethod;
    private double AmountPerP;


    //set the number formats to be used for the $ amounts , and % amounts
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"Inside of OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //add Listeners to Widgets
        editTextBillAmount = findViewById(R.id.editText_BillAmount);
        editTextBillAmount.addTextChangedListener((TextWatcher) this);

        textViewBillAmount = findViewById(R.id.textView_BillAmount);


        textViewPercent =  findViewById(R.id.textView_TipPercent);


        tipTextView =  findViewById(R.id.textView_TipShow);


        totalTextView = findViewById(R.id.textView_TotalShow);

        PerpersonView = findViewById(R.id.PersonTotalAmount);

        Toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(Toolbar);





        SeekBar percentSeekBar =  findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewPercent.setText(" "+progress+"%");
                percent = progress/100.0; //calculate percent based on seeker value
                calculate();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
       // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

        Log.d(TAG,"End of OnCreate");

    }

    public void openDialog(){
        Dialog_Spinner Dialog = new Dialog_Spinner();
        Dialog.show(getSupportFragmentManager(),"Spinner Dialog");


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"Inside of Menu Method");
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
        }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Info_Button:
                openDialog();
                return true;
            case R.id.Share_Button:

                android.content.Intent intent = new android.content.Intent();
                intent.setAction(android.content.Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "The Bill Amount: " +
                        textViewBillAmount.getText() + " Tip: " + tipTextView.getText() +
                        " Total Price: " + totalTextView.getText() + " Amount Per person: " + PerpersonView.getText());
                intent.setType("text/plain");
                android.content.Intent chooser = android.content.Intent.createChooser(intent, "Share with");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                return true;

            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);

    }


    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    /*
    Note:   int i, int i1, and int i2
            represent start, before, count respectively
            The charSequence is converted to a String and parsed to a double for you
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "inside onTextChanged method: charSequence= "+charSequence);
        //surround risky calculations with try catch (what if billAmount is 0 ?
        try {
            billAmount = Double.parseDouble(charSequence.toString()) / 100.0; Log.d("MainActivity", "Bill Amount = "+billAmount);
            textViewBillAmount.setText(currencyFormat.format(billAmount));
        }
        catch (NumberFormatException e) {
            textViewBillAmount.setText("");
            billAmount = 0.0;
        }
        //charSequence is converted to a String and parsed to a double for you
        //setText on the textView
        textViewBillAmount.setText(currencyFormat.format(billAmount));
        //perform tip and total calculation and update UI by calling calculate

    }



    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public void onRadioButtonClicked(View view) {
        Log.d(TAG,"Inside of Radio Checked Buttons");
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.
        switch (view.getId()) {
            case R.id.NoRound:
                if (checked)
                    //
                    displayToast(getString(R.string.Toast_NoAmount));
                    RoundedMethod = 1;

                break;
            case R.id.TipRounded:
                if (checked)
                    //
                    displayToast(getString(R.string.Toast_TipAmount));
                    RoundedMethod = 2;

                break;
            case R.id.TotalRounded:
                if (checked)
                    //
                    displayToast(getString(R.string.Toast_TotalAmount));
                    RoundedMethod = 3;
                break;
            default:
                // Do nothing.
                break;
        }

        Log.d(TAG,"End of Radio Checked Buttons");

    }



    // calculate and display tip and total amounts
    private void calculate() {
        Log.d(TAG, "inside calculate method");

        // format percent and display in percentTextView
        //textViewPercent.setText(percentFormat.format(percent));
        try {
            if (RoundedMethod == 1) {
                // calculate the tip and total
                double tip = billAmount * percent;
                //use the tip example to do the same for the Total
                double total = billAmount + tip;
                //Amount per person.
                double Per_Person = total/AmountPerP;
                // display tip and total formatted as currency
                totalTextView.setText(currencyFormat.format(total));
                //user currencyFormat instead of percentFormat to set the textViewTip
                tipTextView.setText(currencyFormat.format(tip));
                //Per person Display amount.
                PerpersonView.setText("$"+df2.format(Per_Person));


            }
            else if (RoundedMethod == 2){

                double tip = billAmount * percent;

                double tip_rounded = Math.round(tip);

                double total = billAmount + tip_rounded;

                double Per_Person = total/AmountPerP;

                totalTextView.setText(currencyFormat.format(total));

                tipTextView.setText(currencyFormat.format(tip_rounded));

                PerpersonView.setText("$"+df2.format(Per_Person));

            }

            else if (RoundedMethod == 3){

                double tip = billAmount * percent;

                double total = billAmount + tip;

                double total_rounded = Math.round(total);

                double Per_Person = total_rounded/AmountPerP;

                totalTextView.setText(currencyFormat.format(total_rounded));

                tipTextView.setText(currencyFormat.format(tip));

                PerpersonView.setText("$"+df2.format(Per_Person));


            }
        } catch (Exception e){
            Log.d("MainActivity", "Cannot calculate negative values");
        }

        Log.d(TAG, "End of calculate method");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "inside Spinner Selected Item method");
        switch (position) {

            case 1:
                AmountPerP = 2;
                calculate();
                break;
            case 2:
                AmountPerP = 3;
                calculate();
                break;
            case 3:
                AmountPerP = 4;
                calculate();
                break;
            case 4:
                AmountPerP = 5;
                calculate();
                break;
            case 5:
                AmountPerP = 6;
                calculate();
                break;
            default:
                AmountPerP = 1;
                calculate();
                break;
        }

        Log.d(TAG, "End Spinner Selected Item method");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

