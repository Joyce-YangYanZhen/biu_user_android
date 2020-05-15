package com.noplugins.keepfit.userplatform.entity;

public class UpdateTravalEntity {

    /**
     * mood : {"sportsBefore":2,"sportsAfter":1}
     * test : {"weight":200,"stature":180,"bodyfat":120,"waistratebuttocks":123,"kcal":123}
     * sportsNum : CUS123
     */

    private MoodBean mood;
    private TestBean test;
    private String sportsNum;

    public MoodBean getMood() {
        return mood;
    }

    public void setMood(MoodBean mood) {
        this.mood = mood;
    }

    public TestBean getTest() {
        return test;
    }

    public void setTest(TestBean test) {
        this.test = test;
    }

    public String getSportsNum() {
        return sportsNum;
    }

    public void setSportsNum(String sportsNum) {
        this.sportsNum = sportsNum;
    }

    public static class MoodBean {
        /**
         * sportsBefore : 2
         * sportsAfter : 1
         */

        private int sportsBefore;
        private int sportsAfter;

        public int getSportsBefore() {
            return sportsBefore;
        }

        public void setSportsBefore(int sportsBefore) {
            this.sportsBefore = sportsBefore;
        }

        public int getSportsAfter() {
            return sportsAfter;
        }

        public void setSportsAfter(int sportsAfter) {
            this.sportsAfter = sportsAfter;
        }
    }

    public static class TestBean {
        /**
         * weight : 200
         * stature : 180
         * bodyfat : 120
         * waistratebuttocks : 123
         * kcal : 123
         */
        private int weight;
        private int stature;
        private Double finalBodyfat;
        private Double waistratebuttocks;
        private Double kcal;

        public double getFinalKcal() {
            return finalKcal;
        }

        public void setFinalKcal(double finalKcal) {
            this.finalKcal = finalKcal;
        }

        public double getFinalBmi() {
            return finalBmi;
        }

        public void setFinalBmi(double finalBmi) {
            this.finalBmi = finalBmi;
        }

        public double getFinalwaistratebuttocks() {
            return finalwaistratebuttocks;
        }

        public void setFinalwaistratebuttocks(double finalwaistratebuttocks) {
            this.finalwaistratebuttocks = finalwaistratebuttocks;
        }

        private double finalKcal;
        private double finalBmi;
        private double finalwaistratebuttocks;

        public Double getFinalBodyfat() {
            return finalBodyfat;
        }

        public void setFinalBodyfat(Double finalBodyfat) {
            this.finalBodyfat = finalBodyfat;
        }




        public Double getWaistratebuttocks() {
            return waistratebuttocks;
        }

        public void setWaistratebuttocks(Double waistratebuttocks) {
            this.waistratebuttocks = waistratebuttocks;
        }

        public Double getKcal() {
            return kcal;
        }

        public void setKcal(Double kcal) {
            this.kcal = kcal;
        }


        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getStature() {
            return stature;
        }

        public void setStature(int stature) {
            this.stature = stature;
        }


    }
}
