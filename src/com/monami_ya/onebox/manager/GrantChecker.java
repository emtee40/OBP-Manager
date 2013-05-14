package com.monami_ya.onebox.manager;

import android.content.Context;
import android.content.pm.PackageManager;

public class GrantChecker {


	static public boolean checkIntentGranted(Context context) {
		int p = context.getPackageManager().checkPermission(
				"jackpal.androidterm.permission.RUN_SCRIPT",
				context.getPackageName());
		return (p == PackageManager.PERMISSION_GRANTED);
	}


}
