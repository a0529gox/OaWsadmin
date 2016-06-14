package com.sky.fx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.sky.bat.BatRunner;
import com.sky.listener.HotDeployMessageListener;
import com.sky.listener.MessageListener;
import com.sky.listener.TimekeeperListener;
import com.sky.util.FileGetter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class Controller implements Initializable {
	
	protected final String settingsPath;

	protected MessageListener hotDeployMsgListener;
	protected MessageListener autostartMsgListener;

	//Tab HotDeploy START
	@FXML
	Tab hotDeployTab;
	@FXML
	CheckBox qibCb;
	@FXML
	CheckBox qicCb;
	@FXML
	CheckBox qieCb;
	@FXML
	CheckBox qifCb;
	@FXML
	CheckBox qihCb;
	@FXML
	CheckBox qiiCb;
	@FXML
	CheckBox qijCb;
	@FXML
	CheckBox qikCb;
	@FXML
	CheckBox qilCb;
	@FXML
	CheckBox qinCb;
	@FXML
	CheckBox qipCb;
	@FXML
	CheckBox qiqCb;
	@FXML
	CheckBox qiuCb;
	@FXML
	CheckBox qivCb;
	@FXML
	CheckBox qiwCb;
	@FXML
	CheckBox qiyCb;
	
	List<CheckBox> cbs = null;

	@FXML
	Label messageLb;
	@FXML
	Label timekeeperLb;
	@FXML
	public Button deployBtn;
	public TimekeeperListener timekeeperListener;	
	//Tab HotDeploy END


	//Tab Autostart START
	@FXML
	Tab autostartTab;
	@FXML
	CheckBox asQibCb;
	@FXML
	CheckBox asQicCb;
	@FXML
	CheckBox asQieCb;
	@FXML
	CheckBox asQifCb;
	@FXML
	CheckBox asQihCb;
	@FXML
	CheckBox asQiiCb;
	@FXML
	CheckBox asQijCb;
	@FXML
	CheckBox asQikCb;
	@FXML
	CheckBox asQilCb;
	@FXML
	CheckBox asQinCb;
	@FXML
	CheckBox asQipCb;
	@FXML
	CheckBox asQiqCb;
	@FXML
	CheckBox asQiuCb;
	@FXML
	CheckBox asQivCb;
	@FXML
	CheckBox asQiwCb;
	@FXML
	CheckBox asQiyCb;
	
	List<CheckBox> asCbs = null;

	@FXML
	Label asMessageLb;
	@FXML
	Button initBtn;
	@FXML
	Button exeBtn;
	//Tab Autostart END
	

	public Controller() {
		settingsPath = FileGetter.getFile("setting.bat");
	}

	@Override
	public void initialize(URL location, ResourceBundle res) {
		hotDeployMsgListener = new HotDeployMessageListener(messageLb, this);
		autostartMsgListener = new MessageListener(asMessageLb, this);
		timekeeperListener = new TimekeeperListener(timekeeperLb);
		
		//Tab HotDeploy
		initCbs();
		
		if (deployBtn != null) {
			deployBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					deployBtnClickEvent(event);
				}
			});
		}
		
		
		
		//Tab Autostart
		initAsCbs();
		
		if (initBtn != null) {
			initBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					initBtnClickEvent(event);
				}
			});
		}
		if (exeBtn != null) {
			exeBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					exeBtnClickEvent(event);
				}
			});
		}
	}

	private void deployBtnClickEvent(ActionEvent event) {
		try {
			StringBuilder toDeploySb = new StringBuilder();
			boolean isFirst = true;
			for (CheckBox cb : cbs) {
				if (cb.isSelected()) {
					if (isFirst) {
						isFirst = false;
					} else {
						toDeploySb.append(":");
					}
					toDeploySb.append(cb.getText().substring(2));
				}
			}
			
			String toDeploy = toDeploySb.toString();
			if (toDeploy.length() > 0) {
				deployBtn.setDisable(true);
				timekeeperListener.start();

				String pyPath = FileGetter.getFile("hot_deploy.py");
				
				hotDeployMsgListener.show("執行中，在黑窗結束前別關此程式");
				
//				new BatRunner().runBat("run_hot_deploy", toDeploy, pyPath, settingsPath);
				Process p = new BatRunner().runBatInside("run_hot_deploy", toDeploy, pyPath, settingsPath);
				hotDeployMsgListener.listen(p.getInputStream(), "utf8");
				
			}
		} catch (Exception e) {
			hotDeployMsgListener.handleException(e);
		}
	}

	private void initBtnClickEvent(ActionEvent event) {
		List<String> autostartList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			initBtn.setDisable(true);
			exeBtn.setDisable(false);
			
			String pyPath = FileGetter.getFile("read_autostart_from_nodes.py");
			
			Process p = new BatRunner().runBatInside("run_read_autostart_from_nodes",
					pyPath, settingsPath);
			
			br = new BufferedReader(new InputStreamReader(p.getInputStream(), "utf8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("qi") == 0) {
					autostartList.add(line);
				}
			}
			
			for (String str : autostartList) {
				try {
					String[] strArray = str.split("=");
					String oa = strArray[0];
					String enableStr = strArray[1];
					boolean enable = "true".equals(enableStr.toLowerCase()) ? true : false;
					
					Field cbField = this.getClass().getDeclaredField("asQ" + oa.substring(1) + "Cb");
					CheckBox cb = (CheckBox) cbField.get(this);
					cb.setSelected(enable);
				} catch (Exception e) {
					autostartMsgListener.handleException(e);
				}
			}
		} catch (Exception e) {
			autostartMsgListener.handleException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					autostartMsgListener.handleException(e);
				}
			}
		}
	}

	private void exeBtnClickEvent(ActionEvent event) {
		BufferedReader br = null;
		try {
			initBtn.setDisable(false);
			exeBtn.setDisable(true);
			
			StringBuilder toDeploySb = new StringBuilder();
			String toDeploy = null;
			boolean isFirst = true;
			for (CheckBox cb : asCbs) {
				if (isFirst) {
					isFirst = false;
				} else {
					toDeploySb.append("*");
				}
				toDeploySb.append(cb.getText().toLowerCase())
					.append(":")
					.append(cb.isSelected());
			}
			toDeploy = toDeploySb.toString();

			String pyPath = FileGetter.getFile("batch_update_nodes.py");
			
			Process p = new BatRunner().runBatInside("run_batch_update_nodes",
					pyPath, settingsPath, toDeploy);
			
			br = new BufferedReader(new InputStreamReader(p.getInputStream(), "utf8"));
			while (br.readLine() != null) {
				//keep畫面
			}
		} catch (Exception e) {
			autostartMsgListener.handleException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					autostartMsgListener.handleException(e);
				}
			}
		}
		
	}
	
	protected void initCbs() {
		cbs = new ArrayList<CheckBox>();

		if (qibCb != null) {
			cbs.add(qibCb);
		}
		if (qicCb != null) {
			cbs.add(qicCb);
		}
		if (qieCb != null) {
			cbs.add(qieCb);
		}
		if (qifCb != null) {
			cbs.add(qifCb);
		}
		if (qihCb != null) {
			cbs.add(qihCb);
		}
		if (qiiCb != null) {
			cbs.add(qiiCb);
		}
		if (qijCb != null) {
			cbs.add(qijCb);
		}
		if (qikCb != null) {
			cbs.add(qikCb);
		}
		if (qilCb != null) {
			cbs.add(qilCb);
		}
		if (qinCb != null) {
			cbs.add(qinCb);
		}
		if (qipCb != null) {
			cbs.add(qipCb);
		}
		if (qiqCb != null) {
			cbs.add(qiqCb);
		}
		if (qiuCb != null) {
			cbs.add(qiuCb);
		}
		if (qivCb != null) {
			cbs.add(qivCb);
		}
		if (qiwCb != null) {
			cbs.add(qiwCb);
		}
		if (qiyCb != null) {
			cbs.add(qiyCb);
		}
	}
	
	protected void initAsCbs() {
		asCbs = new ArrayList<CheckBox>();

		if (asQibCb != null) {
			asCbs.add(asQibCb);
		}
		if (asQicCb != null) {
			asCbs.add(asQicCb);
		}
		if (asQieCb != null) {
			asCbs.add(asQieCb);
		}
		if (asQifCb != null) {
			asCbs.add(asQifCb);
		}
		if (asQihCb != null) {
			asCbs.add(asQihCb);
		}
		if (asQiiCb != null) {
			asCbs.add(asQiiCb);
		}
		if (asQijCb != null) {
			asCbs.add(asQijCb);
		}
		if (asQikCb != null) {
			asCbs.add(asQikCb);
		}
		if (asQilCb != null) {
			asCbs.add(asQilCb);
		}
		if (asQinCb != null) {
			asCbs.add(asQinCb);
		}
		if (asQipCb != null) {
			asCbs.add(asQipCb);
		}
		if (asQiqCb != null) {
			asCbs.add(asQiqCb);
		}
		if (asQiuCb != null) {
			asCbs.add(asQiuCb);
		}
		if (asQivCb != null) {
			asCbs.add(asQivCb);
		}
		if (asQiwCb != null) {
			asCbs.add(asQiwCb);
		}
		if (asQiyCb != null) {
			asCbs.add(asQiyCb);
		}
	}

}
