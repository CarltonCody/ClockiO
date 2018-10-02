package www.codycarlton.firebaseutils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;

import www.codycarlton.com.clockio.R;
import www.codycarlton.data.objects.Timecard;

public class FirebaseUtils {

    public final FirebaseFirestore mDB;
    public final FirebaseUser mUser;

    //Keys for reading and writing to the database.
    public static final String USERS_KEY = "users";
    public static final String TIMECARD_KEY = "timecards";

    private static final String DOC_ID_KEY = "docId";
    public static final String JOBNAME_KEY = "JobName";
    public static final String DATE_KEY = "Date";
    public static final String CLOCKINTIME_KEY = "ClockinTime";
    public static final String CLOCKOUT_KEY = "ClockoutTime";
    public static final String TOTALHOURS_KEY = "TotalHours";
    public static final String CLOCKEDIN_KEY = "ClockedIn";
    public static final String CLOCKEDINZONED_KEY = "ClockedInZoned";
    public static final String CLOCKEDOUTZONED_KEY = "ClockedOutZoned";
    public static final String TOTALTIMEMILLIS_KEY = "TotalTimeMillis";
    public static final String JOBCOMPLETE_KEY = "JobComplete";

    public FirebaseUtils(){

        this.mDB = FirebaseFirestore.getInstance();
        //to fix warning about Java.Util.Date objects not working
        //Will instead be read back as com.google.firebase.timestamp objects
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDB.setFirestoreSettings(settings);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.mUser = mAuth.getCurrentUser();

    }

    public void writeNewTimeCard(Timecard timecard, final View view){

        HashMap<String, Object> newTimecard = new HashMap<>();
        newTimecard.put(DOC_ID_KEY, timecard.getDocumentId()); //I don't think I need this
        newTimecard.put(JOBNAME_KEY,timecard.getJobTitle());
        newTimecard.put(DATE_KEY, timecard.getDateStamp());
        newTimecard.put(CLOCKINTIME_KEY, timecard.getClockinTimeStamp());
        newTimecard.put(CLOCKOUT_KEY, timecard.getClockoutTimeStamp());
        newTimecard.put(TOTALHOURS_KEY, timecard.getTotalHoursStamp());
        newTimecard.put(CLOCKEDIN_KEY, timecard.getIsClockedin());
        newTimecard.put(CLOCKEDINZONED_KEY, timecard.getClockinZoned());
        newTimecard.put(CLOCKEDOUTZONED_KEY, timecard.getClockoutZoned());
        newTimecard.put(TOTALTIMEMILLIS_KEY, timecard.getTotalTimeMillis());
        newTimecard.put(JOBCOMPLETE_KEY, timecard.getIsJobComplete());

        mDB.collection(USERS_KEY)
                .document(mUser.getUid())
                .collection(TIMECARD_KEY)
                .add(newTimecard)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Snackbar.make(view.findViewById(R.id.dashboard_coordinator), R.string.timecard_added_msg,
                                Snackbar.LENGTH_LONG ).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view.findViewById(R.id.dashboard_coordinator), R.string.timecard_added_err_message,
                                Snackbar.LENGTH_LONG ).show();
                    }
                });

    }//writeNewTimecard(timecard, view)


    public void writeClockin(Timecard timecard){

        HashMap<String, Object> updateTimecard = new HashMap<>();
        updateTimecard.put(DATE_KEY, timecard.getDateStamp());
        updateTimecard.put(CLOCKINTIME_KEY, timecard.getClockinTimeStamp());
        updateTimecard.put(CLOCKEDINZONED_KEY, timecard.getClockinZoned());
        updateTimecard.put(CLOCKEDIN_KEY, true);

        mDB.collection(USERS_KEY)
                .document(mUser.getUid())
                .collection(TIMECARD_KEY)
                .document(timecard.getDocumentId())
                .update(updateTimecard)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }//writeClockin(timecard)

    public void writeClockout(final Timecard timecard){

        HashMap<String, Object> updateTimecard = new HashMap<>();
        updateTimecard.put(CLOCKOUT_KEY, timecard.getClockoutTimeStamp());
        updateTimecard.put(CLOCKEDOUTZONED_KEY, timecard.getClockoutZoned());
        updateTimecard.put(TOTALHOURS_KEY, timecard.getTotalHoursStamp());
        updateTimecard.put(TOTALTIMEMILLIS_KEY, timecard.getTotalTimeMillis());
        updateTimecard.put(CLOCKEDIN_KEY, false);
        updateTimecard.put(JOBCOMPLETE_KEY, true);

        mDB.collection(USERS_KEY)
                .document(mUser.getUid())
                .collection(TIMECARD_KEY)
                .document(timecard.getDocumentId())
                .update(updateTimecard)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }//writeClockout(timecard)

    public void deleteTimecard(Timecard timecard){

        mDB.collection(USERS_KEY)
                .document(mUser.getUid())
                .collection(TIMECARD_KEY)
                .document(timecard.getDocumentId())
                .delete();

    }//deleteTimecard(timecard)

}
