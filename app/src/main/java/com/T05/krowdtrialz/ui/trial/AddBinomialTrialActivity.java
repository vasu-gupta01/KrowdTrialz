package com.T05.krowdtrialz.ui.trial;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.model.experiment.BinomialExperiment;
import com.T05.krowdtrialz.model.experiment.Experiment;
import com.T05.krowdtrialz.model.trial.BinomialTrial;
import com.T05.krowdtrialz.model.trial.Trial;
import com.T05.krowdtrialz.model.user.User;
import com.T05.krowdtrialz.util.Database;
import com.google.firebase.firestore.ListenerRegistration;

public class AddBinomialTrialActivity extends TrialActivity {
    private Database db;
    private User user;
    private EditText passEditText;
    private EditText failEditText;
    private TextView passTextView;
    private TextView failTextView;
    private BinomialTrial binomialTrial;

    private ListenerRegistration expRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_binomial_trial);

        setTitle("Add Binomial Trial");

        db = Database.getInstance();

        passEditText  = findViewById(R.id.binomial1_editText);
        failEditText  = findViewById(R.id.binomial2_editText);

        passTextView = findViewById(R.id.binomial1_textView);
        failTextView = findViewById(R.id.binomial2_textView);

        Intent intent = getIntent();
        expRegistration = db.getExperimentByID(intent.getStringExtra(MainActivity.EXTRA_EXPERIMENT_ID), new Database.GetExperimentCallback() {
            @Override
            public void onSuccess(Experiment experiment) {
                BinomialExperiment binomialExperiment = (BinomialExperiment) experiment;
                passTextView.setText(binomialExperiment.getPassUnit());
                failTextView.setText(binomialExperiment.getFailUnit());
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * Construct a trial from the current application state
     *
     * @return trial
     */
    @Override
    protected Trial createTrial() {

        // get user, location and create a trial
        db = Database.getInstance();
        user = db.getDeviceUser();

        binomialTrial = new BinomialTrial(user);

        // set the pass and the fail counts
        int passText = Integer.parseInt(passEditText.getText().toString());
        int failText = Integer.parseInt(failEditText.getText().toString());
        binomialTrial.setPassCount(passText);
        binomialTrial.setFailCount(failText);

        return binomialTrial;
    } // end createTrial

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop listening to changes in the Database
        expRegistration.remove();
    }
}// end AddBinomialTrialActivity