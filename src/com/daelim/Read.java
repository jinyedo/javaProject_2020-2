package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Read extends Write {
    Read(String userName) {
        super(userName);
        System.out.println("[ Read 생성자 호출 ]");
        setJPanel(read_panel,"#3B4752");
        read_panel.setPreferredSize(new Dimension(900,720));

        setJLabel_back(read_frameLabel, Color.BLACK);
        setJLabel_font(read_authorLabel, Color.WHITE, 30);
        setJLabel_font(read_viewCountLabel, Color.WHITE, 30);
        setJLabel_font(read_titleLabel, Color.WHITE, 30);
        setJLabel_font(read_memoLabel, Color.WHITE, 30);
        setJTextField(read_authorTF, Color.WHITE, 30,27);
        setJTextField(read_viewCountTF, Color.WHITE, 30,27);
        setJTextField(read_titleTF, Color.WHITE, 30,27);
        setJTextArea(read_memoTA, Color.WHITE, 30,135);
        setJBtn(read_exitBtn, Color.WHITE, Color.decode("#5E5E5E"),20);

        read_viewCountTF.setHorizontalAlignment(JLabel.CENTER);

        read_authorTF.setEditable(false);
        read_viewCountTF.setEditable(false);
        read_titleTF.setEditable(false);
        read_memoTA.setEditable(false);

        read_authorLabel.setBounds(100,135,90,30);
        read_authorTF.setBounds(250,130,320,40);
        read_viewCountLabel.setBounds(590,135,90,30);
        read_viewCountTF.setBounds(700,130,100,40);
        read_titleLabel.setBounds(100,220,100,30);
        read_titleTF.setBounds(250,215,550,40);
        read_memoLabel.setBounds(100,305,100,30);
        read_memoTA.setBounds(250,300,550,185);
        read_exitBtn.setBounds(640,525,160,50);
        read_frameLabel.setBounds(50,85,800,535);

        read_panel.add(read_authorLabel);
        read_panel.add(read_authorTF);
        read_panel.add(read_viewCountLabel);
        read_panel.add(read_viewCountTF);
        read_panel.add(read_titleLabel);
        read_panel.add(read_titleTF);
        read_panel.add(read_memoLabel);
        read_panel.add(read_memoTA);
        read_panel.add(read_exitBtn);
        read_panel.add(read_frameLabel);

        // 나가기버튼 이벤트처리
        read_exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPanelFontColor(Color.decode("#16A085"), Color.WHITE, Color.WHITE, Color.WHITE);
                changePanel3(showList_panel);
                try {
                    searchData(showList_table, showList_searchTF, userName);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
