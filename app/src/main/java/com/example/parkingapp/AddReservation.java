package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddReservation extends AppCompatActivity {

    Button dayPicker;
    ProgressBar parkingProgressBar;
    ProgressBar priceProgressBar;
    FirebaseFirestore db;
    Button datePicker;
    Button endDatePicker;
    String day;
    String month;
    String year;
    String hours;
    String minutes;
    String endHours;
    double predictedOccupancy;
    String endMinutes;
    TextView dateAndDay;
    TextView price;
    Boolean dayPicked;
    Boolean TimePicked;
    String totalPrice;
    String priceView;
    GridLayout gridLayout;
    MaterialCardView park1;
    MaterialCardView park2;
    MaterialCardView park3;
    MaterialCardView park4;
    MaterialCardView priceCard;
    String parkingChosen;

    double solde;
    Button pay;

    FirebaseAuth mAuth;
    ExecutorService parkingService;
    ExecutorService priceService;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reservation);
        db = FirebaseFirestore.getInstance();
        dayPicker = (Button) findViewById(R.id.dayPicker);
        datePicker = (Button) findViewById(R.id.timePicker);
        endDatePicker = (Button) findViewById(R.id.endTimePicker);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        dateAndDay = (TextView) findViewById(R.id.textView2);
        price = (TextView) findViewById(R.id.price);
        pay = (Button) findViewById(R.id.payButton);
        park1 = (MaterialCardView) findViewById(R.id.park1);
        park2 = (MaterialCardView) findViewById(R.id.park2);
        park3 = (MaterialCardView) findViewById(R.id.park3);
        park4 = (MaterialCardView) findViewById(R.id.park4);
        priceCard = (MaterialCardView) findViewById(R.id.priceCard);
        parkingProgressBar = (ProgressBar) findViewById(R.id.parkingProgressBar);
        priceProgressBar = (ProgressBar) findViewById(R.id.priceProgressBar);

        gridLayout.setVisibility(View.GONE);
        priceCard.setVisibility(View.GONE);
        parkingProgressBar.setVisibility(View.GONE);
        parkingService = Executors.newSingleThreadExecutor();
        priceService = Executors.newSingleThreadExecutor();
        dayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDayDialog();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog();
            }
        });

        endDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEndTimeDialog();
            }
        });


        View.OnClickListener parkingClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove stroke from all cards
                park1.setStrokeWidth(0);
                park2.setStrokeWidth(0);
                park3.setStrokeWidth(0);
                park4.setStrokeWidth(0);

                // Add stroke to the clicked card and set parkingChosen
                if (view.getId() == R.id.park1) {
                    park1.setStrokeWidth(10);
                    park1.setStrokeColor(Color.rgb(255, 165, 0));
                    parkingChosen = "1";
                    priceCard.setVisibility(View.VISIBLE);
                    priceService.execute(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    price.setVisibility(View.GONE);
                                    priceProgressBar.setVisibility(View.VISIBLE);
                                }
                            });

                            try {
                                URL url = new URL("https://a53a-196-92-7-82.ngrok-free.app/predict"); // Remplacez par l'URL publique de Ngrok
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                                conn.setRequestProperty("Accept", "application/json");
                                conn.setDoOutput(true);
                                String dataInput = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":00";
                                String jsonInputString = "{\"date\": \"" + dataInput + "\"}";
                                Log.d("dt", "the data is : "+ jsonInputString);
                                try(OutputStream os = conn.getOutputStream()) {
                                    byte[] input = jsonInputString.getBytes("utf-8");
                                    os.write(input, 0, input.length);
                                }

                                try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
                                    String response = scanner.useDelimiter("\\A").next();
                                    JSONObject jsonResponse = new JSONObject(response);
                                    predictedOccupancy = jsonResponse.getDouble("predicted_occupancy")*100;
                                    Log.d("dt", "the response is : "+ predictedOccupancy);

                                }


                                conn.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (predictedOccupancy > 70 && predictedOccupancy < 80) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);
                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "her");
                                    } else if (predictedOccupancy > 80 && predictedOccupancy < 90) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);

                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herr");
                                    } else if (predictedOccupancy > 90) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);

                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herrr");
                                    } else {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);
                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herrrr");
                                    }
                                    price.setText(priceView);
                                    priceProgressBar.setVisibility(View.GONE);
                                    price.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    });
                } else if (view.getId() == R.id.park2) {
                    park2.setStrokeWidth(10);
                    park2.setStrokeColor(Color.rgb(255, 165, 0));
                    parkingChosen = "2";
                    priceCard.setVisibility(View.VISIBLE);
                    priceService.execute(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    price.setVisibility(View.GONE);
                                    priceProgressBar.setVisibility(View.VISIBLE);
                                }
                            });

                            try {
                                URL url = new URL("https://a53a-196-92-7-82.ngrok-free.app/predict"); // Remplacez par l'URL publique de Ngrok
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                                conn.setRequestProperty("Accept", "application/json");
                                conn.setDoOutput(true);

                                String jsonInputString = "{\"date\": \"2024-01-28 13:30:00\"}";

                                try(OutputStream os = conn.getOutputStream()) {
                                    byte[] input = jsonInputString.getBytes("utf-8");
                                    os.write(input, 0, input.length);
                                }

                                try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
                                    String response = scanner.useDelimiter("\\A").next();
                                    JSONObject jsonResponse = new JSONObject(response);
                                    double predictedOccupancy = jsonResponse.getDouble("predicted_occupancy")*100;
                                    Log.d("dt", "the response is : "+ predictedOccupancy);
                                    Log.d("res", "response is: " + predictedOccupancy);

                                }

                                conn.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (predictedOccupancy > 70 && predictedOccupancy < 80) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);
                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "her");
                                    } else if (predictedOccupancy > 80 && predictedOccupancy < 90) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);

                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herr");
                                    } else if (predictedOccupancy > 90) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);

                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herrr");
                                    } else {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);
                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herrrr");
                                    }
                                    price.setText(priceView);
                                    priceProgressBar.setVisibility(View.GONE);
                                    price.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    });
                } else if (view.getId() == R.id.park3) {
                    park3.setStrokeWidth(10);
                    park3.setStrokeColor(Color.rgb(255, 165, 0));
                    parkingChosen = "3";
                    priceCard.setVisibility(View.VISIBLE);
                    priceService.execute(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    price.setVisibility(View.GONE);
                                    priceProgressBar.setVisibility(View.VISIBLE);
                                }
                            });

                            try {
                                URL url = new URL("https://a53a-196-92-7-82.ngrok-free.app/predict"); // Remplacez par l'URL publique de Ngrok
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                                conn.setRequestProperty("Accept", "application/json");
                                conn.setDoOutput(true);

                                String jsonInputString = "{\"date\": \"2024-01-28 13:30:00\"}";

                                try(OutputStream os = conn.getOutputStream()) {
                                    byte[] input = jsonInputString.getBytes("utf-8");
                                    os.write(input, 0, input.length);
                                }

                                try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
                                    String response = scanner.useDelimiter("\\A").next();
                                    JSONObject jsonResponse = new JSONObject(response);
                                    double predictedOccupancy = jsonResponse.getDouble("predicted_occupancy")*100;
                                    Log.d("dt", "the response is : "+ predictedOccupancy);
                                    Log.d("res", "response is: " + predictedOccupancy);

                                }

                                conn.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (predictedOccupancy > 70 && predictedOccupancy < 80) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);
                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "her");
                                    } else if (predictedOccupancy > 80 && predictedOccupancy < 90) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);

                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herr");
                                    } else if (predictedOccupancy > 90) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);

                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herrr");
                                    } else {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);
                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herrrr");
                                    }
                                    price.setText(priceView);
                                    priceProgressBar.setVisibility(View.GONE);
                                    price.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    });
                } else if (view.getId() == R.id.park4) {
                    park4.setStrokeWidth(10);
                    park4.setStrokeColor(Color.rgb(255, 165, 0));
                    parkingChosen = "4";
                    priceCard.setVisibility(View.VISIBLE);
                    priceService.execute(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    price.setVisibility(View.GONE);
                                    priceProgressBar.setVisibility(View.VISIBLE);
                                }
                            });

                            try {
                                URL url = new URL("https://a53a-196-92-7-82.ngrok-free.app/predict"); // Remplacez par l'URL publique de Ngrok
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                                conn.setRequestProperty("Accept", "application/json");
                                conn.setDoOutput(true);

                                String jsonInputString = "{\"date\": \"2024-01-28 13:30:00\"}";

                                try(OutputStream os = conn.getOutputStream()) {
                                    byte[] input = jsonInputString.getBytes("utf-8");
                                    os.write(input, 0, input.length);
                                }

                                try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
                                    String response = scanner.useDelimiter("\\A").next();
                                    JSONObject jsonResponse = new JSONObject(response);
                                    double predictedOccupancy = jsonResponse.getDouble("predicted_occupancy")*100;
                                    Log.d("res", "response is: " + predictedOccupancy);

                                }

                                conn.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (predictedOccupancy > 70 && predictedOccupancy < 80) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);
                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "her");
                                    } else if (predictedOccupancy > 80 && predictedOccupancy < 90) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);

                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herr");
                                    } else if (predictedOccupancy > 90) {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);

                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herrr");
                                    } else {
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        totalPrice = df.format(calculateDurationInMinutes(hours, minutes, endHours, endMinutes)*0.13);
                                        priceView = String.valueOf(totalPrice) + " DH";
                                        Log.d("d", "herrrr");
                                    }
                                    price.setText(priceView);
                                    priceProgressBar.setVisibility(View.GONE);
                                    price.setVisibility(View.VISIBLE);
                                }
                            });

                        }


                    });
                }
            }
        };

        // Assign the OnClickListener to each MaterialCardView
        park1.setOnClickListener(parkingClickListener);
        park2.setOnClickListener(parkingClickListener);
        park3.setOnClickListener(parkingClickListener);
        park4.setOnClickListener(parkingClickListener);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                String userEmail = mAuth.getCurrentUser().getEmail();
                String id = day + month + year + hours + minutes + endHours + endMinutes + parkingChosen;
                Map<String, Object> reserv = new HashMap<>();
                reserv.put("day", day);
                reserv.put("month", month);
                reserv.put("year", year);
                reserv.put("startHour",hours);
                reserv.put("startMinutes", minutes);
                reserv.put("endHour", endHours);
                reserv.put("endMinutes", endMinutes);
                reserv.put("client", userEmail);
                reserv.put("payed", true);
                reserv.put("parking", parkingChosen);
                db.collection("user").document(userEmail).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    // Retrieve the 'solde' field
                                    solde = documentSnapshot.getDouble("solde");

                                    // Handle the retrieved solde value
                                    Log.d("s", "solde is" + String.valueOf(solde));
                                } else {
                                    Log.d("s", "no solde");
                                }
                            }
                        });
                db.collection("reservation").document(id).set(reserv)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    db.collection("user").document(userEmail).update("solde", solde - Double.parseDouble(totalPrice.replace(",", ".")));
                                    Toast.makeText(AddReservation.this, "la reservation est ajouté", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(AddReservation.this, Reservations.class);
                                    startActivity(intent);

                                } else {
                                    Log.d("error", "reserv not added");
                                }
                            }
                        });
            }
        });



    }

    private void openDayDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int dayOfMonth) {
                day = String.valueOf(dayOfMonth);
                month = String.valueOf(m+1);
                year = String.valueOf(y);


            }
        }, 2024, 0, 1);
        dialog.show();
    }
    private void openTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hours = String.valueOf(hourOfDay);
                minutes = String.valueOf(minute);
            }
        }, 0, 0, true);
        dialog.show();
    }

    private void openEndTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endHours = String.valueOf(hourOfDay);
                endMinutes = String.valueOf(minute);
                String id = day + month + year + hours + minutes + "2";
                Log.d("time", id);

                parkingService.execute(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gridLayout.setVisibility(View.GONE);
                                parkingProgressBar.setVisibility(View.VISIBLE);
                            }
                        });
                        checkAvailability();
                    }
                });


            }
        }, 0, 0, true);
        dialog.show();

    }
    private void checkAvailability() {
        //String id = day + month + year + hours + minutes;
        for (int i = 1; i < 5; i++) {
            String id = day + month + year + hours + minutes + endHours + endMinutes + String.valueOf(i);
            Log.d("id is", "id is" + id);
            db.collection("reservation").document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    parkingProgressBar.setVisibility(View.GONE);
                                    gridLayout.setVisibility(View.VISIBLE);
                                    char lastChar = id.charAt(id.length() - 1);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            switch (lastChar) {
                                                case '1':
                                                    park1.setCardBackgroundColor(Color.RED);
                                                    break;
                                                case '2':
                                                    park2.setCardBackgroundColor(Color.RED);
                                                    break;
                                                case '3':
                                                    park3.setCardBackgroundColor(Color.RED);
                                                    break;
                                                case '4':
                                                    park4.setCardBackgroundColor(Color.RED);
                                                    break;
                                                default:
                                                    Log.d("CheckAvailability", "Index is out of expected range.");
                                                    break;
                                            }
                                        }
                                    });

                                } else {
                                    // Document does not exist
                                    parkingProgressBar.setVisibility(View.GONE);
                                    gridLayout.setVisibility(View.VISIBLE);
                                    char lastChar = id.charAt(id.length() - 1);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            switch (lastChar) {
                                                case '1':
                                                    park1.setCardBackgroundColor(Color.GREEN);
                                                    break;
                                                case '2':
                                                    park2.setCardBackgroundColor(Color.GREEN);
                                                    break;
                                                case '3':
                                                    park3.setCardBackgroundColor(Color.GREEN);
                                                    break;
                                                case '4':
                                                    park4.setCardBackgroundColor(Color.GREEN);
                                                    break;
                                                default:
                                                    Log.d("CheckAvailability", "Index is out of expected range.");
                                                    break;
                                            }
                                        }
                                    });
                                    Log.d("CheckAvailability", "Document " + document.getId() + " does not exist.");
                                }
                            } else {
                                Log.d("CheckAvailability", "Failed with: ", task.getException());
                            }
                        }
                    });
        }
    }




    public static int calculateDurationInMinutes(String startHours, String startMinutes, String endHours, String endMinutes) {
        // Convert start time and end time to total minutes
        int startTotalMinutes = Integer.parseInt(startHours) * 60 + Integer.parseInt(startMinutes);
        int endTotalMinutes = Integer.parseInt(endHours) * 60 + Integer.parseInt(endMinutes);

        // Calculate the duration in minutes
        int durationMinutes = endTotalMinutes - startTotalMinutes;

        // If the duration is negative, it means the end time is on the next day
        if (durationMinutes < 0) {
            durationMinutes += 24 * 60; // Add 24 hours in minutes
        }

        return durationMinutes;
    }
}
