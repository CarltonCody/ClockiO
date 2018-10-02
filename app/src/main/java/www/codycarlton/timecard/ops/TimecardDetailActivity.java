package www.codycarlton.timecard.ops;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import www.codycarlton.com.clockio.R;
import www.codycarlton.data.objects.Timecard;

public class TimecardDetailActivity extends AppCompatActivity {

    //Serializable key for reading data.
    private static final String TIMECARD_OBJECT_KEY = "TimecardObject";
    //Fragment tag for future reference.
    private static final String TIMECARDDETAILFRAG_TAG = "TimecardDetail.TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timecard_detail);

        Timecard timecard;

        Bundle bundle = new Bundle();

        if (getIntent() != null){
            timecard = (Timecard) getIntent().getSerializableExtra(TIMECARD_OBJECT_KEY);

            bundle.putSerializable(TIMECARD_OBJECT_KEY, timecard);
        }

        TimecardDetailFragment timecardDetailFragment = new TimecardDetailFragment();

        timecardDetailFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.timecardDetail_container, timecardDetailFragment,  TIMECARDDETAILFRAG_TAG).commit();

        MobileAds.initialize(this,
                getString(R.string.app_ad_id));

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
}
