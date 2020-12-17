package com.daelim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Profile extends Read {
    Profile(String userName) {
        super(userName);
        System.out.println("[ Profile 생성자 호출 ]");
        setJPanel(proFile_panel,"#3B4752");
        proFile_panel.setPreferredSize(new Dimension(900,720));

        setJLabel_back(proFile_frameLabel, Color.BLACK);
        setJLabel_font(proFile_logoLabel, Color.WHITE, 40);
        setJLabel_font(proFile_nameLabel, Color.WHITE, 30);
        setJLabel_font(proFile_idLabel, Color.WHITE, 30);
        setJLabel_font(proFile_joinDateLabel, Color.WHITE, 30);
        setJTextField(proFile_nameTF, Color.WHITE, 30,27);
        setJTextField(proFile_idTF, Color.WHITE, 30,27);
        setJTextField(proFile_joinDateTF, Color.WHITE, 30,27);
        setJBtn(proFile_deleteAccountBtn, Color.WHITE, Color.decode("#C0392B"),20);
        setJBtn(proFile_exitBtn, Color.WHITE, Color.decode("#7F7F7F"),20);

        proFile_nameTF.setEditable(false);
        proFile_idTF.setEditable(false);
        proFile_joinDateTF.setEditable(false);

        proFile_logoLabel.setHorizontalAlignment(JLabel.CENTER);
        proFile_nameTF.setHorizontalAlignment(JLabel.CENTER);
        proFile_idTF.setHorizontalAlignment(JLabel.CENTER);
        proFile_joinDateTF.setHorizontalAlignment(JLabel.CENTER);

        proFile_logoLabel.setBounds(250,50,400,40);
        proFile_nameLabel.setBounds(100,220,100,30);
        proFile_nameTF.setBounds(250,215,550,40);
        proFile_idLabel.setBounds(100,305,100,30);
        proFile_idTF.setBounds(250,300,550,40);
        proFile_joinDateLabel.setBounds(100,385,100,30);
        proFile_joinDateTF.setBounds(250,390,550,40);
        proFile_deleteAccountBtn.setBounds(430,475,160,50);
        proFile_exitBtn.setBounds(640,475,160,50);
        proFile_frameLabel.setBounds(50,170,800,405);

        proFile_panel.add(proFile_logoLabel);
        proFile_panel.add(proFile_nameLabel);
        proFile_panel.add(proFile_nameTF);
        proFile_panel.add(proFile_idLabel);
        proFile_panel.add(proFile_idTF);
        proFile_panel.add(proFile_joinDateLabel);
        proFile_panel.add(proFile_joinDateTF);
        proFile_panel.add(proFile_deleteAccountBtn);
        proFile_panel.add(proFile_exitBtn);
        proFile_panel.add(proFile_frameLabel);

        // 회원정보 뿌려주기
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from user where name='" + userName + "'");
            if (rs.next()) {
                proFile_nameTF.setText(rs.getString("name"));
                proFile_idTF.setText(rs.getString("id"));
                proFile_joinDateTF.setText(rs.getString("joinDate").substring(0,11));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 화원탈퇴 버튼 이벤트처리
        proFile_deleteAccountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                try {
                    stmt = con.createStatement();
                    rs = stmt.executeQuery("select * from user where name='" + userName + "'");
                    if (rs.next()) {
                        try {
                            String pw = JOptionPane.showInputDialog("비밀번호를 입력해주세요.");
                            if (pw.equals(rs.getString("pw"))) {
                                int result = JOptionPane.showConfirmDialog(null, "정말로 계정을 삭제하시겠습니까?",
                                        "Sign Out", JOptionPane.YES_NO_OPTION);
                                if (result == JOptionPane.YES_OPTION) {
                                    // 회원정보 삭제
                                    pStmt = con.prepareStatement("delete from user where name='" + userName + "'");
                                    int cnt = pStmt.executeUpdate();
                                    pStmt.close();
                                    // 회원의 게시글 삭제
                                    pStmt = con.prepareStatement("delete from list where author='" + userName + "'");
                                    cnt = pStmt.executeUpdate();
                                    pStmt.close();
                                    // JTable 새로고침
                                    tableRefresh_all(userName);
                                    // user 테이블 idx 초기화
                                    resetIDX("user");
                                    JOptionPane.showMessageDialog(null, "정상적으로 회원 탈퇴가 완료되었습니다. 로그인 페이지로 이동합니다.");
                                    relDB();
                                    dispose();
                                    new Login();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "비밀번호가 올바르지않습니다.");
                            }
                        } catch (NullPointerException e) {
                            System.out.println("\n회원 탈퇴 취소\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 나가기 버튼 이벤트처리
        proFile_exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                leftPanelFontColor(Color.decode("#16A085"), Color.WHITE, Color.WHITE, Color.WHITE);
                leftPanel_panel.remove(leftPanel_changePasswordBtn);
                try {
                    tableRefresh_all(userName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                changePanel2(showList_panel);
            }
        });
    }
}
