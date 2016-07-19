package pl.mslawin.notes.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.util.logging.Logger;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.TaskListApplication;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.service.TasksService;
import pl.mslawin.notes.app.service.UserService;
import pl.mslawin.notes.app.service.user.UserAuthentication;


/**
 * Created by mslawin on 11/12/15.
 */
public class GoogleSignInActivity extends FragmentActivity implements OnClickListener, OnConnectionFailedListener {

    private static final Logger logger = Logger.getLogger(GoogleSignInActivity.class.getName());

    private static final int RC_SIGN_IN = 9001;

    private final UserService userService = new UserService();
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestEmail()
                .requestProfile()
                .requestIdToken("251316470014-u2sqvan0psr55p2f8fdqcvn83vuur6tk.apps.googleusercontent.com")
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setContentView(R.layout.activity_login);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            signIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            UserAuthentication userAuthentication = handleSignInResult(signInResult);
            if (userAuthentication != null) {
                ((TaskListApplication) getApplication()).setUserAuthentication(userAuthentication);
                startActivity(new Intent(this, TaskListActivity.class));
            }
        }
    }

    private UserAuthentication handleSignInResult(GoogleSignInResult signInResult) {
        if (signInResult.isSuccess()) {
            GoogleSignInAccount account = signInResult.getSignInAccount();
            try {
                return userService.authenticate(getApplication(), account.getIdToken());
            } catch (NoNetworkConnectionException e) {
                TasksService.handleNoNetworkException(logger, e, getApplicationContext());
            } catch (TasksListException e) {
                TasksService.handleTasksListException(logger, e, getApplicationContext());
            }
        }
        return null;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}