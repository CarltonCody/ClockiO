package www.codycarlton.com.clockio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import www.codycarlton.data.objects.Timecard;
import www.codycarlton.firebaseutils.FirebaseUtils;
import www.codycarlton.timecard.ops.TimecardActivity;
import www.codycarlton.timecard.ops.TimecardDetailActivity;

public class TimecardRecyclerViewAdapter extends RecyclerView.Adapter<TimecardRecyclerViewAdapter.TimecardViewHolder> {

    private static final String TIMECARD_OBJECT_KEY = "TimecardObject"; //Key for the serializable object.

    private final ArrayList<Timecard> mTimecards;
    private final Context mContext;
    private final FirebaseUtils firebaseUtils;

    TimecardRecyclerViewAdapter(Context context, ArrayList<Timecard> timecards){
        this.mContext = context;
        this.mTimecards = timecards;
        this.firebaseUtils = new FirebaseUtils();
    }

    @NonNull
    @Override
    public TimecardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timecard_list_item, parent, false);
        return new TimecardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TimecardViewHolder holder, int position) {

        holder.jobTitle.setText(mTimecards.get(holder.getAdapterPosition()).getJobTitle());
        holder.dateStarted.setText(mTimecards.get(holder.getAdapterPosition()).getDateStamp());
        holder.totalHours.setText(mTimecards.get(holder.getAdapterPosition()).getTotalHoursStamp());

        //Handling click on list item.
        holder.listItemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent timecardOps = new Intent(mContext, TimecardActivity.class);
                timecardOps.putExtra(TIMECARD_OBJECT_KEY, mTimecards.get(holder.getAdapterPosition()));

                Intent timecardDetail = new Intent(mContext, TimecardDetailActivity.class);
                timecardDetail.putExtra(TIMECARD_OBJECT_KEY, mTimecards.get(holder.getAdapterPosition()));

                if (!mTimecards.get(holder.getAdapterPosition()).getIsJobComplete()){ //If the job isn't complete got to punchcard else go to detail.
                    mContext.startActivity(timecardOps);
                }else{
                    mContext.startActivity(timecardDetail);
                }

            }
        });

        //Contextual click for allowing user to delete a timecard.
        holder.listItemParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                deleteTimecard(holder.getAdapterPosition());

                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return mTimecards.size();
    }

    class TimecardViewHolder extends RecyclerView.ViewHolder{

        final TextView jobTitle;
        final TextView dateStarted;
        final TextView totalHours;
        final RelativeLayout listItemParent;

        TimecardViewHolder(View itemView) {
            super(itemView);

            jobTitle = itemView.findViewById(R.id.job_title);
            dateStarted = itemView.findViewById(R.id.date_started);
            totalHours = itemView.findViewById(R.id.total_hours);

            listItemParent = itemView.findViewById(R.id.list_item_parent);

        }
    }//TimecardViewHolder

    //Confirmation dialog to be shown when deleting a timecard.
    private void deleteTimecard(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme)
                .setTitle(R.string.delete_timecard_title)
                .setMessage(R.string.delete_timecard_message)
                .setPositiveButton(R.string.delete_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        firebaseUtils.deleteTimecard(mTimecards.get(position));

                    }
                })
                .setNegativeButton(R.string.cancel_text_button, null);

        AlertDialog deleteDialog = builder.create();
        deleteDialog.show();

    }//deleteTimecard()

}
