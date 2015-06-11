import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.xml.internal.ws.org.objectweb.asm.Label;


public class MoneyCardGUI {

	public static void main(String[] args) {
		
		MoneyCardGUI m = new MoneyCardGUI();
	}
	
	InputStream is;
	JLabel label = new JLabel();
	
	public MoneyCardGUI() {
		final JFrame frame = new JFrame();
		frame.setSize(300, 500);
		frame.setVisible(true);
		frame.setLayout(null);
		
		JButton queryButton = new JButton();
		queryButton.setSize(180, 40);
		queryButton.setText("查詢");
		queryButton.setLocation(60, 60);
		
		queryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					is = Runtime.getRuntime().exec("python MoneyCard.py aaaa").getInputStream();
					int ch = is.read();
					String str = "" + (char)ch;
					while(ch != 'z') {
						label.setText("Connecting...");
						ch = is.read();
						str += (char)ch;
					}
					
					showMoney(str);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		JButton addButton = new JButton();
		addButton.setSize(180, 40);
		addButton.setText("加值");
		addButton.setLocation(60, 120);
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					is = Runtime.getRuntime().exec("python MoneyCard.py bbbb").getInputStream();
					int ch = is.read();
					String str = "" + (char)ch;
					while(ch != 'z') {
						label.setText("Connecting...");
						ch = is.read();
						str += (char)ch;
					}
					
					showMoney(str);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		JButton subButton = new JButton();
		subButton.setSize(180, 40);
		subButton.setText("扣款");
		subButton.setLocation(60, 180);
		
		subButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					is = Runtime.getRuntime().exec("python MoneyCard.py cccc").getInputStream();
					int ch = is.read();
					String str = "" + (char)ch;
					while(ch != 'z') {
						label.setText("Connecting...");
						ch = is.read();
						str += (char)ch;
					}
					
					showMoney(str);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		JButton exitButton = new JButton();
		exitButton.setSize(180, 40);
		exitButton.setLocation(60, 440);
		exitButton.setText("離開");
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		
		label = new JLabel();
		label.setSize(300, 60);
		label.setLocation(60, 240);
		label.setText("000");
		
		
		frame.add(queryButton);
		frame.add(addButton);
		frame.add(subButton);
		frame.add(exitButton);
		frame.add(label);
	}
	
	public void showMoney(String str) {
		if (str.equals("fail!z")) {
			label.setText("Please Try Again");
		} else {
			String r = "";
			label.setText(str);
			for (int i = 0 ; i < str.length()-1 ; i+= 2) {
				int n = Integer.parseInt(str.substring(i, i+2), 16) - '0';
				r += n;
			}
			label.setText(r);
		}
	}
}
