package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SignUp extends Find {
    SignUp() {
        System.out.println("[ SignUp 생성자 호출 ]");
        setJPanel(signUp_panel,"#3B4752");

        setJLabel_back(signUp_frameLabel, Color.WHITE);
        setJLabel_font(signUp_signUpLabel, Color.WHITE,35);
        setJLabel_font(signUp_idLabel, Color.BLACK,35);
        setJLabel_font(signUp_pwLabel, Color.BLACK,35);
        setJLabel_font(signUp_confirmPW_Label, Color.BLACK,35);
        setJLabel_font(signUp_nameLabel, Color.BLACK,35);
        setJTextField(signUp_idTextField, Color.decode("#E3EFF8"),30,15);
        setJTextField(signUp_nameTextField, Color.decode("#E3EFF8"),30,5);
        setJPasswordField(signUp_pwJPF, Color.decode("#E3EFF8"),30,15);
        setJPasswordField(signUp_confirmPW_JPF, Color.decode("#E3EFF8"),30,15);
        setJBtn(signUp_checkBtn, Color.WHITE, Color.decode("#3498DB"),12);
        setJBtn(signUp_signUpBtn, Color.WHITE, Color.decode("#1ABC9C"),20);
        setJBtn(signUp_cancelBtn, Color.WHITE, Color.decode("#5E5E5E"),20);

        signUp_pwJPF.setEchoChar('*');
        signUp_confirmPW_JPF.setEchoChar('*');

        signUp_signUpLabel.setBounds(530,140,200,40);
        signUp_idLabel.setBounds(420,210,200,40);
        signUp_nameLabel.setBounds(420,510,200,40);
        signUp_idTextField.setBounds(420,260,360,50);
        signUp_pwLabel.setBounds(420,310,200,40);
        signUp_pwJPF.setBounds(420,360,360,50);
        signUp_confirmPW_Label.setBounds(420,410,300,40);
        signUp_confirmPW_JPF.setBounds(420,460,360,50);
        signUp_nameTextField.setBounds(420,560,360,50);
        signUp_checkBtn.setBounds(690,220,90,30);
        signUp_signUpBtn.setBounds(420,630,160,50);
        signUp_cancelBtn.setBounds(620,630,160,50);
        signUp_frameLabel.setBounds(400,200,400,500);

        signUp_panel.add(signUp_signUpLabel);
        signUp_panel.add(signUp_idLabel);
        signUp_panel.add(signUp_checkBtn);
        signUp_panel.add(signUp_idTextField);
        signUp_panel.add(signUp_pwLabel);
        signUp_panel.add(signUp_pwJPF);
        signUp_panel.add(signUp_confirmPW_Label);
        signUp_panel.add(signUp_confirmPW_JPF);
        signUp_panel.add(signUp_nameLabel);
        signUp_panel.add(signUp_nameTextField);
        signUp_panel.add(signUp_signUpBtn);
        signUp_panel.add(signUp_cancelBtn);
        signUp_panel.add(signUp_frameLabel);

        top(signUp_panel, signUp_topLabel, signUp_logoLabel, signUp_signInBtn);

        // 로그인창 이동
        signUp_signInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                confirmIdDuplication = false;
                changePanel(lgn_panel);
            }
        });

        // 아이디 중복확인
        signUp_checkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                confirmIdDuplication = false;
                signUp_id = signUp_idTextField.getText();
                if (!signUp_id.equals("")) {
                    try {
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select * from user where id='" + signUp_id +"'");
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(null, signUp_id + "는 사용가능한 아이디입니다.");
                            confirmIdDuplication = true;
                            confirmIdChange = signUp_id;
                        } else {
                            JOptionPane.showMessageDialog(null, signUp_id + "는 이미 존재합니다.");
                            confirmIdDuplication = false;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 회원가입 완료
        signUp_signUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    // 회원가입 텍스트필드값 가져오기
                    signUp_id = signUp_idTextField.getText();
                    signUp_pw = signUp_pwJPF.getText();
                    signUp_pwCheck = signUp_confirmPW_JPF.getText();
                    String signUp_name = signUp_nameTextField.getText();
                    // 모든 텍스트필드값이 비워져있지 않다면 다음줄 실행
                    if (!signUp_id.equals("") && !signUp_pw.equals("") && !signUp_pwCheck.equals("") && !signUp_name.equals("")) {
                        if (confirmIdDuplication) { // 아이디 중복확인을 했다면
                            if (signUp_pw.equals(signUp_pwCheck)) { // 비밀번호와 비밀번호 확인 값이 같다면
                                // 입력한 이름이 동일한 데이터가 DB에 있다면 가입 X
                                stmt = con.createStatement();
                                rs = stmt.executeQuery("select * from user where name='" + signUp_name + "'");
                                if (getCount(rs) > 0) {
                                    JOptionPane.showMessageDialog(null, signUp_name + "님은 이미 가입하셨습니다.");
                                } else { // 입력한 이름이 동일한 데이터가 DB에 없다면 가입 O
                                    String id = signUp_idTextField.getText();
                                    if (id.equals(confirmIdChange)) {
                                        pStmt = con.prepareStatement("insert into user (id, pw, name) value (?,?,?)");
                                        pStmt.setString(1, signUp_id);
                                        pStmt.setString(2, signUp_pw);
                                        pStmt.setString(3, signUp_name);
                                        pStmt.executeUpdate();
                                        resetIDX("user"); // idx 초기화
                                        JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
                                        confirmIdDuplication = false;
                                        confirmIdChange = "";
                                        signUp_idTextField.setText("");
                                        signUp_pwJPF.setText("");
                                        signUp_confirmPW_JPF.setText("");
                                        signUp_nameTextField.setText("");
                                        changePanel(lgn_panel);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "아이디 중복확인은 필수입니다.");
                                        confirmIdDuplication = false;
                                    }

                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "비빈번호가 동일하지 않습니다.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "아아디 중복확인은 필수입니다.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "정보를 모두 입력해주세요.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        signUp_cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmIdDuplication = false;
                signUp_idTextField.setText("");
                signUp_pwJPF.setText("");
                signUp_confirmPW_JPF.setText("");
                signUp_nameTextField.setText("");
                changePanel(lgn_panel);
            }
        });
    }
}
