package com.example.myfinalproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBref {

    //הקלאס ששומר הפניות לfirebace

    private static String https;
    //פניתי למאגר הfireBace בכדי להגיד לי מה המיקום - איפה נמצא הDB
    public static FirebaseDatabase DBlocation = FirebaseDatabase.getInstance("https://my-final-project-7434c-default-rtdb.europe-west1.firebasedatabase.app/");    //משתמש במיקום הראשי ותן לי את הענף שנקרא Trainees
    public static DatabaseReference TraineesRef = DBlocation.getReference("Trainees");
    //משתמש במיקום הראשי ותן לי את הענף שנקרא Exercises
    public static DatabaseReference ExercisesRef = DBlocation.getReference("Exercises");
}
