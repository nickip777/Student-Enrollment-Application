package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * parent class for panels
 */
public class UIBox extends JPanel {
    //observer
    private static ArrayList<UIObserver> observers = new ArrayList<UIObserver>();
    public UIBox(){
        setLayout(new BorderLayout());
        add(makeButton(), BorderLayout.PAGE_END);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    //button
    private Component makeButton() {
        JButton button = new JButton("Apply");
        button.setBorder(new EmptyBorder(5,5,5,5));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers();
            }
        });
        return button;
    }

    //observers
    public static  void addObserver(UIObserver observer) {
        observers.add(observer);
    }
    private static void notifyObservers() {
        for (UIObserver observer : observers) {
            observer.stateChanged();
        }
    }

}
