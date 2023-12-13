import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;

public class DataSlider {
    private JPanel Main;
    private JTextField txtJudulSlider;
    private JButton saveButton;
    private JButton updateButton;
    private JButton hapusButton;
    private JButton cariButton;
    private JTextField txtCari;
    private JTextField txtSubJudulSlider;
    private JTable tableDataSlider;
    private JTextField txtDeskripsiSlider;
    private JTextField txtGambarSlider;


    public static void main(String[] args) {
        JFrame frame = new JFrame("DataSlider");
        frame.setContentPane(new DataSlider().Main);
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

    void TableSlider() {
        try {
            pst = con.prepareStatement("select * from tb_slider");
            ResultSet rs = pst.executeQuery();
            tableDataSlider.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DataSlider() {
        Connect();
        TableSlider();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_slr, judul_slr, subJudul_slr, deskripsi_slr, gambar_slr;

                Random rand = new Random();
                id_slr = "SLR-" +  String.valueOf(rand.nextInt(999999));
                judul_slr = txtJudulSlider.getText();
                subJudul_slr = txtSubJudulSlider.getText();
                deskripsi_slr = txtDeskripsiSlider.getText();
                gambar_slr = txtGambarSlider.getText();

                try {
                    pst = con.prepareStatement("insert into tb_slider values(?,?,?,?,?)");
                    pst.setString(1, id_slr);
                    pst.setString(2, judul_slr);
                    pst.setString(3, subJudul_slr);
                    pst.setString(4, deskripsi_slr);
                    pst.setString(5, gambar_slr);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data slider berhasil ditambah!");
                    TableSlider();
                    txtJudulSlider.setText("");
                    txtSubJudulSlider.setText("");
                    txtDeskripsiSlider.setText("");
                    txtGambarSlider.setText("");
                    txtJudulSlider.requestFocus();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_slr;

                id_slr = txtCari.getText();

                try {
                    pst = con.prepareStatement("select * from tb_slider where id_slr like ?");
                    pst.setString(1, id_slr);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()) {
                        String judul_slr = rs.getString(2);
                        String subJudul_slr = rs.getString(3);
                        String deskJudul_slr = rs.getString(4);
                        String gambar_slr = rs.getString(5);
                        txtJudulSlider.setText(judul_slr);
                        txtSubJudulSlider.setText(subJudul_slr);
                        txtDeskripsiSlider.setText(deskJudul_slr);
                        txtGambarSlider.setText(gambar_slr);
                    } else {
                        txtJudulSlider.setText("");
                        txtSubJudulSlider.setText("");
                        txtDeskripsiSlider.setText("");
                        txtGambarSlider.setText("");
                        JOptionPane.showMessageDialog(null, "Data slider tidak ada!");
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_slr, judul_slr, subjudul_slr, deskripsi_slr, gambar_slr;

                id_slr = txtCari.getText();
                judul_slr = txtJudulSlider.getText();
                subjudul_slr = txtSubJudulSlider.getText();
                deskripsi_slr = txtDeskripsiSlider.getText();
                gambar_slr = txtGambarSlider.getText();

                try {
                    pst = con.prepareStatement("update tb_slider set judul_slr = ?, subJudul-slr = ?, deskripsi_slr = ?, gambar_slr = ? where id_slr like ?");
                    pst.setString(1, judul_slr);
                    pst.setString(2, subjudul_slr);
                    pst.setString(3, deskripsi_slr);
                    pst.setString(4, gambar_slr);
                    pst.setString(5, id_slr);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data slider berhasil di update!");
                    txtJudulSlider.setText("");
                    txtSubJudulSlider.setText("");
                    txtDeskripsiSlider.setText("");
                    txtSubJudulSlider.setText("");
                    txtGambarSlider.requestFocus();
                    TableSlider();
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_slr;

                id_slr = txtCari.getText();

                try {
                    pst = con.prepareCall("delete from tb_slr where id_slr like ?");
                    pst.setString(1, id_slr);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data slider berhasil dihapus!");
                    TableSlider();
                } catch(SQLException e4) {
                    e4.printStackTrace();
                }
            }
        });
    }
}
