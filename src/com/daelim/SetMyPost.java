package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetMyPost extends ChangePassword {
    SetMyPost(String userName) {
        super(userName);
        System.out.println("[ SetMyPost 생성자 호출 ]");
        setJPanel(setMyPost_panel,"#3B4752");
        setMyPost_panel.setPreferredSize(new Dimension(900,720));

        setJLabel_back(setMyPost_frameLabel, Color.BLACK);
        setJLabel_font(setMyPost_logoLabel, Color.WHITE, 40);
        setJLabel_font(setMyPost_titleLabel, Color.WHITE, 30);
        setJLabel_font(setMyPost_memoLabel, Color.WHITE, 30);
        setJTextField(setMyPost_titleTF, Color.WHITE, 30,27);
        setJTextArea(setMyPost_memoTA, Color.WHITE, 30,135);
        setJBtn(setMyPost_saveBtn, Color.WHITE, Color.decode("#1ABC9C"),20);
        setJBtn(setMyPost_deleteBtn, Color.WHITE, Color.decode("#C0392B"),20);
        setJBtn(setMyPost_cancelBtn, Color.WHITE, Color.decode("#5E5E5E"),20);

        setMyPost_logoLabel.setHorizontalAlignment(JLabel.CENTER);

        setMyPost_logoLabel.setBounds(250,50,400,40);
        setMyPost_titleLabel.setBounds(100,220,100,30);
        setMyPost_titleTF.setBounds(250,215,550,40);
        setMyPost_memoLabel.setBounds(100,305,100,30);
        setMyPost_memoTA.setBounds(250,300,550,185);
        setMyPost_saveBtn.setBounds(250,525,160,50);
        setMyPost_deleteBtn.setBounds(445,525,160,50);
        setMyPost_cancelBtn.setBounds(640,525,160,50);
        setMyPost_frameLabel.setBounds(50,170,800,450);

        setMyPost_panel.add(setMyPost_logoLabel);
        setMyPost_panel.add(setMyPost_titleLabel);
        setMyPost_panel.add(setMyPost_titleTF);
        setMyPost_panel.add(setMyPost_memoLabel);
        setMyPost_panel.add(setMyPost_memoTA);
        setMyPost_panel.add(setMyPost_saveBtn);
        setMyPost_panel.add(setMyPost_deleteBtn);
        setMyPost_panel.add(setMyPost_cancelBtn);
        setMyPost_panel.add(setMyPost_frameLabel);

        // 수정버튼 이벤트처리
        setMyPost_saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                if (setMyPost_titleTF.getText().equals("") && setMyPost_memoTA.getText().equals("")) {
                    setMyPost_titleTF.requestFocus();
                    JOptionPane.showMessageDialog(null, "글제목과 내용을 입력해주세요.");
                } else if (setMyPost_titleTF.getText().equals("") && !setMyPost_memoTA.getText().equals("")) {
                    setMyPost_titleTF.requestFocus();
                    JOptionPane.showMessageDialog(null, "글제목을 입력해주세요.");
                } else if (!setMyPost_titleTF.getText().equals("") && setMyPost_memoTA.getText().equals("")) {
                    setMyPost_memoTA.requestFocus();
                    JOptionPane.showMessageDialog(null, "내용을 입력해주세요.");
                } else {
                    int result = JOptionPane.showConfirmDialog(null,"게시글을 수정하시겠습니까?",
                            "Sign Out",JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            pStmt = con.prepareStatement("update list set title=?, memo=? where idx=" + idx);
                            pStmt.setString(1, setMyPost_titleTF.getText());
                            pStmt.setString(2, setMyPost_memoTA.getText());
                            int cnt = pStmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "수정이 완료되었습니다.");
                            searchData(showMyList_table, showMyList_searchTF, userName);
                            changePanel3(showMyList_panel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        //삭제버튼 이벤트처리
        setMyPost_deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                int result = JOptionPane.showConfirmDialog(null,"게시글을 삭제하시겠습니까?",
                        "Sign Out",JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        pStmt = con.prepareStatement("delete from list where idx=" + idx);
                        pStmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.");
                        searchData(showMyList_table, showMyList_searchTF, userName);
                        changePanel3(showMyList_panel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

       // 취소버튼 이벤트처리
        setMyPost_cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    searchData(showMyList_table, showMyList_searchTF, userName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                changePanel3(showMyList_panel);
            }
        });
    }
}
