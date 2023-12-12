import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;


public class DataBarang {
    private JPanel Main;
    private JTextField txtNama;
    private JTextField txtHarga;
    private JTextField txtStok;
    private JTextField txtDeskripsi;
    private JTextField txtDiskon;
    private JButton saveButton;
    private JTable table1;
    private JButton updateButton;
    private JButton hapusButton;
    private JButton cariButton;
    private JTextField txtCari;


    public static void main(String[] args) {
        JFrame frame = new JFrame("DataBarang");
        frame.setContentPane(new DataBarang().Main);
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

    void TabelBarang() {
        try {
            pst = con.prepareStatement("select * from tb_barang");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DataBarang() {
        Connect();
        TabelBarang();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_brg, kategoriId_brg, nama_brg, deskripsi_brg, gambar_brg;
                Integer harga_brg, stok_brg, diskon_brg, terjual_brg;
                Double rating_brg;

                Random rand = new Random();
                int number =  rand.nextInt(999999);
                id_brg = "BRG-" + String.format("%06d", number);
                kategoriId_brg = "KGR-000000";
                nama_brg = txtNama.getText();
                deskripsi_brg = txtDeskripsi.getText();
                harga_brg = Integer.valueOf(txtHarga.getText());
                diskon_brg = Integer.valueOf(txtDiskon.getText());
                stok_brg = Integer.valueOf(txtStok.getText());
                gambar_brg = "Kosong";
                terjual_brg = 0;
                rating_brg = 0.0;

                try {
                    pst = con.prepareStatement("insert into tb_barang(id_brg, kategoriId_brg,  nama_brg, deskripsi_brg,  harga_brg, diskon_brg, stok_brg, gambar_brg, terjual_brg, rating_brg)values(?,?,?,?,?,?,?,?,?,?)");
                    pst.setString(1, id_brg);
                    pst.setString(2, kategoriId_brg);
                    pst.setString(3, nama_brg);
                    pst.setString(4, deskripsi_brg);
                    pst.setInt(5, harga_brg);
                    pst.setInt(6, diskon_brg);
                    pst.setInt(7, stok_brg);
                    pst.setString(8, gambar_brg);
                    pst.setInt(9, terjual_brg);
                    pst.setDouble(10, rating_brg);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data berhasil ditambah!");
                    TabelBarang();
                    txtNama.setText("");
                    txtDeskripsi.setText("");
                    txtHarga.setText("");
                    txtDiskon.setText("");
                    txtStok.setText("");
                    txtNama.requestFocus();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id_brg = txtCari.getText();
                    pst = con.prepareStatement("select * from tb_barang where id_brg like ?");
                    pst.setString(1, id_brg);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()){
                        String nama = rs.getString(3);
                        String deskripsi = rs.getString(4);
                        Integer harga = rs.getInt(5);
                        Integer diskon = rs.getInt(6);
                        String stok = rs.getString(7);
                        txtNama.setText(nama);
                        txtDeskripsi.setText(deskripsi);
                        txtHarga.setText( String.valueOf(harga));
                        txtDiskon.setText( String.valueOf(diskon));
                        txtStok.setText(String.valueOf(stok));
                        table1.setModel(DbUtils.resultSetToTableModel(rs));
                    } else {
                        txtNama.setText("");
                        txtDeskripsi.setText("");
                        txtHarga.setText("");
                        txtDiskon.setText("");
                        txtStok.setText("");
                        JOptionPane.showMessageDialog(null, "Nama barang tidak ada!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idBrg, nama_brg, deskripsi_brg;
                Integer harga_brg, diskon_brg, stok_brg;

                idBrg = txtCari.getText();
                nama_brg = txtNama.getText();
                deskripsi_brg = txtDeskripsi.getText();
                harga_brg = Integer.valueOf(txtHarga.getText());
                diskon_brg = Integer.valueOf(txtDiskon.getText());
                stok_brg = Integer.valueOf(txtStok.getText());

                try {
                    pst = con.prepareStatement("update tb_barang set nama_brg = ?, deskripsi_brg = ?, harga_brg = ?, diskon_brg = ?, stok_brg = ? where id_brg like ?");
                    pst.setString(1, nama_brg);
                    pst.setString(2, deskripsi_brg);
                    pst.setInt(3, harga_brg);
                    pst.setInt(4, diskon_brg);
                    pst.setInt(5, stok_brg);
                    pst.setString(6, idBrg);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Barang berhasil diupdate!");
                    TabelBarang();
                } catch(SQLException e2) {
                    e2.printStackTrace();
                }
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idBrg = txtCari.getText();
                try {
                    pst = con.prepareStatement("delete from tb_barang where id_brg like ?");
                    pst.setString(1, idBrg);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data barang berhasil dihapus!");
                    TabelBarang();
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
            }
        });
    }
}
