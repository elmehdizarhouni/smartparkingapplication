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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.ReservationModel;

public class UpdateReservation extends AppCompatActivity {
    Button dayPicker;
    FirebaseAuth mAuth;
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
    String endMinutes;
    TextView dateAndDay;
    TextView price;
    Boolean dayPicked;
    Boolean TimePicked;
    GridLayout gridLayout;
    MaterialCardView park1;
    MaterialCardView park2;
    MaterialCardView park3;
    MaterialCardView park4;
    MaterialCardView priceCard;
    String parkingChosen;
    Button pay;
    ExecutorService parkingService;
    ExecutorService priceService;
    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_reservation);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        ReservationModel reservation = (ReservationModel) intent.getSerializableExtra("reservation");
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
        assert reservation != null;
        String dd = reservation.getDay() +"/" + reservation.getMonth() + "/" + reservation.getYear() + " "  + reservation.getStartHour() + ":" + reservation.getStartMinute() + " - " + reservation.getEndHour() + ":" + reservation.getEndMinute();
        dateAndDay.setText(dd);
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
                                // Pause execution for 3 seconds (3000 milliseconds)
                                Thread.sleep(3000);

                                // Alternative using TimeUnit for better readability
                                // TimeUnit.SECONDS.sleep(3);

                            } catch (InterruptedException e) {
                                // Handle the exception if the sleep is interrupted
                                System.out.println("Sleep was interrupted");
                                Thread.currentThread().interrupt(); // Preserve interrupt status
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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
                                // Pause execution for 3 seconds (3000 milliseconds)
                                Thread.sleep(3000);

                                // Alternative using TimeUnit for better readability
                                // TimeUnit.SECONDS.sleep(3);

                            } catch (InterruptedException e) {
                                // Handle the exception if the sleep is interrupted
                                System.out.println("Sleep was interrupted");
                                Thread.currentThread().interrupt(); // Preserve interrupt status
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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
                                // Pause execution for 3 seconds (3000 milliseconds)
                                Thread.sleep(3000);

                                // Alternative using TimeUnit for better readability
                                // TimeUnit.SECONDS.sleep(3);

                            } catch (InterruptedException e) {
                                // Handle the exception if the sleep is interrupted
                                System.out.println("Sleep was interrupted");
                                Thread.currentThread().interrupt(); // Preserve interrupt status
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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
                                // Pause execution for 3 seconds (3000 milliseconds)
                                Thread.sleep(3000);

                                // Alternative using TimeUnit for better readability
                                // TimeUnit.SECONDS.sleep(3);

                            } catch (InterruptedException e) {
                                // Handle the exception if the sleep is interrupted
                                System.out.println("Sleep was interrupted");
                                Thread.currentThread().interrupt(); // Preserve interrupt status
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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
                db.collection("reservation").document(reservation.getId()).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        db.collection("reservation").document(id).set(reserv)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(UpdateReservation.this, "la reservation est modifiée", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(UpdateReservation.this, Reservations.class);
                                                            startActivity(intent);

                                                        } else {
                                                            Log.d("error", "reserv not added");
                                                        }
                                                    }
                                                });
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
                                    // Document exists

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

    private void getPrice(){
        try {
            // Pause execution for 3 seconds (3000 milliseconds)
            Thread.sleep(3000);

            // Alternative using TimeUnit for better readability
            // TimeUnit.SECONDS.sleep(3);

        } catch (InterruptedException e) {
            // Handle the exception if the sleep is interrupted
            System.out.println("Sleep was interrupted");
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }

        price.setText("10 DH");
    }
}
