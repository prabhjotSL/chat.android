package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Household;
import org.chat.android.models.Visit;
import org.chat.android.models.Worker;

import com.j256.ormlite.dao.Dao;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 * TODO: split this further to incorporate role? This will likely need to be rethought, ctrl-f for credential.split(":")
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"colin:chat", "armin:chat", "lisa:chat", "duncan:chat",
			"jim:chat", "sbongile:chat"};

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUserName;
	private String mPassword;

	// UI references.
	private EditText mUserNameView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private Spinner roleSpinner;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "org.chat.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "chat.org";
    // The account name
    public static final String ACCOUNT = "chat-tablet";
    // Create the account type and default account
    static Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mUserName = getIntent().getStringExtra("some string");
		mUserNameView = (EditText) findViewById(R.id.user_name);
		mUserNameView.setText(mUserName);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					attemptLogin();
			}
		});
		
		// TODO: match this up with the UN/PW/ROLE on authentication, maybe thing about other error messages 
		roleSpinner = (Spinner) findViewById(R.id.role_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.role_array, R.layout.login_spinner_item);
		adapter.setDropDownViewResource(R.layout.login_spinner_item);
		roleSpinner.setAdapter(adapter);

	}

	// @Override
//	 public boolean onCreateOptionsMenu(Menu menu) {
//		 super.onCreateOptionsMenu(menu);
//		 getMenuInflater().inflate(R.menu.activity_login, menu);
//		 return true;
//	 }

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUserNameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUserName = mUserNameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password - comment out for testing, use for PROD
//		if (TextUtils.isEmpty(mPassword)) {
//			mPasswordView.setError(getString(R.string.error_field_required));
//			focusView = mPasswordView;
//			cancel = true;
//		} else if (mPassword.length() < 2) {
//			mPasswordView.setError(getString(R.string.error_invalid_password));
//			focusView = mPasswordView;
//			cancel = true;
//		}
//		// Check for a valid user ID.
//		if (TextUtils.isEmpty(mUserName)) {
//			mUserNameView.setError(getString(R.string.error_field_required));
//			focusView = mUserNameView;
//			cancel = true;
//		}
		// /comment
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			// TODO: dump this when we're actually ORMing
//			try {
//				// Simulate network access.
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				return false;
//			}
//
			
			// if for does not return true, ie if un/pw do not match. Again, comment out for testing, use for PROD		
//			for (String credential : DUMMY_CREDENTIALS) {
//				String[] pieces = credential.split(":");
//				if (pieces[0].equals(mUserName)) {
//					// Account exists, return true if the password matches.
//					return pieces[1].equals(mPassword);
//				}
//			}
			// /comment

			return true;
		}

		// @Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);
			
			if (success) {
				int vId = 0;
				// check if there are previously uncompleted visits for this worker. NB: this assumes there is only ever one unrestored visit (TESTME)
				String workerName = mUserNameView.getText().toString();
				int workerId = ModelHelper.getWorkerForName(getApplicationContext(), workerName).getId();
				
				Dao<Visit, Integer> vDao;		
				DatabaseHelper vDbHelper = new DatabaseHelper(getApplicationContext());
				try {
					vDao = vDbHelper.getVisitsDao();
					List<Visit> vList = vDao.queryBuilder().where().eq("worker_id",workerId).and().isNull("end_time").query();
					Iterator<Visit> iter = vList.iterator();
					while (iter.hasNext()) {
						Visit v = iter.next();
						vId = v.getId();
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				// figure out what activity to launch next 
				Intent i;
				Bundle b = new Bundle();
				
				// if there is no uncompleted visit, go to SetupVisitActivity, else go to RestoreVisitActivity
				if (vId == 0) {
					i = new Intent(LoginActivity.this, SetupVisitActivity.class);
				} else {
					i = new Intent(LoginActivity.this, RestoreVisitActivity.class);
					b.putInt("visitId",vId);
				}
				
				b.putString("workerName",workerName);
				b.putString("role",roleSpinner.getSelectedItem().toString());
				i.putExtras(b);
				startActivity(i);
				finish();

			 } else {
				 mPasswordView
				 .setError(getString(R.string.error_incorrect_password));
				 mPasswordView.requestFocus();
			 }
		 }

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	
	// NB: some of this is here for testing, some will be deleted for prod
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_resources:
	    	Intent i = new Intent(LoginActivity.this, ResourcesActivity.class);
	    	Bundle b = new Bundle();
	    	int workerId = ModelHelper.getWorkerForName(getApplicationContext(), mUserNameView.getText().toString()).getId();
	    	b.putInt("workerId",workerId);
	    	i.putExtras(b);  
	    	startActivity(i);
	        return true;
	    case R.id.menu_settings:
	        Toast.makeText(getApplicationContext(), "Running setupDB...", Toast.LENGTH_SHORT).show();
	        prepopulateDB();
	        return true;
	    case R.id.menu_sync:
	        Toast.makeText(getApplicationContext(), "Triggering sync adapter to sync with server...", Toast.LENGTH_LONG).show();
	        triggerSyncAdaper();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
    private void prepopulateDB() {
		Intent i = new Intent(LoginActivity.this, SetupDB.class);
		startActivity(i);
    }
    
    public void triggerSyncAdaper() {
    	Account mAccount = CreateSyncAccount(this);
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }
    
    public static Account CreateSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        	ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true); //this programmatically turns on the sync for new sync adapters.
        	return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        	return null;
        }
    }
}
