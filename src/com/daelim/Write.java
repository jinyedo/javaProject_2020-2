package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Write extends LeftPanel {
    Write(String userName) {
        super(userName);
        System.out.println("[ Write 생성자 호출 ]");
        setJPanel(write_panel,"#3B4752");
        write_panel.setPreferredSize(new Dimension(900,720));

        setJLabel_back(write_frameLabel, Color.BLACK);
        setJLabel_font(write_logoLabel, Color.WHITE, 40);
        setJLabel_font(write_titleLabel, Color.WHITE, 30);
        setJLabel_font(write_memoLabel, Color.WHITE, 30);
        setJTextField(write_titleTF, Color.WHITE, 30,27);
        setJTextArea(write_memoTA, Color.WHITE, 30,135);
        setJBtn(write_saveBtn, Color.WHITE, Color.decode("#1ABC9C"),20);
        setJBtn(write_cancelBtn, Color.WHITE, Color.decode("#5E5E5E"),20);

        write_logoLabel.setHorizontalAlignment(JLabel.CENTER);

        write_logoLabel.setBounds(300,50,300,40);
        write_titleLabel.setBounds(100,220,100,30);
        write_titleTF.setBounds(250,215,550,40);
        write_memoLabel.setBounds(100,305,100,30);
        write_memoTA.setBounds(250,300,550,185);
        write_saveBtn.setBounds(430,525,160,50);
        write_cancelBtn.setBounds(640,525,160,50);
        write_frameLabel.setBounds(50,170,800,450);

        write_panel.add(write_logoLabel);

        write_panel.add(write_titleLabel);
        write_panel.add(write_titleTF);
        write_panel.add(write_memoLabel);
        write_panel.add(write_memoTA);
        write_panel.add(write_saveBtn);
        write_panel.add(write_cancelBtn);
        write_panel.add(write_frameLabel);

        write_saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                if (write_titleTF.getText().equals("") && write_memoTA.getText().equals("")) {
                    write_titleTF.requestFocus();
                    JOptionPane.showMessageDialog(null, "글제목과 내용을 입력해주세요.");
                } else if (write_titleTF.getText().equals("") && !write_memoTA.getText().equals("")) {
                    write_titleTF.requestFocus();
                    JOptionPane.showMessageDialog(null, "글제목을 입력해주세요.");
                } else if (!write_titleTF.getText().equals("") && write_memoTA.getText().equals("")) {
                    write_memoTA.requestFocus();
                    JOptionPane.showMessageDialog(null, "내용을 입력해주세요.");
                } else {
                    try {
                        String newTitle = write_titleTF.getText();
                        String newMemo = write_memoTA.getText();
                        String writer = userName;
                        pStmt = con.prepareStatement("insert into list (title, memo, author) value (?,?,?)");
                        pStmt.setString(1, newTitle);
                        pStmt.setString(2, newMemo);
                        pStmt.setString(3, writer);
                        int cnt = pStmt.executeUpdate();
                        resetIDX("list");
                        JOptionPane.showMessageDialog(null, "게시글 작성이 완료되었습니다.");
                        write_titleTF.setText("");
                        write_memoTA.setText("");
                        tableRefresh_all(userName);
                        leftPanelFontColor(Color.decode("#16A085"), Color.WHITE, Color.WHITE, Color.WHITE);
                        changePanel2(showList_panel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 취소
        write_cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                write_titleTF.setText("");
                write_memoTA.setText("");
                try {
                    tableRefresh_all(userName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                leftPanelFontColor(Color.decode("#16A085"), Color.WHITE, Color.WHITE, Color.WHITE);
                changePanel2(showList_panel);
            }
        });

    }
}
