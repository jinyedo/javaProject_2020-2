package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Find extends Setting {

    Find() {
        jdbcLoad();
        conDB();
        System.out.println("[ Find 생성자 호출 ]");
        jf = this;
        setJPanel(find_panel,"#3B4752");

        setJLabel_back(find_topLabel, Color.BLACK);
        setJLabel_back(find_findID_frameLabel, Color.WHITE);
        setJLabel_back(find_findPW_frameLabel, Color.WHITE);
        setJLabel_font(find_logoLabel, Color.WHITE,35);
        setJLabel_font(find_findID_Label, Color.WHITE,35);
        setJLabel_font(find_findPW_Label, Color.WHITE,35);
        setJLabel_font(find_findID_nameLabel, Color.BLACK,35);
        setJLabel_font(find_findPW_nameLabel, Color.BLACK,35);
        setJLabel_font(find_findPW_idLabel, Color.BLACK,35);
        setJTextField(find_findID_nameTF, Color.decode("#E3EFF8"),30,5);
        setJTextField(find_findPW_nameTF, Color.decode("#E3EFF8"),30,5);
        setJTextField(find_findPW_idTF, Color.decode("#E3EFF8"),30,15);
        setJBtn(find_signInBtn, Color.WHITE, Color.BLACK,20);
        setJBtn(find_findID_Btn, Color.WHITE, Color.decode("#1ABC9C"),20);
        setJBtn(find_findPW_Btn, Color.WHITE, Color.decode("#1ABC9C"),20);


        find_topLabel.setBounds(0,0,1200,80);
        find_logoLabel.setBounds(20,0,150,80);
        find_signInBtn.setBounds(1000,10,150,60);
        find_findID_Label.setBounds(250,180,200,50);
        find_findID_nameLabel.setBounds(170,270,100,40);
        find_findID_nameTF.setBounds(170,320,360,50);
        find_findID_Btn.setBounds(170,485,360,50);
        find_findPW_Label.setBounds(750,180,300,50);
        find_findPW_nameLabel.setBounds(670,270,100,40);
        find_findPW_nameTF.setBounds(670,320,360,50);
        find_findPW_idLabel.setBounds(670,370,150,40);
        find_findPW_idTF.setBounds(670,420,360,50);
        find_findPW_Btn.setBounds(670,485,360,50);
        find_findID_frameLabel.setBounds(150,250,400,300);
        find_findPW_frameLabel.setBounds(650,250,400,300);

        find_panel.add(find_findID_Label);
        find_panel.add(find_findID_nameLabel);
        find_panel.add(find_findID_nameTF);
        find_panel.add(find_findID_Btn);
        find_panel.add(find_findPW_Label);
        find_panel.add(find_findPW_nameLabel);
        find_panel.add(find_findPW_nameTF);
        find_panel.add(find_findPW_idLabel);
        find_panel.add(find_findPW_idTF);
        find_panel.add(find_findPW_Btn);
        find_panel.add(find_findID_frameLabel);
        find_panel.add(find_findPW_frameLabel);

        top(find_panel, find_topLabel, find_logoLabel, find_signInBtn);

        // 로그인창 이동
        find_signInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                changePanel(lgn_panel);
            }
        });

        // 아이디 찾기 이벤트 처리
        find_findID_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                find_findID_name = find_findID_nameTF.getText();
                if (!find_findID_name.equals("")) {
                    try {
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select * from user where name='" + find_findID_name + "'");
                        if (rs.next()) {
                            find_findID_nameTF.setText("");
                            String findID = rs.getString("id");
                            JOptionPane.showMessageDialog(null, "회원님의 아이디는 \"" + findID + "\" 입니다.");
                        } else {
                            find_findID_nameTF.setText("");
                            JOptionPane.showMessageDialog(null,  find_findID_name + "님의 아이디가 존재하지 않습니다.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "회원님의 이름을 입력해주세요.");
                }
            }
        });

        // 비밀번호 찾기 이벤트 처리
        find_findPW_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                find_findPW_id = find_findPW_idTF.getText();
                find_findPW_name = find_findPW_nameTF.getText();
                if (find_findPW_name.equals("") && find_findPW_id.equals("")) {
                    find_findPW_nameTF.requestFocus();
                    JOptionPane.showMessageDialog(null, "회원님의 이름과 아이디를 입력해주세요.");
                } else if (find_findPW_name.equals("") && !find_findPW_id.equals("")) {
                    find_findPW_nameTF.requestFocus();
                    JOptionPane.showMessageDialog(null, "회원님의 이름을 입력해주세요.");
                } else if (!find_findPW_name.equals("") && find_findPW_id.equals("")) {
                    find_findPW_idTF.requestFocus();
                    JOptionPane.showMessageDialog(null, "회원님의 아이디를 입력해주세요.");
                } else if (!find_findPW_name.equals("") && !find_findPW_id.equals("")) {
                    try {
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select * from user where id='" + find_findPW_id + "'" +
                                "and name='" + find_findPW_name + "'");
                        if (rs.next()) {
                            find_findPW_idTF.setText("");
                            find_findPW_nameTF.setText("");
                            String findPW = rs.getString("pw");
                            JOptionPane.showMessageDialog(null, "회원님의 비밀번호는 \"" + findPW + "\" 입니다.");
                        } else {
                            find_findPW_idTF.setText("");
                            find_findPW_nameTF.setText("");
                            JOptionPane.showMessageDialog(null, "입력정보가 올바르지 않습니다.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
