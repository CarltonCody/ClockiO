package www.codycarlton.timecard.ops;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import www.codycarlton.com.clockio.R;
import www.codycarlton.data.objects.Timecard;

public class TimecardDetailFragment extends Fragment {

    private Timecard timecard;

    //Serializable key for reading data.
    private static final String TIMECARD_OBJECT_KEY = "TimecardObject";

    public TimecardDetailFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timecard_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (this.getArguments() != null){
            timecard = (Timecard) this.getArguments().getSerializable(TIMECARD_OBJECT_KEY);
        }

        if (getView() != null){

            TextView jobtitle = getView().findViewById(R.id.detail_jobtitle);
            TextView date = getView().findViewById(R.id.detail_date);
            TextView clockin = getView().findViewById(R.id.detail_clockin);
            TextView clockout = getView().findViewById(R.id.detail_clockout);
            TextView totalhours = getView().findViewById(R.id.detail_totalhours);

            jobtitle.setText(timecard.getJobTitle());
            date.setText(timecard.getDateStamp());
            clockin.setText(timecard.getClockinTimeStamp());
            clockout.setText(timecard.getClockoutTimeStamp());
            totalhours.setText(timecard.getTotalHoursStamp());

        }

    }

}
