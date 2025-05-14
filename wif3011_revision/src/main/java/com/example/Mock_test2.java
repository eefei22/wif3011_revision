/*
    🧪 Concurrent Programming Question 2: Emotion Counter System
    🧠 Scenario:
        A system tracks how many times a specific emotion appears in data
        from different sensors (speech, facial, vitals) across multiple sessions.

    Each sensor thread reports one emotion per session. You want to:
        - Track how often each emotion (e.g., "happy", "neutral", "angry", "sad") 
        appears across all sources and sessions.
        - Keep the counters thread-safe — so they don’t break even when multiple threads update them simultaneously.

    🔧 Requirements:
        - Run 10 sessions (like 10 rounds).
        - In each session:
        - Start 3 sensor threads: SpeechThread, FacialThread, VitalsThread.
        - Each thread:
            - Sleeps for a random time (to simulate I/O delay).
            - Randomly selects an emotion from a predefined list.
            - Increments the global counter for that emotion using atomic variables.

    After all threads in all sessions finish, print the total counts for each emotion.
 */

package com.example;

public class Mock_test2 {
    
}
