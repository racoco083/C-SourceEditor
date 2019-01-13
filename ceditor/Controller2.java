package ceditor;

import java.awt.event.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;

import ceditor.Model;
import ceditor.View;

public class Controller2{
	private JTextPane textPane = View.textPane;

	Controller2(){}
	Controller2(JTextPane textPane) {
		this.textPane = textPane;
	}
}
class MenuEvent extends JFrame implements ActionListener{
	private JTextPane textPane = View.textPane;
	private JFileChooser f = new JFileChooser();
	private FileNameExtensionFilter ff = new FileNameExtensionFilter("텍스트파일(.txt)", "txt");
	private StyledDocument doc = textPane.getStyledDocument();
	private String savepathname = null;
	private String loadpathname = null;

	public void fileLoad(String path) throws BadLocationException{
		InputStreamReader isr = null;
		FileInputStream fi = null;
		BufferedReader bfr = null;

		try{
			fi = new FileInputStream(path);
			isr = new InputStreamReader(fi);
			bfr = new BufferedReader(isr);
			textPane.setText("");
			String str = null;
			String data = new String();
			while((str = bfr.readLine()) != null)
				data = data.concat(str+"\n");
			View.textPane.setText(data);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				fi.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public void fileSave(String path){
		FileOutputStream fo = null;
		try{
			fo = new FileOutputStream(path);
			String str = textPane.getText();
			StringTokenizer fs = new StringTokenizer(str, "\n");
			while(fs.hasMoreTokens())
			{
				fo.write(fs.nextToken().getBytes());
				fo.write(("\r\n").getBytes());
			}      
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				fo.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand() == "Exit                 Ctrl+Q"){
			System.exit(0);
		}
		else if(e.getActionCommand() == "New                Ctrl+N" && !textPane.getText().equals("")){
			int option = JOptionPane.showConfirmDialog(null, "현재 내용을 저장 하시겠습니까?", "New", JOptionPane.YES_NO_CANCEL_OPTION);
			if(option == JOptionPane.OK_OPTION){   //actionPerformed의 Savefile 실행
				ActionEvent ae = new ActionEvent(new Object(), 0, "Savefile         Ctrl+S");
				this.actionPerformed(ae);
			}
			else if(option == JOptionPane.NO_OPTION)
				textPane.setText("");
		}
		else if(e.getActionCommand() == "About C Source Editor         F1"){
			new Help();
		}
		else if(e.getActionCommand() == "Loadfile         Ctrl+O"){
			if(!textPane.getText().equals("")){
				int option = JOptionPane.showConfirmDialog(null, "현재 내용을 저장 하시겠습니까?", "fileLoad", JOptionPane.YES_NO_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION){   //actionPerformed의 Savefile 실행
					ActionEvent ae = new ActionEvent(new Object(), 0, "Savefile         Ctrl+S");
					this.actionPerformed(ae);
				}
				else if(option == JOptionPane.CANCEL_OPTION){
					return;
				}
			}
			FileDialog fload = new FileDialog(this, "fileLoad", FileDialog.LOAD);
			fload.setDirectory(".");
			fload.setVisible(true);
			loadpathname = fload.getFile();
			if(loadpathname != null)
				try {
					fileLoad(loadpathname);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		else if(e.getActionCommand() == "Savefile         Ctrl+S"){
			FileDialog fsave = new FileDialog(this,"fileSave",FileDialog.SAVE);
			fsave.setDirectory(".");
			fsave.setVisible(true); 

			savepathname = fsave.getFile();
			if(savepathname != null)
			{
				fileSave(savepathname);
			}
			new AutoSave(savepathname);
		}else{
			return;
		}
	}
	class AutoSave implements Runnable { 
		String autopath;
		AutoSave(String path) {
			autopath = path;
			new Thread(this).start();
		}

		public void run() {
			while(true) {
				try {
					if(savepathname == null)
					{continue;}
					Thread.sleep(10000);
					fileSave(autopath);
				} catch (InterruptedException e) { return; }
			}
		}
	}

	class MykeyListener extends KeyAdapter{
		private MenuEvent fors = new MenuEvent();
		public void keyPressed(KeyEvent e)
		{
			if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F)
			{
				new Search();
			}
			else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S)
			{
				if(savepathname == null)
				{
					ActionEvent ae = new ActionEvent(new Object(), 0, "Savefile         Ctrl+S");
					fors.actionPerformed(ae);
				}
				else
					fors.fileSave(savepathname);
			}
			else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N)
			{
				ActionEvent ae = new ActionEvent(new Object(), 0, "New                Ctrl+N");
				fors.actionPerformed(ae);
			}
			else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O)
			{
				ActionEvent ae = new ActionEvent(new Object(), 0, "Loadfile         Ctrl+O");
				fors.actionPerformed(ae);
			}
			else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Q)
			{
				System.exit(0);
			}
			else if(e.getKeyCode() == KeyEvent.VK_F1)
			{
				ActionEvent ae = new ActionEvent(new Object(), 0, "About C Source Editor         F1");
				fors.actionPerformed(ae);
			}
		}
	}
}
class Help extends JFrame
{
	private JTextArea ta;
	//컨텐트팬 여기다가 쓰면 오류남
	Help(){
		setTitle("About C Source Editor");
		setBounds(300, 300, 400, 400);
		ta = new JTextArea();
		getContentPane().add(ta);
		ta.setText(
				"Version : C92.5(1.0)"
				+"\n"+"<개발자 : 여태훈, 김수현>"
				+"\n"+"Manufactured Date : 2015-12-06"+"\n"
				+"\n"+"<사용법>"
				+"\n"+"이 프로그램은 C코드에 대한 편집 기능을 제공하는 편집기 입니다."
				+"\n"+"전체화면은 메뉴, 입력창, 커서위치에 따른  줄,열을 "
				+"\n"+"나타내는 표시창으로 구성되어 있습니다."
				+"\n"+"새창을 띄우실 때에는 메뉴창에 New를 클릭하시거나"
				+"\n"+"ctrl + n키를 사용하시면 됩니다."
				+"\n"+"단어를 하나씩 찾아 바꾸거나 모두 찾아 바꿀시엔"
				+"\n"+"ctrl + f키를 사용하시면 됩니다."
				+"\n"+"파일을 저장하거나 불러올 경우 열기 또는 저장 메뉴를 사용하거나"
				+"\n"+"ctrl + o 또는 ctrl + s키를 사용하시면 됩니다."
				+"\n"+"이 프로그램 사용시 도움말이 필요하시면 F1을 누르시거나"
				+"\n"+"메뉴창에서 About C Source Editor를 클릭 하시면 됩니다."
				+"\n"+"키를 너무 빠르게 입력하면 색깔이 밀리는 버그가 있습니다."
				+"\n"+"사용하실때에 이런점은 유의하여 주시면 감사하겠습니다."
				+"\n"+"프로그램을 사용해 주셔서 감사합니다.^^"
				);
				ta.setEditable(false);
				setVisible(true);
	}
}
class Search extends JFrame {
	private JTextField tf1;
	private JTextField tf2;
	private JButton bt1;
	private JButton bt2;
	private JButton bt3;
	private JButton bt4;
	private JTextPane textPane = View.textPane;
	private Highlighter h = textPane.getHighlighter();
	private int pos = 0;
	private int last_pos = 0;
	private String find;
	private String change = "";
	private String text;
	private int cnt=0;
	private int word_cnt=0;

