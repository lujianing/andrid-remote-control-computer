package qianyan.rc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.Activity;
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
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Toast;

public class HandActivity extends Activity {
	private FrameLayout handcontrol;
	private FrameLayout handshoot;

	private static float mx = 0; // 发送的鼠标移动的差值
	private static float my = 0;
	private static float lx; // 记录上次鼠标的位置
	private static float ly;
	private static float fx; // 手指第一次接触屏幕时的坐标
	private static float fy;
	private String volumedownkey = "leftButton";
	private String volumeupkey = "leftButton";
	private String shootkey = "leftButton";
	private boolean iskeyboard = false;
	private DatagramSocket socket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hand);
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handcontrol = (FrameLayout) findViewById(R.id.handcontrol);
		handshoot = (FrameLayout) findViewById(R.id.handshoot);

		handcontrol.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if (ev.getAction() == MotionEvent.ACTION_DOWN) {
					onMouseDown(ev);
					// return true;
				}
				if (ev.getAction() == MotionEvent.ACTION_UP) {
					if (iskeyboard)
						onKeyboardUp();
					else
						onMouseUp(ev);
					onShootUp();
					// return true;
				}

				if (ev.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
					onShootDown();
					// return true;
				}
				if (ev.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
					onShootUp();
					// return true;
				}

				if (ev.getAction() == MotionEvent.ACTION_MOVE) {
					if (!iskeyboard)
						onMouseMove(ev);
					else
						onKeyboardMove(ev);
					// return true;

				}

				return true;

			}
		});

		handshoot.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if (ev.getAction() == MotionEvent.ACTION_DOWN) {

					onShootDown();
				}
				if (ev.getAction() == MotionEvent.ACTION_UP) {
					if (iskeyboard)
						onKeyboardUp();
					else
						onMouseUp(ev);
					onShootUp();
				}
				if (ev.getAction() == MotionEvent.ACTION_MOVE) {
					if (!iskeyboard)
						onfingerMove(ev);
					else
						onfingerKeyboardMove(ev);
				}
				if (ev.getAction() == MotionEvent.ACTION_POINTER_2_DOWN)
					onfingerDown(ev);
				if (ev.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
					onMouseUp(ev);

					if (iskeyboard)
						onKeyboardUp();
					else
						onMouseUp(ev);
					onShootUp();
				}
				return true;
			}
		});

	}

	private void onMouseDown(MotionEvent ev) {
		lx = ev.getX(); // 当手机第一放入时 把当前坐标付给lx
		ly = ev.getY();
		fx = ev.getX();
		fy = ev.getY();
	}

	private void onMouseMove(MotionEvent ev) {
		float x = ev.getX();
		mx = lx - x; // 当前鼠标位置 - 上次鼠标的位置
		lx = x; // 把当前鼠标的位置付给lx 以备下次使用
		float y = ev.getY();
		my = y - ly;
		ly = y;
		if (mx != 0 && my != 0)
			sendMessage("mouse:" + my + "," + mx);// 横屏下 坐标相反

	}

	private void onKeyboardMove(MotionEvent ev) {
		float x = ev.getX() - fx;
		float y = ev.getY() - fy;

		if (y < 0) {
			// 圆 上
			if (((x / y) < (42 / 90.0) && (x / y) > 0)
					|| ((x / y) < 0 && (x / y) > (-42 / 90.0)) || x == 0) {
				sendMessage("keyboard:key,Left,down");
				sendMessage("keyboard:key,Up,up");
				sendMessage("keyboard:key,Down,up");
				sendMessage("keyboard:key,Right,up");

			}

			if ((x / y) > (-90 / 42.0) && (x / y < (-42 / 90.0))) {
				sendMessage("keyboard:key,Up,up");
				sendMessage("keyboard:key,Down,up");
				sendMessage("keyboard:key,Left,up");
				sendMessage("keyboard:key,Right,up");
				sendMessage("keyboard:key,Up,down");
				sendMessage("keyboard:key,Left,down");
			}

		}

		if (y > 0) {

			// 圆下
			if (((x / y) < (42 / 90.0) && (x / y) > 0)
					|| ((x / y) < 0 && (x / y) > (-42 / 90.0)) || x == 0) {

				sendMessage("keyboard:key,Right,down");
				sendMessage("keyboard:key,Up,up");
				sendMessage("keyboard:key,Down,up");
				sendMessage("keyboard:key,Left,up");
			}

			if ((x / y) > (-90 / 42.0) && (x / y < (-42 / 90.0))) {
				sendMessage("keyboard:key,Up,up");
				sendMessage("keyboard:key,Down,up");
				sendMessage("keyboard:key,Left,up");
				sendMessage("keyboard:key,Right,up");
				sendMessage("keyboard:key,Down,down");
				sendMessage("keyboard:key,Right,down");
			}

		}

		if (x < 0) {

			// 圆左
			if ((x / y) > (90 / 42.0) || (x / y) < (-90 / 42.0) || y == 0) {
				sendMessage("keyboard:key,Down,down");
				sendMessage("keyboard:key,Up,up");

				sendMessage("keyboard:key,Left,up");
				sendMessage("keyboard:key,Right,up");

			}

			if ((x / y) > (42 / 90.0) && (x / y) < (90 / 42.0)) {
				sendMessage("keyboard:key,Up,up");
				sendMessage("keyboard:key,Down,up");
				sendMessage("keyboard:key,Left,up");
				sendMessage("keyboard:key,Right,up");
				sendMessage("keyboard:key,Down,down");
				sendMessage("keyboard:key,Left,down");

			}
		}

		if (x > 0) {

			// 圆右
			if ((x / y) > (90 / 42.0) || (x / y) < (-90 / 42.0) || y == 0) {

				sendMessage("keyboard:key,Up,down");
				sendMessage("keyboard:key,Down,up");
				sendMessage("keyboard:key,Left,up");
				sendMessage("keyboard:key,Right,up");
			}

			if ((x / y) > (42 / 90.0) && (x / y) < (90 / 42.0)) {
				sendMessage("keyboard:key,Up,up");
				sendMessage("keyboard:key,Down,up");
				sendMessage("keyboard:key,Left,up");
				sendMessage("keyboard:key,Right,up");
				sendMessage("keyboard:key,Up,down");
				sendMessage("keyboard:key,Right,down");

			}

		}

	}

	/**
	 * 当键盘模式手离开时
	 * */
	private void onKeyboardUp() {
		fx = 0;
		fy = 0;
		sendMessage("keyboard:key,Up,up");
		sendMessage("keyboard:key,Down,up");
		sendMessage("keyboard:key,Left,up");
		sendMessage("keyboard:key,Right,up");
	}

	private void onfingerDown(MotionEvent ev) {
		lx = ev.getX(1); // 当手机第一放入时 把当前坐标付给lx
		ly = ev.getY(1);
		fx = ev.getX(1);
		fy = ev.getY(1);
	}

	private void onfingerMove(MotionEvent ev) {
		if (ev.getPointerCount() > 1) {
			float x = ev.getX(1);
			mx = lx - x; // 当前鼠标位置 - 上次鼠标的位置
			lx = x; // 把当前鼠标的位置付给lx 以备下次使用
			float y = ev.getY(1);
			my = y - ly;
			ly = y;
			if (mx != 0 && my != 0)
				sendMessage("mouse:" + my + "," + mx);// 横屏下 坐标相反
		}
	}

	private void onMouseUp(MotionEvent ev) {
		/*
		 * if (fx == ev.getX() && fy == ev.getY()) {
		 * sendMessage("leftButton:down"); sendMessage("leftButton:release"); }
		 */
		fx = 0;
		fy = 0;
	}

	private void onShootDown() {
		if (shootkey.equals("leftButton"))
			sendMessage("leftButton:down");
		else if (shootkey.equals("rightButton"))
			sendMessage("rightButton:down");
		else
			sendMessage("keyboard:key," + shootkey + ",down");
	}

	private void onShootUp() {
		if (shootkey.equals("leftButton"))
			sendMessage("leftButton:release");
		else if (shootkey.equals("rightButton"))
			sendMessage("rightButton:release");
		else
			sendMessage("keyboard:key," + shootkey + ",up");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.handmenu, menu);
		return true;
	}

	/**
	 * 捕捉菜单事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.handhelp:
			help();
			return true;
		case R.id.handshootset:
			downsetting();
			return true;
		case R.id.handcontrolkeyboard:
			handcontrolkeyboard();
			return true;
		case R.id.handcontrolmouse:
			handcontrolkeymouse();
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

	private void help() {
		new AlertDialog.Builder(HandActivity.this)
				.setTitle("使用帮助")
				.setMessage("左边为控制 右边为射击  可进行单击选择")
				.setIcon(R.drawable.icon)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// finish();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}

				}).show();
	}

	private void downsetting() {
		final String[] items = { "鼠标左键", "鼠标右键", "Ctrl键", "Z键", "空格键", "Down键" };
		new AlertDialog.Builder(this).setTitle("选择按键") // 此处 this 代表当前Activity
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						Toast.makeText(getApplicationContext(), items[item],
								Toast.LENGTH_SHORT).show(); // 将选中的文本内容按照土司提示
															// 方式显示出来,
															// 此处的getApplicationContext()
															// 得到的也是当前的Activity对象，可用当前Activity对象的名字.this代替（Activity.this）
						switch (item) {
						case 0:
							shootkey = "leftButton";
							break;
						case 1:
							shootkey = "rightButton";
							break;
						case 2:
							shootkey = "Ctrl";
							break;
						case 3:
							shootkey = "Z";
							break;
						case 4:
							shootkey = "Space";
							break;
						case 5:
							shootkey = "Down";
							break;
						}
					}
				}).show();// 显示对话框
	}

	private void sendMessage(String str) {
		// System.out.println(str);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if (volumedownkey.equals("leftButton"))
				sendMessage("leftButton:down");
			else if (volumedownkey.equals("rightButton"))
				sendMessage("rightButton:down");
			else
				sendMessage("keyboard:key," + volumedownkey + ",down");
			return true;

		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			if (volumeupkey.equals("leftButton"))
				sendMessage("leftButton:down");
			else if (volumeupkey.equals("rightButton"))
				sendMessage("rightButton:down");
			else
				sendMessage("keyboard:key," + volumeupkey + ",down");
			return true;

		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

	// 屏蔽home键
	@Override
	public void onAttachedToWindow() {

		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);

		super.onAttachedToWindow();
	}

	private void doBack() {
		Intent intent = new Intent(HandActivity.this, ControlActivity.class);
		HandActivity.this.startActivity(intent);
		this.finish();
	}

	private void handcontrolkeyboard() {
		iskeyboard = true;
	}

	private void handcontrolkeymouse() {
		iskeyboard = false;
	}

	private void onfingerKeyboardMove(MotionEvent ev) {
		if (ev.getPointerCount() > 1) {
			float x = ev.getX(1) - fx;
			float y = ev.getY(1) - fy;

			if (y < 0) {
				// 圆 上
				if (((x / y) < (42 / 90.0) && (x / y) > 0)
						|| ((x / y) < 0 && (x / y) > (-42 / 90.0)) || x == 0) {
					sendMessage("keyboard:key,Left,down");
					sendMessage("keyboard:key,Up,up");
					sendMessage("keyboard:key,Down,up");
					sendMessage("keyboard:key,Right,up");

				}

				if ((x / y) > (-90 / 42.0) && (x / y < (-42 / 90.0))) {
					sendMessage("keyboard:key,Up,up");
					sendMessage("keyboard:key,Down,up");
					sendMessage("keyboard:key,Left,up");
					sendMessage("keyboard:key,Right,up");
					sendMessage("keyboard:key,Up,down");
					sendMessage("keyboard:key,Left,down");
				}

			}

			if (y > 0) {

				// 圆下
				if (((x / y) < (42 / 90.0) && (x / y) > 0)
						|| ((x / y) < 0 && (x / y) > (-42 / 90.0)) || x == 0) {

					sendMessage("keyboard:key,Right,down");
					sendMessage("keyboard:key,Up,up");
					sendMessage("keyboard:key,Down,up");
					sendMessage("keyboard:key,Left,up");
				}

				if ((x / y) > (-90 / 42.0) && (x / y < (-42 / 90.0))) {
					sendMessage("keyboard:key,Up,up");
					sendMessage("keyboard:key,Down,up");
					sendMessage("keyboard:key,Left,up");
					sendMessage("keyboard:key,Right,up");
					sendMessage("keyboard:key,Down,down");
					sendMessage("keyboard:key,Right,down");
				}

			}

			if (x < 0) {

				// 圆左
				if ((x / y) > (90 / 42.0) || (x / y) < (-90 / 42.0) || y == 0) {
					sendMessage("keyboard:key,Down,down");
					sendMessage("keyboard:key,Up,up");

					sendMessage("keyboard:key,Left,up");
					sendMessage("keyboard:key,Right,up");

				}

				if ((x / y) > (42 / 90.0) && (x / y) < (90 / 42.0)) {
					sendMessage("keyboard:key,Up,up");
					sendMessage("keyboard:key,Down,up");
					sendMessage("keyboard:key,Left,up");
					sendMessage("keyboard:key,Right,up");
					sendMessage("keyboard:key,Down,down");
					sendMessage("keyboard:key,Left,down");

				}
			}

			if (x > 0) {

				// 圆右
				if ((x / y) > (90 / 42.0) || (x / y) < (-90 / 42.0) || y == 0) {

					sendMessage("keyboard:key,Up,down");
					sendMessage("keyboard:key,Down,up");
					sendMessage("keyboard:key,Left,up");
					sendMessage("keyboard:key,Right,up");
				}

				if ((x / y) > (42 / 90.0) && (x / y) < (90 / 42.0)) {
					sendMessage("keyboard:key,Up,up");
					sendMessage("keyboard:key,Down,up");
					sendMessage("keyboard:key,Left,up");
					sendMessage("keyboard:key,Right,up");
					sendMessage("keyboard:key,Up,down");
					sendMessage("keyboard:key,Right,down");

				}

			}

		}
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
