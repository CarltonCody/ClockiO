package www.codycarlton.timecard.ops;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import www.codycarlton.com.clockio.R;
import www.codycarlton.data.objects.Timecard;
import www.codycarlton.firebaseutils.FirebaseUtils;
import www.codycarlton.utils.StringTimeUtils;

public class TimecardActivity extends AppCompatActivity implements PunchCardFragment.PunchCardListener {

    private final FirebaseUtils firebaseUtils = new FirebaseUtils();

    //Serializable key for setting data.
    private static final String TIMECARD_OBJECT_KEY = "TimecardObject";
    //Fragment tag for future reference.
    private static final String PUNCHCARDFRAG_TAG = "PunchcardFrag.Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timecard);

        Timecard timecard ;
        Bundle bundle = new Bundle();

        if (getIntent() != null){
            timecard = (Timecard) getIntent().getSerializableExtra(TIMECARD_OBJECT_KEY);

            bundle.putSerializable(TIMECARD_OBJECT_KEY, timecard);
        }

        PunchCardFragment punchCardFragment = new PunchCardFragment();

        punchCardFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.timecard_container, punchCardFragment,  PUNCHCARDFRAG_TAG).commit();


    }//onCreate()

    @Override
    public void clockIn(Timecard timecard) {
       firebaseUtils.writeClockin(timecard);

    }//clockIn()

    @Override
    public void clockOut(Timecard timecard) {

        long timeStampMillis = timecard.convertTimeStampsToTotalMillis(
                timecard.getClockinZoned(), timecard.getClockoutZoned());

        long lunchBreakMillis = timecard.getLunchBreakTimeMillis();

        timecard.setTotalTimeMillis(timeStampMillis - lunchBreakMillis);

        timecard.setTotalHoursStamp(
                StringTimeUtils.totalHoursTime(timecard.getTotalTimeMillis()));

        firebaseUtils.writeClockout(timecard);

    }//clockOut()

}
