package com.example.myfinalproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class payments extends Fragment {

    private Spinner spinnerTrainees;
    private TextView txtStatus;
    private TextView txtEndDate;
    private TextView txtRemaining;
    private TextView txtMonthly;
    private TextView txtMonthsPaid;
    private TextView txtPaymentDate;
    private Button btnUpdateDebt;

    // מכיל את רשימת ה-MiniTrainee שנשמרה ב-SplashActivity
    private List<MiniTrainee> traineeList;

    // נשמור את המתאמן המלא שמוצג כרגע כדי שנוכל לעדכן את החוב שלו
    private Trainee currentTrainee;

    public payments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payments, container, false);

        // חיבור הרכיבים מה-XML
        spinnerTrainees = view.findViewById(R.id.spinnerTrainees);
        txtStatus = view.findViewById(R.id.txtStatus);
        txtEndDate = view.findViewById(R.id.txtEndDate);
        txtRemaining = view.findViewById(R.id.txtRemaining);
        txtMonthly = view.findViewById(R.id.txtMonthly);
        txtMonthsPaid = view.findViewById(R.id.txtMonthsPaid);
        txtPaymentDate = view.findViewById(R.id.txtPaymentDate);
        btnUpdateDebt = view.findViewById(R.id.btnUpdateDebt);

        loadTraineesData();
        setupSpinner();

        // מאזין ללחיצה על כפתור עדכון חוב
        btnUpdateDebt.setOnClickListener(v -> {
            if (currentTrainee != null) {
                showUpdateDebtDialog();
            } else {
                Toast.makeText(requireContext(), "אנא בחר מתאמן קודם", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadTraineesData() {
        if (SplashActivity.traineesList != null) {
            traineeList = SplashActivity.traineesList;
        } else {
            traineeList = new ArrayList<>();
        }
    }

    private void setupSpinner() {
        List<String> traineeNames = new ArrayList<>();
        for (MiniTrainee miniTrainee : traineeList) {
            traineeNames.add(miniTrainee.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, traineeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrainees.setAdapter(adapter);

        spinnerTrainees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position >= 0 && position < traineeList.size()) {
                    MiniTrainee selectedMini = traineeList.get(position);
                    // שליפת האובייקט המלא מ-Firebase לפי ה-ID של ה-MiniTrainee
                    loadFullTrainee(selectedMini.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                resetDetails();
                currentTrainee = null;
            }
        });

        // טעינת הנתונים של המתאמן הראשון כברירת מחדל
        if (!traineeList.isEmpty()) {
            loadFullTrainee(traineeList.get(0).getId());
        }
    }

    private void loadFullTrainee(String traineeId) {
        DBref.TraineesRef.child(String.valueOf(traineeId))
                .get()
                .addOnSuccessListener(snapshot -> {
                    Trainee fullTrainee = snapshot.getValue(Trainee.class);
                    if (fullTrainee != null) {
                        currentTrainee = fullTrainee; // שומרים את המתאמן שנבחר
                        updatePaymentDetails(fullTrainee);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PaymentsFragment", "Error loading full trainee", e);
                });
    }

    private void updatePaymentDetails(Trainee trainee) {
        String status = trainee.getRemainingDebt() > 0 ? "פעיל - קיים חוב" : "פעיל - שולם במלואו";

        txtStatus.setText("סטטוס: " + status);
        txtEndDate.setText("תאריך סיום: " + (trainee.getSubscriptionEndDate() != null ? trainee.getSubscriptionEndDate() : "לא הוגדר"));
        txtRemaining.setText("נותר לתשלום: " + trainee.getRemainingDebt() + " ₪");
        txtMonthly.setText("תשלום חודשי: " + trainee.getMonthlyCost() + " ₪");
        txtMonthsPaid.setText("חודשים ששולמו: " + calculatePaidMonths(trainee));
        txtPaymentDate.setText("תאריך חיוב: " + trainee.getDayOfPayment() + " לחודש");
    }

    private int calculatePaidMonths(Trainee trainee) {
        if (trainee.getMonthlyCost() > 0) {
            // אם אתה רוצה להציג רק חודשים שלמים, הקאסט ל-int בסדר.
            // שים לב שזה מחשב כמה חודשים מתוך החוב הקיים (או ההפך, תלוי בלוגיקה העסקית שלך).
            return (int) (trainee.getRemainingDebt() / trainee.getMonthlyCost());
        }
        return 0;
    }

    private void resetDetails() {
        txtStatus.setText("סטטוס: ");
        txtEndDate.setText("תאריך סיום: ");
        txtRemaining.setText("נותר לתשלום: ");
        txtMonthly.setText("תשלום חודשי: ");
        txtMonthsPaid.setText("חודשים ששולמו: ");
        txtPaymentDate.setText("תאריך חיוב: ");
    }

    // הפונקציה שמציגה את החלונית לקבלת סכום מהמשתמש
    private void showUpdateDebtDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("עדכון חוב");
        builder.setMessage("הכנס את הסכום ששולם:");

        // יצירת שדה טקסט להזנת מספרים (כולל עשרוניים)
        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        // כפתור אישור
        builder.setPositiveButton("אישור", (dialog, which) -> {
            String amountStr = input.getText().toString().trim();
            if (!amountStr.isEmpty()) {
                double amountPaid = Double.parseDouble(amountStr);
                double currentDebt = currentTrainee.getRemainingDebt();

                if (amountPaid > currentDebt) {
                    // הסכום גבוה מהחוב
                    Toast.makeText(requireContext(), "המחיר גבוה מידי", Toast.LENGTH_LONG).show();
                } else {
                    // חישוב החוב החדש ועדכון ב-Firebase
                    double newDebt = currentDebt - amountPaid;
                    updateDebtInFirebase(newDebt);
                }
            } else {
                Toast.makeText(requireContext(), "לא הוכנס סכום", Toast.LENGTH_SHORT).show();
            }
        });

        // כפתור ביטול
        builder.setNegativeButton("ביטול", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // פונקציה לעדכון החוב מול Firebase
    private void updateDebtInFirebase(double newDebt) {
        DBref.TraineesRef.child(currentTrainee.getId()).child("remainingDebt").setValue(newDebt)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "החוב עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                    // רענון הנתונים במסך אחרי העדכון
                    loadFullTrainee(currentTrainee.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "שגיאה בעדכון החוב: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}