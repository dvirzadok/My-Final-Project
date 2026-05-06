    package com.example.myfinalproject;

    public class DataHolder {

        // המתאמן המלא (בשימוש במקומות אחרים בפרויקט)
        public static Trainee selectedTraineeFull;

        // המתאמן הקטן (למסכים כמו TrainingPrograms)
        public static MiniTrainee selectedMiniTrainee;

        // ===== Trainee מלא =====
        public static void setSelectedTrainee(Trainee trainee) {
            selectedTraineeFull = trainee;
        }

        public static Trainee getSelectedTrainee() {
            return selectedTraineeFull;
        }

        // ===== MiniTrainee =====
        public static void setSelectedMiniTrainee(MiniTrainee trainee) {
            selectedMiniTrainee = trainee;
        }

        public static MiniTrainee getSelectedMiniTrainee() {
            return selectedMiniTrainee;
        }
    }