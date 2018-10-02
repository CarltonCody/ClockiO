package www.codycarlton.com.clockio;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


import javax.annotation.Nullable;

import www.codycarlton.data.objects.Timecard;
import www.codycarlton.firebaseutils.FirebaseUtils;
import www.codycarlton.login.signup.Login_Signup_Activity;
import www.codycarlton.utils.StringTimeUtils;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private final FirebaseUtils firebaseUtils = new FirebaseUtils();
    private boolean mClockedin = false;
    private Timecard mTimecard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        displayTimecards();
        listenForChanges();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        MobileAds.initialize(this,
                getString(R.string.app_ad_id));

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sign_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.signout:

                //Going back to the login page when the user is signed out.
                FirebaseAuth.getInstance().signOut();
                Intent signoutIntent = new Intent(this, Login_Signup_Activity.class);
                signoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signoutIntent);

                return true;

                default:
                    return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View view) {
       createTimeCard();
    }

    //Creating a new timecard via an alert dialog.
    /*This implementation has a custom check box,
    for allowing the user to clock in when the timecard is created.*/
    private void createTimeCard(){

        final View view = View.inflate(this, R.layout.new_timecard_alertdialog_edittext, null);

        final CheckBox clockin_check = view.findViewById(R.id.clockin_check);
        final TextView clockin_msg = view.findViewById(R.id.clockin_msg);

        clockin_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (clockin_check.isChecked()){
                    clockin_msg.setVisibility(View.VISIBLE);
                    mClockedin = true;
                }else{
                    clockin_msg.setVisibility(View.INVISIBLE);
                    mClockedin = false;
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme)

        .setTitle(R.string.new_timecard_title_dialog)
        .setView(view)
                .setPositiveButton(R.string.create_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        TextInputEditText editText = view.findViewById(R.id.edittext_alertdialog);

                        mTimecard = new Timecard();
                        mTimecard.setJobTitle(editText.getText().toString());
                        mTimecard.setClockedin(mClockedin);
                        addTimeCardtoDB(mTimecard);

                    }
                })
                .setNegativeButton(R.string.cancel_text, null);

        AlertDialog newJob = builder.create();
        newJob.show();

    }//createTimecard()

    //Adding the timecard to the database
    private void addTimeCardtoDB(Timecard timecard){

        timecard.setDateStamp(StringTimeUtils.currentDate());

        if (mClockedin){

            timecard.setClockedin(true);
            //Getting the more readable form of the time.
            timecard.setClockinTimeStamp(StringTimeUtils.currentTime());
            //Using zoned time to get more accurate time even across timezones.
            timecard.setClockinZoned(StringTimeUtils.currentZonedTime());

        }else{
            timecard.setClockedin(false);
            String currentTime = "00:00:00";    //Default view for having no hours worked.
            timecard.setClockinTimeStamp(currentTime);
        }

        timecard.setTotalHoursStamp("0" +" "+ "Hrs worked"); //Default view for having no hours worked.
        timecard.setJobComplete(false);

        firebaseUtils.writeNewTimeCard(timecard, this.findViewById(R.id.dashboard_coordinator));
        displayTimecards();

    }//addTimecardToDB(timecard)

    //Reading timecards from the database
    private void displayTimecards(){

        final RecyclerView timeCardList = findViewById(R.id.jobsList);
        final TextView noTimecardsMsg = findViewById(R.id.noTimeCardsMessage);

        final ArrayList<Timecard> timecards = new ArrayList<>();

        if (firebaseUtils.mUser != null){

            firebaseUtils.mDB.collection(FirebaseUtils.USERS_KEY).document(firebaseUtils.mUser.getUid())
                    .collection(FirebaseUtils.TIMECARD_KEY).orderBy(FirebaseUtils.DATE_KEY, Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){

                                for (QueryDocumentSnapshot document : task.getResult()){

                                    if (document.exists()){
                                        Timecard timecard = new Timecard();
                                        timecard.setDocumentId(document.getId());
                                        timecard.setJobTitle(document.getString(FirebaseUtils.JOBNAME_KEY));
                                        timecard.setDateStamp(document.getString(FirebaseUtils.DATE_KEY));
                                        timecard.setClockinTimeStamp(document.getString(FirebaseUtils.CLOCKINTIME_KEY));
                                        timecard.setClockoutTimeStamp(document.getString(FirebaseUtils.CLOCKOUT_KEY));
                                        timecard.setTotalHoursStamp(document.getString(FirebaseUtils.TOTALHOURS_KEY));
                                        timecard.setClockedin((boolean) document.get(FirebaseUtils.CLOCKEDIN_KEY));
                                        timecard.setClockinZoned(document.getString(FirebaseUtils.CLOCKEDINZONED_KEY));
                                        timecard.setClockoutZoned(document.getString(FirebaseUtils.CLOCKEDOUTZONED_KEY));
                                        timecard.setTotalTimeMillis((long) document.get(FirebaseUtils.TOTALTIMEMILLIS_KEY));
                                        timecard.setJobComplete((boolean) document.get(FirebaseUtils.JOBCOMPLETE_KEY));
                                        timecards.add(timecard);
                                    }
                                }
                            }

                            if (timecards.size() > 0){
                                noTimecardsMsg.setVisibility(View.GONE);
                                timeCardList.setVisibility(View.VISIBLE);
                            }else{
                                noTimecardsMsg.setVisibility(View.VISIBLE);
                                timeCardList.setVisibility(View.GONE);
                            }

                            setTimecardListAdapter(timeCardList, timecards);

                        }
                    });
        }
    }//displayTimecards()

    //Listening for changes to the database inorder to update the list in real time.
    private void listenForChanges(){

        if (firebaseUtils.mUser != null){

            firebaseUtils.mDB.collection(FirebaseUtils.USERS_KEY).document(firebaseUtils.mUser.getUid())
                    .collection(FirebaseUtils.TIMECARD_KEY).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (queryDocumentSnapshots != null){
                        displayTimecards();
                    }

                }
            });

        }
    }//listenForChanges()

    private void setTimecardListAdapter(RecyclerView timeCardList, ArrayList<Timecard> timecards){
        timeCardList.setAdapter(new TimecardRecyclerViewAdapter(this, timecards));
        timeCardList.setLayoutManager(new LinearLayoutManager(this));
    }//setTimecardListAdapter(timeCardList, timecards)


}
