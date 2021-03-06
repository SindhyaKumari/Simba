package com.email.backgroundmaillibrary;

import java.util.ArrayList;

import com.email.backgroundmaillibrary.mail.GmailSender;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Kristijan Drača https://plus.google.com/u/0/+KristijanDrača
 */
public class BackgroundMail {
	String TAG = "Bacground Mail Library";
	String username, password, mailto, subject, body, sendingMessage,
			sendingMessageSuccess;
	boolean processVisibility = true;
	//ArrayList<String> attachments = new ArrayList<String>();
	Context mContext;

	public BackgroundMail(Context context) {
		this.mContext = context;
	}

	public void setGmailUserName(String string) {
		this.username = string;
	}

	public void setGmailPassword(String string) {
		this.password = string;
	}

	public void setProcessVisibility(boolean state) {
		this.processVisibility = state;
	}

	public void setMailTo(String string) {
		this.mailto = string;
	}

	public void setFormSubject(String string) {
		this.subject = string;
	}

	public void setFormBody(String string) {
		this.body = string;
	}

	public void setSendingMessage(String string) {
		this.sendingMessage = string;
	}

	public void setSendingMessageSuccess(String string) {
		this.sendingMessageSuccess = string;

	}
	
	public void setAttachment(String attachments) {
		//this.attachments.add(attachments);

	}

	public void send() {
		boolean valid = true;
		if (username == null && username.isEmpty()) {
			Log.e(TAG, "You didn't set Gmail username!");
			valid = false;
		}
		if (password == null && password.isEmpty()) {
			Log.e(TAG, "You didn't set Gmail password!");
			valid = false;
		}
		if (mailto == null && mailto.isEmpty()) {
			Log.e(TAG, "You didn't set email recipient!");
			valid = false;
		}
		if (Utils.isNetworkAvailable(mContext) == false) {
			Log.e(TAG, "User don't have internet connection!");
			valid = false;
		}
		if (valid == true) {
			new startSendingEmail().execute();
		}
	}

	public class startSendingEmail extends AsyncTask<String, Void, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			if (processVisibility != false) {
				pd = new ProgressDialog(mContext);
				if (sendingMessage != null && !sendingMessage.isEmpty()) {
					pd.setMessage(sendingMessage);
				} else {
					Log.d(TAG, "We dont have sending message so we use generic");
					pd.setMessage("Loading...");
				}
				pd.setCancelable(false);
				pd.show();
			}
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			try {
				GmailSender sender = new GmailSender(username, password);
				/*if(!attachments.isEmpty()){
					for (int i = 0; i < attachments.size(); i++) {
							if(!attachments.get(i).isEmpty()){
								sender.addAttachment(attachments.get(i));
							}
					}
				}*/
				sender.sendMail(subject, body, username, mailto);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage().toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (processVisibility != false) {
				pd.dismiss();
				if (sendingMessageSuccess != null
						&& !sendingMessageSuccess.isEmpty()) {
					Toast.makeText(mContext, sendingMessageSuccess,
							Toast.LENGTH_SHORT).show();
				} else {
					Log.d(TAG,
							"We dont have sending success message so we use generic");
					Toast.makeText(mContext,
							"PIN number has been sent to your mail.",
							Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}
	}

}
