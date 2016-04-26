package com.sky.listener;

import com.sky.fx.Controller;
import javafx.scene.control.Label;

public class HotDeployMessageListener extends MessageListener {
	
	public HotDeployMessageListener(Label label, Controller ctrl) {
		super(label, ctrl);
		// TODO Auto-generated constructor stub
	}

	@Override
	void updateMessageCallback(String msg) {
		super.updateMessageCallback(msg);
		
	}

	@Override
	void onSucceededCallback() {
		super.onSucceededCallback();
		ctrl.deployBtn.setDisable(false);
	}


}
