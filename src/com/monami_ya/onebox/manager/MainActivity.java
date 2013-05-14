package com.monami_ya.onebox.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.monami_ya.onebox.manager.tasks.ExtractInstaller;
import com.monami_ya.onebox.manager.tasks.InstallerTask;

public class MainActivity extends Activity {

	static final String ENVIRONMENT_MANAGER_PACKAGE_NAME = "com.monami_ya.onebox.manager.environment";

	private Handler handler;
	private InstallerTask task;
	private Button nextButton;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		nextButton = (Button) findViewById(R.id.next);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					if (msg.obj instanceof InstallerTask) {
						task = (InstallerTask) (msg.obj);
						new Thread(task).start();
					} else if (msg.obj instanceof Intent) {
						Intent intent = (Intent) (msg.obj);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					} else if (msg.obj instanceof String) {
						TextView view = (TextView) findViewById(R.id.information);
						view.setText((String) (msg.obj));
					} else if (msg.obj instanceof Exception) {
						TextView view = (TextView) findViewById(R.id.log_message);
						CharSequence text = view.getText();
						view.setText(text.toString() + "\n"
								+ ((Exception) (msg.obj)).getMessage());
					}
				}
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (GrantChecker.checkIntentGranted(this) == false) {
			if (PackageChecker.getInstance().isInstalledPackage(this,
					ENVIRONMENT_MANAGER_PACKAGE_NAME)) {
				Intent intent = new Intent();
				intent.setClassName(ENVIRONMENT_MANAGER_PACKAGE_NAME,
						ENVIRONMENT_MANAGER_PACKAGE_NAME + ".CheckActivity");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			} else {
				new AlertDialog.Builder(this)
				.setMessage(R.string.environment_corrupted)
				.setCancelable(false)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								Intent intent = new Intent();
								Uri uri = Uri.parse("market://details?id=com.monami_ya.onebox.manager.environment");
								intent = new Intent(Intent.ACTION_VIEW, uri);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								finish();
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						}).show();

			}
		}
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = Message.obtain(handler);
				message.obj = new ExtractInstaller(getApplicationContext(),
						handler);
				handler.sendMessage(message);
				nextButton.setEnabled(false);
			}
		});

		Button cancelBtn = (Button) findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (task != null) {
					task.interrupt();
				}
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
