package com.dermatech.android.patient;



import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dermatech.android.ImageClassifier;
import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.dermatech.android.admin.questions.AdapterQuestion;
import com.dermatech.android.model.Diagnosis;
import com.dermatech.android.model.Question;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.yalantis.ucrop.UCrop;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;


public class ScanFragment extends Fragment {

    private ImageClassifier classifier;
    ImageView imageViewForSkin;
//    TextView resultTextView;


    ImageView overlayImageView;
    ImageView body;

    RelativeLayout step1,step2;
    CardView step3;

    int index = 1;

    Button continueButton;
    TextView retakePhoto;

    RecyclerView recyclerViewQuestions;
   static Diagnosis diagnosis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        // Initialize the classifier
        try {
            classifier = new ImageClassifier(getActivity());
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error loading model or labels", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        HomePatientActivity.screenTitle.setText("Select Area");
        imageViewForSkin = view.findViewById(R.id.imageViewForSkin);
        step1 = view.findViewById(R.id.step1);
        step2 = view.findViewById(R.id.step2);
        step3 = view.findViewById(R.id.step3);
        continueButton = view.findViewById(R.id.continueButton);
        retakePhoto = view.findViewById(R.id.retakePhoto);
        recyclerViewQuestions = view.findViewById(R.id.recyclerViewQuestions);
        HomePatientActivity.donut_progress.setText("1/4");

//end ai code

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == 2){
                    ImagePicker.Companion.with(getActivity())
                            .maxResultSize(512,512,true)
                            .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                            .createIntentFromDialog((Function1)(new Function1(){
                                public Object invoke(Object var1){
                                    this.invoke((Intent)var1);
                                    return Unit.INSTANCE;
                                }
                                public final void invoke(@NotNull Intent it){
                                    Intrinsics.checkNotNullParameter(it,"it");
                                    launcher.launch(it);
                                }
                            }));
                }else {
                    if(continueButton.getText().toString().equals("Show Result")){
                        for (Question question : questions){

                            if(question.answer == null){
                                Toast.makeText(getActivity(), "Answer All Questions", Toast.LENGTH_SHORT).show();
                                Log.e("Answer ===> Null",question.answer+"");
                                return;
                            }
                            String age = "";
                            if(question.questionType.equals("Multiple Choice")){
                                age = question.answer;
                            }
                            Log.e("Answer ===> "+question.question +" ",question.answer);

                            String id = FirebaseDatabase.getInstance().getReference().child("diagnosis").push().getKey();
                            String curontDate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                            diagnosis = new Diagnosis(id,skinRecognition, ImageUtils.convert(skinBitmap),curontDate,questions, FirebaseAuth.getInstance().getCurrentUser().getUid(),age);
                            Intent intent = new Intent(getActivity(), DiagnosisResultActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
//                            getActivity().finish();
                            index = 1;
                            HomePatientActivity activity = (HomePatientActivity) getActivity();
                            if (activity != null) {
                                BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
                                bottomNavigationView.setSelectedItemId(R.id.scan);
                            }

                        }
                    }else {
                        setStep();
                    }
                }
            }
        });

        retakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(getActivity())
                        .maxResultSize(512,512,true)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog((Function1)(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }
                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                launcher.launch(it);
                            }
                        }));
            }
        });

        body = view.findViewById(R.id.body);
        overlayImageView = view.findViewById(R.id.overlayImageView);
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

        return view;
    }

    ArrayList<Question> questions = new ArrayList<>();

    void loadQuestions(String type){
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        AdapterQuestionForPatient adapterQuestion = new AdapterQuestionForPatient(getActivity(),questions);
        recyclerViewQuestions.setAdapter(adapterQuestion);

        FirebaseDatabase.getInstance().getReference().child("questions").orderByChild("skinDiseaseType").equalTo(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questions.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Question question = dataSnapshot.getValue(Question.class);
                    questions.add(question);
                }
                adapterQuestion.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    void setStep(){
        index++;
        Log.e("Index === ",index+"");
        if(index == 2){
            HomePatientActivity.screenTitle.setText("Take photo");
            HomePatientActivity.donut_progress.setText("2/4");
            HomePatientActivity.donut_progress.setProgress(2);
            step1.setVisibility(View.GONE);
            step2.setVisibility(View.VISIBLE);
            step3.setVisibility(View.GONE);
        }else if (index == 3){
            HomePatientActivity.screenTitle.setText("Take photo");
            step3.setVisibility(View.VISIBLE);
            step1.setVisibility(View.GONE);
            step2.setVisibility(View.GONE);
            HomePatientActivity.donut_progress.setText("3/4");
            HomePatientActivity.donut_progress.setProgress(3);
            recognizeImage();
        }else if(index == 4){
            HomePatientActivity.screenTitle.setText("Questionnaire");
            HomePatientActivity.donut_progress.setText("4/4");
            HomePatientActivity.donut_progress.setProgress(4);
            loadQuestions(skinRecognition);
            recyclerViewQuestions.setVisibility(View.VISIBLE);
            step1.setVisibility(View.GONE);

            step2.setVisibility(View.GONE);
            step3.setVisibility(View.GONE);
            retakePhoto.setVisibility(View.GONE);
            continueButton.setText("Show Result");
        }
    }

    String skinRecognition;
    void recognizeImage(){

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());
//        imageViewForSkin.setDrawingCacheEnabled(true);
//        imageViewForSkin.buildDrawingCache();
//        final Bitmap bitmap = Bitmap.createBitmap(imageViewForSkin.getDrawingCache());
//        imageViewForSkin.setDrawingCacheEnabled(false);

        executorService.execute(() -> {
            // Classify the image
            List<ImageClassifier.Recognition> results = classifier.classifyImage(skinBitmap);

            // Update UI on the main thread
            mainHandler.post(() -> {
                // Hide loading indicator if you showed one
                // loadingIndicator.setVisibility(View.GONE);

                // Display the results
//                            StringBuilder resultText = new StringBuilder();
//                            results.get(1).getConfidence()
//                            for (int i = 0; i < Math.min(3, results.size()); i++) {
//                                ImageClassifier.Recognition recognition = results.get(i);
//                                resultText.append(recognition.getLabel())
//                                        .append(": ")
//                                        .append(String.format("%.2f%%", recognition.getConfidence() * 100))
//                                        .append("\n");
//                            }
//                            resultTextView.setText(resultText.toString());

                // Initialize variables to hold the highest confidence and corresponding recognition
                float highestConfidence = 0.0f;
                ImageClassifier.Recognition bestRecognition = null;

                // Iterate through the results to find the highest confidence
                for (ImageClassifier.Recognition recognition : results) {
                    float confidence = recognition.getConfidence();
                    if (confidence > highestConfidence) {
                        highestConfidence = confidence;
                        bestRecognition = recognition;
                    }
                }
                if (bestRecognition != null) {
                    System.out.println("Best Recognition: " + bestRecognition.getLabel());
                    System.out.println("Highest Confidence: " + bestRecognition.getConfidence());
                    skinRecognition = bestRecognition.getLabel();
                    if((bestRecognition.getConfidence() * 100) < 70){
                        Log.e("skinRecognition ===> " , "Unknown");
                    }
                 //   Log.e("skinRecognition === > ", skinRecognition +String.format("%.2f%%", bestRecognition.getConfidence() * 100));
                 //   resultTextView.setText(bestRecognition.getLabel() +" : " +String.format("%.2f%%", bestRecognition.getConfidence() * 100));
                } else {
                    System.out.println("No recognitions found.");
                }
            });
        });
    }

//    private final ActivityResultLauncher<Intent> launcherCrop = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getData() != null) {
//                        Uri uri = UCrop.getOutput(result.getData());
//                        imageViewForSkin.setImageURI(uri);
//                        recognizeImage();
//                    }
//                }
//            });
Bitmap skinBitmap;
    ActivityResultLauncher<Intent> launcher=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                if(result.getResultCode()==RESULT_OK){

                    Uri uri=result.getData().getData();
                    // Use the uri to load the image
                    try {
                        skinBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    imageViewForSkin.setImageURI(uri);
                    if(retakePhoto.getVisibility() != View.VISIBLE){
                        setStep();
                    }




                    retakePhoto.setVisibility(View.VISIBLE);
                }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                }
            });
}