package com.daelim;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopPanel extends Setting {

    TopPanel(String userName) {
        jdbcLoad();
        conDB();
        System.out.println("로그인 완료 | 접속자: " + userName);
        System.out.println("[ TopPanel 생성자 호출 ] ");
        jf = this;
        jf.setLayout(new BorderLayout());
        setJPanel(topPanel_panel, "000000");
        topPanel_panel.setPreferredSize(new Dimension(1200,80));

        setJLabel_font(topPanel_logoLabel, Color.WHITE,35);
        setJBtn(topPanel_signOutBtn, Color.WHITE, Color.BLACK,20);

        topPanel_frameLabel.setBounds(0,0,1200,80);
        topPanel_signOutBtn.setBounds(1000,10,150,60);
        topPanel_logoLabel.setBounds(20,0,150,80);

        topPanel_panel.add(topPanel_logoLabel);
        topPanel_panel.add(topPanel_signOutBtn);
        topPanel_panel.add(topPanel_frameLabel);

        // 로그아웃
        topPanel_signOutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signOut();
            }
        });
        jf.add(topPanel_panel, BorderLayout.NORTH);
    }
}
