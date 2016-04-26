package com.sky.bat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sky.util.FileGetter;

public class BatRunner {

	public BatRunner() {
		// TODO Auto-generated constructor stub
	}

	public Process runBat(String name, String... params) {
		String uri = FileGetter.getFile(name + ".bat");
		
		List<String> commands = new ArrayList<String>();
		commands.add("cmd.exe");
		commands.add("/c");
		commands.add("start");
		commands.add(uri);
		if (params != null) {
			for (String param : params) {
				commands.add(param);
			}
		}
		
		return runBat(commands);
	}
	
	public Process runBatInside(String name, String... params) {
		String uri = FileGetter.getFile(name + ".bat");
		
		List<String> commands = new ArrayList<String>();
		commands.add("cmd.exe");
		commands.add("/c");
		commands.add(uri);
		if (params != null) {
			for (String param : params) {
				commands.add(param);
			}
		}
		
		return runBat(commands);
	}
	
	private Process runBat(List<String> commands) {
		ProcessBuilder pb = new ProcessBuilder(commands);
		Process p = null;
		try {
			p = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static void main(String[] args) throws Exception {
		new BatRunner().runBat("run_hot_deploy", "qib");
		
//		String path = BatRunner.class.getResource("run_hot_deploy.bat").getFile();
//		System.out.println(path);
	}
}
