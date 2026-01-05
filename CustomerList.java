// Linked List for Customers
class CustomerList {
    Customer head;

    public void addCustomer(Customer c) {
        if (head == null) head = c;
        else {
            Customer temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = c;
        }
    }

    public void updateCustomer(int id, int newUnits) {
        Customer temp = head;
        while (temp != null) {
            if (temp.id == id) {
                temp.units = newUnits;
                temp.bill = newUnits * 5;
                break;
            }
            temp = temp.next;
        }
    }

    public Customer[] toArray() {
        int size = 0;
        Customer temp = head;
        while (temp != null) {
            size++;
            temp = temp.next;
        }
        Customer[] arr = new Customer[size];
        temp = head;
        for (int i = 0; i < size; i++) {
            arr[i] = temp;
            temp = temp.next;
        }
        return arr;
    }
}