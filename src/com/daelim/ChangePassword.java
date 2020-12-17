package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePassword extends Profile {
    ChangePassword(String userName) {
        super(userName);
        System.out.println("[ ChangePassword 생성자 호출 ]");
        setJPanel(changePassword_panel,"#3B4752");
        changePassword_panel.setPreferredSize(new Dimension(900,720));

        setJLabel_back(changePassword_frameLabel, Color.BLACK);
        setJLabel_font(changePassword_logoLabel, Color.WHITE, 40);
        setJLabel_font(changePassword_passwordLabel, Color.WHITE, 30);
        setJLabel_font(changePassword_newPasswordLabel, Color.WHITE, 30);
        setJLabel_font(changePassword_newPasswordConfirmationLabel, Color.WHITE, 30);
        setJPasswordField(changePassword_passwordJPF, Color.WHITE, 30,27);
        setJPasswordField(changePassword_newPasswordJPF, Color.WHITE, 30,27);
        setJPasswordField(changePassword_newPasswordConfirmationJPF, Color.WHITE, 30,27);
        setJBtn(changePassword_saveBtn, Color.WHITE, Color.decode("#1ABC9C"),20);
        setJBtn(changePassword_cancelBtn, Color.WHITE, Color.decode("#7F7F7F"),20);

        changePassword_logoLabel.setHorizontalAlignment(JLabel.CENTER);
        changePassword_passwordJPF.setEchoChar('*');
        changePassword_newPasswordJPF.setEchoChar('*');
        changePassword_newPasswordConfirmationJPF.setEchoChar('*');

        changePassword_logoLabel.setBounds(250,50,400,40);
        changePassword_passwordLabel.setBounds(100,220,240,30);
        changePassword_passwordJPF.setBounds(390,215,410,40);
        changePassword_newPasswordLabel.setBounds(100,305,240,30);
        changePassword_newPasswordJPF.setBounds(390,300,410,40);
        changePassword_newPasswordConfirmationLabel.setBounds(100,385,240,30);
        changePassword_newPasswordConfirmationJPF.setBounds(390,390,410,40);
        changePassword_saveBtn.setBounds(430,475,160,50);
        changePassword_cancelBtn.setBounds(640,475,160,50);
        changePassword_frameLabel.setBounds(50,170,800,405);

        changePassword_panel.add(changePassword_logoLabel);
        changePassword_panel.add(changePassword_passwordLabel);
        changePassword_panel.add(changePassword_passwordJPF);
        changePassword_panel.add(changePassword_newPasswordLabel);
        changePassword_panel.add(changePassword_newPasswordJPF);
        changePassword_panel.add(changePassword_newPasswordConfirmationLabel);
        changePassword_panel.add(changePassword_newPasswordConfirmationJPF);
        changePassword_panel.add(changePassword_saveBtn);
        changePassword_panel.add(changePassword_cancelBtn);
        changePassword_panel.add(changePassword_frameLabel);

        // 변경완료 버튼 이벤트처리
        changePassword_saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                String password = changePassword_passwordJPF.getText(),
                       newPassword = changePassword_newPasswordJPF.getText(),
                       newPasswordConfirmation = changePassword_newPasswordConfirmationJPF.getText();
                if (!(password.equals("") && newPassword.equals("") && newPasswordConfirmation.equals(""))) {
                    try {
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select * from user where name='" + userName + "'");
                        if (rs.next()) {
                            int result = JOptionPane.showConfirmDialog(null,"정말로 비밀번호를 변경하시겠습니까?",
                                    "Sign Out",JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.YES_OPTION) {
                                if (!password.equals(newPassword)) {
                                    if (password.equals(rs.getString("pw"))) {
                                        if (newPassword.equals(newPasswordConfirmation)) {
                                            pStmt = con.prepareStatement("update user set pw=? where name='" + userName + "'");
                                            pStmt.setString(1, newPassword);
                                            int cnt = pStmt.executeUpdate();
                                            pStmt.close();
                                            JOptionPane.showMessageDialog(null, "비밀번호 변경이 완료되었습니다. 로그인 창으로 이동합니다.");
                                            relDB();
                                            dispose();
                                            new Login();
                                        } else {
                                            JOptionPane.showMessageDialog(null, "새로운 비밀번호의 값이 일치하지 않습니다.");
                                        }
                                    } else {
                                        changePassword_passwordJPF.requestFocus();
                                        JOptionPane.showMessageDialog(null, "현재 비밀번호가 일치하지않습니다.");
                                    }
                                } else {
                                    changePassword_newPasswordJPF.requestFocus();
                                    JOptionPane.showMessageDialog(null, "현재 사용중인 비밀번호입니다.\n다른 비밀번호를 입력해주세요.");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "정보를 모두 입력해주세요.");
                }
            }
        });

        // 취소버튼 이벤트처리
        changePassword_cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanel2(proFile_panel);
            }
        });
    }
}
