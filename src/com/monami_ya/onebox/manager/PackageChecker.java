package com.monami_ya.onebox.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageChecker {
	private static PackageChecker self;

	private PackageChecker() {
		/* singleton */
	}
	
	public static PackageChecker getInstance() {
		if (self == null) {
			self = new PackageChecker();
		}
		return self;
	}
	
	public boolean isInstalledPackage(Context context, String packageName) {
		boolean result;
		try {
			PackageManager packageManager = context.getPackageManager();
			packageManager.getApplicationInfo(packageName,
					PackageManager.GET_META_DATA);
			result = true;
		} catch (NameNotFoundException exception) {
			/* Package is not installed. */
			result = false;
		}
		return result;
	}


}
