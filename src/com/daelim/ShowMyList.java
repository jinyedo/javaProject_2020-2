package com.daelim;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class ShowMyList extends SetMyPost {
    ShowMyList(String userName) {
        super(userName);
        System.out.println("[ ShowMyList 생성자 호출 ]");
        try {
            setJPanel(showMyList_panel,"#3B4752");
            showMyList_panel.setPreferredSize(new Dimension(900,720));

            showMyList_table = new JTable(tableData("select * from list where author='" + userName + "'"));
            showMyList_scroll = new JScrollPane(showMyList_table);
            showMyList_scroll.getViewport().setBackground(Color.decode("#E0E0E0"));

            setJLabel_font(showMyList_logoLabel, Color.WHITE, 40);
            setJLabel_font(showMyList_searchLabel, Color.WHITE, 30);
            setJTextField(showMyList_searchTF, Color.WHITE,30,27);
            setJBtn(showMyList_searchBtn, Color.WHITE, Color.decode("#5E5E5E"),30);
            setJTable(showMyList_table);

            showMyList_logoLabel.setHorizontalAlignment(JLabel.CENTER);
            showMyList_searchLabel.setHorizontalAlignment(JLabel.RIGHT);

            showMyList_logoLabel.setBounds(300,50,300,40);
            showMyList_searchLabel.setBounds(140,140,150,40);
            showMyList_searchTF.setBounds(320,140,350,40);
            showMyList_searchBtn.setBounds(700,140,150,40);
            showMyList_scroll.setBounds(50,200,800,450);

            showMyList_panel.add(showMyList_logoLabel);
            showMyList_panel.add(showMyList_searchLabel);
            showMyList_panel.add(showMyList_searchTF);
            showMyList_panel.add(showMyList_searchBtn);
            showMyList_panel.add(showMyList_scroll);

            showMyList_table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent m) {
                    // 선택된 컬럼의 데이터 저장
                    int row = showMyList_table.getSelectedRow();
                    TableModel data = showMyList_table.getModel();
                    String selectTitle = (String) data.getValueAt(row,1);
                    String selectIdx = (String) data.getValueAt(row,0);
                    try {
                        // 저장된 데이터로 DB 조회
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select * from list where idx='" + selectIdx + "' and title='" + selectTitle + "'");
                        if (rs.next()) { // 조회된 데이터가 있을시
                            // 읽기 데이터 뿌려주기
                            setMyPost_titleTF.setText(rs.getString("title"));
                            setMyPost_memoTA.setText(rs.getString("memo"));
                            idx = rs.getString("idx");
                            changePanel3(setMyPost_panel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            showMyList_searchBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    try {
                        searchData(showMyList_table, showMyList_searchTF, userName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
