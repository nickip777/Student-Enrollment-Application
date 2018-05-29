package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * student filter jpanel allows user to select year
 */
public class StudentFilter extends UIBox {
    private String header;
    private JPanel contents;
    private JCheckBox inYear;
    private JTextField inYearInput;
    private JCheckBox gradYear;
    private JTextField gradYearInput;

    public StudentFilter(){
        super();
        this.setSize(400,400);
        header = "Filter Students:";
        inYear = new JCheckBox("In selected program during year:");
        inYearInput = new JTextField();
        inYearInput.setSize(40,10);
        gradYear = new JCheckBox("Graduated selected program in year");
        gradYearInput = new JTextField();
        this.setName(header);
        JLabel label = new JLabel(header);
        this.add(label,BorderLayout.NORTH);
        label.setBorder(new EmptyBorder(5,5,0,0));
        contents = new JPanel();
        contents.setLayout(new GridLayout(2,2));
        contents.add(inYear);
        contents.add(inYearInput);
        contents.add(gradYear);
        contents.add(gradYearInput);
        this.add(contents);

    }

    //get the year selected
    public int getInYear(){
        int result = -1;
        if(inYear.isSelected()){
            try{
                result = Integer.parseInt(inYearInput.getText());
            }catch(NumberFormatException e){
                e.printStackTrace();
                return -2;
            }
        }else{
            return -1;
        }
        return result;
    }

    //get the grad year selescted
    public int getGradYear(){
        int result = -1;
        if(gradYear.isSelected()){
            try{
                result = Integer.parseInt(gradYearInput.getText());
            }catch(NumberFormatException e){
                e.printStackTrace();
                return -2;
            }
        }else{
            return -1;
        }
        return result;
    }
}
