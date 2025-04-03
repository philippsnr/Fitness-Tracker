package com.example.fitnesstracker.ui.onboarding;

/**
 * Interface for handling onboarding data.
 * This interface is used to pass collected onboarding data
 * to a higher-level component.
 */
public interface OnboardingDataListener {

    /**
     * Called when onboarding data has been collected.
     *
     * @param key  The key describing the type of data (e.g., "birthday", "goal").
     * @param data The collected data object, which can be a string, number, or another type depending on the key.
     */
    void onDataCollected(String key, Object data);
}