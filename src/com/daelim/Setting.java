package com.daelim;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.*;

public class Setting extends JFrame {
/* ******************* DB ******************* */
    private static final String url = "jdbc:mysql://localhost:3306/java?serverTimezone=UTC";
    private static final String user = "root";
    private static final String pw = "911059";
    Connection con;
    Statement stmt;
    PreparedStatement pStmt;
    ResultSet rs;

    void jdbcLoad() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("!! JDBC ERROR. Driver Load Error");
            e.printStackTrace();
        }
    }

    void conDB() {
        try {
            con = DriverManager.getConnection(url, user, pw);
            System.out.println("DB 정상 연결 완료");
        } catch (SQLException e) {
            System.out.println("!! con ERROR");
            e.printStackTrace();
        }
    }

    void relDB() {
        try {
            if(con != null) {
                con.close();
                System.out.println("DB 정상 해제 완료\n");
            }
        } catch (SQLException e) {
            System.out.println("!! CLOSE ERROR");
            e.printStackTrace();
        }
    }

    // DB 중복값 확인
    int getCount(ResultSet rs) throws SQLException {
        int i = 0;
        while (rs.next()) {
            i++;
        }
        return i;
    }

    // idx 초기화
    void resetIDX(String tableName) throws SQLException {
        pStmt = con.prepareStatement("set @cnt = 0;");
        pStmt.executeUpdate();
        pStmt = con.prepareStatement("update " + tableName + " set " + tableName + ".idx = @cnt:=@cnt+1");
        pStmt.executeUpdate();
        pStmt.close();
    }

    // JTable 에 데이터 뿌리기
    DefaultTableModel tableData(String sql) throws SQLException {
        String[] header = new String[] {"번호", "글제목", "작성자", "작성일", "조회수"};
        Object[][] data = new Object[0][0];
        DefaultTableModel model = new DefaultTableModel(data, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pStmt = con.prepareStatement(sql);
        rs = pStmt.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getString("idx"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("write_date").substring(0,16),
                    rs.getString("viewcount")
            });
        }
        pStmt.close();
        return model;
    }

    // 게시글 검색 
    void searchData(JTable jTable, JTextField jTextField, String userName) throws Exception {
        // 테이블에 뿌려질 model 생성
        String[] header = new String[] {"번호", "글제목", "작성자", "작성일", "조회수"};
        Object[][] data = new Object[0][0];
        DefaultTableModel model = new DefaultTableModel(data, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String select = null; // 콤보박스 선택값 가져오기
        if (jTable == showList_table) {
            select = String.valueOf(showList_comboBox.getSelectedItem());
        } else if (jTable == showMyList_table) {
            select = "글제목";
        }
        String text = jTextField.getText(); // 텍스트필드 입력갑 가져오기
        stmt = con.createStatement();
        rs = stmt.executeQuery("select * from list");
        // 게시글이 존재한다면
        if (rs.next()) {
            if (text.equals("")) { // 검색란에 아무것도 입력하지 않고 검색 버튼 클릭시 전체 목록 띄우기
                tableRefresh_all(userName);
            } else {
                 if (select != null && select.equals("글제목")) { // 콤보박스 선택값이 글제목 이라면
                     // 글제목에 텍스트필드값과 동일한 데이터가 있다면
                     if (jTable == showList_table) {
                         rs = stmt.executeQuery("select * from list where title='" + text + "' order by idx desc");
                     } else if (jTable == showMyList_table) {
                         rs = stmt.executeQuery("select * from list where author='"+ userName + "' and title='" + text + "' order by idx desc");
                     }
                     while (rs.next()) { // 해당 데이터를 model 에 추가
                         model.addRow(new Object[]{
                                 rs.getString("idx"),
                                 rs.getString("title"),
                                 rs.getString("author"),
                                 rs.getString("write_date").substring(0,16),
                                 rs.getString("viewcount")
                         });
                     }

                     // 연관 게시글 출력 기능
                     Map<Integer,String> map = new HashMap<Integer, String>();
                     int i = 0;
                     if (jTable == showList_table) {
                         rs = stmt.executeQuery("select * from list order by idx desc");
                     } else if (jTable == showMyList_table) {
                         rs = stmt.executeQuery("select * from list where author='" + userName + "' order by idx desc");
                     }
                     while (rs.next()) {
                         // 연관 게시글 제목만 저장 - 입력값과 똑같은 게시글은 위에서 출력했기 때문에
                         if(rs.getString("title").contains(text) && !rs.getString("title").equals(text)) {
                             map.put(i, rs.getString("idx"));
                             i++;
                         }
                     }
                     Set<Integer> keySet = map.keySet();
                     Iterator<Integer> keyIterator = keySet.iterator();
                     if (map.size() > 0) {
                         while (keyIterator.hasNext()) {
                             Integer key = keyIterator.next();
                             String value = map.get(key);
                             if (jTable == showList_table) {
                                 rs = stmt.executeQuery("select * from list where idx='" + value + "'");
                             } else if (jTable == showMyList_table) {
                                 rs = stmt.executeQuery("select * from list where author='" + userName + "' and idx='" + value + "'");
                             }
                             if (rs.next()) {
                                 model.addRow(new Object[]{
                                         rs.getString("idx"),
                                         rs.getString("title"),
                                         rs.getString("author"),
                                         rs.getString("write_date").substring(0, 16),
                                         rs.getString("viewcount")
                                 });
                             }
                         }
                     }
                     jTable.setModel(model);
                     setJTable(jTable);
                 // 콤보박스 선택값이 작성자 라면
                 } else if (select != null && select.equals("작성자")) {
                     rs = stmt.executeQuery("select * from list where author='" + text + "'");
                     if (rs.next()) {
                         rs = stmt.executeQuery("select * from list where author='" + text + "' order by idx desc");
                         while (rs.next()) {
                             model.addRow(new Object[]{
                                     rs.getString("idx"),
                                     rs.getString("title"),
                                     rs.getString("author"),
                                     rs.getString("write_date").substring(0, 16),
                                     rs.getString("viewcount")
                             });
                         }
                     }
                     jTable.setModel(model);
                     setJTable(jTable);
                 }
            }
        } else {
            JOptionPane.showMessageDialog(null, "현재 작성된 게시글이 없습니다.");
        }
    }

    // 테이블 새로고침
    void tableRefresh_all(String userName) throws SQLException {
        tableRefresh_showList();
        tableRefresh_showMyList(userName);
    }
    void tableRefresh_showList() throws SQLException {
        resetIDX("list");
        showList_table.setModel(tableData("select * from list order by idx desc"));
        setJTable(showList_table);
    }
    void tableRefresh_showMyList(String userName) throws SQLException {
        resetIDX("list");
        showMyList_table.setModel(tableData("select * from list where author='" + userName + "' order by idx desc"));
        setJTable(showMyList_table);
    }

/* ******************* Swing ******************* */
    String[] select_jComboBox = {"글제목","작성자"};
    String panelName;
    JFrame jf;

    // Login
    JPanel lgn_panel = new JPanel();
    JLabel lgn_topLabel = new JLabel(),
           lgn_frameLabel = new JLabel(),
           lgn_logoLabel = new JLabel("Java"),
           lgn_textLabel = new JLabel("JAVA"),
           lgn_text2Label = new JLabel("201740233 진예도"),
           lgn_idLabel = new JLabel("아이디"),
           lgn_pwLabel = new JLabel("비밀번호");
    JButton lgn_signUpBtn = new JButton("회원가입"),
            lgn_signInBtn = new JButton("로그인"),
            lgn_findBtn = new JButton("아이디 / 비밀번호 찾기");
    JTextField lgn_idTextField = new JTextField();
    JPasswordField lgn_jPasswordField = new JPasswordField();

    // SignUp
    JPanel signUp_panel = new JPanel();
    JLabel signUp_topLabel = new JLabel(),
           signUp_frameLabel = new JLabel(),
           signUp_logoLabel = new JLabel("Java"),
           signUp_idLabel = new JLabel("아이디"),
           signUp_pwLabel = new JLabel("비밀번호"),
           signUp_confirmPW_Label = new JLabel("비밀번호 확인"),
           signUp_nameLabel = new JLabel("이름"),
           signUp_signUpLabel = new JLabel("회원가입") ;
    JButton signUp_signInBtn = new JButton("로그인"),
            signUp_signUpBtn = new JButton("가입완료"),
            signUp_cancelBtn = new JButton("취소"),
            signUp_checkBtn = new JButton("중복확인");
    JTextField signUp_idTextField = new JTextField(),
               signUp_nameTextField = new JTextField();
    JPasswordField signUp_pwJPF = new JPasswordField(),
                   signUp_confirmPW_JPF = new JPasswordField();
    String signUp_id,
           signUp_pw,
           signUp_pwCheck,
           confirmIdChange;
    boolean confirmIdDuplication;

    // Find
    JPanel find_panel = new JPanel();
    JLabel find_topLabel = new JLabel(),
           find_logoLabel = new JLabel("Java"),
           find_findID_Label = new JLabel("아이디 찾기"),
           find_findPW_Label = new JLabel("비밀번호 찾기"),
           find_findID_nameLabel = new JLabel("이름"),
           find_findPW_nameLabel = new JLabel("이름"),
           find_findPW_idLabel = new JLabel("아이디"),
           find_findID_frameLabel = new JLabel(),
           find_findPW_frameLabel = new JLabel();
    JButton find_signInBtn = new JButton("로그인"),
            find_findID_Btn = new JButton("아이디 찾기"),
            find_findPW_Btn = new JButton("비밀번호 찾기");
    JTextField find_findID_nameTF = new JTextField(),
               find_findPW_nameTF = new JTextField(),
               find_findPW_idTF = new JTextField();
    String find_findID_name,
           find_findPW_id,
           find_findPW_name;

    // TopPanel
    JPanel topPanel_panel = new JPanel();
    JLabel topPanel_frameLabel = new JLabel(),
            topPanel_logoLabel = new JLabel("Java");
    JButton topPanel_signOutBtn = new JButton("로그아웃");

    // LeftPanel
    JPanel leftPanel_panel = new JPanel();
    JLabel leftPanel_userLabel;
    JButton leftPanel_listBtn = new JButton("게시글 리스트"),
            leftPanel_myListBtn = new JButton("작성한 게시글"),
            leftPanel_writeBtn = new JButton("게시글 작성"),
            leftPanel_profileBtn = new JButton("회원 정보"),
            leftPanel_changePasswordBtn = new JButton("- 비밀번호 변경");

    // Write
    JPanel write_panel = new JPanel();
    JLabel write_logoLabel = new JLabel("게시글 작성"),
           write_frameLabel = new JLabel(),
           write_titleLabel = new JLabel("글제목"),
           write_memoLabel = new JLabel("내용");
    JTextField write_titleTF = new JTextField();
    JTextArea write_memoTA = new JTextArea();
    JButton write_saveBtn = new JButton("작성 완료"),
            write_cancelBtn = new JButton("취소");

    // Read
    JPanel read_panel = new JPanel();
    JLabel read_frameLabel = new JLabel(),
           read_titleLabel = new JLabel("글제목"),
           read_authorLabel = new JLabel("작성자"),
           read_viewCountLabel = new JLabel("조회수"),
           read_memoLabel = new JLabel("내용");
    JTextField read_titleTF = new JTextField(),
               read_viewCountTF = new JTextField(),
               read_authorTF = new JTextField();
    JTextArea read_memoTA = new JTextArea();
    JButton read_exitBtn = new JButton("나가기");

    // SetMyPost
    JPanel setMyPost_panel = new JPanel();
    JLabel setMyPost_logoLabel = new JLabel("게시글 수정 및 삭제"),
           setMyPost_frameLabel = new JLabel(),
           setMyPost_titleLabel = new JLabel("글제목"),
           setMyPost_memoLabel = new JLabel("내용");
    JTextField setMyPost_titleTF = new JTextField();
    JTextArea setMyPost_memoTA = new JTextArea();
    JButton setMyPost_saveBtn = new JButton("수정"),
            setMyPost_deleteBtn = new JButton("삭제"),
            setMyPost_cancelBtn = new JButton("취소");

    // ShowList
    JPanel showList_panel = new JPanel();
    JLabel showList_logoLabel = new JLabel("게시글");
    JTextField showList_searchTF = new JTextField();
    JButton showList_searchBtn = new JButton("검색");
    JComboBox<String> showList_comboBox = new JComboBox<String>(select_jComboBox);
    JTable showList_table;
    JScrollPane showList_scroll;

    // ShowMyList
    JPanel showMyList_panel = new JPanel();
    JLabel showMyList_logoLabel = new JLabel("작성한 게시글"),
           showMyList_searchLabel = new JLabel("글제목:");
    JTextField showMyList_searchTF = new JTextField();
    JButton showMyList_searchBtn = new JButton("검색");
    JTable showMyList_table;
    JScrollPane showMyList_scroll;
    String idx;

    // ProFile
    JPanel proFile_panel = new JPanel();
    JLabel proFile_logoLabel = new JLabel("회원정보"),
           proFile_nameLabel = new JLabel("이름"),
           proFile_idLabel = new JLabel("아이디"),
           proFile_joinDateLabel = new JLabel("가입일"),
           proFile_frameLabel = new JLabel();
    JTextField proFile_nameTF = new JTextField(),
               proFile_idTF = new JTextField(),
               proFile_joinDateTF = new JTextField();
    JButton proFile_deleteAccountBtn = new JButton("회원탈퇴"),
            proFile_exitBtn = new JButton("나가기");

    // ChangePassword
    JPanel changePassword_panel = new JPanel();
    JLabel changePassword_logoLabel = new JLabel("비밀번호 변경"),
           changePassword_passwordLabel = new JLabel("현재 비밀번호"),
           changePassword_newPasswordLabel = new JLabel("새 비밀번호"),
           changePassword_newPasswordConfirmationLabel = new JLabel("새 비밀번호 확인"),
           changePassword_frameLabel = new JLabel();
    JPasswordField changePassword_passwordJPF = new JPasswordField(),
                   changePassword_newPasswordJPF = new JPasswordField(),
                   changePassword_newPasswordConfirmationJPF = new JPasswordField();
    JButton changePassword_saveBtn = new JButton("변경 완료"),
            changePassword_cancelBtn = new JButton("취소");

    // JComboBox 가운데정렬
    void setJComboBox_alignCenter(JComboBox<String> jComboBox) {
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        jComboBox.setRenderer(listRenderer);
    }

    // JComboBox 설정
    void setComboBox(JComboBox<String> jComboBox, Color backgroundColor, int fontSize) {
        jComboBox.setBackground(backgroundColor); // 배경색 설정
        jComboBox.setFont(new Font("나눔고딕", Font.BOLD, fontSize)); // 폰트크기 설정정
    }

    // 로그인전 상단바
    void top(JPanel jPanel, JLabel topLabel, JLabel logoLabel, JButton btn) {
        setJLabel_back(topLabel, Color.BLACK);
        setJLabel_font(logoLabel, Color.WHITE,35);
        setJBtn(btn, Color.WHITE, Color.BLACK,20);
        topLabel.setBounds(0,0,1200,80);
        logoLabel.setBounds(20,0,150,80);
        btn.setBounds(1000,10,150,60);
        jPanel.add(btn);
        jPanel.add(logoLabel);
        jPanel.add(topLabel);
    }

    // 패널이동
    void changePanel(JPanel jPanel) {
        lgn_idTextField.setText("");
        lgn_jPasswordField.setText("");
        signUp_idTextField.setText("");
        signUp_pwJPF.setText("");
        signUp_confirmPW_JPF.setText("");
        signUp_nameTextField.setText("");
        find_findID_nameTF.setText("");
        find_findPW_nameTF.setText("");
        find_findPW_idTF.setText("");
        jf.getContentPane().removeAll();
        jf.getContentPane().add(jPanel);
        jf.revalidate();
        jf.repaint();
    }
    void changePanel2(JPanel add) {
        jf.getContentPane().removeAll();
        showList_searchTF.setText("");
        showMyList_searchTF.setText("");
        write_titleTF.setText("");
        write_memoTA.setText("");
        changePassword_passwordJPF.setText("");
        changePassword_newPasswordJPF.setText("");
        changePassword_newPasswordConfirmationJPF.setText("");
        jf.getContentPane().add(add, BorderLayout.CENTER);
        jf.getContentPane().add(topPanel_panel, BorderLayout.NORTH);
        jf.getContentPane().add(leftPanel_panel, BorderLayout.WEST);
        jf.revalidate();
        jf.repaint();
    }
    void changePanel3(JPanel add) {
        jf.getContentPane().removeAll();
        jf.getContentPane().add(add, BorderLayout.CENTER);
        jf.getContentPane().add(topPanel_panel, BorderLayout.NORTH);
        jf.getContentPane().add(leftPanel_panel, BorderLayout.WEST);
        jf.revalidate();
        jf.repaint();
    }

    // 좌측 메뉴 폰트색 설정
    void leftPanelFontColor(Color list, Color myList, Color write, Color proFile) {
        leftPanel_listBtn.setForeground(list);
        leftPanel_myListBtn.setForeground(myList);
        leftPanel_writeBtn.setForeground(write);
        leftPanel_profileBtn.setForeground(proFile);
    }

    // JTable 설정
    void setJTable(JTable jTable) {
        jTable.setBackground(Color.decode("#E0E0E0"));
        jTable.setFont(new Font("나눔고딕", Font.BOLD ,20));
        // 컬럼 height 크기설정
        jTable.setRowHeight(50);
        // 컬럼 width 크기설정
        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(270);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        jTable.getColumnModel().getColumn(3).setPreferredWidth(210);
        jTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        // 가운데 정렬
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel tcm = jTable.getColumnModel();
        for (int i = 0; i < 5; i++) {
            tcm.getColumn(i).setCellRenderer(dtcr);
        }
        // 헤더 설정
        JTableHeader header = jTable.getTableHeader();
        header.setPreferredSize(new Dimension(0, 50));
        header.setFont(new Font("Han Sans", Font.BOLD ,25));
        header.setBackground(Color.decode("#EAEAEA"));
    }

    // JFrame 설정
    void setJFrame(JFrame JFrame) {
        JFrame.setTitle("201740233"); // 창의 타이틀
        JFrame.setSize(1200, 800); // 프레임의 크기
        JFrame.setResizable(false); // 창의 크기를 변경하지 못하게
        JFrame.setLocationRelativeTo(null); // 창이 가운데 나오게
        JFrame.setVisible(true); // 창이 보이게
        JFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // JFrame 이 정상적으로 종료되게
    }

    // JPanel 레이아웃, 배경색 설정 (JPanel, background color)
    void setJPanel(JPanel jPanel, String color) {
        jPanel.setLayout(null);
        jPanel.setBackground(Color.decode(color));
    }

    // JButton 폰트색상 및 배경색 설정 (JButton, font color, background color, fontSize)
    void setJBtn(JButton jBtn, Color fontColor, Color backgroundColor, int fontSize) {
        jBtn.setFont(new Font("나눔고딕", Font.BOLD,fontSize)); // 폰트크기 설정
        jBtn.setForeground(fontColor); // 폰트색 설정
        jBtn.setBackground(backgroundColor); // 배경색 설정
        jBtn.setBorderPainted(false); // 버튼 테두리 삭제
        jBtn.setFocusPainted(false); // 버튼 클릭시 테두리 삭제
    }

    // JButton 투명설정
    void setJBtn_Transparent(JButton jBtn, Color fontColor, int fontSize) {
        jBtn.setContentAreaFilled(false);; // 투명
        jBtn.setFont(new Font("나눔고딕", Font.BOLD,fontSize)); // 폰트크기 설정
        jBtn.setForeground(fontColor); // 폰트색 설정
        jBtn.setBorderPainted(false); // 버튼 테두리 삭제
        jBtn.setFocusPainted(false); // 버튼 클릭시 테두리 삭제
    }

    // JLabel 배경색 설정 (JLabel, background color)
    void setJLabel_back(JLabel jLabel, Color backgroundColor) {
        jLabel.setOpaque(true);
        jLabel.setBackground(backgroundColor);
    }

    // JLabel 폰트크기, 색 설정 (JLabel, fontColor, fontSize)
    void setJLabel_font(JLabel jLabel, Color fontColor, int fontSize) {
        jLabel.setForeground(fontColor);
        jLabel.setFont(new Font("나눔고딕", Font.BOLD, fontSize));
    }

    // JTextField 설정
    void setJTextField(JTextField jTextField, Color backgroundColor, int fontSize, int maxLength) {
        jTextField.setBackground(backgroundColor); // 배경색 설정
        jTextField.setFont(new Font("나눔고딕", Font.BOLD, fontSize)); // 폰트크기 설정정
        jTextField_maxLength(jTextField, maxLength);
    }

    void setJPasswordField(JPasswordField jPasswordField, Color backgroundColor, int fontSize, int maxLength) {
        jPasswordField.setBackground(backgroundColor); // 배경색 설정
        jPasswordField.setFont(new Font("나눔고딕", Font.BOLD, fontSize)); // 폰트크기 설정정
        jPasswordField_maxLength(jPasswordField, maxLength);
    }

    void setJTextArea(JTextArea jTextArea, Color backgroundColor, int fontSize, int maxLength) {
        jTextArea.setBackground(backgroundColor);
        jTextArea.setFont(new Font("나눔고딕", Font.BOLD, fontSize));
        jTextArea.setLineWrap(true);
        jTextArea_maxLength(jTextArea, maxLength);
    }

    // JTextArea 글자수 제한
    void jTextArea_maxLength(JTextArea jTextArea, int max) {
        jTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(jTextArea.getText().length() > max) e.consume();
            }
        });
    }

    // JTextField 글자수 제한
    void jTextField_maxLength(JTextField jTextField, int max) {
        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(jTextField.getText().length() > max) e.consume();
            }
        });
    }

    void jPasswordField_maxLength(JPasswordField jPasswordField, int max) {
        jPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(jPasswordField.getText().length() > max) e.consume();
            }
        });
    }

    // 로그아웃
    void signOut() {
        int result = JOptionPane.showConfirmDialog(null,"정말로 로그아웃 하시겠습니까?",
                "Sign Out",JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            relDB();
            dispose();
            System.out.println("로그아웃\n");
            new Login();
        }
    }
}
