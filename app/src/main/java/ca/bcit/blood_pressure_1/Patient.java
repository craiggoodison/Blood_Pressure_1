package ca.bcit.blood_pressure_1;

public class Patient {

        String patientId;
        int systolicReading;
        int diastolicReading;
        String condition;
        String readingTime;
        String readingDate;

        public Patient() {}

        public Patient(String patientId, int systolicReading, int diastolicReading,
                       String condition, String readingTime,  String readingDate) {
            this.patientId = patientId;
            this.systolicReading = systolicReading;
            this.diastolicReading = diastolicReading;
            this.condition =condition;
            this.readingTime = readingTime;
            this.readingDate = readingDate;
        }

        public String getPatientId() { return patientId; }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

//        public String getStudentFirstName() {return studentFirstName;}
//
//        public void setStudentFirstName(String studentFirstName) {
//            this.studentFirstName = studentFirstName;
//        }
//
//        public String getStudentLastName() {return studentLastName;}
//
//        public void setStudentLastName(String studentLastName) {
//            this.studentLastName = studentLastName;
//        }

        public int getSystolicReading() {return systolicReading;}

        public void setSystolicReading(int systolicReading) {
            this.systolicReading = systolicReading;
        }

        public int getDiastolicReading() {return diastolicReading;}

        public void setDiastolicReading(int systolicReading) {
            this.systolicReading = systolicReading;
        }

        public String condition() {return condition;}

        public void condition(String condition) {
            this.condition = condition;
        }
        public String getReadingTime() {return readingTime;}

        public void setReadingTime(String readingTime) {
            this.readingTime = readingTime;
        }
        public String getReadingDate() {return readingDate;}

        public void setReadingDate(String readingDate) {
            this.readingDate = readingDate;
        }

    }


