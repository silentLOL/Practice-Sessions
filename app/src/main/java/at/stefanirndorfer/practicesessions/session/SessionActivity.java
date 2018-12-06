package at.stefanirndorfer.practicesessions.session;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import at.stefanirndorfer.practicesessions.R;
import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.session.input.SessionItemActionListener;
import timber.log.Timber;

public class SessionActivity extends AppCompatActivity implements SessionItemActionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SessionsFragment sessionsFragment = new SessionsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.sessions_fragment_container, sessionsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Timber.d("Option item home pressed");
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void onSessionItemClicked(Session session) {
        Timber.d("Session item: " + session.getName() + " clicked");
        showSessionDetailFragment(session);
    }

    private void showSessionDetailFragment(Session session) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SessionDetailFragment sessionDetailsFragment = SessionDetailFragment.newInstance(session.getId());
        fragmentManager.beginTransaction()
                .replace(R.id.sessions_fragment_container, sessionDetailsFragment)
                .addToBackStack(sessionDetailsFragment.getClass().getCanonicalName())
                .commit();
    }
}
