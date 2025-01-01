package fr.arinonia.launcher.utils;

public interface CallBack {
    default void onProgress(final double progress, final String status) {}
    default void onComplete() {}
    default void onError(String error) {}
}
