package com.daelim;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ShowList extends ShowMyList {
    ShowList(String userName) {
        super(userName);
        System.out.println("[ ShowList 생성자 호출 ]");
        try {
            setJPanel(showList_panel,"#3B4752");
            showList_panel.setPreferredSize(new Dimension(900,720));
            panelName = "showListPanel";
            showList_table = new JTable(tableData("select * from list order by idx desc"));
            showList_scroll = new JScrollPane(showList_table);
            showList_scroll.getViewport().setBackground(Color.decode("#E0E0E0"));

            setJLabel_font(showList_logoLabel, Color.WHITE, 40);
            setJTextField(showList_searchTF, Color.WHITE,30,27);
            setComboBox(showList_comboBox, Color.WHITE, 30);
            setJComboBox_alignCenter(showList_comboBox);
            setJBtn(showList_searchBtn, Color.WHITE, Color.decode("#5E5E5E"),30);
            setJTable(showList_table);

            showList_logoLabel.setHorizontalAlignment(JLabel.CENTER);

            showList_logoLabel.setBounds(300,50,300,40);
            showList_comboBox.setBounds(140,140,150,40);
            showList_searchTF.setBounds(320,140,350,40);
            showList_searchBtn.setBounds(700,140,150,40);
            showList_scroll.setBounds(50,200,800,450);

            showList_panel.add(showList_logoLabel);
            showList_panel.add(showList_comboBox);
            showList_panel.add(showList_searchTF);
            showList_panel.add(showList_searchBtn);
            showList_panel.add(showList_scroll);

            // 글읽기
            showList_table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent m) {
                    // 선택된 컬럼의 데이터 저장
                    int row = showList_table.getSelectedRow();
                    TableModel data = showList_table.getModel();
                    String selectTitle = (String) data.getValueAt(row,1);
                    String selectIdx = (String) data.getValueAt(row,0);
                    try {
                        // 저장된 데이터로 DB 조회
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select * from list where idx='" + selectIdx + "' and title='" + selectTitle + "'");
                        if (rs.next()) { // 조회된 데이터가 있을시
                            // 조회수 +1
                            pStmt = con.prepareStatement("update list set viewcount=viewcount+1 where idx='" + selectIdx + "'");
                            int cnt = pStmt.executeUpdate();
                            int viewCount = Integer.parseInt(rs.getString("viewcount")) + 1;
                            // 읽기 데이터 뿌려주기
                            read_authorTF.setText(rs.getString("author"));
                            read_viewCountTF.setText(String.valueOf(viewCount));
                            read_titleTF.setText(rs.getString("title"));
                            read_memoTA.setText(rs.getString("memo"));
                            changePanel3(read_panel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            showList_searchBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    try {
                        searchData(showList_table, showList_searchTF, userName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // 창 닫았을 시 DB 연결 종료
            jf.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent w) {
                    relDB();
                    System.out.println("프로그램 종료");
                }
            });

            jf.add(showList_panel, BorderLayout.CENTER);
            setJFrame(jf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
