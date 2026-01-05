import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

// Customer Node
class Customer {
    int id;
    String name;
    int units;
    double bill;
    Customer next;

    public Customer(int id, String name, int units) {
        this.id = id;
        this.name = name;
        this.units = units;
        this.bill = units * 5; // 5 per unit
        this.next = null;
    }
}


