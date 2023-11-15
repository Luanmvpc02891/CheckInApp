/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.formdev.flatlaf.FlatClientProperties;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nhutin.component.TableGradientCell;
import com.nhutin.dao.EventsDao;
import com.nhutin.helper.DialogHelper;
import com.nhutin.model.Events;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.jdatepicker.JDatePanel;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.UtilDateModel;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author User
 */
public class Menu extends javax.swing.JFrame {

    private JXDatePicker dateTimePicker;
    int x = 210;    //chieu rong
    int y = 600;    //chieu cao

    /**
     * Creates new form Menu
     */
    public Menu() throws SQLException {

        initComponents();
        cardTrangChu.setVisible(true);
        cardTaiKhoan.setVisible(false);
        jplSlideMenu.setSize(210, 600);
        initDateTimePicker();
        initTable();
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    private byte[] getQRCodeImageByteArray(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }

    private void initDateTimePicker() {
        dateTimePicker = new JXDatePicker();
        dateTimePicker.setFormats(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
        dateTimePicker.setBounds(445, 55, 200, 30);
        cardTrangChu.add(dateTimePicker);
    }

    public void openMenu() {
        jplSlideMenu.setSize(x, y);
        if (x == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i <= 210; i++) {
                            jplSlideMenu.setSize(i, y);
                            Thread.sleep(1);
                        }
                    } catch (Exception e) {
                    }
                }
            }).start();
            x = 210;
        }
    }

    public void closeMenu() {
        jplSlideMenu.setSize(x, y);
        if (x == 210) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 210; i >= 0; i--) {
                            jplSlideMenu.setSize(i, y);
                            Thread.sleep(2);
                        }
                    } catch (Exception e) {
                    }
                }
            }).start();
            x = 0;
        }
    }
    EventsDao dao = new EventsDao();

    public void initTable() throws SQLException {

        DefaultTableModel model = (DefaultTableModel) tableEvent.getModel();
        model.setRowCount(0);
        try {
            List<Events> list = dao.getAllEvents();

            for (Events events : list) {
                Object[] row = {
                    events.getEventID(),
                    events.getEventName(),
                    events.getLocation(),
                    events.getEventDate(),
                    events.getStartTime(),
                    events.getEndTime()};

                model.addRow(row);
            }
        } catch (Exception e) {
            System.out.println(e);
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
    int index;

    void edit() {
        try {
            int id = (int) tableEvent.getValueAt(this.index, 0);
            Events model = dao.getEventById(id);

            if (model != null) {
                txtid.setText(String.valueOf(model.getEventID()));
                txtname.setText(model.getEventName());
                txtLocation.setText(model.getLocation());
                txtStartDate.setText(model.getLocation());
                dateTimePicker.setDate(model.getEventDate());
                txtStartDate.setText(String.valueOf(model.getStartTime()));
                txtEndDate.setText(String.valueOf(model.getEndTime()));
            }
        } catch (Exception e) {
            System.out.println(e);
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void clear() {
        txtid.setText("");
        txtname.setText("");
        txtStartDate.setText("");
        dateTimePicker.setDate(null);
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtLocation.setText("");
    }

    void insert() throws ParseException, SQLException {
        try {
            // Lấy giá trị ngày giờ từ JXDatePicker
            Date selectedDate = dateTimePicker.getDate();

            String fileName = txtname.getText(); // Lấy nội dung từ trường txtname
            String QR_CODE_IMAGE_PATH = "./" + fileName + ".png";

            // Định dạng ngày trong chuỗi
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(selectedDate);

            String dateString1 = txtStartDate.getText();
            String dateString2 = txtEndDate.getText();

            // Định dạng ngày trong chuỗi
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            // Ép kiểu từ chuỗi sang LocalDateTime
            LocalDateTime parsedDateTime1 = LocalDateTime.parse(dateString1, formatter);
            LocalDateTime parsedDateTime2 = LocalDateTime.parse(dateString2, formatter);

            // Định dạng ngày trong chuỗi
            Date date = dateFormat.parse(dateString);

            Events event = new Events();
            event.setEventName(txtname.getText());
            event.setLocation(txtLocation.getText());
            event.setEventDate(date);
            event.setStartTime(parsedDateTime1);
            event.setEndTime(parsedDateTime2);

            generateQRCodeImage(txtname.getText(), 350, 350, QR_CODE_IMAGE_PATH);

            dao.addEvent(event);
            DialogHelper.alert(this, "Thêm thành công!");
            clear();
            initTable();
        } catch (WriterException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateEvent() throws SQLException {
        // Lấy giá trị ngày giờ từ JXDatePicker
        Date selectedDate = dateTimePicker.getDate();

        // Định dạng ngày trong chuỗi
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(selectedDate);

        String dateString1 = txtStartDate.getText();
        String dateString2 = txtEndDate.getText();

        // Định dạng ngày trong chuỗi
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Ép kiểu từ chuỗi sang LocalDateTime
        LocalDateTime parsedDateTime1 = LocalDateTime.parse(dateString1, formatter);
        LocalDateTime parsedDateTime2 = LocalDateTime.parse(dateString2, formatter);

        // Định dạng ngày trong chuỗi
        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu có
            return;
        }

        Events event = new Events();
        event.setEventName(txtname.getText());
        event.setLocation(txtStartDate.getText());
        event.setEventDate(date);
        event.setStartTime(parsedDateTime1);
        event.setEndTime(parsedDateTime2);
        event.setEventID(Integer.parseInt(txtid.getText()));
        System.out.println("Id là: " + txtid.getText());

        // Gọi hàm updateEvent thực hiện cập nhật trong CSDL
        dao.updateEvent(event);

        DialogHelper.alert(this, "Cập nhật thành công!");
        clear();
        initTable();
    }

    public void deleteEvent() throws SQLException {
        // Lấy giá trị từ trường nhập txtid
        String eventIdString = txtid.getText();

        // Kiểm tra xem txtid có giá trị không
        if (eventIdString.isEmpty()) {
            DialogHelper.alert(this, "Vui lòng nhập ID sự kiện cần xóa!");
            return;
        }

        try {
            // Chuyển đổi txtid sang kiểu dữ liệu phù hợp (ví dụ: Integer)
            int eventId = Integer.parseInt(eventIdString);

            // Gọi hàm xóa sự kiện từ lớp DAO
            dao.deleteEvent(eventId);

            DialogHelper.alert(this, "Xóa thành công!");
            clear();
            initTable();
        } catch (NumberFormatException e) {
            DialogHelper.alert(this, "ID sự kiện phải là một số nguyên!");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jplSlideMenu = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblCloseMenu = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTrangChu = new javax.swing.JLabel();
        lblTaiKhoan = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jpllMenuBar = new javax.swing.JPanel();
        lblOpenMenu = new javax.swing.JLabel();
        jplTitle = new javax.swing.JPanel();
        jplMain = new javax.swing.JPanel();
        cardTrangChu = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtEndDate = new javax.swing.JTextField();
        btnThem = new com.nhutin.component.Button();
        btnMoi = new com.nhutin.component.Button();
        button2 = new com.nhutin.component.Button();
        button3 = new com.nhutin.component.Button();
        button1 = new com.nhutin.component.Button();
        txtid = new com.nhutin.component.TextField();
        txtname = new com.nhutin.component.TextField();
        txtStartDate = new com.nhutin.component.TextField();
        txtLocation = new com.nhutin.component.TextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableEvent = new javax.swing.JTable();
        btnquet = new com.nhutin.component.Button();
        cardTaiKhoan = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnMoi1 = new com.nhutin.component.Button();
        btnXoa = new com.nhutin.component.Button();
        button5 = new com.nhutin.component.Button();
        btnSua = new com.nhutin.component.Button();
        txtparticipantID = new com.nhutin.component.TextField();
        txtlastName = new com.nhutin.component.TextField();
        txtfirstName = new com.nhutin.component.TextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        btnThem1 = new com.nhutin.component.Button();
        btnquet1 = new com.nhutin.component.Button();
        txtemail = new com.nhutin.component.TextField();
        cboEventId = new com.motel.swing.ComboBoxSuggestion();
        cardEvent = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cardUser = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jplSlideMenu.setBackground(new java.awt.Color(255, 255, 255));
        jplSlideMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jplSlideMenu.setPreferredSize(new java.awt.Dimension(190, 590));
        jplSlideMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/anh-2.jpg"))); // NOI18N
        jLabel3.setFocusCycleRoot(true);

        jLabel4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 2, 14)); // NOI18N
        jLabel4.setText("Admin");

        lblCloseMenu.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblCloseMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCloseMenu.setText("X");
        lblCloseMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(67, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(lblCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(78, 78, 78))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jplSlideMenu.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 150));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Thống Kê");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jplSlideMenu.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 210, 30));

        lblTrangChu.setBackground(new java.awt.Color(255, 255, 255));
        lblTrangChu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblTrangChu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTrangChu.setText("Trang Chủ");
        lblTrangChu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTrangChuMouseClicked(evt);
            }
        });
        jplSlideMenu.add(lblTrangChu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 210, 30));

        lblTaiKhoan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblTaiKhoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTaiKhoan.setText("Tài Khoản");
        lblTaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTaiKhoanMouseClicked(evt);
            }
        });
        jplSlideMenu.add(lblTaiKhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 210, 30));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Event");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jplSlideMenu.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 210, 30));
        jplSlideMenu.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 452, 210, 10));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Đăng Xuất");
        jplSlideMenu.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, 210, 30));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Giới Thiệu");
        jplSlideMenu.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 210, 30));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Trợ Giúp");
        jplSlideMenu.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 210, 30));

        jPanel1.add(jplSlideMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 0, 600));

        jpllMenuBar.setBackground(new java.awt.Color(255, 255, 255));

        lblOpenMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/menu.png"))); // NOI18N
        lblOpenMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOpenMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpllMenuBarLayout = new javax.swing.GroupLayout(jpllMenuBar);
        jpllMenuBar.setLayout(jpllMenuBarLayout);
        jpllMenuBarLayout.setHorizontalGroup(
            jpllMenuBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpllMenuBarLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblOpenMenu)
                .addContainerGap(890, Short.MAX_VALUE))
        );
        jpllMenuBarLayout.setVerticalGroup(
            jpllMenuBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpllMenuBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOpenMenu)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel1.add(jpllMenuBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 940, 60));

        jplTitle.setBackground(new java.awt.Color(255, 204, 0));

        javax.swing.GroupLayout jplTitleLayout = new javax.swing.GroupLayout(jplTitle);
        jplTitle.setLayout(jplTitleLayout);
        jplTitleLayout.setHorizontalGroup(
            jplTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
        );
        jplTitleLayout.setVerticalGroup(
            jplTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel1.add(jplTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 940, 30));

        jplMain.setBackground(new java.awt.Color(255, 255, 255));
        jplMain.setLayout(new java.awt.CardLayout());

        cardTrangChu.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel12.setText("TRANG CHỦ");

        txtEndDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEndDateActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(51, 255, 51));
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnMoi.setBackground(new java.awt.Color(51, 51, 255));
        btnMoi.setForeground(new java.awt.Color(255, 255, 255));
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        button2.setBackground(new java.awt.Color(255, 51, 51));
        button2.setForeground(new java.awt.Color(255, 255, 255));
        button2.setText("Xóa");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        button3.setText("In");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        button1.setBackground(new java.awt.Color(255, 153, 51));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Sửa");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        txtid.setLabelText("ID");
        txtid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidActionPerformed(evt);
            }
        });

        txtname.setLabelText("Event Name");
        txtname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnameActionPerformed(evt);
            }
        });

        txtStartDate.setLabelText("StartDate");
        txtStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStartDateActionPerformed(evt);
            }
        });

        txtLocation.setLabelText("Location");
        txtLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocationActionPerformed(evt);
            }
        });

        tableEvent.setForeground(new java.awt.Color(0, 0, 51));
        tableEvent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id", "EventName", "Location", "EventDate", "StartDay", "EndDate"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableEvent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEventMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableEvent);

        btnquet.setText("Quét mã");
        btnquet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnquetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardTrangChuLayout = new javax.swing.GroupLayout(cardTrangChu);
        cardTrangChu.setLayout(cardTrangChuLayout);
        cardTrangChuLayout.setHorizontalGroup(
            cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTrangChuLayout.createSequentialGroup()
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardTrangChuLayout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cardTrangChuLayout.createSequentialGroup()
                                .addGap(199, 199, 199)
                                .addComponent(jLabel12))
                            .addGroup(cardTrangChuLayout.createSequentialGroup()
                                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnquet, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(cardTrangChuLayout.createSequentialGroup()
                                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtname, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                    .addComponent(txtid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(125, 125, 125)
                                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(cardTrangChuLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(185, Short.MAX_VALUE))
        );
        cardTrangChuLayout.setVerticalGroup(
            cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTrangChuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cardTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnquet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );

        jplMain.add(cardTrangChu, "card2");

        cardTaiKhoan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Tài Khoản");

        btnMoi1.setBackground(new java.awt.Color(51, 51, 255));
        btnMoi1.setForeground(new java.awt.Color(255, 255, 255));
        btnMoi1.setText("Mới");
        btnMoi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoi1ActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 51, 51));
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        button5.setText("In");
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 153, 51));
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        txtparticipantID.setLabelText("ID");
        txtparticipantID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtparticipantIDActionPerformed(evt);
            }
        });

        txtlastName.setLabelText("lastName");
        txtlastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtlastNameActionPerformed(evt);
            }
        });

        txtfirstName.setLabelText("firstName");
        txtfirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtfirstNameActionPerformed(evt);
            }
        });

        tblUser.setForeground(new java.awt.Color(0, 0, 51));
        tblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id", "EventName", "firstName", "lastName", "Email", "Code"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUserMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblUser);

        btnThem1.setBackground(new java.awt.Color(51, 255, 51));
        btnThem1.setForeground(new java.awt.Color(255, 255, 255));
        btnThem1.setText("Thêm");
        btnThem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem1ActionPerformed(evt);
            }
        });

        btnquet1.setText("Quét mã");
        btnquet1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnquet1ActionPerformed(evt);
            }
        });

        txtemail.setLabelText("email");
        txtemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtemailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardTaiKhoanLayout = new javax.swing.GroupLayout(cardTaiKhoan);
        cardTaiKhoan.setLayout(cardTaiKhoanLayout);
        cardTaiKhoanLayout.setHorizontalGroup(
            cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                .addGap(409, 409, 409)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardTaiKhoanLayout.createSequentialGroup()
                .addContainerGap(146, Short.MAX_VALUE)
                .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardTaiKhoanLayout.createSequentialGroup()
                        .addComponent(cboEventId, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116)
                        .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtlastName, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(178, 178, 178))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardTaiKhoanLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(138, 138, 138))))
            .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                    .addGap(194, 194, 194)
                    .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                            .addComponent(btnThem1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(17, 17, 17)
                            .addComponent(btnMoi1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnquet1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtfirstName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                            .addComponent(txtparticipantID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap(218, Short.MAX_VALUE)))
        );
        cardTaiKhoanLayout.setVerticalGroup(
            cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtlastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboEventId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(135, 135, 135)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
            .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(cardTaiKhoanLayout.createSequentialGroup()
                    .addGap(61, 61, 61)
                    .addComponent(txtparticipantID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                    .addComponent(txtfirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(cardTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnThem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnMoi1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnquet1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(264, 264, 264)))
        );

        jplMain.add(cardTaiKhoan, "card3");

        cardEvent.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setText("EVENT");

        javax.swing.GroupLayout cardEventLayout = new javax.swing.GroupLayout(cardEvent);
        cardEvent.setLayout(cardEventLayout);
        cardEventLayout.setHorizontalGroup(
            cardEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardEventLayout.createSequentialGroup()
                .addGap(387, 387, 387)
                .addComponent(jLabel2)
                .addContainerGap(471, Short.MAX_VALUE))
        );
        cardEventLayout.setVerticalGroup(
            cardEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardEventLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addContainerGap(484, Short.MAX_VALUE))
        );

        jplMain.add(cardEvent, "card3");

        cardUser.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel6.setText("User");

        javax.swing.GroupLayout cardUserLayout = new javax.swing.GroupLayout(cardUser);
        cardUser.setLayout(cardUserLayout);
        cardUserLayout.setHorizontalGroup(
            cardUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardUserLayout.createSequentialGroup()
                .addGap(387, 387, 387)
                .addComponent(jLabel6)
                .addContainerGap(506, Short.MAX_VALUE))
        );
        cardUserLayout.setVerticalGroup(
            cardUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardUserLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel6)
                .addContainerGap(484, Short.MAX_VALUE))
        );

        jplMain.add(cardUser, "card3");

        jPanel1.add(jplMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 940, 540));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblCloseMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMenuMouseClicked
        closeMenu();
    }//GEN-LAST:event_lblCloseMenuMouseClicked

    private void lblOpenMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOpenMenuMouseClicked
        openMenu();
    }//GEN-LAST:event_lblOpenMenuMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        jplSlideMenu.setSize(0, y);
        x = 0;
    }//GEN-LAST:event_formWindowOpened

    private void lblTrangChuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTrangChuMouseClicked
        cardTrangChu.setVisible(true);
        cardTaiKhoan.setVisible(false);
        cardEvent.setVisible(false);
    }//GEN-LAST:event_lblTrangChuMouseClicked

    private void lblTaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTaiKhoanMouseClicked
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(true);
        cardEvent.setVisible(false);
    }//GEN-LAST:event_lblTaiKhoanMouseClicked

    private void txtEndDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEndDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEndDateActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardEvent.setVisible(true);

    }//GEN-LAST:event_jLabel8MouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        try {
            insert();        // TODO add your handling code here:
        } catch (ParseException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        clear();           // TODO add your handling code here:
    }//GEN-LAST:event_btnMoiActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        try {
            updateEvent();        // TODO add your handling code here:
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_button1ActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        try {
            deleteEvent();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_button2ActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        // TODO add your handling code here:
        try {
            // Kết nối đến cơ sở dữ liệu SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=CheckInApp;user=sa;password=123";
            Connection connection = DriverManager.getConnection(connectionUrl);

            JasperReport report = JasperCompileManager.compileReport("src/GUI/report1.jrxml");

            final JasperPrint p = JasperFillManager.fillReport(report, null, connection);
            JasperViewer.viewReport(p, false);

            // Thực hiện hàm update ở đây 
            // Đóng kết nối sau khi hoàn thành
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_button3ActionPerformed

    private void txtidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidActionPerformed

    private void txtnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnameActionPerformed

    private void txtStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStartDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStartDateActionPerformed

    private void txtLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocationActionPerformed

    private void tableEventMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEventMouseClicked
        if (evt.getClickCount() == 2) {
            this.index = tableEvent.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.edit();
                btnThem.setEnabled(false);
            }
        }       // TODO add your handling code here:
    }//GEN-LAST:event_tableEventMouseClicked

    private void btnquetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnquetActionPerformed
        new Camera().setVisible(true);
    }//GEN-LAST:event_btnquetActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        cardTrangChu.setVisible(false);
        cardTaiKhoan.setVisible(false);
        cardEvent.setVisible(false);
        cardUser.setVisible(true);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void btnMoi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoi1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMoi1ActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaActionPerformed

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button5ActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuaActionPerformed

    private void txtparticipantIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtparticipantIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtparticipantIDActionPerformed

    private void txtlastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtlastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtlastNameActionPerformed

    private void txtfirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtfirstNameActionPerformed

    private void tblUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUserMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblUserMouseClicked

    private void btnThem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnThem1ActionPerformed

    private void txtemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtemailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtemailActionPerformed

    private void btnquet1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnquet1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnquet1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Menu().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nhutin.component.Button btnMoi;
    private com.nhutin.component.Button btnMoi1;
    private com.nhutin.component.Button btnSua;
    private com.nhutin.component.Button btnThem;
    private com.nhutin.component.Button btnThem1;
    private com.nhutin.component.Button btnXoa;
    private com.nhutin.component.Button btnquet;
    private com.nhutin.component.Button btnquet1;
    private com.nhutin.component.Button button1;
    private com.nhutin.component.Button button2;
    private com.nhutin.component.Button button3;
    private com.nhutin.component.Button button5;
    private javax.swing.JPanel cardEvent;
    private javax.swing.JPanel cardTaiKhoan;
    private javax.swing.JPanel cardTrangChu;
    private javax.swing.JPanel cardUser;
    private com.motel.swing.ComboBoxSuggestion cboEventId;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jplMain;
    private javax.swing.JPanel jplSlideMenu;
    private javax.swing.JPanel jplTitle;
    private javax.swing.JPanel jpllMenuBar;
    private javax.swing.JLabel lblCloseMenu;
    private javax.swing.JLabel lblOpenMenu;
    private javax.swing.JLabel lblTaiKhoan;
    private javax.swing.JLabel lblTrangChu;
    private javax.swing.JTable tableEvent;
    private javax.swing.JTable tblUser;
    private javax.swing.JTextField txtEndDate;
    private com.nhutin.component.TextField txtLocation;
    private com.nhutin.component.TextField txtStartDate;
    private com.nhutin.component.TextField txtemail;
    private com.nhutin.component.TextField txtfirstName;
    private com.nhutin.component.TextField txtid;
    private com.nhutin.component.TextField txtlastName;
    private com.nhutin.component.TextField txtname;
    private com.nhutin.component.TextField txtparticipantID;
    // End of variables declaration//GEN-END:variables
}
