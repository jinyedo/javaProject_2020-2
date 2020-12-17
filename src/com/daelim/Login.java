package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Login extends SignUp {
    Login(){
        System.out.println("[ Login 생성자 호출 ]");
        setJPanel(lgn_panel,"#3B4752");

        setJLabel_back(lgn_frameLabel, Color.WHITE);
        setJLabel_font(lgn_textLabel, Color.WHITE,100);
        setJLabel_font(lgn_text2Label, Color.WHITE,70);
        setJLabel_font(lgn_idLabel, Color.BLACK,35);
        setJLabel_font(lgn_pwLabel, Color.BLACK,35);
        setJTextField(lgn_idTextField, Color.decode("#E3EFF8"),30,15);
        setJPasswordField(lgn_jPasswordField, Color.decode("#E3EFF8"),30,15);
        setJBtn(lgn_findBtn, Color.BLUE, Color.WHITE,20);
        setJBtn(lgn_signInBtn, Color.WHITE, Color.decode("#1ABC9C"),20);

        lgn_jPasswordField.setEchoChar('*');

        lgn_textLabel.setBounds(40,140,300,200);
        lgn_text2Label.setBounds(40, 300, 600, 150);
        lgn_idLabel.setBounds(720,225,200,40);
        lgn_pwLabel.setBounds(720,350,200,40);
        lgn_idTextField.setBounds(720,275,360,50);
        lgn_jPasswordField.setBounds(720,400,360,50);
        lgn_findBtn.setBounds(720,470,360,50);
        lgn_signInBtn.setBounds(720,550,360,50);
        lgn_frameLabel.setBounds(700,170,400,500);

        lgn_panel.add(lgn_textLabel);
        lgn_panel.add(lgn_text2Label);
        lgn_panel.add(lgn_idLabel);
        lgn_panel.add(lgn_pwLabel);
        lgn_panel.add(lgn_idTextField);
        lgn_panel.add(lgn_jPasswordField);
        lgn_panel.add(lgn_findBtn);
        lgn_panel.add(lgn_signInBtn);
        lgn_panel.add(lgn_frameLabel);

        top(lgn_panel, lgn_topLabel, lgn_logoLabel, lgn_signUpBtn);

        // 회원가입
        lgn_signUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanel(signUp_panel);
            }
        });

        // 아아디 비밀번호 찾기
        lgn_findBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanel(find_panel);
            }
        });

        // 로그인
        lgn_signInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    String id = lgn_idTextField.getText();
                    String pw = lgn_jPasswordField.getText();
                    if (!(id.equals("") && pw.equals(""))) {
                        if (id.equals("") && !pw.equals("")) {
                            lgn_idTextField.requestFocus();
                            JOptionPane.showMessageDialog(null, "아이디를 입력해주세요");
                        } else if (pw.equals("") && !id.equals("")) {
                            lgn_jPasswordField.requestFocus();
                            JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.");
                        } else {
                            // 아아디와 비밀번호가 모두 입력되었다면
                            if (!id.equals("") && !pw.equals("")) {
                                // 입력한 아이디 값과 user 테이블 id 컬럼에 동일한 값이 존재하는지 조회
                                stmt = con.createStatement();
                                rs = stmt.executeQuery("select * from user where id='" + id + "'");
                                // 값이 존재한다면
                                if (rs.next()) {
                                    // 조회된 데이터에 입력한 비밀번호 값과 동일한 값이 있는지 조회
                                    if (rs.getString("pw").equals(pw)) { // 값이 있다면
                                        // 입력한 아이디, 비밀번호 값과 동일한 값이 user 테이블에 있는지 조회
                                        rs = stmt.executeQuery("select * from user where id='" + id + "' and pw='" + pw + "'");
                                        if (rs.next()) { // 동일한 값이 있다면
                                            String name = rs.getString("name");
                                            dispose();
                                            relDB();
                                            System.out.println("로그인중...\n");
                                            new ShowList(name);
                                        }
                                    } else {
                                        lgn_jPasswordField.setText("");
                                        lgn_jPasswordField.requestFocus();
                                        JOptionPane.showMessageDialog(null, "비밀번호가 올바르지 않습니다.");
                                    }
                                } else {
                                    lgn_idTextField.setText("");
                                    lgn_jPasswordField.setText("");
                                    lgn_idTextField.requestFocus();
                                    JOptionPane.showMessageDialog(null, "\""+ id +"\"는 존재하지 않는 아이디입니다.");
                                }
                            }
                        }
                    } else {
                        lgn_idTextField.requestFocus();
                        JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 입력해주세요.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        add(lgn_panel);
        setJFrame(jf);

        // 창 닫았을 시 DB 연결 종료
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent w) {
                relDB();
                System.out.println("프로그램 종료");
            }
        });
    }

    public static void main(String[] args) {
        new Login();
    }
}
