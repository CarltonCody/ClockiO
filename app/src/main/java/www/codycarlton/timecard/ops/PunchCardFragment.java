package www.codycarlton.timecard.ops;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import www.codycarlton.com.clockio.R;
import www.codycarlton.data.objects.Timecard;
import www.codycarlton.utils.StringTimeUtils;

/*
* Fragment view for punching in or out.*/

public class PunchCardFragment extends Fragment implements View.OnClickListener {

    private Timecard timecard;
    private Button punchButton;
    private TextView clockedinIndicator;
    private TextView workingIndicator;

    private static final String TIMECARD_OBJECT_KEY = "TimecardObject";

    public PunchCardFragment(){
    }

    public interface PunchCardListener{

        void clockIn(Timecard timecard);
        void clockOut(Timecard timecard);

    }

    private PunchCardListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.listener = (PunchCardListener) context;
    }//onAttach()

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_punchcard, container, false);
    }//onCreateView()

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (this.getArguments() != null){
            timecard = (Timecard) this.getArguments().getSerializable(TIMECARD_OBJECT_KEY);
        }

        if (getView() != null){

            workingIndicator = getView().findViewById(R.id.working_indicator);
            TextView jobTitle = getView().findViewById(R.id.job_title);
            TextView dateStamp = getView().findViewById(R.id.date_stamp);

            jobTitle.setText(timecard.getJobTitle());
            dateStamp.setText(timecard.getDateStamp());

            clockedinIndicator = getView().findViewById(R.id.clockinTime);
            clockedinIndicator.setText(timecard.getClockinTimeStamp());

            punchButton = getView().findViewById(R.id.clockin_out_button);
            punchButton.setOnClickListener(this);

            if (timecard.getIsClockedin()){
                workingIndicator.setText(R.string.working_pc_text);
                if (getActivity() != null)
                punchButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.clockout_button_background));
                punchButton.setText(R.string.clock_out_pc_text);

            }else if (!timecard.getIsClockedin()){
                if (getContext() != null)
                workingIndicator.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                workingIndicator.setText(R.string.not_working_pc_text);
                if (getActivity() != null)
                punchButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.clockin_button_background));
                punchButton.setText(R.string.clock_in_pc_text);
            }

        }

    }//onActivityCreated()

    @Override
    public void onClick(View view) {

        if (getView() != null){
            if (!timecard.getIsClockedin()){    //Not clocked in

                timecard.setClockedin(true);
                timecard.setDateStamp(StringTimeUtils.currentDate());
                timecard.setClockinTimeStamp(StringTimeUtils.currentTime());
                timecard.setClockinZoned(StringTimeUtils.currentZonedTime());
                String currentTime = StringTimeUtils.currentTime();

                workingIndicator = getView().findViewById(R.id.working_indicator);
                clockedinIndicator = getView().findViewById(R.id.clockinTime);
                workingIndicator.setText(R.string.working_pc_text);
                if (getActivity() != null)
                workingIndicator.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreenClockin));
                clockedinIndicator.setText(currentTime);

                punchButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.clockout_button_background));
                punchButton.setText(R.string.clock_out_pc_text);

                listener.clockIn(timecard);

            }else if (timecard.getIsClockedin()){   //Clocked in

                timecard.setClockedin(false);
                timecard.setClockoutTimeStamp(StringTimeUtils.currentTime());
                timecard.setClockoutZoned(StringTimeUtils.currentZonedTime());

                if (getContext() != null)
                workingIndicator.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                workingIndicator.setText(R.string.not_working_pc_text);

                if (getActivity() != null)
                punchButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.clockin_button_background));
                punchButton.setText(R.string.clockin_pc_text);

                setBreakTime(timecard);
                listener.clockOut(timecard);
            }
        }

    }//onClick()

    private void setBreakTime(final Timecard timecard){

        final View view = View.inflate(getActivity(), R.layout.set_break_time_alertdialog, null);

        final RadioButton thirtyMinuteButton = view.findViewById(R.id.thirtyMinuteBreak);
        final RadioButton oneHourButton = view.findViewById(R.id.oneHourBreak);

        if (getActivity() != null){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                    .setTitle(R.string.unpaid_break_text)
                    .setView(view)
                    .setPositiveButton(R.string.set_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (thirtyMinuteButton.isChecked()){
                                long thirtyMinuteMilli = 1800000;
                                timecard.setLunchBreakTimeMillis(thirtyMinuteMilli);

                            }else if (oneHourButton.isChecked()){
                                long oneHourMilli = 3600000;
                                timecard.setLunchBreakTimeMillis(oneHourMilli);

                            }else{
                                timecard.setLunchBreakTimeMillis(0);
                            }

                        }
                    })
                    .setNegativeButton(R.string.no_break_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            timecard.setLunchBreakTimeMillis(0);
                        }
                    });

            AlertDialog setBreak = builder.create();
            setBreak.show();

        }

    }

}
