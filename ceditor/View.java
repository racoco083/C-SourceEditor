package ceditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;

import ceditor.Controller2;

import javax.swing.text.BadLocationException;

import org.w3c.dom.traversal.DocumentTraversal;

public class View extends JFrame{
   private MenuEvent fors = new MenuEvent(); 
   private JFrame fce = new JFrame();
   static JTextPane textPane = new JTextPane();
   static String datastring = textPane.getText();
   private Highlighter h = textPane.getHighlighter();
   private Controller1 ct1 = new Controller1(textPane);
   private JLabel la = new JLabel("");
   private StatusThread st = new StatusThread();
   public View(){
      setLayout(null);
      fce.setTitle("C Source Editor");
      fce.setSize(900, 620);
      fce.setLocation(100, 50);
      fce.setDefaultCloseOperation(fce.EXIT_ON_CLOSE);
      Container c = fce.getContentPane();
      c.setLayout(null);

      JScrollPane scrollPane = new JScrollPane(textPane);
      scrollPane.setSize(860, 515);   
      scrollPane.setLocation(10, 10);
      c.add(scrollPane);
      la.setSize(860,20);
      la.setLocation(10,525);
      c.add(la);

      JMenuBar menuBar = new JMenuBar();
      fce.setJMenuBar(menuBar);

      JMenu menu1 = new JMenu("File");
      menuBar.add(menu1);

      JMenu menu3 = new JMenu("Help");
      menuBar.add(menu3);

      JMenuItem menu1Item = new JMenuItem("New                Ctrl+N");
      menu1.add(menu1Item);

      JMenuItem menu2Item = new JMenuItem("Loadfile         Ctrl+O");
      menu1.add(menu2Item);

      JMenuItem menu3Item = new JMenuItem("Savefile         Ctrl+S");
      menu1.add(menu3Item);

      JMenuItem menu4Item = new JMenuItem("Exit                 Ctrl+Q");
      menu1.add(menu4Item);

      JMenuItem menu31Item = new JMenuItem("About C Source Editor         F1");
      menu3.add(menu31Item);

      menu1Item.addActionListener(fors);
      menu2Item.addActionListener(fors);
      menu3Item.addActionListener(fors);
      menu4Item.addActionListener(fors);
      menu31Item.addActionListener(fors);
      fce.setVisible(true);
      ct1.start();
      st.start();
      textPane.addKeyListener(fors.new MykeyListener());
      textPane.addKeyListener(new KRH());
      textPane.addMouseListener(new MRH());
   }
   class MRH extends MouseAdapter
   {
      public void mousePressed(MouseEvent e) {
         h.removeAllHighlights();
      }
   }
   class KRH extends KeyAdapter
   {
      public void keyPressed(KeyEvent e) {
         if(e.isControlDown())
         {
            if(e.getKeyCode() == KeyEvent.VK_C)
            {}
         }
         else if(e.isShiftDown())
         {
            if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            {}
            else if(e.getKeyCode() == KeyEvent.KEY_LOCATION_RIGHT)
            {}
            else if(e.getKeyCode() == KeyEvent.VK_UP)
            {}
            else if(e.getKeyCode() == KeyEvent.VK_DOWN)
            {}
            else if(e.getKeyCode() == KeyEvent.VK_LEFT)
            {}
         }
         else
            h.removeAllHighlights();
      }
   }

   public String getdatastring()
   {
      return datastring;
   }

   class StatusThread extends Thread{
      public void run(){
         while(true){
            char[] c = textPane.getText().replace("\r\n","\n").toCharArray();
            int row=1;
            int col=0;
            for(int i=0; i<textPane.getCaretPosition(); i++){
               if(c[i] == '\n'){
                  row++;
                  col = i;
               }
            }
            la.setText("ÁÙ :"+row+" ¿­ :"+(textPane.getCaretPosition()-col));
            try{Thread.sleep(150);}
            catch(InterruptedException e){return ;}
         }
      }
   }
}