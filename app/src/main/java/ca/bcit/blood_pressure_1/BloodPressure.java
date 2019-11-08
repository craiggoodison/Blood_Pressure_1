package ca.bcit.blood_pressure_1;

public class BloodPressure {

    private String condition;

    private int diastolicReading;

    private String patientId;

    private String readingDate;

    private String readingTime;

    private int systolicReading;

    public void setCondition(String condition){
        this.condition = condition;
    }
    public String getCondition(){
        return this.condition;
    }
    public void setDiastolicReading(int diastolicReading){
        this.diastolicReading = diastolicReading;
    }
    public int getDiastolicReading(){
        return this.diastolicReading;
    }
    public void setPatientId(String patientId){
        this.patientId = patientId;
    }
    public String getPatientId(){
        return this.patientId;
    }
    public void setReadingDate(String readingDate){
        this.readingDate = readingDate;
    }
    public String getReadingDate(){
        return this.readingDate;
    }
    public void setReadingTime(String readingTime){
        this.readingTime = readingTime;
    }
    public String getReadingTime(){
        return this.readingTime;
    }
    public void setSystolicReading(int systolicReading){
        this.systolicReading = systolicReading;
    }
    public int getSystolicReading(){
        return this.systolicReading;
    }

}
