public class Patient {

        String patientId;
        String patientFirstName;
        String patientLastName;
        Integer systolicReading;
        Integer diastolicReading;
        String condition;
        String readingTime;
        String readingDate;

        public Patient() {}

        public Patient(String patientId, Integer systolicReading, Integer diastolicReading,
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

        public Integer getSystolicReading() {return systolicReading;}

        public void setSystolicReading(Integer systolicReading) {
            this.systolicReading = systolicReading;
        }


    public Integer geDiastolicReading() {return diastolicReading;}

    public void seDiastolicReading(Integer systolicReading) {
        this.systolicReading = systolicReading;
    }


    }




}
