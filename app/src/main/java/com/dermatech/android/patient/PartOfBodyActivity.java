package com.dermatech.android.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dermatech.android.R;

public class PartOfBodyActivity extends AppCompatActivity {
    ImageView overlayImageView;
    ImageView body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_of_body);
        getSupportActionBar().hide();

         body = findViewById(R.id.body);
         overlayImageView = findViewById(R.id.overlayImageView);
        int[] posXY = new int[2];
        overlayImageView.getLocationOnScreen(posXY);
        body.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Get the x and y coordinates of the click relative to the ImageView
                    float x = event.getX();
                    float y = event.getY();

                    // Calculate percentage position
                    float imageWidth = overlayImageView.getWidth();
                    float imageHeight = overlayImageView.getHeight();
                    float xPercent = (x / imageWidth) * 100;
                    float yPercent = (y / imageHeight) * 100;

                    // Display the coordinates
//                    String message = String.format("Clicked at: (%.1f, %.1f) pixels\n(%.1f%%, %.1f%%)",
//                            x, y, xPercent, yPercent);
//                    coordinatesText.setText(message);

                    // Show the overlay image

                    int touchX = (int) event.getX();
                    int touchY = (int) event.getY();

                    int imageX = touchX - posXY[0]; // posXY[0] is the X coordinate
                    int imageY = touchY - posXY[1]; // posXY[1] is the y coordinate
                    showOverlayImage(imageX, imageY);

                    // Return true to indicate the event was handled
                    return true;
//                }
//                   return true;
            }
        });

    }
    private void showOverlayImage(float x, float y) {
        // Position the overlay image based on click coordinates if needed
        // For example, if you want to show it directly above the click point:
         overlayImageView.setX(x += body.getLeft());
         overlayImageView.setY(y += body.getTop());

        // Make the overlay image visible
//        if (overlayImageView.getVisibility() == View.INVISIBLE) {
            // Optional: Add animation


            overlayImageView.setVisibility(View.VISIBLE);


//        }
    }
}