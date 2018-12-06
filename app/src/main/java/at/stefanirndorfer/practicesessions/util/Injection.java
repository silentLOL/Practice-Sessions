package at.stefanirndorfer.practicesessions.util;

import at.stefanirndorfer.practicesessions.data.source.SessionsRepository;
import at.stefanirndorfer.practicesessions.data.source.remote.SessionsNetworkDataSource;

/**
 * Enables injection of mock implementations for
 * {@link at.stefanirndorfer.practicesessions.data.source.SessionsDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 * TODO: No fake data sources implemented yet
 */
class Injection {
    public static SessionsRepository provideTasksRepository() {
        return SessionsRepository.getInstance(SessionsNetworkDataSource.getInstance(), new AppExecutors());
    }
}
