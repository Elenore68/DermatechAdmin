package com.dermatech.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageClassifier {
    private static final String TAG = "ImageClassifier";

    private static final String MODEL_PATH = "model.tflite";
//    private static final String MODEL_PATH = "skin_quant_model.tflite";
//    private static final String LABEL_PATH = "labels.txt";
    private static final String LABEL_PATH = "labels.txt";

    private static final int INPUT_SIZE = 224; // Default for many models
    private static final int PIXEL_SIZE = 3; // RGB channels
    private static final int BATCH_SIZE = 1;
    private static final int NUM_CHANNELS = 3;
    private static final float IMAGE_MEAN = 127.5f;
    private static final float IMAGE_STD = 127.5f;

    // The model expects float inputs (4 bytes per channel)
    private static final int BYTES_PER_CHANNEL = 4;

    private Interpreter interpreter;
    private List<String> labelList;
    private int inputSize;
    private ByteBuffer inputBuffer;

    public ImageClassifier(Context context) throws IOException {
        // Load the model file
        MappedByteBuffer modelFile = loadModelFile(context.getAssets());
        Interpreter.Options options = new Interpreter.Options();
        interpreter = new Interpreter(modelFile, options);

        // Get input tensor shape to confirm dimensions
        int[] inputShape = interpreter.getInputTensor(0).shape();
        inputSize = inputShape[1]; // Should be 224 based on your code

        // Calculate the correct buffer size
        int bufferSize = BATCH_SIZE * inputSize * inputSize * NUM_CHANNELS * BYTES_PER_CHANNEL;
        inputBuffer = ByteBuffer.allocateDirect(bufferSize);
        inputBuffer.order(ByteOrder.nativeOrder());

        // Load labels
        labelList = loadLabelList(context.getAssets());

        Log.d(TAG, "Model loaded. Input shape: " +
                inputShape[0] + "x" + inputShape[1] + "x" +
                inputShape[2] + "x" + inputShape[3] +
                ", Buffer size: " + bufferSize + " bytes");
    }

    public List<Recognition> classifyImage(Bitmap bitmap) {
        if (bitmap == null) {
            return new ArrayList<>();
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        convertBitmapToByteBuffer(resizedBitmap);

        // Create output container
        float[][] output = new float[1][labelList.size()];

        try {
            // Run inference
            interpreter.run(inputBuffer, output);
            return getSortedResult(output[0]);
        } catch (Exception e) {
            Log.e(TAG, "Error during inference: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<String> loadLabelList(AssetManager assetManager) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(LABEL_PATH)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (bitmap == null || inputBuffer == null) {
            return;
        }

        inputBuffer.rewind();

        int[] intValues = new int[inputSize * inputSize];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        // Convert the image to a byte buffer for the model
        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                final int val = intValues[pixel++];

                // Extract RGB values and normalize for float input
                inputBuffer.putFloat((((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                inputBuffer.putFloat((((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                inputBuffer.putFloat(((val & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
            }
        }
    }

    private List<Recognition> getSortedResult(float[] probabilities) {
        Map<String, Float> map = new HashMap<>();
        for (int i = 0; i < labelList.size(); ++i) {
            map.put(labelList.get(i), probabilities[i]);
        }

        // Sort results by confidence
        List<Recognition> recognitions = new ArrayList<>();
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            recognitions.add(new Recognition(entry.getKey(), entry.getValue()));
        }

        recognitions.sort((o1, o2) -> Float.compare(o2.getConfidence(), o1.getConfidence()));
        return recognitions;
    }

    // Inner class to represent a recognition result
    public static class Recognition {
        private final String label;
        private final float confidence;

        public Recognition(String label, float confidence) {
            this.label = label;
            this.confidence = confidence;
        }

        public String getLabel() {
            return label;
        }

        public float getConfidence() {
            return confidence;
        }

        @Override
        public String toString() {
            return "Label: " + label + ", Confidence: " + confidence;
        }
    }

    public void close() {
        if (interpreter != null) {
            interpreter.close();
            interpreter = null;
        }
    }
}