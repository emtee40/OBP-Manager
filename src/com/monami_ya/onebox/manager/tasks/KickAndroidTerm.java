package com.monami_ya.onebox.manager.tasks;

import java.io.File;

import com.monami_ya.onebox.manager.GrantChecker;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;

public class KickAndroidTerm extends InstallerTask {
	public static final String KBOX2_BASE_INSTALLER = "kbox2-base-installer";

	public KickAndroidTerm(Context context, Handler handler) {
		super(context, handler);
	}
	
	public KickAndroidTerm(InstallerTask task) {
		super(task);
	}

	@Override
	public void run() {
		final String installer = "kbox2-base-installer";
		Context context = getContext();

		if (GrantChecker.checkIntentGranted(context) == false) {
			reportException(new RuntimeException("Cannot send RUN_SCRIPT intent. Check your environment."));
		} else {
			File outDir = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
			File outFile = new File(outDir, KBOX2_BASE_INSTALLER);
	
			Intent i = new Intent("jackpal.androidterm.RUN_SCRIPT");
			i.addCategory(Intent.CATEGORY_DEFAULT);
			i.putExtra("jackpal.androidterm.iInitialCommand", "cd /data/data/jackpal.androidterm && " +
					"cat " + outFile.getAbsolutePath() + " > " + KBOX2_BASE_INSTALLER + " && " +
					"chmod 755 ./" + installer + " && " +
					"./" + installer + " && " +
					"./setup && exit");
			startActivity(i);
		}
	}
}
