package rekammedis;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariPetugas;

/**
 *
 * @author dosen
 */
public class RMMedicalcekup extends javax.swing.JDialog {

    private final DefaultTableModel tabMode;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i = 0, gejala1 = 0, gejala2 = 0, gejala3 = 0, resiko1 = 0, resiko2a = 0, resiko2b = 0, resiko2c = 0, resiko2d = 0, resiko2e = 0, gejala = 0, resiko = 0;
    private DlgCariPetugas petugas = new DlgCariPetugas(null, false);

    /**
     * Creates new form DlgPemberianInfus
     *
     * @param parent
     * @param modal
     */
    public RMMedicalcekup(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        tabMode = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.R.M.", "Nama Pasien", "Alamat","Tanggal Lahir","Tanggal MCU",
            "Keadaan Umum","Riwayat Penyakit Dahulu","Suhu","Tinggi Bdan","Berat","Tensi",
            "Respirasi","Nadi","Kepala","Mata","THT","Mulut","Leher","Kulit","Tangan",
            "thorax","Abdomen","lower"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 24; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(105);
            } else if (i == 1) {
                column.setPreferredWidth(65);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            } else if (i == 3) {
                column.setPreferredWidth(170);
            } else if (i == 4) {
                column.setPreferredWidth(100);
            } else if (i == 5) {
                column.setPreferredWidth(100);
            } else if (i == 6) {
                column.setPreferredWidth(150);
            } else if (i == 7) {
                column.setPreferredWidth(150);
            } else if (i == 8) {
                column.setPreferredWidth(50);
            } else if (i == 9) {
                column.setPreferredWidth(50);
            } else if (i == 10) {
                column.setPreferredWidth(50);
            } else if (i == 11) {
                column.setPreferredWidth(50);
            } else if (i == 12) {
                column.setPreferredWidth(50);
            } else if (i == 13) {
                column.setPreferredWidth(50);
            } else if (i == 14) {
                column.setPreferredWidth(50);
            } else if (i == 15) {
                column.setPreferredWidth(50);
            } else if (i == 16) {
                column.setPreferredWidth(50);
            } else if (i == 17) {
                column.setPreferredWidth(50);
            } else if (i == 18) {
                column.setPreferredWidth(50);
            } else if (i == 19) {
                column.setPreferredWidth(50);
            } else if (i == 20) {
                column.setPreferredWidth(50);
            } else if (i == 21) {
                column.setPreferredWidth(50);
            } else if (i == 22) {
                column.setPreferredWidth(50);
           }
            else if (i == 23) {
                column.setPreferredWidth(50);
           }
            
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TCari.setDocument(new batasInput((byte) 100).getKata(TCari));
        mata1.setDocument(new batasInput((byte) 50).getKata(mata1));
        RiwayatPenyakitSebelumnya.setDocument(new batasInput((byte) 50).getKata(RiwayatPenyakitSebelumnya));
        TSuhu.setDocument(new batasInput((byte) 5).getKata(TSuhu));
        TTensi.setDocument(new batasInput((byte) 7).getKata(TTensi));
        TTinggi.setDocument(new batasInput((byte) 5).getKata(TTinggi));
        TBerat.setDocument(new batasInput((byte) 5).getKata(TBerat));
        TRespirasi.setDocument(new batasInput((byte) 3).getOnlyAngka(TRespirasi));
        TNadi1.setDocument(new batasInput((byte) 3).getOnlyAngka(TNadi1));


