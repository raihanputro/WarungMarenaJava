import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;

public class DataAdmin {
    private JPanel Main;
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JTextField txtAlamat;
    private JButton saveButton;
    private JTable tableDataAdmin;
    private JButton updateButton;
    private JButton hapusButton;
    private JButton cariButton;
    private JTextField txtCari;
    private JTextField txtPassword;
    private JTextField txtNama;
    private JTextField txtTelepon;
    private JTextField txtFoto;

    public static void main(String[] args) {
        JFrame frame = new JFrame("DataAdmin");
        frame.setContentPane(new DataAdmin().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/warungmarena_db", "root", "");
            System.out.println("Database terhubung!");
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void TableDataAdmin() {
        try {
            pst = con.prepareStatement("select id_usr, email_usr, username_usr, nama_usr, alamat_usr, telepon_usr from tb_user where role_usr like 'Admin'");
            ResultSet rs = pst.executeQuery();
            tableDataAdmin.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DataAdmin() {
        Connect();
        TableDataAdmin();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_usr, email_usr, password_usr, username_usr, nama_usr, alamat_usr, telepon_usr;

                Random rand = new Random();
                int number = rand.nextInt(999999);
                id_usr = "ADM-" + String.format("%06d",number);
                email_usr = txtEmail.getText();
                password_usr = txtPassword.getText();
                username_usr = txtUsername.getText();
                nama_usr = txtNama.getText();
                alamat_usr = txtAlamat.getText();
                telepon_usr = txtTelepon.getText();

                try {
                    pst = con.prepareStatement("insert into tb_usr values(?,?,?,?,?,?,?)");
                    pst.setString(1, id_usr);
                    pst.setString(2, email_usr);
                    pst.setString(3, password_usr);
                    pst.setString(4, username_usr);
                    pst.setString(5, nama_usr);
                    pst.setString(6, alamat_usr);
                    pst.setString(7, telepon_usr);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Data admin berhasil ditambah!");
                    TableDataAdmin();
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtUsername.setText("");
                    txtNama.setText("");
                    txtAlamat.setText("");
                    txtTelepon.setText("");
                    txtEmail.requestFocus();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_usr;

                id_usr = txtCari.getText();

                try {
                    pst = con.prepareStatement("select id_usr, email_usr, username_usr, nama_usr, alamat_usr, telepon_usr from tb_user where role_usr like 'Admin' && id_usr like ?");
                    pst.setString(1, id_usr);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){
                        String email_usr = rs.getString(2);
                        String password_usr = rs.getString(3);
                        String username_usr = rs.getString(4);
                        String nama_usr = rs.getString(5);
                        String alamat_usr = rs.getString(6);
                        String telepon_usr = rs.getString(9);
                        txtEmail.setText(email_usr);
                        txtPassword.setText(password_usr);
                        txtUsername.setText(username_usr);
                        txtNama.setText(nama_usr);
                        txtAlamat.setText(alamat_usr);
                        txtTelepon.setText(telepon_usr);
                    } else {
                        txtEmail.setText("");
                        txtPassword.setText("");
                        txtUsername.setText("");
                        txtNama.setText("");
                        txtAlamat.setText("");
                        txtTelepon.setText("");
                        txtEmail.requestFocus();
                        JOptionPane.showMessageDialog(null, "Data admin tidak ada!");
                        TableDataAdmin();
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_usr, email_usr, password_usr, username_usr, nama_usr, alamat_usr, telepon_usr;

                id_usr = txtCari.getText();
                email_usr = txtEmail.getText();
                password_usr = txtPassword.getText();
                username_usr = txtUsername.getText();
                nama_usr = txtNama.getText();
                alamat_usr = txtAlamat.getText();
                telepon_usr = txtTelepon.getText();

                try {
                    pst = con.prepareStatement("update tb_user set email_usr = ?, password_usr = ?, username_usr = ?, nama_usr = ?, alamat_usr = ?, telepon_usr = ? where role_usr like 'Admin' && id_usr like ?");
                    pst.setString(1, email_usr);
                    pst.setString(2, password_usr);
                    pst.setString(3, username_usr);
                    pst.setString(4, nama_usr);
                    pst.setString(5, alamat_usr);
                    pst.setString(6, telepon_usr);
                    pst.setString(7, id_usr);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data admin berhasil di update!");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtUsername.setText("");
                    txtNama.setText("");
                    txtAlamat.setText("");
                    txtTelepon.setText("");
                    txtEmail.requestFocus();
                    TableDataAdmin();
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_usr;

                id_usr = txtCari.getText();

                try {
                    pst = con.prepareCall("delete from tb_usr where role_usr like 'Admin' && id_usr like ?");
                    pst.setString(1, id_usr);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data admin berhasil dihapus!");
                    TableDataAdmin();
                } catch(SQLException e4) {
                    e4.printStackTrace();
                }
            }
        });
    }
}
