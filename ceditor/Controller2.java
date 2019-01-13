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
	private FileNameExtensionFilter ff = new FileNameExtensionFilter("�ؽ�Ʈ����(.txt)", "txt");
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
			int option = JOptionPane.showConfirmDialog(null, "���� ������ ���� �Ͻðڽ��ϱ�?", "New", JOptionPane.YES_NO_CANCEL_OPTION);
			if(option == JOptionPane.OK_OPTION){   //actionPerformed�� Savefile ����
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
				int option = JOptionPane.showConfirmDialog(null, "���� ������ ���� �Ͻðڽ��ϱ�?", "fileLoad", JOptionPane.YES_NO_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION){   //actionPerformed�� Savefile ����
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
	//����Ʈ�� ����ٰ� ���� ������
	Help(){
		setTitle("About C Source Editor");
		setBounds(300, 300, 400, 400);
		ta = new JTextArea();
		getContentPane().add(ta);
		ta.setText(
				"Version : C92.5(1.0)"
				+"\n"+"<������ : ������, �����>"
				+"\n"+"Manufactured Date : 2015-12-06"+"\n"
				+"\n"+"<����>"
				+"\n"+"�� ���α׷��� C�ڵ忡 ���� ���� ����� �����ϴ� ������ �Դϴ�."
				+"\n"+"��üȭ���� �޴�, �Է�â, Ŀ����ġ�� ����  ��,���� "
				+"\n"+"��Ÿ���� ǥ��â���� �����Ǿ� �ֽ��ϴ�."
				+"\n"+"��â�� ���� ������ �޴�â�� New�� Ŭ���Ͻðų�"
				+"\n"+"ctrl + nŰ�� ����Ͻø� �˴ϴ�."
				+"\n"+"�ܾ �ϳ��� ã�� �ٲٰų� ��� ã�� �ٲܽÿ�"
				+"\n"+"ctrl + fŰ�� ����Ͻø� �˴ϴ�."
				+"\n"+"������ �����ϰų� �ҷ��� ��� ���� �Ǵ� ���� �޴��� ����ϰų�"
				+"\n"+"ctrl + o �Ǵ� ctrl + sŰ�� ����Ͻø� �˴ϴ�."
				+"\n"+"�� ���α׷� ���� ������ �ʿ��Ͻø� F1�� �����ðų�"
				+"\n"+"�޴�â���� About C Source Editor�� Ŭ�� �Ͻø� �˴ϴ�."
				+"\n"+"Ű�� �ʹ� ������ �Է��ϸ� ������ �и��� ���װ� �ֽ��ϴ�."
				+"\n"+"����ϽǶ��� �̷����� �����Ͽ� �ֽø� �����ϰڽ��ϴ�."
				+"\n"+"���α׷��� ����� �ּż� �����մϴ�.^^"
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

		setTitle("ã�� & �ٲٱ�");
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
		bt1.setText("��� ã��");
		bt2.setText("��� �ٲٱ�");
		bt3.setText("ã��");
		bt4.setText("�ٲٱ�");

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
			if(b.getText() == "ã��")
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
					JOptionPane.showMessageDialog(null, "�� �̻�  ã�� �ܾ �����ϴ�.", "message", JOptionPane.WARNING_MESSAGE);
				}
			}
			else if(b.getText() == "�ٲٱ�")
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
			else if(b.getText() == "��� ã��")
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
					JOptionPane.showMessageDialog(null, "�� �̻�  ã�� �ܾ �����ϴ�.", "message", JOptionPane.WARNING_MESSAGE);
				}
			}
			else if(b.getText() == "��� �ٲٱ�")
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