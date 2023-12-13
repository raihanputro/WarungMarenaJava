import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

public class DataKategoriBarang {
    private JPanel Main;
    private JTextField txtNamaKategori;
    private JTextField txtIkonKategori;
    private JTextField txtCari;
    private JButton updateButton;
    private JButton hapusButton;
    private JButton cariButton;
    private JButton saveButton;
    private JTable tableKategoriBarang;

    public static void main(String[] args) {
        JFrame frame = new JFrame("DataKategoriBarang");
        frame.setContentPane(new DataKategoriBarang().Main);
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void TableKategoriBarang() {
        try {
            pst = con.prepareStatement("select * from tb_kategori");
            ResultSet rs = pst.executeQuery();
            tableKategoriBarang.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DataKategoriBarang() {
        Connect();
        TableKategoriBarang();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String id_kgr, nama_kgr, ikon_kgr;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            Random rand = new Random();
            int number = rand.nextInt(999999);
            id_kgr = "KGR-" + String.format("%06d",number);
            nama_kgr = txtNamaKategori.getText();
            ikon_kgr = txtIkonKategori.getText();

            try {
                pst = con.prepareStatement("insert into tb_kategori values(?,?,?,?)");
                pst.setString(1, id_kgr);
                pst.setString(2, nama_kgr);
                pst.setString(3, ikon_kgr);
                pst.setString(4, dateFormat.format(date));
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null,"Data Kategori Barang berhasil ditambah!");
                TableKategoriBarang();
                txtNamaKategori.setText("");
                txtIkonKategori.setText("");
                txtNamaKategori.requestFocus();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            }
        });
        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_kgr;

                id_kgr = txtCari.getText();

                try {
                    pst = con.prepareStatement("select * from tb_kategori where id_kgr like ?");
                    pst.setString(1, id_kgr);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){
                        String namaKategori = rs.getString(2);
                        String ikonKategori = rs.getString(3);
                        txtNamaKategori.setText(namaKategori);
                        txtIkonKategori.setText(ikonKategori);
                    } else {
                        txtNamaKategori.setText("");
                        txtIkonKategori.setText("");
                        JOptionPane.showMessageDialog(null, "Data kategori barang tidak ada!");
                        TableKategoriBarang();
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_kgr, nama_kgr, ikon_kgr;

                id_kgr = txtCari.getText();
                nama_kgr = txtNamaKategori.getText();
                ikon_kgr = txtIkonKategori.getText();

                try {
                    pst = con.prepareStatement("update tb_kategori set nama_kgr = ?, ikon_kgr = ? where id_kgr like ?");
                    pst.setString(1, nama_kgr);
                    pst.setString(2, ikon_kgr);
                    pst.setString(3, id_kgr);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data kategori barang berhasil di update!");
                    txtNamaKategori.setText("");
                    txtIkonKategori.setText("");
                    txtNamaKategori.requestFocus();
                    TableKategoriBarang();
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_kgr;

                id_kgr = txtCari.getText();

                try {
                    pst = con.prepareCall("delete from tb_kategori where id_kgr like ?");
                    pst.setString(1, id_kgr);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data Kategori Barang berhasil dihapus!");
                    TableKategoriBarang();
                } catch(SQLException e4) {
                    e4.printStackTrace();
                }

            }
        });
    }

}
