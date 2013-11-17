package qianyan.rc;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class KeyBoardActivity extends Activity {
	private EditText inputET;
	private Button sendbutton;
	private Button clearbutton;
	private Button enterButton;
	private Button dosButton;
	private Button upButton;
	private Button downButton;
	private Button leftButton;
	private Button rightButton;
	private RadioGroup radiogroup;
	private RadioButton udlrButton;
	private RadioButton wsadButton;
	private boolean isUSLR = true;
	private String volumedownkey =  "leftButton";
	private String volumeupkey =  "leftButton";
	private DatagramSocket socket;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyboard);
		inputET = (EditText) findViewById(R.id.InputEditText);
		sendbutton = (Button) findViewById(R.id.SendButton);
		clearbutton = (Button) findViewById(R.id.ClearButton);
		enterButton = (Button) findViewById(R.id.EnterButton);
		dosButton = (Button) findViewById(R.id.dosButton);
		upButton = (Button) findViewById(R.id.UP);
		downButton = (Button) findViewById(R.id.DOWN);
		leftButton = (Button) findViewById(R.id.LEFT);
		rightButton = (Button) findViewById(R.id.RIGHT);
		radiogroup = (RadioGroup) findViewById(R.id.radioGroup);
		udlrButton = (RadioButton) findViewById(R.id.udlr);
		wsadButton = (RadioButton) findViewById(R.id.wsad);
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String s = inputET.getText().toString();
				if (s == null || s.equals("")) {
					Toast.makeText(KeyBoardActivity.this, "信息为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				sendMessage("keyboard:message," + s);
			}

		});

		clearbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendMessage("keyboard:key,BackSpace,click");
			}

		});

		enterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendMessage("keyboard:key,Enter,click");
			}

		});

		dosButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String s = inputET.getText().toString();
				if (s == null || s.equals("")) {
					Toast.makeText(KeyBoardActivity.this, "信息为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				sendMessage("keyboard:dosmessage," + s);
			}

		});

		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (udlrButton.getId() == checkedId) {
					isUSLR = true;
				} else if (wsadButton.getId() == checkedId) {
					isUSLR = false;
				}
			}
		});

		upButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (isUSLR)
						sendMessage("keyboard:key,Up,down");
					else
						sendMessage("keyboard:key,W,dwon");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isUSLR) {
						sendMessage("keyboard:key,Up,up");
						sendMessage("keyboard:key,Left,up");
						sendMessage("keyboard:key,Right,up");
					} else {
						sendMessage("keyboard:key,W,up");
						sendMessage("keyboard:key,A,up");
						sendMessage("keyboard:key,D,up");
					}
				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
					if (event.getX(1) < 0 && event.getY(1) > 50) {
						if (isUSLR)
							sendMessage("keyboard:key,Left,down");
						else
							sendMessage("keyboard:key,A,dwon");
					}

					if (event.getX(1) > 150 && event.getX(1) < 300
							&& event.getY(1) > 50) {
						if (isUSLR)
							sendMessage("keyboard:key,Right,down");
						else
							sendMessage("keyboard:key,D,dwon");
					}

				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
					if (event.getX(1) < 0 && event.getY(1) > 50) {
						if (isUSLR)
							sendMessage("keyboard:key,Left,up");
						else
							sendMessage("keyboard:key,A,up");
					}

					if (event.getX(1) > 150 && event.getX(1) < 300
							&& event.getY(1) > 50) {
						if (isUSLR)
							sendMessage("keyboard:key,Right,up");
						else
							sendMessage("keyboard:key,D,up");
					}
				}
				return false;
			}
		});

		leftButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (isUSLR)
						sendMessage("keyboard:key,Left,down");
					else
						sendMessage("keyboard:key,A,dwon");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isUSLR) {
						sendMessage("keyboard:key,Left,up");
						sendMessage("keyboard:key,Up,up");
						sendMessage("keyboard:key,Down,up");
					} else {
						sendMessage("keyboard:key,A,up");
						sendMessage("keyboard:key,W,up");
						sendMessage("keyboard:key,S,up");
					}
				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
					if (event.getX(1) > 150 && event.getX(1) < 300
							&& event.getY(1) < 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Up,down");
						else
							sendMessage("keyboard:key,W,down");
					}
					if (event.getX(1) > 150 && event.getX(1) < 300
							&& event.getY(1) > 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Down,down");
						else
							sendMessage("keyboard:key,S,down");
					}

				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
					if (event.getX(1) > 150 && event.getX(1) < 300
							&& event.getY(1) < 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Up,up");
						else
							sendMessage("keyboard:key,W,up");
					}
					if (event.getX(1) > 150 && event.getX(1) < 300
							&& event.getY(1) > 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Down,up");
						else
							sendMessage("keyboard:key,S,up");
					}

				}
				return false;
			}
		});

		downButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (isUSLR) {
						sendMessage("keyboard:key,Down,down");

					} else {
						sendMessage("keyboard:key,S,dwon");

					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isUSLR) {
						sendMessage("keyboard:key,Down,up");
						sendMessage("keyboard:key,Left,up");
						sendMessage("keyboard:key,Right,up");
					} else {
						sendMessage("keyboard:key,S,up");
						sendMessage("keyboard:key,A,up");
						sendMessage("keyboard:key,D,up");
					}
				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {

					if (event.getX(1) < 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Left,down");
						else
							sendMessage("keyboard:key,A,down");
					}
					if (event.getX(1) > 150) {
						if (isUSLR)
							sendMessage("keyboard:key,Right,down");
						else
							sendMessage("keyboard:key,D,down");
					}

				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {

					if (event.getX(1) < 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Left,up");
						else
							sendMessage("keyboard:key,A,up");
					}
					if (event.getX(1) > 150) {
						if (isUSLR)
							sendMessage("keyboard:key,Right,up");
						else
							sendMessage("keyboard:key,D,up");
					}
				}
				return false;
			}
		});

		rightButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (isUSLR)
						sendMessage("keyboard:key,Right,down");
					else
						sendMessage("keyboard:key,D,dwon");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isUSLR) {
						sendMessage("keyboard:key,Right,up");
						sendMessage("keyboard:key,Up,up");
						sendMessage("keyboard:key,Down,up");
					} else {
						sendMessage("keyboard:key,D,up");
						sendMessage("keyboard:key,W,up");
						sendMessage("keyboard:key,S,up");
					}
				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
					if (event.getX(1) < 0 && event.getY(1) < 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Up,down");
						else
							sendMessage("keyboard:key,W,down");
					}
					if (event.getX(1) < 0 && event.getY(1) > 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Down,down");
						else
							sendMessage("keyboard:key,S,down");
					}

				} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
					if (event.getX(1) < 0 && event.getY(1) < 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Up,up");
						else
							sendMessage("keyboard:key,W,up");
					}
					if (event.getX(1) < 0 && event.getY(1) > 0) {
						if (isUSLR)
							sendMessage("keyboard:key,Down,up");
						else
							sendMessage("keyboard:key,S,up");
					}
					sendMessage(event.getX(1) + ":" + event.getY(1));
				}
				return false;
			}
		});

	}

	/*
	 * public void onclick(View v){ switch(v.getId()){ case R.id.UP:
	 * 
	 * break;
	 * 
	 * case R.id.DOWN: if(isUSLR) sendMessage("keyboard:key,Down"); else
	 * sendMessage("keyboard:key,S"); break;
	 * 
	 * case R.id.LEFT: if(isUSLR) sendMessage("keyboard:key,Left"); else
	 * sendMessage("keyboard:key,A"); break; case R.id.RIGHT: if(isUSLR)
	 * sendMessage("keyboard:key,Right"); else sendMessage("keyboard:key,D");
	 * break; }
	 * 
	 * }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if(volumedownkey.equals("leftButton"))
				sendMessage("leftButton:down");
			else if(volumedownkey.equals("rightButton"))
				sendMessage("rightButton:down");
			else
				sendMessage("keyboard:key,"+volumedownkey+",down");
			return true;

		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			if(volumeupkey.equals("leftButton"))
				sendMessage("leftButton:down");
			else if(volumeupkey.equals("rightButton"))
				sendMessage("rightButton:down");
			else
				sendMessage("keyboard:key,"+volumeupkey+",down");
			return true;

		} else if( keyCode== KeyEvent.KEYCODE_HOME){
			return true;
		} else if( keyCode== KeyEvent.KEYCODE_BACK){
			return true;
		} 
		
		return super.onKeyDown(keyCode, event);

	}
	
	
	 @Override  
	 public void onAttachedToWindow() {  
	        
	     this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);  
	        
	     super.onAttachedToWindow();  
	 } 
	
	

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if(volumedownkey.equals("leftButton"))
				sendMessage("leftButton:release");
			else if(volumedownkey.equals("rightButton"))
				sendMessage("rightButton:release");
			else
				sendMessage("keyboard:key,"+volumedownkey+",up");
			return true;

		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			if(volumeupkey.equals("leftButton"))
				sendMessage("leftButton:release");
			else if(volumeupkey.equals("rightButton"))
				sendMessage("rightButton:release");
			else
				sendMessage("keyboard:key,"+volumeupkey+",up");
			return true;

		} 
		
		return super.onKeyUp(keyCode, event);
	}

	private void sendMessage(String str) {
		try {
			// 首先创建一个DatagramSocket对象
			
			// 创建一个InetAddree
			InetAddress serverAddress = InetAddress.getByName(Settings.ipnum);
			byte data[] = str.getBytes();
			// 创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
			DatagramPacket packet = new DatagramPacket(data, data.length,
					serverAddress, Settings.scoketnum);
			// 调用socket对象的send方法，发送数据
			socket.send(packet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.keyboardmenu, menu);
		return true;
	}
	
	/**
	 * 捕捉菜单事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.keyboardhelp:
			help();
			return true;
		case R.id.keyboardvalumndown:
			downsetting();
			return true;
		case R.id.keyboardvalumnup:
			upsetting();
			return true;	
		
		case R.id.reback:
			doBack();
			return true;
		case R.id.exit:
			doExit();
			return true;
		}
		return false;
	}
	
	private void help(){
		new AlertDialog.Builder(KeyBoardActivity.this).setTitle("使用帮助")
		.setMessage("本页面可进行信息的发送 其中DOS发送是在DOS窗口下 信息的发送   \n使用设置 可设置音量键的操作 以方便你的使用和操作").setIcon(R.drawable.icon)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// finish();
			}
		}).setNegativeButton("返回",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub

					}

				}).show();
	}
	
	private void downsetting(){
		final String[] items = {"鼠标左键","鼠标右键", "Ctrl键", "Z键","空格键","Down键"}; 
    	new AlertDialog.Builder(this) 
    	.setTitle("选择按键") //此处 this 代表当前Activity 
    	.setItems(items, new DialogInterface.OnClickListener() { 
    	public void onClick(DialogInterface dialog, int item) { 
    	Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show(); //将选中的文本内容按照土司提示 方式显示出来, 此处的getApplicationContext() 得到的也是当前的Activity对象，可用当前Activity对象的名字.this代替（Activity.this） 
    	switch (item) {
		case 0:
			volumedownkey="leftButton";
			break;
		case 1:
			volumedownkey="rightButton";
			break;	
		case 2:
			volumedownkey="Ctrl";
			break;
		case 3:
			volumedownkey="Z";
			break;
		case 4:
			volumedownkey="Space";
			break;
		case 5:
			volumedownkey="Down";
			break;
		}
    	} 
    	}).show();//显示对话框 
	}
	
	private void upsetting(){
		final String[] items = {"鼠标左键","鼠标右键", "Ctrl键", "Z键","空格键","Up键"}; 
    	new AlertDialog.Builder(this) 
    	.setTitle("选择按键") //此处 this 代表当前Activity 
    	.setItems(items, new DialogInterface.OnClickListener() { 
    	public void onClick(DialogInterface dialog, int item) { 
    	Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show(); //将选中的文本内容按照土司提示 方式显示出来, 此处的getApplicationContext() 得到的也是当前的Activity对象，可用当前Activity对象的名字.this代替（Activity.this） 
    	switch (item) {
		case 0:
			volumeupkey="leftButton";
			break;
		case 1:
			volumeupkey="rightButton";
			break;	
		case 2:
			volumeupkey="Ctrl";
			break;
		case 3:
			volumeupkey="Z";
			break;
		case 4:
			volumeupkey="Space";
			break;
		case 5:
			volumeupkey="Up";
			break;
		}
    	} 
    	}).show();//显示对话框 
	}
	

	
	private void doBack(){
		 Intent intent = new Intent(KeyBoardActivity.this,ControlActivity.class);
		 KeyBoardActivity.this.startActivity(intent);
		 this.finish();
	}
	
	protected void doExit() {
		new AlertDialog.Builder(this)
				.setMessage(getString(R.string.exit_message))
				.setPositiveButton(getString(R.string.confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(
								DialogInterface dialoginterface, int i) {
								finish();
							}
						})
				.setNeutralButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}

						}).show();

	}
	
}