        if (koneksiDB.CARICEPAT().equals("aktif")) {
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampil();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampil();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampil();
                    }
                }
            });
        }

        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (petugas.getTable().getSelectedRow() != -1) {
                    
                }
                
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        ChkInput.setSelected(false);
        isForm();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        cetak_pemeriksaan_mcu = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass7 = new widget.panelisi();
        jLabel15 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel17 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        NoRM = new widget.TextBox();
        NamaPasien = new widget.TextBox();
        jLabel7 = new widget.Label();
        NoRawat = new widget.TextBox();
        jLabel12 = new widget.Label();
        jLabel8 = new widget.Label();
        mata1 = new widget.TextBox();
        TglLahir = new widget.TextBox();
        jLabel14 = new widget.Label();
        jLabel16 = new widget.Label();
        Alamat = new widget.TextBox();
        jLabel18 = new widget.Label();
        TglMCU = new widget.Tanggal();
        jLabel23 = new widget.Label();
        jLabel24 = new widget.Label();
        jLabel26 = new widget.Label();
        jLabel27 = new widget.Label();
        kepala = new widget.ComboBox();
        RiwayatPenyakitSebelumnya = new widget.TextBox();
        jLabel39 = new widget.Label();
        jLabel9 = new widget.Label();
        jLabel4 = new widget.Label();
        jLabel19 = new widget.Label();
        jLabel20 = new widget.Label();
        jLabel21 = new widget.Label();
        jLabel22 = new widget.Label();
        TSuhu = new widget.TextBox();
        TTensi = new widget.TextBox();
        TTinggi = new widget.TextBox();
        TRespirasi = new widget.TextBox();
        TBerat = new widget.TextBox();
        jLabel13 = new widget.Label();
        jLabel44 = new widget.Label();
        tht = new widget.ComboBox();
        jLabel49 = new widget.Label();
        mulut = new widget.ComboBox();
        jLabel50 = new widget.Label();
        leher = new widget.ComboBox();
        jLabel52 = new widget.Label();
        kulit = new widget.ComboBox();
        jLabel53 = new widget.Label();
        tangan = new widget.ComboBox();
        jLabel54 = new widget.Label();
        thorax = new widget.ComboBox();
        jLabel55 = new widget.Label();
        abdomen = new widget.ComboBox();
        jLabel56 = new widget.Label();
        lower = new widget.ComboBox();
        jLabel57 = new widget.Label();
        TNadi1 = new widget.TextBox();
        keadaanUmum1 = new widget.TextBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        cetak_pemeriksaan_mcu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/PrinterSettings.png"))); // NOI18N
        cetak_pemeriksaan_mcu.setText("Cetak Pemeriksaan MCU");
        cetak_pemeriksaan_mcu.setName("cetak_pemeriksaan_mcu"); // NOI18N
        cetak_pemeriksaan_mcu.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                cetak_pemeriksaan_mcuMouseWheelMoved(evt);
            }
        });
        cetak_pemeriksaan_mcu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cetak_pemeriksaan_mcuMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cetak_pemeriksaan_mcuMouseReleased(evt);
            }
        });
        cetak_pemeriksaan_mcu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetak_pemeriksaan_mcuActionPerformed(evt);
            }
        });
        jPopupMenu1.add(cetak_pemeriksaan_mcu);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pemeriksaan MCU Oleh Petugas ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbObat.setAutoCreateRowSorter(true);
        tbObat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(jLabel10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass8.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        panelGlass7.setName("panelGlass7"); // NOI18N
        panelGlass7.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel15.setText("Tgl.Masuk :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(63, 23));
        panelGlass7.add(jLabel15);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-02-2021" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass7.add(DTPCari1);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("s.d");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(24, 23));
        panelGlass7.add(jLabel17);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-02-2021" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass7.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass7.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(360, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass7.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('7');
        BtnCari.setToolTipText("Alt+7");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass7.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass7.add(BtnAll);

        jPanel3.add(panelGlass7, java.awt.BorderLayout.CENTER);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 306));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setText(".: Input Data");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N

        FormInput.setBorder(null);
        FormInput.setMaximumSize(new java.awt.Dimension(32700, 32700));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(500, 400));
        FormInput.setLayout(null);

        NoRM.setEditable(false);
        NoRM.setHighlighter(null);
        NoRM.setName("NoRM"); // NOI18N
        FormInput.add(NoRM);
        NoRM.setBounds(219, 10, 100, 23);

        NamaPasien.setEditable(false);
        NamaPasien.setHighlighter(null);
        NamaPasien.setName("NamaPasien"); // NOI18N
        FormInput.add(NamaPasien);
        NamaPasien.setBounds(321, 10, 690, 23);

        jLabel7.setText("No.Rawat :");
        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        FormInput.add(jLabel7);
        jLabel7.setBounds(0, 10, 72, 23);

        NoRawat.setEditable(false);
        NoRawat.setHighlighter(null);
        NoRawat.setName("NoRawat"); // NOI18N
        FormInput.add(NoRawat);
        NoRawat.setBounds(76, 10, 141, 23);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("C. Pemeriksaan Fisik Head To Toe");
        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(590, 110, 190, 23);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Keadaan Umum");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(30, 140, 200, 23);

        mata1.setHighlighter(null);
        mata1.setName("mata1"); // NOI18N
        mata1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mata1KeyPressed(evt);
            }
        });
        FormInput.add(mata1);
        mata1.setBounds(700, 190, 90, 23);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(710, 40, 90, 23);

        jLabel14.setText("Tgl.Lahir :");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(620, 40, 80, 23);

        jLabel16.setText("Alamat :");
        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 72, 23);

        Alamat.setEditable(false);
        Alamat.setHighlighter(null);
        Alamat.setName("Alamat"); // NOI18N
        FormInput.add(Alamat);
        Alamat.setBounds(80, 40, 557, 23);

        jLabel18.setText("Tgl. MCU :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(20, 70, 80, 23);

        TglMCU.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-02-2021" }));
        TglMCU.setDisplayFormat("dd-MM-yyyy");
        TglMCU.setName("TglMCU"); // NOI18N
        TglMCU.setOpaque(false);
        TglMCU.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglMCUKeyPressed(evt);
            }
        });
        FormInput.add(TglMCU);
        TglMCU.setBounds(110, 70, 90, 23);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("Riwayat Penyakit Dahulu");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(30, 170, 200, 23);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText(":");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(160, 170, 10, 23);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText(":");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(160, 140, 10, 23);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("A. Anamnesa");
        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(20, 110, 120, 23);

        kepala.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        kepala.setName("kepala"); // NOI18N
        kepala.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                kepalaItemStateChanged(evt);
            }
        });
        kepala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kepalaKeyPressed(evt);
            }
        });
        FormInput.add(kepala);
        kepala.setBounds(700, 150, 85, 23);

        RiwayatPenyakitSebelumnya.setHighlighter(null);
        RiwayatPenyakitSebelumnya.setName("RiwayatPenyakitSebelumnya"); // NOI18N
        RiwayatPenyakitSebelumnya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwayatPenyakitSebelumnyaKeyPressed(evt);
            }
        });
        FormInput.add(RiwayatPenyakitSebelumnya);
        RiwayatPenyakitSebelumnya.setBounds(170, 170, 350, 23);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel39.setText(" Kepala :");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(620, 150, 70, 23);

        jLabel9.setText("Suhu (°C) :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(10, 240, 85, 23);

        jLabel4.setText("Tensi (mmHg) :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(10, 290, 85, 23);

        jLabel19.setText("Berat(Kg) :");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(330, 240, 79, 23);

        jLabel20.setText("Nadi(/menit) :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(330, 290, 79, 23);

        jLabel21.setText("Tinggi Badan(Cm) :");
        jLabel21.setName("jLabel21"); // NOI18N
        FormInput.add(jLabel21);
        jLabel21.setBounds(160, 240, 100, 23);

        jLabel22.setText("Respirasi(/menit) :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(160, 290, 100, 23);

        TSuhu.setFocusTraversalPolicyProvider(true);
        TSuhu.setName("TSuhu"); // NOI18N
        FormInput.add(TSuhu);
        TSuhu.setBounds(100, 240, 55, 23);

        TTensi.setHighlighter(null);
        TTensi.setName("TTensi"); // NOI18N
        FormInput.add(TTensi);
        TTensi.setBounds(100, 290, 55, 23);

        TTinggi.setFocusTraversalPolicyProvider(true);
        TTinggi.setName("TTinggi"); // NOI18N
        FormInput.add(TTinggi);
        TTinggi.setBounds(270, 240, 55, 23);

        TRespirasi.setHighlighter(null);
        TRespirasi.setName("TRespirasi"); // NOI18N
        FormInput.add(TRespirasi);
        TRespirasi.setBounds(270, 290, 55, 23);

        TBerat.setHighlighter(null);
        TBerat.setName("TBerat"); // NOI18N
        FormInput.add(TBerat);
        TBerat.setBounds(410, 240, 55, 23);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("B. Pemeriksaan Fisik");
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(20, 200, 140, 23);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setText("Mata :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(630, 190, 70, 23);

        tht.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        tht.setName("tht"); // NOI18N
        tht.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                thtItemStateChanged(evt);
            }
        });
        tht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thtActionPerformed(evt);
            }
        });
        tht.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                thtKeyPressed(evt);
            }
        });
        FormInput.add(tht);
        tht.setBounds(700, 230, 85, 23);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel49.setText("THT :");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(630, 230, 70, 23);

        mulut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        mulut.setName("mulut"); // NOI18N
        mulut.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mulutItemStateChanged(evt);
            }
        });
        mulut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mulutActionPerformed(evt);
            }
        });
        mulut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mulutKeyPressed(evt);
            }
        });
        FormInput.add(mulut);
        mulut.setBounds(700, 270, 85, 23);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel50.setText("Mulut :");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(630, 270, 70, 23);

        leher.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        leher.setName("leher"); // NOI18N
        leher.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                leherItemStateChanged(evt);
            }
        });
        leher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leherActionPerformed(evt);
            }
        });
        leher.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                leherKeyPressed(evt);
            }
        });
        FormInput.add(leher);
        leher.setBounds(700, 310, 85, 23);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("Leher :");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(630, 310, 70, 23);

        kulit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        kulit.setName("kulit"); // NOI18N
        kulit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                kulitItemStateChanged(evt);
            }
        });
        kulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kulitKeyPressed(evt);
            }
        });
        FormInput.add(kulit);
        kulit.setBounds(920, 150, 85, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText(" Kulit :");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(870, 150, 70, 23);

        tangan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        tangan.setName("tangan"); // NOI18N
        tangan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tanganItemStateChanged(evt);
            }
        });
        tangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tanganActionPerformed(evt);
            }
        });
        tangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tanganKeyPressed(evt);
            }
        });
        FormInput.add(tangan);
        tangan.setBounds(920, 190, 85, 23);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel54.setText("Tangan :");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(860, 190, 70, 23);

        thorax.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        thorax.setName("thorax"); // NOI18N
        thorax.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                thoraxItemStateChanged(evt);
            }
        });
        thorax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thoraxActionPerformed(evt);
            }
        });
        thorax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                thoraxKeyPressed(evt);
            }
        });
        FormInput.add(thorax);
        thorax.setBounds(920, 230, 85, 23);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("Thorax :");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(860, 230, 70, 23);

        abdomen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        abdomen.setName("abdomen"); // NOI18N
        abdomen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                abdomenItemStateChanged(evt);
            }
        });
        abdomen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abdomenActionPerformed(evt);
            }
        });
        abdomen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                abdomenKeyPressed(evt);
            }
        });
        FormInput.add(abdomen);
        abdomen.setBounds(920, 270, 85, 23);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel56.setText("Abdomen :");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(850, 270, 70, 23);

        lower.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        lower.setName("lower"); // NOI18N
        lower.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lowerItemStateChanged(evt);
            }
        });
        lower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowerActionPerformed(evt);
            }
        });
        lower.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lowerKeyPressed(evt);
            }
        });
        FormInput.add(lower);
        lower.setBounds(920, 310, 85, 23);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("Lower :");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(870, 310, 60, 23);

        TNadi1.setFocusTraversalPolicyProvider(true);
        TNadi1.setName("TNadi1"); // NOI18N
        FormInput.add(TNadi1);
        TNadi1.setBounds(410, 290, 55, 23);

        keadaanUmum1.setHighlighter(null);
        keadaanUmum1.setName("keadaanUmum1"); // NOI18N
        keadaanUmum1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                keadaanUmum1KeyPressed(evt);
            }
        });
        FormInput.add(keadaanUmum1);
        keadaanUmum1.setBounds(170, 140, 350, 23);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(NoRM.getText().trim().equals("")||NamaPasien.getText().trim().equals("")){
            Valid.textKosong(NoRM,"Pasien");
        
        }else{
            if(Sequel.menyimpantf("pemeriksaan_mcu", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 20, new String[]{
                    NoRawat.getText(), 
                    Valid.SetTgl(TglMCU.getSelectedItem() + ""),
                    keadaanUmum1.getText(),
                    RiwayatPenyakitSebelumnya.getText(),
                    TSuhu.getText(),
                    TTinggi.getText(),
                    TBerat.getText(),
                    TTensi.getText(),
                    TRespirasi.getText(),
                    TNadi1.getText(),
                    kepala.getSelectedItem().toString(),
                    mata1.getText(),
                    tht.getSelectedItem().toString(),
                    mulut.getSelectedItem().toString(),
                    leher.getSelectedItem().toString(),
                    kulit.getSelectedItem().toString(),
                    tangan.getSelectedItem().toString(),
                    thorax.getSelectedItem().toString(),
                    abdomen.getSelectedItem().toString(),
                    lower.getSelectedItem().toString()
                }) == true) {

                    emptTeks();
                    tampil();
                }
            }
        
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnSimpanActionPerformed(null);
        } else {
            //Valid.pindah(evt,Comorbid,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm();
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            emptTeks();
        } else {
            Valid.pindah(evt, BtnSimpan, BtnHapus);
        }
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TglMCU.requestFocus();
        }else if(NamaPasien.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\n Klik data pada table untuk memilih...!!!!");
        }else if(!(NamaPasien.getText().trim().equals(""))){
            try{
                Sequel.queryu("delete from pemeriksaan_mcu " +
                        "where no_rawat='"+NoRawat.getText()+"' " +
                        "and tgl_mcu='"+Valid.SetTgl(TglMCU.getSelectedItem()+"")+"' " +
                        "and keadaan_umum='"+keadaanUmum1.getText()+"' " +
                        "and suhu='"+TSuhu.getText()+"' " +
                        "and tinggi_badan='"+TTinggi.getText()+"' " +
                        "and berat_badan='"+TBerat.getText()+"' " +
                        "and tensi='"+TTensi.getText()+"' " +
                        "and respirasi='"+TRespirasi.getText()+"' " +
                        "and nadi='"+TNadi1.getText()+"' " +
                        "and kepala='"+kepala.getSelectedItem()+"' " +
                        "and mata='"+mata1.getText()+"' " +
                        "and tht='"+tht.getSelectedItem()+"' " +
                        "and mulut='"+mulut.getSelectedItem()+"' " +
                        "and leher='"+leher.getSelectedItem()+"' " +
                        "and kulit='"+kulit.getSelectedItem()+"' " +
                        "and tangan='"+tangan.getSelectedItem()+"' " +
                        "and thorax='"+thorax.getSelectedItem()+"' " +
                        "and abdomen='"+abdomen.getSelectedItem()+"' " +
                        "and lower='"+lower.getSelectedItem()+"' ");
                tampil();
                emptTeks();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih terlebih dulu data yang mau anda hapus...\n Klik data pada table untuk memilih data...!!!!");
            }
        }
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnHapusActionPerformed(null);
        } 
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            dispose();
        } 
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            tampil();
            TCari.setText("");
        } else {
            Valid.pindah(evt, BtnCari, NamaPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCari.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariActionPerformed(null);
        } else {
            Valid.pindah(evt, TCari, BtnAll);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if (NoRM.getText().trim().equals("") || NamaPasien.getText().trim().equals("")) {
            Valid.textKosong(NoRM, "Pasien");
        
        } else {
            if (tbObat.getSelectedRow() > -1) {
                if (Sequel.mengedittf("pemeriksaan_mcu", "no_rawat=?", "no_rawat=?,tgl_mcu=?,keadaan_umum=?,riwayat_penyakit_dahulu=?,suhu=?,tinggi_badan=?,"
                        + "berat_badan=?,tensi=?,respirasi=?,nadi=?,kepala=?,mata=?,tht=?,mulut=?,leher=?,kulit=?,tangan=?,thorax=?,lower=?", 20, new String[]{
                    NoRawat.getText(), 
                    Valid.SetTgl(TglMCU.getSelectedItem()+""), 
                    keadaanUmum1.getText(),
                    RiwayatPenyakitSebelumnya.getText(),
                    TSuhu.getText(),
                    TTinggi.getText(),
                    TBerat.getText(),
                    TTensi.getText(),
                    TRespirasi.getText(),
                    TNadi1.getText(),
                    kepala.getSelectedItem().toString(),
                    mata1.getText(),
                    tht.getSelectedItem().toString(),
                    mulut.getSelectedItem().toString(),
                    leher.getSelectedItem().toString(),
                    kulit.getSelectedItem().toString(),
                    tangan.getSelectedItem().toString(),
                    thorax.getSelectedItem().toString(),
                    abdomen.getSelectedItem().toString(),
                    lower.getSelectedItem().toString(),
                    tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()
                }) != true) {
                } else {
                    emptTeks();
                    tampil();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Maaf silahkan pilih data terlebih dahulu..!!");
            }
        }
    }//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnEditActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
    }//GEN-LAST:event_BtnEditKeyPressed

    private void mata1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mata1KeyPressed

    }//GEN-LAST:event_mata1KeyPressed

    private void TglMCUKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglMCUKeyPressed
        Valid.pindah(evt, TCari, mata1);
    }//GEN-LAST:event_TglMCUKeyPressed

    private void RiwayatPenyakitSebelumnyaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwayatPenyakitSebelumnyaKeyPressed
        Valid.pindah(evt, mata1, kepala);
    }//GEN-LAST:event_RiwayatPenyakitSebelumnyaKeyPressed

    private void kepalaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kepalaKeyPressed
        //      Valid.pindah(evt, RiwayatPenyakitSebelumnya, AsalDaerah);
    }//GEN-LAST:event_kepalaKeyPressed

    private void kepalaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_kepalaItemStateChanged

    }//GEN-LAST:event_kepalaItemStateChanged

    private void thtItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_thtItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_thtItemStateChanged

    private void thtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_thtActionPerformed

    private void thtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_thtKeyPressed

    private void mulutItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mulutItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_mulutItemStateChanged

    private void mulutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mulutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mulutActionPerformed

    private void mulutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mulutKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_mulutKeyPressed

    private void leherItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_leherItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_leherItemStateChanged

    private void leherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_leherActionPerformed

    private void leherKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_leherKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_leherKeyPressed

    private void kulitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_kulitItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_kulitItemStateChanged

    private void kulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kulitKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kulitKeyPressed

    private void tanganItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tanganItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tanganItemStateChanged

    private void tanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tanganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tanganActionPerformed

    private void tanganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tanganKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tanganKeyPressed

    private void thoraxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_thoraxItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_thoraxItemStateChanged

    private void thoraxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thoraxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_thoraxActionPerformed

    private void thoraxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thoraxKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_thoraxKeyPressed

    private void abdomenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_abdomenItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_abdomenItemStateChanged

    private void abdomenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abdomenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_abdomenActionPerformed

    private void abdomenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_abdomenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_abdomenKeyPressed

    private void lowerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lowerItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_lowerItemStateChanged

    private void lowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lowerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lowerActionPerformed

    private void lowerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lowerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lowerKeyPressed

    private void keadaanUmum1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keadaanUmum1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_keadaanUmum1KeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
