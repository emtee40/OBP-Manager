package com.monami_ya.onebox.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class KBox2Launcher extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if (GrantChecker.checkIntentGranted(this) == false) {
			if (PackageChecker.getInstance().isInstalledPackage(this,
					MainActivity.ENVIRONMENT_MANAGER_PACKAGE_NAME)) {
				Intent intent = new Intent();
				intent.setClassName(
						MainActivity.ENVIRONMENT_MANAGER_PACKAGE_NAME,
						MainActivity.ENVIRONMENT_MANAGER_PACKAGE_NAME
								+ ".CheckActivity");
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
										Uri uri = Uri
												.parse("market://details?id=com.monami_ya.onebox.manager.environment");
										intent = new Intent(Intent.ACTION_VIEW,
												uri);
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
		Intent intent = new Intent("jackpal.androidterm.RUN_SCRIPT");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra("jackpal.androidterm.iInitialCommand", "/data/data/jackpal.androidterm/kbox2/bin/kbox_shell");
		startActivity(intent);
		
	}
}
