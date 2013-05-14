package com.monami_ya.onebox.manager.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Handler;

public class ExtractInstaller extends InstallerTask {

	public ExtractInstaller(Context context, Handler handler) {
		super(context, handler);
	}

	@Override
	public void run() {
		Context context = getContext();
		try {
			AssetManager am = context.getResources().getAssets();
			InputStream is = am.open("bin/kbox2-base-installer",
					AssetManager.ACCESS_BUFFER);
			File outDir = new File(Environment.getExternalStorageDirectory(), context.getPackageName());  
			if (outDir.exists() == false) {  
			    outDir.mkdir();  
			}
			File outFile = new File(outDir, "kbox2-base-installer");
			FileOutputStream fos = new FileOutputStream(outFile, false);
			
			byte[] buf = new byte[1024];
			int size = 0;
			while ((size = is.read(buf, 0, buf.length)) > -1) {
				fos.write(buf, 0, size);
			}
			fos.close();
			
			sendNextTask(new KickAndroidTerm(this));
		} catch (Exception e) {
			reportException(e);
		}
	}

}