//        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        if(tabMode.getRowCount()==0){
//            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
//            BtnBatal.requestFocus();
//        }else if(tabMode.getRowCount()!=0){
//            Map<String, Object> param = new HashMap<>();
//            param.put("namars",akses.getnamars());
//            param.put("alamatrs",akses.getalamatrs());
//            param.put("kotars",akses.getkabupatenrs());
//            param.put("propinsirs",akses.getpropinsirs());
//            param.put("kontakrs",akses.getkontakrs());
//            param.put("emailrs",akses.getemailrs());
//            param.put("logo",Sequel.cariGambar("select logo from setting"));
//            Valid.MyReportqry("rptDeteksiDiniCorona.jasper","report","::[ Deteksi Dini Pasien Corona ]::",
//                "select deteksi_dini_corona.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"+
//                "pasien.no_ktp,pasien.no_tlp,pasien.pekerjaan,pasien.tgl_lahir,deteksi_dini_corona.tanggal,deteksi_dini_corona.nip,petugas.nama,deteksi_dini_corona.gejala_demam,deteksi_dini_corona.gejala_batuk,"+
//                "deteksi_dini_corona.gejala_sesak,deteksi_dini_corona.gejala_tanggal_pertama,deteksi_dini_corona.gejala_riwayat_sakit,deteksi_dini_corona.gejala_riwayat_periksa,"+
//                "deteksi_dini_corona.faktor_riwayat_perjalanan,deteksi_dini_corona.faktor_asal_daerah,deteksi_dini_corona.faktor_tanggal_kedatangan,deteksi_dini_corona.faktor_paparan_kontakpositif,"+
//                "deteksi_dini_corona.faktor_paparan_kontakpdp,deteksi_dini_corona.faktor_paparan_faskespositif,deteksi_dini_corona.faktor_paparan_perjalananln,deteksi_dini_corona.faktor_paparan_pasarhewan,"+
//                "deteksi_dini_corona.kesimpulan,deteksi_dini_corona.tindak_lanjut from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
//                "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
//                "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
//                "inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                "inner join deteksi_dini_corona on deteksi_dini_corona.no_rawat=reg_periksa.no_rawat "+
//                "inner join petugas on deteksi_dini_corona.nip=petugas.nip "+
//                "where deteksi_dini_corona.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+
//                (TCari.getText().trim().equals("")?"":"and (deteksi_dini_corona.no_rawat like '%"+TCari.getText().trim()+"%' or "+
//                    "pasien.no_rkm_medis like '%"+TCari.getText().trim()+"%' or pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
//                    "pasien.no_tlp like '%"+TCari.getText().trim()+"%' or deteksi_dini_corona.kesimpulan like '%"+TCari.getText().trim()+"%' or "+
//                    "deteksi_dini_corona.tindak_lanjut like '%"+TCari.getText().trim()+"%')")+" order by deteksi_dini_corona.tanggal",param);
//        }
//        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
    }//GEN-LAST:event_BtnPrintKeyPressed

    private void cetak_pemeriksaan_mcuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetak_pemeriksaan_mcuActionPerformed
                                            
        if (NamaPasien.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Maaf, Silahkan anda pilih dulu pasien...!!!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
             
             param.put("tgl_mcu", Sequel.cariIsi("select date_format(tgl_mcu,'%d-%m-%Y') as tgl_mcu from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("keadaan_umum", Sequel.cariIsi("select keadaan_umum from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("riwayat_penyakit_dahulu", Sequel.cariIsi("select riwayat_penyakit_dahulu from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("berat_badan", Sequel.cariIsi("select berat_badan from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("tinggi_badan", Sequel.cariIsi("select tinggi_badan from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("tekanan_darah", Sequel.cariIsi("select tensi from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("nadi", Sequel.cariIsi("select nadi from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("suhu", Sequel.cariIsi("select suhu  from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("pernapasan", Sequel.cariIsi("select respirasi from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("kepala", Sequel.cariIsi("select kepala from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("mata", Sequel.cariIsi("select mata from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("tht", Sequel.cariIsi("select tht from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("mulut", Sequel.cariIsi("select mulut from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("leher", Sequel.cariIsi("select leher from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("kulit", Sequel.cariIsi("select kulit from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("tangan", Sequel.cariIsi("select tangan from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("thorax", Sequel.cariIsi("select thorax from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("abdomen", Sequel.cariIsi("select abdomen from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
             param.put("lower", Sequel.cariIsi("select lower from pemeriksaan_mcu where no_rawat=?", NoRawat.getText()));
//param.put("td", Sequel.cariIsi("select tensi from pemeriksaan_ralan where no_rawat=?", TNoRw.getText()));
            //param.put("tb", Sequel.cariIsi("select tinggi from pemeriksaan_ralan where no_rawat=?", TNoRw.getText()));
            param.put("logo", Sequel.cariGambar("select logo from setting"));
            param.put("bismillah", Sequel.cariGambar("select gambar FROM testgambar WHERE nm_gambar='bismillah' "));
            Valid.MyReportqry("rptmcu.jasper", "report", "::[ Cetak Pemeriksaan MCU ]::",
                    "SELECT pemeriksaan_mcu.no_rawat, \n" +
                        "reg_periksa.no_rkm_medis,\n" +
                        "pasien.nm_pasien,\n" +
                        "pasien.alamat,\n" +
                        "date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,\n" +
                        "if (pasien.jk='L','Laki-laki','Perempuan') AS jk,\n" +
                        "dokter.nm_dokter, \n"+
                        "date_format(pemeriksaan_mcu.tgl_mcu,'%d-%m-%Y') as tgl_mcu, \n" +
                        "pemeriksaan_mcu.keadaan_umum,\n" +
                        "pemeriksaan_mcu.riwayat_penyakit_dahulu,\n" +
                        "pemeriksaan_mcu.suhu,\n" +
                        "pemeriksaan_mcu.tinggi_badan, \n" +
                        "pemeriksaan_mcu.berat_badan, \n" +
                        "pemeriksaan_mcu.tensi, \n" +
                        "pemeriksaan_mcu.respirasi,\n" +
                        "pemeriksaan_mcu.nadi, \n" +
                        "pemeriksaan_mcu.kepala,\n" +
                        "pemeriksaan_mcu.mata,\n" +
                        "pemeriksaan_mcu.tht,\n" +
                        "pemeriksaan_mcu.mulut,\n" +
                        "pemeriksaan_mcu.leher,\n" +
                        "pemeriksaan_mcu.kulit,\n" +
                        "pemeriksaan_mcu.tangan,\n" +
                        "pemeriksaan_mcu.thorax,\n" +
                        "pemeriksaan_mcu.abdomen,\n" +
                        "pemeriksaan_mcu.lower \n" +
                        " FROM reg_periksa inner join pemeriksaan_mcu inner join pasien inner join dokter on\n" +
                        " reg_periksa.no_rawat=pemeriksaan_mcu.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis" +
                        " AND reg_periksa.kd_dokter=dokter.kd_dokter "+
                        " where reg_periksa.no_rawat='" + NoRawat.getText() + "' ", param);
            this.setCursor(Cursor.getDefaultCursor());
        }
    
    }//GEN-LAST:event_cetak_pemeriksaan_mcuActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbObatKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if (tabMode.getRowCount() != 0) {
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbObatMouseClicked

    private void cetak_pemeriksaan_mcuMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_cetak_pemeriksaan_mcuMouseWheelMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_cetak_pemeriksaan_mcuMouseWheelMoved

    private void cetak_pemeriksaan_mcuMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cetak_pemeriksaan_mcuMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cetak_pemeriksaan_mcuMouseReleased

    private void cetak_pemeriksaan_mcuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cetak_pemeriksaan_mcuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cetak_pemeriksaan_mcuMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMMedicalcekup dialog = new RMMedicalcekup(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.TextBox Alamat;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.PanelBiasa FormInput;
    private widget.Label LCount;
    private widget.TextBox NamaPasien;
    private widget.TextBox NoRM;
    private widget.TextBox NoRawat;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox RiwayatPenyakitSebelumnya;
    private widget.ScrollPane Scroll;
    private widget.TextBox TBerat;
    private widget.TextBox TCari;
    private widget.TextBox TNadi1;
    private widget.TextBox TRespirasi;
    private widget.TextBox TSuhu;
    private widget.TextBox TTensi;
    private widget.TextBox TTinggi;
    private widget.TextBox TglLahir;
    private widget.Tanggal TglMCU;
    private widget.ComboBox abdomen;
    private javax.swing.JMenuItem cetak_pemeriksaan_mcu;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel39;
    private widget.Label jLabel4;
    private widget.Label jLabel44;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
    private widget.Label jLabel52;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private widget.TextBox keadaanUmum1;
    private widget.ComboBox kepala;
    private widget.ComboBox kulit;
    private widget.ComboBox leher;
    private widget.ComboBox lower;
    private widget.TextBox mata1;
    private widget.ComboBox mulut;
    private widget.panelisi panelGlass7;
    private widget.panelisi panelGlass8;
    private widget.ScrollPane scrollInput;
    private widget.ComboBox tangan;
    private widget.Table tbObat;
    private widget.ComboBox thorax;
    private widget.ComboBox tht;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            ps = koneksi.prepareStatement(
                            "SELECT \n" +
"pemeriksaan_mcu.no_rawat,\n" +
"pasien.no_rkm_medis,\n" +
"pasien.nm_pasien,\n" +
"pasien.alamat,\n" +
"pasien.tgl_lahir,\n" +
"pemeriksaan_mcu.tgl_mcu,\n" +
"pemeriksaan_mcu.keadaan_umum,\n" +
"pemeriksaan_mcu.riwayat_penyakit_dahulu,\n" +
"pemeriksaan_mcu.suhu,\n" +
"pemeriksaan_mcu.tinggi_badan,\n" +
"pemeriksaan_mcu.berat_badan,\n" +
"pemeriksaan_mcu.tensi,\n" +
"pemeriksaan_mcu.respirasi,\n" +
"pemeriksaan_mcu.nadi,\n" +
"pemeriksaan_mcu.kepala,\n" +
"pemeriksaan_mcu.mata,\n" +
"pemeriksaan_mcu.tht,\n" +
"pemeriksaan_mcu.mulut,\n" +
"pemeriksaan_mcu.leher,\n" +
"pemeriksaan_mcu.kulit,\n" +
"pemeriksaan_mcu.tangan,\n" +
"pemeriksaan_mcu.thorax,\n" +
"pemeriksaan_mcu.abdomen,\n" +
"pemeriksaan_mcu.lower\n" +
 
 
"FROM \n" +
"reg_periksa inner join pemeriksaan_mcu inner join pasien on\n" +
"reg_periksa.no_rawat=pemeriksaan_mcu.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis\n" +
"WHERE \n" +
"pemeriksaan_mcu.tgl_mcu BETWEEN ? AND ? "+
(TCari.getText().trim().equals("")?"":" and pasien.no_rkm_medis like ? or pemeriksaan_mcu.no_rawat like ? or pasien.nm_pasien like ? order by pemeriksaan_mcu.tgl_mcu"));
            
            try {
                ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));
                if (!TCari.getText().trim().equals("")) {
                    ps.setString(3, "%" + TCari.getText().trim() + "%");
                    ps.setString(4, "%" + TCari.getText().trim() + "%");
                    ps.setString(5, "%" + TCari.getText().trim() + "%");
//                    ps.setString(6, "%" + TCari.getText().trim() + "%");
//                    ps.setString(7, "%" + TCari.getText().trim() + "%");
//                    ps.setString(8, "%" + TCari.getText().trim() + "%");
                   
                   
                }
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"), 
                        rs.getString("no_rkm_medis"), 
                        rs.getString("nm_pasien"),
                        rs.getString("alamat"), 
                        rs.getString("tgl_lahir"),
                        rs.getString("tgl_mcu"),
                        rs.getString("keadaan_umum"),
                        rs.getString("riwayat_penyakit_dahulu"), 
                        rs.getString("suhu"),
                        rs.getString("tinggi_badan"),
                        rs.getString("berat_badan"), 
                        rs.getString("tensi"),
                        rs.getString("respirasi"),
                        rs.getString("nadi"),
                        rs.getString("kepala"),
                        rs.getString("mata"),
                        rs.getString("tht"),
                        rs.getString("mulut"),
                        rs.getString("leher"),
                        rs.getString("kulit"), 
                        rs.getString("tangan"),
                        rs.getString("thorax"), 
                        rs.getString("abdomen"),
                        rs.getString("lower")

                            
                        
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tabMode.getRowCount());
    }

    public void emptTeks() {
        NoRM.setText("");
        NamaPasien.setText("");
        NoRawat.setText("");
        TglLahir.setText("");
        Alamat.setText("");
        TglMCU.setDate(new Date());
        keadaanUmum1.setText("");
        RiwayatPenyakitSebelumnya.setText("");
        mata1.setText("");
        kepala.setSelectedIndex(0);
        mata1.setText("");
        tht.setSelectedIndex(0);
        mulut.setSelectedIndex(0);
        leher.setSelectedIndex(0);
        kulit.setSelectedIndex(0);
        tangan.setSelectedIndex(0);
        thorax.setSelectedIndex(0);
        abdomen.setSelectedIndex(0);
        lower.setSelectedIndex(0);
        TSuhu.setText("");
        TTensi.setText("");
        TBerat.setText("");
        TTinggi.setText("");
        TRespirasi.setText("");
       

        ChkInput.setSelected(true);
        isForm();

    }

    private void getData() {
        if (tbObat.getSelectedRow() != -1) {
            NoRawat.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            NoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 1).toString());
            NamaPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString());
            Alamat.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 4).toString());
            keadaanUmum1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 6).toString());
            RiwayatPenyakitSebelumnya.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 7).toString());
            TSuhu.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString());
            TTinggi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 9).toString());
            TBerat.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 10).toString());
            TTensi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 11).toString());
            TRespirasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 12).toString());
            TNadi1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 13).toString());
            kepala.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            mata1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 15).toString());
            tht.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            mulut.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            leher.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            kulit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            tangan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            thorax.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            abdomen.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            lower.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            
            
            
