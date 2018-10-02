package www.codycarlton.data.objects;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/*
* Data class for handling of uploading to firebase and reading values
* to display in the ui of the application.*/

public class Timecard implements Serializable {

    private String documentId = "";

    private String jobTitle = "";
    private String clockinTimeStamp = "";
    private String clockoutTimeStamp = "";
    private String totalHoursStamp = "";
    private String dateStamp = "";

    private boolean clockedin = false;

    private String clockinZoned = "";
    private String clockoutZoned = "";
    private long totalTimeMillis = 0;
    private long lunchBreakTimeMillis = 0;


    private boolean jobComplete = false;

    public Timecard(){
    }

    //Beginning of setters
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public void setClockinTimeStamp(String clockinTimeStamp) {
        this.clockinTimeStamp = clockinTimeStamp;
    }

    public void setClockoutTimeStamp(String clockoutTimeStamp) {
        this.clockoutTimeStamp = clockoutTimeStamp;
    }

    public void setTotalHoursStamp(String totalHoursStamp){
        this.totalHoursStamp = totalHoursStamp;
    }

    public void setClockedin(boolean clockedin) {
        this.clockedin = clockedin;
    }

    public void setClockinZoned(String clockinZoned) {
        this.clockinZoned = clockinZoned;
    }

    public void setClockoutZoned(String clockoutZoned) {
        this.clockoutZoned = clockoutZoned;
    }

    public void setTotalTimeMillis(long totalTimeMillis) {
        this.totalTimeMillis = totalTimeMillis;
    }

    public void setLunchBreakTimeMillis(long lunchBreakTimeMillis) {
        this.lunchBreakTimeMillis = lunchBreakTimeMillis;
    }

    public void setJobComplete(boolean jobComplete) {
        this.jobComplete = jobComplete;
    }

    //End of setters

    //Beginning of getters
    public String getDocumentId() {
        return documentId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public String getClockinTimeStamp() {
        return clockinTimeStamp;
    }

    public String getClockoutTimeStamp() {
        return clockoutTimeStamp;
    }

    public String getTotalHoursStamp() {
        return totalHoursStamp;
    }

    public boolean getIsClockedin() {
        return clockedin;
    }

    public String getClockinZoned() {
        return clockinZoned;
    }

    public String getClockoutZoned() {
        return clockoutZoned;
    }

    public long getTotalTimeMillis() {
        return totalTimeMillis;
    }

    public long getLunchBreakTimeMillis() {
        return lunchBreakTimeMillis;
    }

    public boolean getIsJobComplete() {
        return jobComplete;
    }

    //end of getters

    //Converting timestamps to millis inorder to do the math later for getting the total time.
    public long convertTimeStampsToTotalMillis(String clockinZoned, String clockoutZoned){

        ZonedDateTime start = ZonedDateTime.parse(clockinZoned);
        ZonedDateTime end = ZonedDateTime.parse(clockoutZoned);

        return ChronoUnit.MILLIS.between(start, end);

    }//convertTimeStampsToTotalMillis

}
