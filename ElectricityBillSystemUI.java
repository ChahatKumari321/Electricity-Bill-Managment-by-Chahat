import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;


// Main GUI
class ElectricityBillSystemUI extends JFrame {
    CustomerList list = new CustomerList();
    DefaultTableModel tableModel;
    JTable table;

    public ElectricityBillSystemUI() {
        setTitle("Electricity Bill Management System");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel for title and logo
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Electricity Bill Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 204));

        // Load company logo and make it circular
        ImageIcon logoIcon = new ImageIcon("Logo.png"); // replace with your path
        Image img = logoIcon.getImage();
        int diameter = 60;
        BufferedImage circleImg = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(img.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH), 0, 0, null);
        g2.dispose();
        JLabel logoLabel = new JLabel(new ImageIcon(circleImg));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(logoLabel, BorderLayout.EAST);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Units", "Bill"}, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] btnNames = {"Add Customer", "Update Units", "Sort Customers", "Search Customer", "Exit"};
        JButton[] buttons = new JButton[btnNames.length];
        Color btnColor = new Color(0, 102, 204); // single blue color

        for (int i = 0; i < btnNames.length; i++) {
            JButton btn = new JButton(btnNames[i]);
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));

            // Hover effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(btnColor.darker());
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(btnColor);
                }
            });

            buttons[i] = btn;
            buttonPanel.add(btn);
        }

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        buttons[0].addActionListener(e -> addCustomer());
        buttons[1].addActionListener(e -> updateCustomer());
        buttons[2].addActionListener(e -> sortCustomers());
        buttons[3].addActionListener(e -> searchCustomer());
        buttons[4].addActionListener(e -> System.exit(0));
    }

    // Add Customer
    private void addCustomer() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Customer ID:"));
            String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
            int units = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Units:"));
            list.addCustomer(new Customer(id, name, units));
            refreshTable(list.toArray());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    // Update Customer
    private void updateCustomer() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Customer ID to Update:"));
            int units = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter New Units:"));
            list.updateCustomer(id, units);
            refreshTable(list.toArray());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    // Sort Customers
    private void sortCustomers() {
        String[] options = {"ID", "Name"};
        String choice = (String) JOptionPane.showInputDialog(this, "Sort by:", "Sort Customers",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        Customer[] arr = list.toArray();
        if (choice != null) {
            if (choice.equals("ID")) bubbleSortByID(arr);
            else arr = mergeSortByName(arr);
            refreshTable(arr);
        }
    }

    // Search Customer
    private void searchCustomer() {
        String[] options = {"ID", "Name"};
        String choice = (String) JOptionPane.showInputDialog(this, "Search by:", "Search Customer",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice != null) {
            if (choice.equals("ID")) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter ID:"));
                    Customer c = linearSearchByID(list.toArray(), id);
                    if (c != null) showCustomer(c);
                    else JOptionPane.showMessageDialog(this, "Customer not found!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input!");
                }
            } else {
                String name = JOptionPane.showInputDialog(this, "Enter Name:");
                Customer c = linearSearchByName(list.toArray(), name);
                if (c != null) showCustomer(c);
                else JOptionPane.showMessageDialog(this, "Customer not found!");
            }
        }
    }

    // Refresh Table
    private void refreshTable(Customer[] arr) {
        tableModel.setRowCount(0);
        for (Customer c : arr) {
            tableModel.addRow(new Object[]{c.id, c.name, c.units, c.bill});
        }
    }

    private void showCustomer(Customer c) {
        JOptionPane.showMessageDialog(this,
                "ID: " + c.id + "\nName: " + c.name + "\nUnits: " + c.units + "\nBill: " + c.bill,
                "Customer Found", JOptionPane.INFORMATION_MESSAGE);
    }

    // Bubble sort by ID
    private void bubbleSortByID(Customer[] arr) {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (arr[j].id > arr[j+1].id) {
                    Customer temp = arr[j]; arr[j] = arr[j+1]; arr[j+1] = temp;
                }
            }
        }
    }

    // Merge sort by Name
    private Customer[] mergeSortByName(Customer[] arr) {
        if (arr.length <= 1) return arr;
        int mid = arr.length / 2;
        Customer[] left = mergeSortByName(Arrays.copyOfRange(arr, 0, mid));
        Customer[] right = mergeSortByName(Arrays.copyOfRange(arr, mid, arr.length));
        return mergeByName(left, right);
    }

    private Customer[] mergeByName(Customer[] left, Customer[] right) {
        Customer[] merged = new Customer[left.length + right.length];
        int i=0,j=0,k=0;
        while(i<left.length && j<right.length) {
            if(left[i].name.compareToIgnoreCase(right[j].name)<=0) merged[k++] = left[i++];
            else merged[k++] = right[j++];
        }
        while(i<left.length) merged[k++] = left[i++];
        while(j<right.length) merged[k++] = right[j++];
        return merged;
    }

    private Customer linearSearchByID(Customer[] arr, int id) {
        for(Customer c : arr) if(c.id == id) return c;
        return null;
    }

    private Customer linearSearchByName(Customer[] arr, String name) {
        for(Customer c : arr) if(c.name.equalsIgnoreCase(name)) return c;
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ElectricityBillSystemUI().setVisible(true));
    }
}