//            mata1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 15).toString());
//            RiwayatPenyakitSebelumnya.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString());
//            kepala.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 9).toString());
//            tht.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 10).toString());
//            mulut.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 11).toString());
//            leher.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 12).toString());
//            kulit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 13).toString());
//            tangan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 14).toString());
//            thorax.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 16).toString());
//            abdomen.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 17).toString());
//            lower.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 18).toString());
//            Valid.SetTgl(TglMCU, tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString());
//           
        }
    }

    private void isForm() {
        if (ChkInput.isSelected() == true) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, this.getHeight() - 122));
            FormInput.setVisible(true);
            ChkInput.setVisible(true);
        } else if (ChkInput.isSelected() == false) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 20));
            FormInput.setVisible(false);
            ChkInput.setVisible(true);
        }
    }

    public void isCek() {
        BtnSimpan.setEnabled(akses.getdeteksi_corona());
        BtnHapus.setEnabled(akses.getdeteksi_corona());
        BtnEdit.setEnabled(akses.getdeteksi_corona());
        if (akses.getjml2() >= 1) {
           
//            Sequel.cariIsi("select nama from petugas where nip=?", nmptg, kdptg.getText());
        }
    }

    public void setNoRm(String norawat, Date tgl2) {
        NoRawat.setText(norawat);
        TCari.setText(norawat);
        try {
            ps = koneksi.prepareStatement(
                    "select pasien.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"
                    + "pasien.no_ktp,pasien.no_tlp,pasien.pekerjaan,pasien.tgl_lahir "
                    + "from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "
                    + "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "
                    + "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "
                    + "inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis where reg_periksa.no_rawat=?");
            try {
                ps.setString(1, norawat);
                rs = ps.executeQuery();
                while (rs.next()) {
                    NoRM.setText(rs.getString("no_rkm_medis"));
                    NamaPasien.setText(rs.getString("nm_pasien"));
                    Alamat.setText(rs.getString("asal"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                }
            } catch (Exception ex) {
                System.out.println(ex);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        DTPCari2.setDate(tgl2);

    }

}
