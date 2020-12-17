package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LeftPanel extends TopPanel {

    LeftPanel(String userName) {
        super(userName);
        System.out.println("[ LeftPanel 생성자 호출 ] ");
        setJPanel(leftPanel_panel, "#2C3E50");
        leftPanel_panel.setPreferredSize(new Dimension(300,720));
        leftPanel_userLabel = new JLabel(userName + "님");

        setJLabel_font(leftPanel_userLabel, Color.WHITE,40);
        setJBtn_Transparent(leftPanel_listBtn, Color.decode("#16A085"),30);
        setJBtn_Transparent(leftPanel_myListBtn, Color.WHITE, 30);
        setJBtn_Transparent(leftPanel_writeBtn, Color.WHITE, 30);
        setJBtn_Transparent(leftPanel_profileBtn, Color.WHITE, 30);

        leftPanel_userLabel.setHorizontalAlignment(JLabel.CENTER);
        leftPanel_listBtn.setHorizontalAlignment(JLabel.CENTER);
        leftPanel_myListBtn.setHorizontalAlignment(JLabel.CENTER);
        leftPanel_writeBtn.setHorizontalAlignment(JLabel.CENTER);
        leftPanel_profileBtn.setHorizontalAlignment(JLabel.CENTER);

        leftPanel_userLabel.setBounds(25,80,250,40);
        leftPanel_listBtn.setBounds(25,200,250,30);
        leftPanel_myListBtn.setBounds(25,280,250,30);
        leftPanel_writeBtn.setBounds(25,360,250,30);
        leftPanel_profileBtn.setBounds(25,440,250,30);

        leftPanel_panel.add(leftPanel_userLabel);
        leftPanel_panel.add(leftPanel_listBtn);
        leftPanel_panel.add(leftPanel_myListBtn);
        leftPanel_panel.add(leftPanel_writeBtn);
        leftPanel_panel.add(leftPanel_profileBtn);

        // 게시글 리스트 버튼 이벤트처리
        leftPanel_listBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    tableRefresh_showList();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                leftPanelFontColor(Color.decode("#16A085"), Color.WHITE, Color.WHITE, Color.WHITE);
                leftPanel_panel.remove(leftPanel_changePasswordBtn);
                panelName = "showListPanel";
                changePanel2(showList_panel);
            }
        });

        // 작성한 게시글 보기버튼 이벤트처리
        leftPanel_myListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    stmt = con.createStatement();
                    rs = stmt.executeQuery("select * from list where author='" + userName + "'");
                    if (rs.next()) {
                        tableRefresh_showMyList(userName);
                        leftPanelFontColor(Color.WHITE, Color.decode("#16A085"), Color.WHITE, Color.WHITE);
                        leftPanel_panel.remove(leftPanel_changePasswordBtn);
                        panelName = "showMyListPanel";
                        changePanel2(showMyList_panel);
                    } else {
                        JOptionPane.showMessageDialog(null, "작성하신 게시글이없습니다.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 글쓰기 버튼 이벤트처리
        leftPanel_writeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                leftPanelFontColor(Color.WHITE, Color.WHITE, Color.decode("#16A085"), Color.WHITE);
                leftPanel_panel.remove(leftPanel_changePasswordBtn);
                panelName = "writePanel";
                changePanel2(write_panel);
            }
        });

        // 회원정보버튼 이벤트처리
        leftPanel_profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                if (panelName.equals("changePasswordPanel")) {
                    leftPanelFontColor(Color.WHITE, Color.WHITE, Color.WHITE, Color.decode("#16A085"));
                    setJBtn_Transparent(leftPanel_changePasswordBtn, Color.WHITE, 20);
                    panelName = "proFilePanel";
                    changePanel2(proFile_panel);
                } else {
                    String pw = JOptionPane.showInputDialog("비밀번호를 입력해주세요.");
                    if (pw != null) {
                        try {
                            stmt = con.createStatement();
                            rs = stmt.executeQuery("select * from user where name='" + userName + "'");
                            if (rs.next()) {
                                if (pw.equals(rs.getString("pw"))) {
                                    leftPanelFontColor(Color.WHITE, Color.WHITE, Color.WHITE, Color.decode("#16A085"));
                                    setJBtn_Transparent(leftPanel_changePasswordBtn, Color.WHITE, 20);
                                    panelName = "proFilePanel";
                                    changePanel2(proFile_panel);
                                    leftPanel_changePasswordBtn.setBounds(65,490,200,20);
                                    leftPanel_panel.add(leftPanel_changePasswordBtn);
                                    leftPanel_changePasswordBtn.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            leftPanelFontColor(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
                                            leftPanel_changePasswordBtn.setForeground(Color.decode("#16A085"));
                                            panelName = "changePasswordPanel";
                                            changePanel2(changePassword_panel);
                                        }
                                    });
                                } else {
                                    JOptionPane.showMessageDialog(null, "비밀번호가 올바르지않습니다.");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        jf.add(leftPanel_panel, BorderLayout.WEST);
    }
}
