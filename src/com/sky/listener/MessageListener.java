package com.sky.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.sky.fx.Controller;
import com.sky.util.AppMsgUtil;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class MessageListener {
	Label messageViewer = null;
	Controller ctrl = null;

	private MessageListener() {
		// TODO Auto-generated constructor stub
	}
	
	public MessageListener(Label label, Controller ctrl) {
		this();
		setMessageViewer(label);
		this.ctrl = ctrl;
	}

	public void setMessageViewer(Label label) {
		messageViewer = label;
	}
	
	public void show(String msg) {
		messageViewer.setText(msg);
	}
	
	public void handleException(Exception e) {
		show(e.toString());
	}
	
	public void listen(InputStream is, String encoding) {
		final InputStream f_is = is;
        final String f_encoding = encoding;
		
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(f_is, f_encoding));
					String line = null;
					while ((line = br.readLine()) != null) {
						if (line.indexOf("msg.") == 0) {
							String msg = AppMsgUtil.get(line.split(","));
							updateMessage(msg);
							updateMessageCallback(msg);
						}
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return null;
			}
		};
		messageViewer.textProperty().bind(task.messageProperty());
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent paramT) {
				messageViewer.textProperty().unbind();
				onSucceededCallback();
			}
		});
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}
	
	void updateMessageCallback(String msg) {
		
	}
	
	void onSucceededCallback() {
		
	}
}