	Search()
	{
		setLayout(null);
		tf1 = new JTextField();
		tf2 = new JTextField();
		bt1 = new JButton();
		bt2 = new JButton();
		bt3 = new JButton();
		bt4 = new JButton();

		setTitle("찾기 & 바꾸기");
		setSize(387, 135);
		setLocation(600, 400);
		setResizable(false);
		Container container = getContentPane();
		container.setLayout(null);
		tf1.setBounds(10,15, 150, 30);
		tf2.setBounds(10,60, 150, 30);
		bt1.setBounds(260, 15, 110, 30);
		bt2.setBounds(260, 60, 110, 30);
		bt3.setBounds(170, 15, 80, 30);
		bt4.setBounds(170, 60, 80, 30);
		bt1.setText("모두 찾기");
		bt2.setText("모두 바꾸기");
		bt3.setText("찾기");
		bt4.setText("바꾸기");

		container.add(tf1);
		container.add(tf2);
		container.add(bt1);
		container.add(bt2);
		container.add(bt3);
		container.add(bt4);
		bt1.addActionListener(new MyActionListener());
		bt2.addActionListener(new MyActionListener());
		bt3.addActionListener(new MyActionListener());
		bt4.addActionListener(new MyActionListener());
		setVisible(true);
	}
	class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int fi=0;
			int max;
			JButton b = (JButton)e.getSource();
			if(b.getText() == "찾기")
			{

				h.removeAllHighlights();
				text = textPane.getText().replace("\r\n", "\n");
				find = tf1.getText();
				if(text.contains(find))
				{
					word_cnt=1;
					pos = text.indexOf(find, pos);
					cnt=0;
					try {
						h.addHighlight(pos, pos+find.length(), DefaultHighlighter.DefaultPainter);
					} catch (BadLocationException e2) {
					}
					pos = pos+find.length();
					if(pos > text.lastIndexOf(find))
					{
						last_pos = pos;
						cnt=1;
						pos = 0;
					}
				}
				else
				{
					word_cnt=0;
					JOptionPane.showMessageDialog(null, "더 이상  찾는 단어가 없습니다.", "message", JOptionPane.WARNING_MESSAGE);
				}
			}
			else if(b.getText() == "바꾸기")
			{   
			if(word_cnt!=0)
			{
				change = tf2.getText();
				if(cnt == 1)
				{
					textPane.select(last_pos-find.length(), last_pos); 
					textPane.replaceSelection(change);
					try {
						h.addHighlight(last_pos-find.length(), last_pos-find.length()+change.length(), DefaultHighlighter.DefaultPainter);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					cnt=0;
				}
				else
				{
					textPane.select(pos-find.length(), pos); 
					textPane.replaceSelection(change);
					try {
						h.addHighlight(pos-find.length(), pos-find.length()+change.length(), DefaultHighlighter.DefaultPainter);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
			word_cnt=0;
			}
			else if(b.getText() == "모두 찾기")
			{
				h.removeAllHighlights();
				text = textPane.getText().replace("\r\n", "\n");
				find = tf1.getText();
				StringTokenizer st = new StringTokenizer(text, "\u0020\t\r\n");
				if(text.contains(find))
				{
					max = text.lastIndexOf(find);
					for(int k=0; ; k++)
					{
						fi = text.indexOf(find, fi);
						try {
							h.addHighlight(fi, fi+find.length(), DefaultHighlighter.DefaultPainter);
						} catch (BadLocationException ble) {
						}
						fi = fi+find.length();
						if(fi >= max)
							break;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "더 이상  찾는 단어가 없습니다.", "message", JOptionPane.WARNING_MESSAGE);
				}
			}
			else if(b.getText() == "모두 바꾸기")
			{
				int cpos=0;
				String cur, after;
				change = tf2.getText();
				cur = textPane.getText();
				after = cur.replaceAll(find, change);
				textPane.selectAll(); 
				textPane.replaceSelection(after); 
				int max2;
				max2 = after.lastIndexOf(change);
				for(int k=0; k<after.length(); k++)
				{
					cpos = after.indexOf(change, cpos);
					try {
						h.addHighlight(cpos, cpos+change.length(), DefaultHighlighter.DefaultPainter);
					} catch (BadLocationException ble) {
					}
					cpos = cpos+change.length();
					if(cpos >= max2)
						break;
				}
			}
		}
	}
}