import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class SwingApp {

    private static StudentManager manager = new StudentManager();

    private static JFrame frame;
    private static JPanel contentPanel;
    private static CardLayout cardLayout;
    private static JLabel statusLabel;

    // Sidebar tracking
    private static List<JButton> sidebarButtons = new ArrayList<>();
    private static String currentPanel = "dashboard";

    // Panels
    private static JPanel dashboardPanel;
    private static JPanel addPanel;
    private static JPanel viewPanel;
    private static JPanel searchPanel;
    private static JPanel deletePanel;
    private static JPanel topStudentPanel;

    // Dashboard Components
    private static JLabel dashTotalLabel;
    private static JLabel dashAvgLabel;
    private static JPanel topStudentsListPanel;

    // View Components
    private static JTable studentTable;
    private static DefaultTableModel tableModel;

    // Premium Color Palette
    private static final Color COLOR_PRIMARY = new Color(24, 144, 255); // Professional Blue
    private static final Color COLOR_ACCENT = new Color(0, 110, 210);
    private static final Color COLOR_BG = new Color(248, 249, 250); // Very Soft Gray BG
    private static final Color COLOR_SIDE_BG = Color.WHITE;
    private static final Color COLOR_SIDE_ACTIVE_BG = new Color(230, 247, 255);
    private static final Color COLOR_TEXT_HEAD = new Color(0, 0, 0, 220);
    private static final Color COLOR_TEXT_SUB = new Color(0, 0, 0, 120);
    private static final Color COLOR_BORDER = new Color(235, 237, 240);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(SwingApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Academic Information System - Institutional Console");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 850);
        frame.setMinimumSize(new Dimension(1100, 750));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // NAVIGATION (SIDEBAR)
        JPanel sidebar = createSidebar();
        frame.add(sidebar, BorderLayout.WEST);

        // CONTENT AREA
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(COLOR_BG);

        initAllPanels();
        frame.add(contentPanel, BorderLayout.CENTER);

        // FOOTER / STATUS BAR
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, COLOR_BORDER), new EmptyBorder(12, 30, 12, 30)));
        statusLabel = new JLabel("System Status: Operational | Secure Institutional Link Active");
        statusLabel.setForeground(COLOR_TEXT_SUB);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusBar.add(statusLabel, BorderLayout.WEST);
        frame.add(statusBar, BorderLayout.SOUTH);

        showPanel("dashboard");
        frame.setVisible(true);
    }

    private static JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_SIDE_BG);
        panel.setPreferredSize(new Dimension(340, 0));
        panel.setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 1, COLOR_BORDER), new EmptyBorder(30, 25, 30, 25)));

        JLabel brand = new JLabel("ACADEMIC PRO");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brand.setForeground(COLOR_ACCENT);
        brand.setBorder(new EmptyBorder(0, 20, 50, 20));
        panel.add(brand);

        addSidebarBtn(panel, "⛃ Dashboard", "dashboard");
        panel.add(Box.createVerticalStrut(10));
        addSidebarBtn(panel, "✚ Register Student", "add");
        panel.add(Box.createVerticalStrut(10));
        addSidebarBtn(panel, "☰ Master Records", "view");
        panel.add(Box.createVerticalStrut(10));
        addSidebarBtn(panel, "🏆 Top Performer", "top");
        panel.add(Box.createVerticalStrut(10));
        addSidebarBtn(panel, "🔍 Search Students", "search");
        panel.add(Box.createVerticalStrut(10));
        addSidebarBtn(panel, "🗑 Delete Records", "delete");

        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    private static void addSidebarBtn(JPanel parent, String text, String cardID) {
        JButton btn = new JButton(text);
        btn.setActionCommand(cardID);
        btn.setMaximumSize(new Dimension(290, 65));
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 20, 0, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> showPanel(cardID));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!currentPanel.equals(cardID)) {
                    btn.setBackground(new Color(250, 250, 250));
                    btn.setForeground(COLOR_ACCENT);
                    btn.repaint();
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!currentPanel.equals(cardID)) {
                    btn.setBackground(COLOR_SIDE_BG);
                    btn.setForeground(COLOR_TEXT_SUB);
                    btn.repaint();
                }
            }
        });

        sidebarButtons.add(btn);
        parent.add(btn);
    }

    private static void updateSidebarUI() {
        for (JButton btn : sidebarButtons) {
            if (btn.getActionCommand().equals(currentPanel)) {
                btn.setBackground(COLOR_SIDE_ACTIVE_BG);
                btn.setForeground(COLOR_ACCENT);
                btn.setFont(new Font("Segoe UI Bold", Font.PLAIN, 17));
                // Add left accent indicator
                btn.setBorder(new CompoundBorder(new MatteBorder(0, 4, 0, 0, COLOR_ACCENT), new EmptyBorder(0, 16, 0, 10)));
            } else {
                btn.setBackground(COLOR_SIDE_BG);
                btn.setForeground(COLOR_TEXT_SUB);
                btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
                btn.setBorder(new EmptyBorder(0, 20, 0, 10));
            }
            btn.repaint();
        }
    }

    private static void initAllPanels() {
        dashboardPanel = createDashboardUI();
        addPanel = createRegistrationPanel();
        viewPanel = createTableUI();
        topStudentPanel = createTopUI();
        searchPanel = createQueryUI();
        deletePanel = createMaintenanceUI();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(addPanel, "add");
        contentPanel.add(viewPanel, "view");
        contentPanel.add(topStudentPanel, "top");
        contentPanel.add(searchPanel, "search");
        contentPanel.add(deletePanel, "delete");
    }

    private static void showPanel(String name) {
        currentPanel = name;
        updateSidebarUI();
        if (name.equals("dashboard")) refreshDashboardData();
        if (name.equals("view")) refreshMasterTable();
        if (name.equals("top")) refreshTopPerformer();
        cardLayout.show(contentPanel, name);
        statusLabel.setText("Active Context: " + name.toUpperCase());
    }

    // --- DASHBOARD ---
    private static JPanel createDashboardUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(50, 60, 50, 60));

        JLabel title = new JLabel("Institutional Console Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(COLOR_TEXT_HEAD);
        title.setBorder(new EmptyBorder(0, 0, 40, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.insets = new Insets(10, 0, 10, 0);

        // Stat Cards
        JPanel stats = new JPanel(new GridLayout(1, 3, 30, 0));
        stats.setOpaque(false);
        
        dashTotalLabel = new JLabel("0");
        dashAvgLabel = new JLabel("0.00 %");
        
        stats.add(createCard("ACTIVE ENROLLMENTS", dashTotalLabel, COLOR_PRIMARY));
        stats.add(createCard("AVERAGE PERFORMANCE", dashAvgLabel, new Color(82, 196, 26)));
        
        JButton topBtn = new JButton("Detailed Analytics View");
        topBtn.setBackground(COLOR_PRIMARY);
        topBtn.setForeground(Color.WHITE);
        topBtn.setFont(new Font("Segoe UI Bold", Font.PLAIN, 15));
        topBtn.setFocusPainted(false);
        topBtn.setOpaque(true);
        topBtn.setBorderPainted(false);
        topBtn.addActionListener(e -> showPanel("top"));
        
        JPanel cardBtn = new JPanel(new BorderLayout());
        cardBtn.setBackground(Color.WHITE);
        cardBtn.setBorder(new LineBorder(COLOR_BORDER, 1));
        cardBtn.add(topBtn, BorderLayout.CENTER);
        stats.add(cardBtn);

        gbc.gridy = 0; grid.add(stats, gbc);

        // Performance List
        JPanel listCard = new JPanel(new BorderLayout());
        listCard.setBackground(Color.WHITE);
        listCard.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1), new EmptyBorder(30, 40, 30, 40)));
        
        JLabel listTitle = new JLabel("Academic Performance Rankings");
        listTitle.setFont(new Font("Segoe UI Bold", Font.PLAIN, 18));
        listTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        listCard.add(listTitle, BorderLayout.NORTH);

        topStudentsListPanel = new JPanel();
        topStudentsListPanel.setLayout(new BoxLayout(topStudentsListPanel, BoxLayout.Y_AXIS));
        topStudentsListPanel.setBackground(Color.WHITE);
        listCard.add(new JScrollPane(topStudentsListPanel), BorderLayout.CENTER);

        gbc.gridy = 1; gbc.weighty = 1; gbc.insets = new Insets(30, 0, 0, 0);
        grid.add(listCard, gbc);

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createCard(String title, JLabel label, Color topBorder) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1), new EmptyBorder(25, 30, 25, 30)));
        card.setBorder(new CompoundBorder(new MatteBorder(4, 0, 0, 0, topBorder), card.getBorder()));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        t.setForeground(COLOR_TEXT_SUB);
        
        label.setFont(new Font("Segoe UI Bold", Font.PLAIN, 40));
        label.setForeground(COLOR_TEXT_HEAD);
        label.setBorder(new EmptyBorder(10, 0, 0, 0));

        card.add(t, BorderLayout.NORTH);
        card.add(label, BorderLayout.CENTER);
        return card;
    }

    private static void refreshDashboardData() {
        int count = manager.getAllStudents().size();
        dashTotalLabel.setText(String.valueOf(count));
        dashAvgLabel.setText(String.format("%.2f %%", manager.getAverageMarks()));

        topStudentsListPanel.removeAll();
        List<Student> top = manager.getTopStudents();
        for (Student s : top) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(Color.WHITE);
            row.setBorder(new EmptyBorder(12, 10, 12, 10));
            row.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDER));
            
            JLabel n = new JLabel("STUDENT RECORD ID: " + s.id + " | " + s.name.toUpperCase());
            n.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
            JLabel m = new JLabel(s.marks + " %");
            m.setFont(new Font("Segoe UI Bold", Font.PLAIN, 15));
            m.setForeground(COLOR_PRIMARY);
            
            row.add(n, BorderLayout.WEST);
            row.add(m, BorderLayout.EAST);
            topStudentsListPanel.add(row);
        }
        topStudentsListPanel.revalidate();
        topStudentsListPanel.repaint();
    }

    // --- TABLE UI ---
    private static JPanel createTableUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(50, 60, 50, 60));

        JLabel title = new JLabel("Master Information Protocol");
        title.setFont(new Font("Segoe UI Bold", Font.PLAIN, 30));
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        panel.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"IDENTIFIER", "ENTITY NAME", "MARKS (%)"}, 0);
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(50);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        studentTable.setSelectionBackground(COLOR_SIDE_ACTIVE_BG);
        studentTable.setSelectionForeground(COLOR_ACCENT);
        studentTable.setShowVerticalLines(false);
        studentTable.setGridColor(COLOR_BORDER);
        
        JTableHeader h = studentTable.getTableHeader();
        h.setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
        h.setBackground(Color.WHITE);
        h.setPreferredSize(new Dimension(0, 50));
        h.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDER));

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        return panel;
    }

    private static void refreshMasterTable() {
        tableModel.setRowCount(0);
        for (Student s : manager.getAllStudents()) {
            tableModel.addRow(new Object[]{String.format("SN-%04d", s.id), s.name, s.marks + " %"});
        }
    }

    // --- REGISTRATION PANEL (AUTO-GENERATED ID) ---
    private static JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1), new EmptyBorder(60, 80, 60, 80)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel head = new JLabel("Register Student"); 
        head.setFont(new Font("Segoe UI Bold", Font.PLAIN, 30));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(0, 15, 45, 15);
        card.add(head, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(15, 15, 15, 15);
        JTextField nameF = createStyledField();
        JTextField marksF = createStyledField();

        gbc.gridy = 1; card.add(new JLabel("STUDENT NAME:"), gbc);
        gbc.gridx = 1; card.add(nameF, gbc);

        gbc.gridx = 0; gbc.gridy = 2; card.add(new JLabel("MARKS (0-100):"), gbc);
        gbc.gridx = 1; card.add(marksF, gbc);

        JButton sub = new JButton("Register Now");
        sub.setBackground(COLOR_ACCENT);
        sub.setForeground(Color.WHITE);
        sub.setFont(new Font("Segoe UI Bold", Font.PLAIN, 15));
        sub.setFocusPainted(false);
        sub.setOpaque(true);
        sub.setBorderPainted(false);
        sub.setPreferredSize(new Dimension(0, 55));
        sub.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.insets = new Insets(50, 15, 0, 15);
        card.add(sub, gbc);

        sub.addActionListener(e -> {
            try {
                String name = nameF.getText().trim();
                String markText = marksF.getText().trim();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter the student's name", "Simple Alert", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                double m = Double.parseDouble(markText);
                if (m < 0 || m > 100) {
                    JOptionPane.showMessageDialog(frame, "Marks must be between 0 and 100", "Simple Alert", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int newId = manager.registerStudent(name, m);
                if (newId != -1) {
                    JOptionPane.showMessageDialog(frame, "Registration Successful!\nGenerated Student ID: " + newId, "Success", JOptionPane.INFORMATION_MESSAGE);
                    nameF.setText(""); marksF.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Error: Could not save student.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for marks", "Simple Alert", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(card);
        return panel;
    }

    // --- OTHER FORMS (UPDATE) ---
    private static JPanel createFormPanel(String title, String btnTxt, boolean isUpdate) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1), new EmptyBorder(60, 80, 60, 80)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel head = new JLabel(title); head.setFont(new Font("Segoe UI Bold", Font.PLAIN, 30));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(0, 15, 45, 15);
        card.add(head, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(15, 15, 15, 15);
        JTextField idF = createStyledField();
        JTextField nameF = createStyledField();
        JTextField marksF = createStyledField();

        gbc.gridy = 1; card.add(new JLabel("STUDENT ID:"), gbc);
        gbc.gridx = 1; card.add(idF, gbc);

        gbc.gridx = 0; gbc.gridy = 2; card.add(new JLabel("STUDENT NAME:"), gbc);
        gbc.gridx = 1; card.add(nameF, gbc);

        gbc.gridx = 0; gbc.gridy = 3; card.add(new JLabel("MARKS (0-100):"), gbc);
        gbc.gridx = 1; card.add(marksF, gbc);

        JButton sub = new JButton(btnTxt);
        sub.setBackground(COLOR_ACCENT);
        sub.setForeground(Color.WHITE);
        sub.setFont(new Font("Segoe UI Bold", Font.PLAIN, 15));
        sub.setFocusPainted(false);
        sub.setOpaque(true);
        sub.setBorderPainted(false);
        sub.setPreferredSize(new Dimension(0, 55));
        sub.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(50, 15, 0, 15);
        card.add(sub, gbc);

        sub.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idF.getText().trim());
                String name = nameF.getText().trim();
                double m = Double.parseDouble(marksF.getText().trim());
                if (name.isEmpty() || m < 0 || m > 100) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid details and marks (0-100)", "Simple Alert", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean res = manager.updateStudent(new Student(id, name, m));
                if (res) {
                    JOptionPane.showMessageDialog(frame, "Profile Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    idF.setText(""); nameF.setText(""); marksF.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "No such Student ID found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers for ID and Marks", "Simple Alert", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(card);
        return panel;
    }

    private static JTextField createStyledField() {
        JTextField f = new JTextField(20);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        f.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER, 1), new EmptyBorder(12, 15, 12, 15)));
        return f;
    }

    // --- SEARCH ---
    private static JPanel createQueryUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(50, 60, 50, 60));

        JLabel title = new JLabel("Search Student Database");
        title.setFont(new Font("Segoe UI Bold", Font.PLAIN, 32));
        title.setBorder(new EmptyBorder(0, 0, 40, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1;

        JPanel card = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(COLOR_BORDER));

        JTextField inputF = createStyledField();
        inputF.setPreferredSize(new Dimension(350, 50));
        JButton btn = new JButton("Search Now");
        btn.setBackground(COLOR_ACCENT); btn.setForeground(Color.WHITE); btn.setFont(new Font("Segoe UI Bold", Font.PLAIN, 15));
        btn.setPreferredSize(new Dimension(220, 50));
        btn.setOpaque(true); btn.setBorderPainted(false);

        card.add(new JLabel("ENTER ID OR NAME:")); card.add(inputF); card.add(btn);
        
        gbc.gridy = 0; content.add(card, gbc);

        JPanel results = new JPanel();
        results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));
        results.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(results);
        scroll.setBorder(new CompoundBorder(new EmptyBorder(30, 0, 0, 0), new LineBorder(COLOR_BORDER)));
        
        gbc.gridy = 1; gbc.weighty = 1; content.add(scroll, gbc);

        btn.addActionListener(e -> {
            results.removeAll();
            String input = inputF.getText().trim();
            if (input.isEmpty()) { 
                JOptionPane.showMessageDialog(frame, "Please enter an ID or Name");
                return;
            }

            try {
                int id = Integer.parseInt(input);
                Student s = manager.searchStudent(id);
                if (s != null) addResultRow(results, s);
            } catch (Exception ex) {
                List<Student> list = manager.searchStudentsByName(input);
                for (Student s : list) addResultRow(results, s);
            }

            if (results.getComponentCount() == 0) {
                JLabel none = new JLabel("No records found.");
                none.setBorder(new EmptyBorder(20, 20, 20, 20));
                results.add(none);
            }
            results.revalidate(); results.repaint();
        });

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private static void addResultRow(JPanel parent, Student s) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDER), new EmptyBorder(15, 25, 15, 25)));
        JLabel txt = new JLabel(String.format("ID: %04d | NAME: %s | MARKS: %.2f%%", s.id, s.name.toUpperCase(), s.marks));
        txt.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        row.add(txt, BorderLayout.WEST);
        parent.add(row);
    }

    // --- TOP ---
    private static JPanel createTopUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);
        JPanel card = new JPanel(new BorderLayout(0, 40));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(COLOR_ACCENT, 2), new EmptyBorder(80, 100, 80, 100)));

        JLabel label = new JLabel("TOP INSTITUTIONAL PERFORMER", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI Bold", Font.PLAIN, 15));
        label.setForeground(COLOR_TEXT_SUB);
        card.add(label, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 15));
        info.setOpaque(false);
        JLabel n = new JLabel("NAME", SwingConstants.CENTER); n.setFont(new Font("Segoe UI", Font.BOLD, 48)); n.setForeground(COLOR_ACCENT);
        JLabel m = new JLabel("0.00 %", SwingConstants.CENTER); m.setFont(new Font("Segoe UI Bold", Font.PLAIN, 32)); m.setForeground(COLOR_TEXT_HEAD);
        info.add(n); info.add(m);
        card.add(info, BorderLayout.CENTER);

        panel.add(card);
        return panel;
    }

    private static void refreshTopPerformer() {
        List<Student> list = manager.getTopStudents();
        Container card = (Container) topStudentPanel.getComponent(0);
        JPanel info = (JPanel) card.getComponent(1);
        JLabel nl = (JLabel) info.getComponent(0);
        JLabel ml = (JLabel) info.getComponent(1);

        if (list.isEmpty()) { nl.setText("NO RECORDS FOUND"); ml.setText("-"); }
        else { nl.setText(manager.getTopStudentsNames()); ml.setText(list.get(0).marks + " %"); }
    }

    // --- DELETE ---
    private static JPanel createMaintenanceUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);
        JPanel card = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 40));
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(255, 77, 79), 1));

        JTextField idF = createStyledField();
        JButton btn = new JButton("Delete Student Details");
        btn.setBackground(new Color(255, 77, 79)); btn.setForeground(Color.WHITE); btn.setFont(new Font("Segoe UI Bold", Font.PLAIN, 15));
        btn.setPreferredSize(new Dimension(280, 50));
        btn.setOpaque(true); btn.setBorderPainted(false);

        card.add(new JLabel("TARGET ID:")); card.add(idF); card.add(btn);
        
        btn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idF.getText().trim());
                Student s = manager.searchStudent(id);
                if (s == null) {
                    JOptionPane.showMessageDialog(frame, "No such student found with ID " + id, "Record Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String confirmMsg = String.format("Found: %s (Marks: %.2f%%)\nDo you want to delete this data?", s.name, s.marks);
                if (JOptionPane.showConfirmDialog(frame, confirmMsg, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (manager.deleteStudent(id)) {
                        JOptionPane.showMessageDialog(frame, "Data Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        idF.setText("");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid Student ID number", "ID Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(card);
        return panel;
    }
}