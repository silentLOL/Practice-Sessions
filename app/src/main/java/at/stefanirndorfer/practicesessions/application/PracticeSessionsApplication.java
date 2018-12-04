package at.stefanirndorfer.practicesessions.application;

import android.app.Application;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import at.stefanirndorfer.practicesessions.BuildConfig;
import timber.log.Timber;

import static android.util.Log.INFO;

public class PracticeSessionsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // custom Timber tree to prevent logging below severity w on production code
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private static final class CrashReportingTree extends Timber.Tree {
        @Override
        protected boolean isLoggable(@Nullable String tag, int priority) {
            return priority >= INFO;
        }


        @Override
        protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
            if (t != null) {
                if (priority == Log.ERROR) {
                    Timber.e(t);
                } else if (priority == Log.WARN) {
                    Timber.w(t);
                }
            }
        }
    }
}
