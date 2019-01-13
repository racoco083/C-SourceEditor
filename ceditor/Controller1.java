package ceditor;

import java.util.*;

import javax.swing.*;

import java.awt.*;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class Controller1 extends Thread{
   private JTextPane text;
   Model mcd = new Model();
   private ArrayList<String> md = new ArrayList<String>();
   private String[] getKdata;
   private String[] getHdata;
   static int ccnt;
   private int pos;
   private int last_length = 0;
   private int cnt=0;
   private boolean isWait = false;
   Stack stack = new Stack();
   private String str;
   private StyledDocument doc;
   private StyleContext context = new StyleContext();
   private SimpleAttributeSet attribute = new SimpleAttributeSet();
   private AttributeSet as_green = context.addAttribute(attribute.EMPTY,
         StyleConstants.Foreground, new Color(100,220,0));
   private AttributeSet as_blue = context.addAttribute(attribute.EMPTY,
         StyleConstants.Foreground, Color.BLUE);
   private AttributeSet as_red = context.addAttribute(attribute.EMPTY,
         StyleConstants.Foreground, Color.RED);
   private AttributeSet as_orange = context.addAttribute(attribute.EMPTY,
         StyleConstants.Foreground, Color.ORANGE);
   private AttributeSet as_pink = context.addAttribute(attribute.EMPTY,
         StyleConstants.Foreground, Color.PINK);
   private AttributeSet as_black = context.addAttribute(attribute.EMPTY,
         StyleConstants.Foreground, Color.BLACK);
   private AttributeSet as_purple = context.addAttribute(attribute.EMPTY,
         StyleConstants.Foreground, Color.MAGENTA);
   private AttributeSet as_underline = context.addAttribute(attribute.EMPTY,
         StyleConstants.Underline, Boolean.TRUE);
   private AttributeSet as_error = context.addAttributes(as_red, as_underline);

   Controller1(){}
   Controller1(JTextPane text) {
      this.text = text;
   }
   class Element{
      public Element(){}
      public Element(char c,int l){this.c = c; this.l = l;}
      private char c;         //괄호 및 따옴표
      private int l;         //괄호 및 따옴표 위치
      public char getChar(){return c;}
      public int getLocation(){return l;}
   }
   class Stack{
      public Stack(){stack_top = -1; item = new Element[100];}
      public void push(Element item){this.item[++stack_top] = item;}
      public Element Peek(){return item[stack_top];}
      public Element pop(){return item[stack_top--];}
      public boolean isEmpty(){if(stack_top == -1) return true; else return false;}
      private int stack_top;
      private Element[] item;
   }
   public void pauseThread(){
      isWait = true;
      synchronized(this){notify();}
   }
   public void resumeThread(){
      isWait = false;
      synchronized(this){notify();}
   }
   public void run() { 
      while (true) {
         str = text.getText().replace("\r\n", "\n");
         doc = text.getStyledDocument();
         md.clear();
         String sst;
         ArrayList<String> word = new ArrayList<String>();
         ArrayList<String> line = new ArrayList<String>();
         StringTokenizer lst = new StringTokenizer(str, "\n");
         StringTokenizer st = new StringTokenizer(str, "\u0020\t\r\n");
         getKdata=mcd.getk();
         getHdata=mcd.geth();
         //doc.setCharacterAttributes(0, text.getText().length(), as_black, true);
         char[] char_text = str.toCharArray();//View의 textPane내용을 받아온 str을 char배열에 저장
         char[] index = new char[char_text.length];
         int chpos = 0;
         ArrayList<String> cons = new ArrayList<String>();
         int fi=0;
         int hpos = 0;
         int ipos = 0;
         while(lst.hasMoreElements())      //line으로 잘라서 스트링 저장
         {
            line.add(lst.nextToken());
         }
         while(st.hasMoreElements())         //단어로 잘라서 스트링 저장
         {
            word.add(st.nextToken());
         }
         for(int j=0; j<word.size(); j++)
         {
            for(int i=0; i<getKdata.length; i++)
            {
               if(word.get(j).equals(getKdata[i]))
               {
                  fi = str.indexOf(getKdata[i], fi);
                  for(int k=fi; k<fi+getKdata[i].length();k++)
                     index[k] = 'b';
                  //doc.setCharacterAttributes(fi, getKdata[i].length(), as_blue, true);
                  fi = fi + getKdata[i].length();
               }
               else if(word.get(j).equals("("+getKdata[i])||word.get(j).equals("{"+getKdata[i])||word.get(j).equals("["+getKdata[i]))
               {
                  String str = word.get(j);
                  fi = str.indexOf(str, fi);
                  for(int k=fi+1; k<fi+getKdata[i].length();k++)
                     index[k] = 'b';
                  //doc.setCharacterAttributes(fi+1, getKdata[i].length(), as_blue, true);
                  fi = fi + getKdata[i].length()+1;
               }
               else if(word.get(j).equals(getKdata[i]+")")||word.get(j).equals(getKdata[i]+"}")||word.get(j).equals(getKdata[i]+"]"))
               {
                  String str = word.get(j);
                  fi = str.indexOf(str, fi);
                  for(int k=fi; k<fi+getKdata[i].length();k++)
                     index[k] = 'b';
                  //doc.setCharacterAttributes(fi, getKdata[i].length(), as_blue, true);
                  fi = fi + getKdata[i].length()+1;
               }
               else if(word.get(j).equals("("+getKdata[i]+")")||word.get(j).equals("{"+getKdata[i]+"}")||word.get(j).equals("["+getKdata[i]+"]"))
               {
                  String str = word.get(j);
                  fi = str.indexOf(str, fi);
                  for(int k=fi+1; k<fi+1+getKdata[i].length();k++)
                     index[k] = 'b';
                  //doc.setCharacterAttributes(fi+1, getKdata[i].length(), as_blue, true);
                  fi = fi + getKdata[i].length()+2;
               }
               else if(word.get(j).contains(getKdata[i]))
               {
                  fi = str.indexOf(word.get(j), fi);
                  fi = fi + word.get(j).length();
               }
            }
         }
         ipos = 0;
         for(int i=0; i<word.size(); i++)
         {
            if(i-1 == -1)
               {continue;}
            if(word.get(i-1).equals("#include"))
            {
               ipos = str.indexOf("#include", ipos);
               ipos = ipos + word.get(i-1).length();
               for(int j=0; j<getHdata.length; j++)
               {
                  if(word.get(i).equals(getHdata[j]))
                  {
                     ipos = str.indexOf(getHdata[j], ipos);
                     for(int k=ipos; k<ipos+word.get(i).length();k++)
                        index[k] = 'k';
                     //doc.setCharacterAttributes(ipos, word.get(i).length(), as_pink, true);
                  }
               }
            }
         }
         ipos=0;
         cons.clear();
         for(int i=0; i<word.size(); i++)
         {
            if(i-1 == -1)
            {continue;}
            if(word.get(i-1).equals("#define"))
            {
               ipos = str.indexOf("#define", ipos);
               ipos = ipos + word.get(i-1).length();
               char ch = word.get(i).charAt(0);
               if(!Character.isDigit(ch)&&ch!='!'&&ch!='@'&&ch!='#'&&ch!='%'&&ch!='^'&&ch!='&'&&ch!='*'&&ch!='('&&ch!=')')
               {
                  cons.add(word.get(i));
                  ipos = str.indexOf(word.get(i), ipos);
                  for(int k=ipos; k<ipos+word.get(i).length();k++)
                     index[k] = 'p';
                  //doc.setCharacterAttributes(ipos, word.get(i).length(), as_purple, true);
                  ipos = ipos + word.get(i).length();
               }

            }
         }
         ipos=0;
         for(int i=0; i<word.size(); i++)
         {
            if(i-1 == -1)
            {continue;}
            if(word.get(i-1).equals("char")||word.get(i-1).equals("short")||word.get(i-1).equals("int")||word.get(i-1).equals("float")||word.get(i-1).equals("long")||word.get(i-1).equals("double"))
            {
               char ch = word.get(i).charAt(0);
               if(Character.isDigit(ch)||ch=='!'||ch=='@'||ch=='#'||ch=='%'||ch=='^'||ch=='&'||ch=='*'||ch=='('||ch==')'||ch=='{'||ch=='}'||ch=='['||ch==']')
               {
                  ipos = str.indexOf(word.get(i), ipos);
                  index[ipos] = 'r';
                  //doc.setCharacterAttributes(ipos, 1, as_red, true);
                  ipos = ipos + word.get(i).length();
               }   
            }
         }
         fi=0;
         for(int j=0; j<word.size(); j++)
         {
            if(word.get(j).equals("#define"))
            {
               fi = str.indexOf("#define", fi);
               fi = fi + word.get(j).length();
            }
            for(int i=0; i<cons.size(); i++)
            {
               if(word.get(j).equals(cons.get(i)))
               {
                  fi = str.indexOf(cons.get(i), fi);
                  for(int k=fi; k<fi+cons.get(i).length();k++)
                     index[k] = 'p';
                  //doc.setCharacterAttributes(fi, cons.get(i).length(), as_purple, true);
                  fi = fi + cons.get(i).length();
               }
               else if(word.get(j).equals(cons.get(i)+";"))
               {
                  fi = str.indexOf(word.get(j), fi);
                  for(int k=fi; k<fi+cons.get(i).length();k++)
                     index[k] = 'p';
                  //doc.setCharacterAttributes(fi, cons.get(i).length(), as_purple, true);
                  fi = fi + cons.get(i).length()+1; 
               }
               else if(word.get(j).equals("("+cons.get(i)+");")||word.get(j).equals("{"+cons.get(i)+"};")||word.get(j).equals("["+cons.get(i)+"];"))
               {
                  fi = str.indexOf(word.get(j), fi);
                  for(int k=fi+1; k<fi+1+cons.get(i).length();k++)
                     index[k] = 'p';
                  //doc.setCharacterAttributes(fi+1, cons.get(i).length(), as_purple, true);
                  fi = fi + cons.get(i).length()+3; 
               }
               else if(word.get(j).equals("("+cons.get(i))||word.get(j).equals("{"+cons.get(i))||word.get(j).equals("["+cons.get(i)))
               {
                  fi = str.indexOf(word.get(j), fi);
                  for(int k=fi+1; k<fi+1+cons.get(i).length();k++)
                     index[k] = 'p';
                  //doc.setCharacterAttributes(fi+1, cons.get(i).length(), as_purple, true);
                  fi = fi + cons.get(i).length()+1;
               }
               else if(word.get(j).equals(cons.get(i)+")")||word.get(j).equals(cons.get(i)+"}")||word.get(j).equals(cons.get(i)+"]"))
               {
                  fi = str.indexOf(word.get(j), fi);
                  for(int k=fi; k<fi+cons.get(i).length();k++)
                     index[k] = 'p';
                  //doc.setCharacterAttributes(fi, cons.get(i).length(), as_purple, true);
                  fi = fi + cons.get(i).length()+1;
               }
               else if(word.get(j).equals("("+cons.get(i)+")")||word.get(j).equals("{"+cons.get(i)+"}")||word.get(j).equals("["+cons.get(i)+"]"))
               {
                  fi = str.indexOf(word.get(j), fi);
                  for(int k=fi+1; k<fi+1+cons.get(i).length();k++)
                     index[k] = 'p';
                  //doc.setCharacterAttributes(fi+1, cons.get(i).length(), as_purple, true);
                  fi = fi + cons.get(i).length()+2;
               }
            }
         }


         for(int i=0;i<char_text.length;i++){
            //주석(//) 검사
            //스택이 비어있으면 //를 바로 push
            if(stack.isEmpty() && (i+1<char_text.length) && ((char_text[i] == '/') && (char_text[i+1] == '/'))){
               stack.push(new Element(char_text[i],i));
               stack.push(new Element(char_text[i+1],i+1));
               int newline=0;
               for(int j = i; j<=char_text.length; j++){   // \n 찾기
                  if(char_text[j] == '\n' || j == char_text.length-1){
                     newline = j-i+1;
                     break;
                  }
               }//i부터 줄바뀌기 전까지 초록색
               for(int j=i;j<i+newline;j++)
                  index[j] = 'g';
               //doc.setCharacterAttributes(i, newline, as_green, true);
               continue;
            }//스택이 비어있지 않으면 "와 /*와 //가 안열려 있을 때만 push
            else if((!stack.isEmpty()) && (stack.Peek().getChar() != '"') && 
                  (stack.Peek().getChar() != '*') && (stack.Peek().getChar() != '/') && 
                  (i+1<char_text.length) && ((char_text[i] == '/') && (char_text[i+1] == '/'))){
               stack.push(new Element(char_text[i],i));
               stack.push(new Element(char_text[i+1],i+1));
               int newline=0;
               for(int j = i; j<=char_text.length; j++){
                  if(char_text[j] == '\n' || j == char_text.length-1){
                     newline = j-i+1;
                     break;
                  }
               }
               for(int j=i;j<i+newline;j++)
                  index[j] = 'g';
               //doc.setCharacterAttributes(i, newline, as_green, true);
               continue;
            }
            // 줄바꿈일 때 스택에 //가 들어있으면 pop
            if((char_text[i] == '\n') && (!stack.isEmpty()) && (stack.Peek().getChar() == '/')){
               stack.pop();
               int pos = stack.pop().getLocation();
               for(int j=pos;j<i+1;j++)
                  index[j] = 'g';
               //doc.setCharacterAttributes(pos, i-pos+1, as_green, true);
            }
            //주석(/**/) 검사
            //스택에 /*가 들어 있을 때 */이 나오면 주석 끝, 안나오면 검사하지않고 i++
            if((!stack.isEmpty()) && (stack.Peek().getChar() == '*') && (i+1<char_text.length)){
               if(char_text[i] != '*')
                  continue;
               else{
                  if(char_text[i+1]=='/'){
                     stack.pop();
                     int pos = stack.pop().getLocation();
                     for(int j=pos;j<i+2;j++)
                        index[j] = 'g';
                     //doc.setCharacterAttributes(pos, i-pos+2, as_green, true);
                     //pop을 하면서 나오는 Element의 i,i+1위치에 오류지우기 및 초록색으로 변환
                     continue;
                  }
                  continue;
               }
            }
            if(stack.isEmpty()){         //열리지 않은 주석 오류 (스택이 빈 경우)
               if((i+1<char_text.length) && ((char_text[i] == '*') && (char_text[i+1] == '/'))){
                  index[i] = index[i+1] = 'e';
                  //doc.setCharacterAttributes(i, 2, as_error, true);//TODO i,i+1에 오류표시
                  continue;
               }
            }
            else{                     //열리지 않은 주석 오류 (스택에 괄호가 있는 경우)
               if(((stack.Peek().getChar() != '*')  && (stack.Peek().getChar() != '"'))
                     && (i+1<char_text.length) && ((char_text[i] == '*') && (char_text[i+1] == '/'))){
                  index[i] = index[i+1] = 'e';
                  //doc.setCharacterAttributes(i, 2, as_error, true); //TODO i,i+1에 오류표시
                  continue;
               }
            }
            //주석시작
            if((i+1<char_text.length) && ((char_text[i] == '/') && (char_text[i+1] == '*'))){
               if(stack.isEmpty()){      //스택이 비어있을 경우에는 바로 push, 스레드 이용때문에 계속 push방지
                  stack.push(new Element(char_text[i],i));
                  stack.push(new Element(char_text[i+1],i+1));
                  index[i] = index[i+1] = 'e';
                  //doc.setCharacterAttributes(i, 2, as_error, true);
                  continue;
               }
               else{      //스택이 비어있지 않을 경우는 이전에 주석이 없었을 때나 문자열 작성중이 아닐때 push
                  if((stack.Peek().getChar() != '*') && (stack.Peek().getChar() != '"')
                        &&(stack.Peek().getChar() != '/')){
                     stack.push(new Element(char_text[i],i));
                     stack.push(new Element(char_text[i+1],i+1));
                     index[i] = index[i+1] = 'e';
                     //doc.setCharacterAttributes(i, 2, as_error, true);
                     continue;
                  }
               }
            }

            //"" 검사
            if((!stack.isEmpty()) && (stack.Peek().getChar() == '"')){
               if(char_text[i] == '"'){
                  int pos = stack.pop().getLocation();
                  for(int j=pos;j<i+1;j++)
                     index[j] = 'o';
                  //doc.setCharacterAttributes(pos, i-pos+1 , as_orange, true);
                  continue;

               }
               else if(char_text[i] == '\n'){
                  stack.pop();
                  continue;
               }
               else continue;
            }
            if(char_text[i] == '"'){
               stack.push(new Element(char_text[i],i));
               index[i] = 'e';
               //doc.setCharacterAttributes(i, 1, as_error, true);
               continue;
            }
            //''검사
            if(char_text[i] == '\''){
               index[i] = 'e';
               //doc.setCharacterAttributes(i, 1, as_error, true);
               if((i>=1) && (char_text[i-1] == '\'')){
                  index[i-1] = index[i] = 'o';
                  //doc.setCharacterAttributes(i-1, 2, as_orange, true);
                  continue;
               }
               else if((i>=2) && (char_text[i-2] == '\'')){
                  index[i-2] = index[i-1] = index[i] = 'o';
                  //doc.setCharacterAttributes(i-2, 3, as_orange, true);
                  continue;
               }
            }

            //괄호검사
            if((char_text[i] == '(') || (char_text[i] == '{') || (char_text[i]== '[')){
               stack.push(new Element(char_text[i],i));
            }
            else if ((char_text[i] == ')') || (char_text[i] == '}') || (char_text[i]== ']')){
               if(stack.isEmpty()){       //오른쪽 괄호가 더 많은 경우의 에러 처리.
                  index[i] = 'e';
                  //doc.setCharacterAttributes(i, 1, as_error, true);
                  continue;
               }
               if(((stack.Peek().getChar() == '(') && (char_text[i] == ')')) ||
                     ((stack.Peek().getChar() == '{') && (char_text[i] == '}')) ||
                     ((stack.Peek().getChar() == '[') && (char_text[i] == ']'))){
                  pos = stack.pop().getLocation();
                  index[pos] = index[i] = 'p';
                  //doc.setCharacterAttributes(pos, 1, as_purple, true);
                  //doc.setCharacterAttributes(i, 1, as_purple, true);
               }
               else{
                  pos = stack.Peek().getLocation();
                  index[pos] = index[i] = 'e';
                  //doc.setCharacterAttributes(pos, 1, as_error, true);
                  //doc.setCharacterAttributes(i, 1, as_error, true);
               }
            }
         }

         if(!stack.isEmpty()){      //TODO stack.pop().getLocation()위치의 JTextPane에 오류 표시.
            while(!stack.isEmpty()){               //왼쪽 괄호가 더 많은 경우 에러 처리.
               if((stack.Peek().getChar() != '*' && stack.Peek().getChar() != '/')
                     && stack.Peek().getChar() != '"'){
                  pos = stack.pop().getLocation();
                  index[pos] = 'e';
                  //doc.setCharacterAttributes(pos, 1, as_error, true);
               }
               else if (stack.Peek().getChar() == '*'){            //TODO 두개 밑줄 그어서 오류 표시
                  stack.pop();
                  pos = stack.pop().getLocation();
                  for(int j=pos;j<char_text.length;j++)
                     index[j] = 'e';
                  //doc.setCharacterAttributes(pos, str.length() - pos + 1, as_error, true);
               }
               else if(stack.Peek().getChar() == '"'){ //큰따옴표 안닫힌 경우 에러
                  pos = stack.pop().getLocation();
                  index[pos] = 'e';
                  //doc.setCharacterAttributes(pos, 1, as_error, true);
               }
               else if(stack.Peek().getChar() == '/'){   // 주석(//)이 젤 마지막에 있을 경우 처리
                  stack.pop();
                  pos = stack.pop().getLocation();
                  index[pos] = index[pos+1] = 'g';
                  //doc.setCharacterAttributes(pos, 2, as_green, true);
               }
            }
         }

         try{
            for(int i = 0; i < char_text.length; i++){
               if(index[i] == 'g')
                  doc.setCharacterAttributes(i, 1, as_green, true);
               else if(index[i] == 'o')
                  doc.setCharacterAttributes(i, 1, as_orange, true);
               else if(index[i] == 'e')
                  doc.setCharacterAttributes(i, 1, as_error, true);
               else if(index[i] == 'p')
                  doc.setCharacterAttributes(i, 1, as_purple, true);
               else if(index[i] == 'b')
                  doc.setCharacterAttributes(i, 1, as_blue, true);
               else if(index[i] == 'k')
                  doc.setCharacterAttributes(i, 1, as_pink, true);
               else if(index[i] == 'r')
                  doc.setCharacterAttributes(i, 1, as_red, true);
               else
                  doc.setCharacterAttributes(i, 1, as_black, true);
            }
            Thread.sleep(10);
         }
         catch(InterruptedException e){
            return;
         }
      }
   }
}


