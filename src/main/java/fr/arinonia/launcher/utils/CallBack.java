package fr.arinonia.launcher.utils;

public interface CallBack {
    default void onProgress(final double progress, final String status) {}
    default void onComplete() {}
    default void onError(final String error) {}

    //!for later
    //default void onError(final String error, final Throwable throwable, final boolean shouldStop) {}
}
