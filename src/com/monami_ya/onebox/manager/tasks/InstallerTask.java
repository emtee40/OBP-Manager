package com.monami_ya.onebox.manager.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

abstract public class InstallerTask implements Runnable {
	private Context context;
	private Handler handler;
	private boolean interrupted = false;
	
	public InstallerTask(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	
	public InstallerTask(InstallerTask task) {
		this(task.getContext(), task.getHandler());
	}

	protected Context getContext() {
		return context;
	}
	
	protected Handler getHandler() {
		return handler;
	}
	
	protected void sendNextTask(InstallerTask nextTask) {
		Message message = Message.obtain(handler);
		message.obj = nextTask;
		handler.sendMessage(message);
	}
	

	protected void startActivity(Intent i) {
		Message message = Message.obtain(handler);
		message.obj = i;
		handler.sendMessage(message);
	}

	protected void reportException(Exception e) {
		Message message = Message.obtain(handler);
		message.obj = e;
		handler.sendMessage(message);
	}

	public void interrupt() {
		interrupted = true;
	}
	
	public boolean isInterrupted() {
		return interrupted;
	}
}
