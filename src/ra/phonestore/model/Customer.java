package ra.phonestore.model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private boolean isDeleted;


    public Customer() {}

    public Customer(int id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }


    @Override
    public String toString() {
        return String.format("| %-4d | %-20s | %-12s | %-25s | %-30s |", 
                id, name, phone, email, address);
    }
}
