package com.missouristate.guadagnano.autopurchase1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PurchaseActivity extends Activity {
    //Auto object will hole info for purchased vehicle
    Auto mAuto;

    //Data passed to loan activity
    String loanReport;
    String monthlyPayment;

    //Layout input references
    private EditText carPriceET;
    private EditText downPayET;
    private RadioGroup loanTermRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_layout);

        //references to et's & rb's
        carPriceET = findViewById(R.id.editText1);
        downPayET = findViewById(R.id.editText2);
        loanTermRG = findViewById(R.id.radioGroup1);

        //object storing auto data
        mAuto = new Auto();
    }

    private void collectedAutoInputData() {
        //first set car price
        mAuto.setPrice((double)Integer.valueOf(carPriceET.getText().toString()));

        //then set the down payment
        mAuto.setDownPayment((double)Integer.valueOf(downPayET.getText().toString()));

        //finally set the loan term
        Integer radioId = loanTermRG.getCheckedRadioButtonId();
        RadioButton term = findViewById(radioId);
        mAuto.setLoanTerm(term.getText().toString());
    }

    private void buildLoanReport() {
        //Construct monthly payments
        Resources res = getResources();
        monthlyPayment = res.getString(R.string.report_line1) + String.format("%.02f", mAuto.monthlyPayment());

        //Construct loan report
        loanReport = res.getString(R.string.report_line6) + String.format("%10.02f", mAuto.getPrice());
        loanReport += res.getString(R.string.report_line7) + String.format("%10.02f", mAuto.getDownPayment());

        loanReport += res.getString(R.string.report_line9) + String.format("%18.02f", mAuto.taxAmount());
        loanReport += res.getString(R.string.report_line10) + String.format("%18.02f", mAuto.totalCost());

        loanReport += res.getString(R.string.report_line11) + String.format("%12.02f", mAuto.borrowedAmount());
        loanReport += res.getString(R.string.report_line12) + String.format("%12.02f", mAuto.interestAmount());

        loanReport += "\n\n" + res.getString(R.string.report_line8) + " " + mAuto.getLoanTerm() + " years.";

        loanReport += "\n\n" + res.getString(R.string.report_line2);
        loanReport += res.getString(R.string.report_line3);
        loanReport += res.getString(R.string.report_line4);
        loanReport += res.getString(R.string.report_line5);
    }

    public void activateLoanSummary(View view) {
        //build loan report
        collectedAutoInputData();
        buildLoanReport();

        //create intent to display loan summary
        Intent launchReport = new Intent(this, LoanSummaryActivity.class);

        //Pass loan report and monthly payment in loan summary
        launchReport.putExtra("LoanReport", loanReport);
        launchReport.putExtra("MonthlyPayment", monthlyPayment);

        //start the activity
        startActivity(launchReport);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
