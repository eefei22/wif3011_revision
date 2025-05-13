/*
 * Scenario:
 * - Backend pipeline to simultaneously handle mutiple sources of data
 * - Data sources include: speech from microhphone, images from camera, and numerical data from a wearable device
 * - Individual data sources should be processed concurrently
 * Requirements:
 * Three threads: 
 *  - SpeechThread – simulates recording and transcribing user speech.
 *  - FacialThread – simulates capturing and analyzing facial expressions.
 *  - VitalsThread – simulates reading from wearable sensor data.
 * Each thread should:
 *  - Sleep for a random amount of time (simulate I/O delay).
 *  - Then “produce” some mock result (e.g., "happy", "neutral", "angry").
 * Once all three inputs are ready, a CoordinatorThread should:
 *  - Wait until all results are available.
 *  - Combine the data and print a summary (e.g., CombinedEmotion: [speech=happy, facial=neutral, vitals=angry]).
 *  - Reset for the next round.
 * Ensure:
 *  - Safe access to shared data.
 *  - Proper thread synchronization so the coordinator doesn’t read incomplete data.
 *  - Each round runs independently and repeatedly (e.g., 5 rounds total).
 */

package com.example;

public class Mock_test {

    // Shared data class to hold results from different threads
    static class SharedData {
        private String speechResult;
        private String facialResult;
        private String vitalsResult;
        private boolean isSpeechReady = false;
        private boolean isFacialReady = false;
        private boolean isVitalsReady = false;

        public synchronized void setSpeechResult(String result) {
            this.speechResult = result;
            this.isSpeechReady = true;
            notifyAll();
        }

        public synchronized void setFacialResult(String result) {
            this.facialResult = result;
            this.isFacialReady = true;
            notifyAll();
        }

        public synchronized void setVitalsResult(String result) {
            this.vitalsResult = result;
            this.isVitalsReady = true;
            notifyAll();
        }

        public synchronized String getResults() throws InterruptedException {
            while (!isSpeechReady || !isFacialReady || !isVitalsReady) {
                wait();
            }
            return "CombinedEmotion: [speech=" + speechResult + ", facial=" + facialResult + ", vitals=" + vitalsResult + "]";
        }

        public synchronized void reset() {
            isSpeechReady = false;
            isFacialReady = false;
            isVitalsReady = false;
        }
    }
    // Thread to simulate speech processing
    static class SpeechThread extends Thread {
        private SharedData sharedData;

        public SpeechThread(SharedData sharedData) {
            this.sharedData = sharedData;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((long) (Math.random() * 1000));
                String result = "happy"; // Mock result
                sharedData.setSpeechResult(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Thread to simulate facial expression processing
    static class FacialThread extends Thread {
        private SharedData sharedData;

        public FacialThread(SharedData sharedData) {
            this.sharedData = sharedData;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((long) (Math.random() * 1000));
                String result = "neutral"; // Mock result
                sharedData.setFacialResult(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Thread to simulate vitals processing
    static class VitalsThread extends Thread {
        private SharedData sharedData;

        public VitalsThread(SharedData sharedData) {
            this.sharedData = sharedData;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((long) (Math.random() * 1000));
                String result = "angry"; // Mock result
                sharedData.setVitalsResult(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Coordinator thread to combine results
    static class CoordinatorThread extends Thread {
        private SharedData sharedData;

        public CoordinatorThread(SharedData sharedData) {
            this.sharedData = sharedData;
        }

        @Override
        public void run() {
            try {
                String combinedResult = sharedData.getResults();
                System.out.println(combinedResult);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    
    public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 5; i++) {
        SharedData sharedData = new SharedData();

        Thread speechThread = new SpeechThread(sharedData);
        Thread facialThread = new FacialThread(sharedData);
        Thread vitalsThread = new VitalsThread(sharedData);
        Thread coordinatorThread = new CoordinatorThread(sharedData); 

        speechThread.start();
        facialThread.start();
        vitalsThread.start();
        coordinatorThread.start(); 

        Thread.sleep(1500); 
    }
}
    
}
