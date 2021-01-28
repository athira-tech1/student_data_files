import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class student implements Serializable
{
   String name,phone,ktuID,roll;
   student(String name,String roll,String phone,String ktuID)
   {
      this.name=name;
      this.roll=roll;
      this.phone=phone;
      this.ktuID=ktuID;
   }}

class assignment extends JFrame implements ActionListener
{  
   JPanel pMain,pEnter,pView;
   //MAIN PANEL
   JButton bEnter,bView;
   JLabel lHeader;
    
   //DATA ENTERING PANEL
   JLabel lName,lRoll,lPhone,lKtuid;
   JTextField tName,tRoll,tPhone,tKtuid;
   JButton bSubmit,bBack;

   //DATA VIEW PANEL
   JScrollPane scrollBar;
   JButton bDone;
   JTable table;
   DefaultTableModel dtm;
   
   assignment()
   {
      setSize(500,500);
      setLayout(null);
      setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
 
      pMain=new JPanel();
      pEnter=new JPanel();
      pView=new JPanel();
      add(pMain);
      add(pEnter);
      add(pView);

   // MAIN PANEL COMPONENTS
      
      pMain.setLayout(null);
      bEnter=new JButton("ENTER DATA");
      bView=new JButton("VIEW DATA");
      lHeader=new JLabel("STUDENTS DATA");
      
      pMain.setBounds(0,0,500,500);
      lHeader.setBounds(180,120,150,40);
      bEnter.setBounds(160,200,150,30);
      bView.setBounds(160,250,150,30);

      pMain.add(lHeader);
      pMain.add(bEnter);
      pMain.add(bView);
      
      bEnter.addActionListener(this);
      bView.addActionListener(this);
      bEnter.setFocusable(false);                    
      bView.setFocusable(false);
      
      //ENTER PANEL COMPONENTS PART
      pEnter.setLayout(null);
      pEnter.setBounds(0,0,500,500);
      pEnter.setVisible(false);

      lName=new JLabel("NAME - ");
      lRoll=new JLabel("ROLL NUMBER -");
      lPhone=new JLabel("MOBILE NUMBER -");   
      lKtuid=new JLabel("KTU ID -");
      tName=new JTextField();
      tRoll=new JTextField();
      tPhone=new JTextField();
      tKtuid=new JTextField();
      bBack=new JButton("BACK");
      bSubmit=new JButton("SUBMIT");

      lName.setBounds(40,80,150,30);    tName.setBounds(200,80,250,30);
      lRoll.setBounds(40,140,150,30);   tRoll.setBounds(200,140,250,30);
      lPhone.setBounds(40,200,150,30);  tPhone.setBounds(200,200,250,30);
      lKtuid.setBounds(40,260,150,30);  tKtuid.setBounds(200,260,250,30);
      bBack.setBounds(120,350,100,30);   bSubmit.setBounds(270,350,100,30);
      
      pEnter.add(lName);   pEnter.add(tName);
      pEnter.add(lRoll);   pEnter.add(tRoll);
      pEnter.add(lPhone);  pEnter.add(tPhone);
      pEnter.add(lKtuid);  pEnter.add(tKtuid);
      pEnter.add(bBack);   pEnter.add(bSubmit);

      bBack.addActionListener(this);
      bSubmit.addActionListener(this);
      bBack.setFocusable(false);
      bSubmit.setFocusable(false);

      //VIEW PANEL COMPONENTS
      pView.setLayout(null);
      pView.setVisible(false);
      bDone=new JButton("DONE");
      
      table=new JTable();
      dtm=new DefaultTableModel(0,0);
      String header[]=new String[]{"Roll","name","phone","ktuid"}; 
      dtm.setColumnIdentifiers(header);
      table.setModel(dtm);
      scrollBar=new JScrollPane(table);
      
      pView.setBounds(0,0,500,500);
      bDone.setBounds(210,400,80,30);
      scrollBar.setBounds(50,30,400,350);

      pView.add(scrollBar);
      pView.add(bDone);

      bDone.addActionListener(this);
      bDone.setFocusable(false);
   }
   
   public void actionPerformed(ActionEvent e)
   {
     if(e.getSource()==bEnter)
     {pMain.setVisible(false);
      pEnter.setVisible(true);
     }
     else if(e.getSource()==bBack)
     {
        pMain.setVisible(true);
        pEnter.setVisible(false);
     }
     else if(e.getSource()==bSubmit)
     {
        pEnter.setVisible(false);
        pMain.setVisible(true);
        submit();
     }
     else if(e.getSource()==bView)
     {
        pMain.setVisible(false);
        pView.setVisible(true);
        display();
     }
     else if(e.getSource()==bDone)
     {
        pView.setVisible(false);
        pMain.setVisible(true);
     }
   }

   List<student> deSerialise()                             //method for deserialising
   {
     List<student> sList=new ArrayList<student>();
     student sObj=null;
     try(ObjectInputStream in=new ObjectInputStream(new FileInputStream("data.txt")))
     {
       while(true) 
          {   
           try
          {
             sObj=(student)in.readObject();
             sList.add(sObj);
          }
           catch(EOFException eofEx)
           {
            break;
           }}
       in.close();
      }

     catch(Exception e)
     { System.out.println("file data.txt is created"); }
    return sList;
   }
  
   void submit()
   {
      List<student> sList=new ArrayList<student>();
      try
       { int checkRoll= Integer.parseInt(tRoll.getText());
         }
      catch(NumberFormatException e)
      {
         JOptionPane.showMessageDialog(this,"INVALID ROLL NUMBER","ERROR",JOptionPane.WARNING_MESSAGE);
         clearText();
         return;
      }
      if(tName.getText().equals(""))
      { 
         JOptionPane.showMessageDialog(this,"NAME NOT ENTERED","ERROR",JOptionPane.WARNING_MESSAGE);
         clearText();
         return;
      }
      student s=new student(tName.getText(),tRoll.getText(),tPhone.getText(),tKtuid.getText());
      sList=deSerialise();
      sList.add(s);
      try
        {
         ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.txt"));
         for(student sObj:sList){  
         out.writeObject(sObj);
         }
         out.close();
         }
        catch(Exception ex)
        {
            System.out.println("Error");
        }
        clearText();
   }

   void display()               
      {
       int rowCount = dtm.getRowCount();              
       for (int i=rowCount-1;i>=0;i--) 
       dtm.removeRow(i);

       List<student> sList=new ArrayList<student>();
       try(ObjectInputStream in=new ObjectInputStream(new FileInputStream("data.txt")))
       {
         sList=deSerialise();
         for(int i=0;i<sList.size();++i) 
         {
           String tempName=sList.get(i).name;
           String tempRoll=sList.get(i).roll;
           String tempPhone=sList.get(i).phone;
           String tempID=sList.get(i).ktuID;
           Object[] data={tempRoll,tempName,tempPhone,tempID};
           dtm.addRow(data);
         }
        }
        catch(IOException ex)
        {
         JOptionPane.showMessageDialog(this,"NO RECORDS","",JOptionPane.WARNING_MESSAGE);
        }    
      }
   
   void clearText()
   {
      tName.setText("");
      tRoll.setText("");
      tPhone.setText("");
      tKtuid.setText("");
   }
public static void main(String args[])
   {
      new assignment();
      
   }}
   
