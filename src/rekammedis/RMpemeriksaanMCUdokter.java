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
import simrskhanza.DlgKamarInap;
import simrskhanza.DlgRujuk;

/**
 *
 * @author dosen
 */
public class RMpemeriksaanMCUdokter extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0,gejala1=0,gejala2=0,gejala3=0,resiko1=0,resiko2a=0,resiko2b=0,resiko2c=0,resiko2d=0,resiko2e=0,gejala=0,resiko=0;
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    

    /** Creates new form DlgPemberianInfus
     * @param parent
     * @param modal */
    public RMpemeriksaanMCUdokter(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        tabMode=new DefaultTableModel(null,new Object[]{
                "No.Rawat","No.R.M.","Nama Pasien","Alamat","Tgl.Lahir","tgl_mcu","ekg","HBsAg","Pregnancey Test","diagnosa","kesimpulan","saran"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 12; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(150);
            }else if(i==3){
                column.setPreferredWidth(170);
            }else if(i==4){
                column.setPreferredWidth(100);
            }else if(i==5){
                column.setPreferredWidth(100);
            }else if(i==6){
                column.setPreferredWidth(170);
            }else if(i==7){
                column.setPreferredWidth(65);
            }else if(i==8){
                column.setPreferredWidth(170);
            }else if(i==9){
                column.setPreferredWidth(170);
            }else if(i==10){
                column.setPreferredWidth(170);
            }else if(i==11){
                column.setPreferredWidth(170);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        diagnosa.setDocument(new batasInput((byte)50).getKata(diagnosa));
        saran.setDocument(new batasInput((byte)100).getKata(saran));

        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        } 
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){                   
                }  
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        ChkInput.setSelected(false);
        isForm();
        
    }
 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popup = new javax.swing.JPopupMenu();
        cetak_pemeriksaan_report_mcu = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
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
        TglLahir = new widget.TextBox();
        jLabel14 = new widget.Label();
        jLabel16 = new widget.Label();
        Alamat = new widget.TextBox();
        jLabel18 = new widget.Label();
        TglMCU = new widget.Tanggal();
        jLabel23 = new widget.Label();
        jLabel24 = new widget.Label();
        jLabel26 = new widget.Label();
        kesimpulan = new widget.ComboBox();
        jLabel33 = new widget.Label();
        jLabel34 = new widget.Label();
        scrollPane5 = new widget.ScrollPane();
        HasilLaborat = new widget.TextArea();
        jLabel44 = new widget.Label();
        scrollPane6 = new widget.ScrollPane();
        HasilRadiologi = new widget.TextArea();
        jLabel52 = new widget.Label();
        jLabel9 = new widget.Label();
        jLabel12 = new widget.Label();
        ekg = new widget.TextBox();
        jLabel27 = new widget.Label();
        jLabel11 = new widget.Label();
        hbsag = new widget.TextBox();
        jLabel28 = new widget.Label();
        jLabel13 = new widget.Label();
        pt = new widget.TextBox();
        jLabel29 = new widget.Label();
        jLabel19 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        diagnosa = new widget.TextArea();
        scrollPane3 = new widget.ScrollPane();
        saran = new widget.TextArea();

        popup.setName("popup"); // NOI18N

        cetak_pemeriksaan_report_mcu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/PrinterSettings.png"))); // NOI18N
        cetak_pemeriksaan_report_mcu.setText("Cetak Pemeriksaan Report MCU");
        cetak_pemeriksaan_report_mcu.setName("cetak_pemeriksaan_report_mcu"); // NOI18N
        cetak_pemeriksaan_report_mcu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cetak_pemeriksaan_report_mcuMouseReleased(evt);
            }
        });
        cetak_pemeriksaan_report_mcu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetak_pemeriksaan_report_mcuActionPerformed(evt);
            }
        });
        popup.add(cetak_pemeriksaan_report_mcu);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pemeriksaan MCU Oleh Dokter ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbObat.setAutoCreateRowSorter(true);
        tbObat.setComponentPopupMenu(popup);
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

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "09-02-2021" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        DTPCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DTPCari1ActionPerformed(evt);
            }
        });
        panelGlass7.add(DTPCari1);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("s.d");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(24, 23));
        panelGlass7.add(jLabel17);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "09-02-2021" }));
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
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(830, 713));
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
        NamaPasien.setBounds(321, 10, 497, 23);

        jLabel7.setText("No.Rawat :");
        jLabel7.setName("jLabel7"); // NOI18N
        FormInput.add(jLabel7);
        jLabel7.setBounds(0, 10, 72, 23);

        NoRawat.setEditable(false);
        NoRawat.setHighlighter(null);
        NoRawat.setName("NoRawat"); // NOI18N
        FormInput.add(NoRawat);
        NoRawat.setBounds(76, 10, 141, 23);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(728, 40, 90, 23);

        jLabel14.setText("Tgl.Lahir :");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(644, 40, 80, 23);

        jLabel16.setText("Alamat :");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 72, 23);

        Alamat.setEditable(false);
        Alamat.setHighlighter(null);
        Alamat.setName("Alamat"); // NOI18N
        FormInput.add(Alamat);
        Alamat.setBounds(80, 40, 557, 23);

        jLabel18.setText("Tgl.MCU :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(0, 70, 70, 23);

        TglMCU.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "09-02-2021" }));
        TglMCU.setDisplayFormat("dd-MM-yyyy");
        TglMCU.setName("TglMCU"); // NOI18N
        TglMCU.setOpaque(false);
        TglMCU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TglMCUActionPerformed(evt);
            }
        });
        TglMCU.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglMCUKeyPressed(evt);
            }
        });
        FormInput.add(TglMCU);
        TglMCU.setBounds(90, 70, 90, 23);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("Kesimpulan");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(40, 470, 60, 23);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText(":");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(110, 470, 10, 23);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText(":");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(110, 380, 10, 23);

        kesimpulan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FIT", "UNFIT", "FIT DENGAN PENGOBATAN" }));
        kesimpulan.setName("kesimpulan"); // NOI18N
        kesimpulan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                kesimpulanItemStateChanged(evt);
            }
        });
        kesimpulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kesimpulanActionPerformed(evt);
            }
        });
        kesimpulan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kesimpulanKeyPressed(evt);
            }
        });
        FormInput.add(kesimpulan);
        kesimpulan.setBounds(130, 470, 160, 23);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("Saran");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(40, 510, 50, 23);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText(":");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(110, 510, 10, 30);

        scrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane5.setName("scrollPane5"); // NOI18N

        HasilLaborat.setEditable(false);
        HasilLaborat.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        HasilLaborat.setColumns(20);
        HasilLaborat.setRows(5);
        HasilLaborat.setName("HasilLaborat"); // NOI18N
        scrollPane5.setViewportView(HasilLaborat);

        FormInput.add(scrollPane5);
        scrollPane5.setBounds(550, 110, 300, 110);

        jLabel44.setText("Hasil Lab :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(460, 110, 77, 23);

        scrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane6.setName("scrollPane6"); // NOI18N

        HasilRadiologi.setEditable(false);
        HasilRadiologi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        HasilRadiologi.setColumns(20);
        HasilRadiologi.setRows(5);
        HasilRadiologi.setName("HasilRadiologi"); // NOI18N
        scrollPane6.setViewportView(HasilRadiologi);

        FormInput.add(scrollPane6);
        scrollPane6.setBounds(140, 110, 300, 110);

        jLabel52.setText("Hasil Radiologi :");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(10, 110, 120, 23);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Diagnosa");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(40, 380, 200, 23);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Kesimpulan dan Saran Hasil Medical Report ");
        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(30, 340, 260, 23);

        ekg.setHighlighter(null);
        ekg.setName("ekg"); // NOI18N
        ekg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ekgActionPerformed(evt);
            }
        });
        ekg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ekgKeyPressed(evt);
            }
        });
        FormInput.add(ekg);
        ekg.setBounds(110, 260, 170, 40);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText(":");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(100, 270, 10, 23);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("EKG");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(60, 270, 200, 23);

        hbsag.setHighlighter(null);
        hbsag.setName("hbsag"); // NOI18N
        hbsag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hbsagActionPerformed(evt);
            }
        });
        hbsag.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hbsagKeyPressed(evt);
            }
        });
        FormInput.add(hbsag);
        hbsag.setBounds(380, 260, 170, 40);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText(":");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(370, 270, 10, 23);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("HBsAg");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(320, 270, 200, 23);

        pt.setHighlighter(null);
        pt.setName("pt"); // NOI18N
        pt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ptActionPerformed(evt);
            }
        });
        pt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ptKeyPressed(evt);
            }
        });
        FormInput.add(pt);
        pt.setBounds(680, 260, 170, 40);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText(":");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(660, 270, 10, 23);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Pregnancey Test");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(560, 270, 200, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        diagnosa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        diagnosa.setColumns(20);
        diagnosa.setRows(5);
        diagnosa.setName("diagnosa"); // NOI18N
        diagnosa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                diagnosaKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(diagnosa);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(130, 390, 541, 50);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setName("scrollPane3"); // NOI18N

        saran.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        saran.setColumns(20);
        saran.setRows(5);
        saran.setName("saran"); // NOI18N
        saran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saranKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(saran);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(130, 520, 541, 50);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(NoRM.getText().trim().equals("")||NamaPasien.getText().trim().equals("")){
            Valid.textKosong(NoRM,"Pasien");
        
        }else{
            if(Sequel.menyimpantf("pemeriksaan_mcu_dr", "?,?,?,?,?,?,?,?", "No.Rawat", 8, new String[]{
                    NoRawat.getText(), 
                    Valid.SetTgl(TglMCU.getSelectedItem() + ""),
                    diagnosa.getText(),
                    kesimpulan.getSelectedItem().toString(),
                    saran.getText(),
                    ekg.getText(),
                    hbsag.getText(),
                    pt.getText(),
                   }) == true) {

                    emptTeks();
                    tampil();
                }
            }
        
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
           //Valid.pindah(evt,Comorbid,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm(); 
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TglMCU.requestFocus();
        }else if(NamaPasien.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\n Klik data pada table untuk memilih...!!!!");
        }else if(!(NamaPasien.getText().trim().equals(""))){
            try{
                Sequel.queryu("delete from pemeriksaan_mcu_dr " +
                        "where no_rawat='"+NoRawat.getText()+"' " +
                        "and tgl_mcu='"+Valid.SetTgl(TglMCU.getSelectedItem()+"")+"' " +
                        "and diagnosa='"+diagnosa.getText()+"' " +
                        "and kesimpulan='"+kesimpulan.getSelectedItem()+"' " +
                        "and saran='"+saran.getText()+"' " +
                        "and ekg='"+ekg.getText()+"'"+
                        "and hbsag='"+hbsag.getText()+"'"+
                        "and pt='"+pt.getText()+"'");
                tampil();
                emptTeks();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih terlebih dulu data yang mau anda hapus...\n Klik data pada table untuk memilih data...!!!!");
            }
        }


//        if(tbObat.getSelectedRow()> -1){ 
//            Sequel.meghapus("pemeriksaan_mcu_dr","no_rawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
//            tampil();
//        }else{
//            JOptionPane.showMessageDialog(null,"Maaf silahkan pilih data terlebih dahulu..!!");
//        }
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
           
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, NamaPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
  isForm();                
}//GEN-LAST:event_ChkInputActionPerformed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(NoRM.getText().trim().equals("")||NamaPasien.getText().trim().equals("")){
            Valid.textKosong(NoRM,"Pasien");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(Sequel.mengedittf("pemeriksaan_mcu_dr","no_rawat=?","no_rawat=?,tgl_mcu=?,diagnosa=?,kesimpulan=?,saran=?,ekg=?,hbsag=?,pt=?",9,new String[]{
                    NoRawat.getText(), 
                    Valid.SetTgl(TglMCU.getSelectedItem()+""),
                    diagnosa.getText(),
                    kesimpulan.getSelectedItem().toString(),
                    saran.getText(),
                    ekg.getText(),
                    hbsag.getText(),
                    pt.getText(),
                    tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
                    })==true){
                       tampil();
                       emptTeks();
                }
            }else{
                JOptionPane.showMessageDialog(null,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }




//        if(NoRM.getText().trim().equals("")||NamaPasien.getText().trim().equals("")){
//            Valid.textKosong(NoRM,"Pasien");
////        }else{
//            if(tbObat.getSelectedRow()> -1){ 
//                if(Sequel.mengedittf("pemeriksaan_mcu_dr","no_rawat=?","no_rawat=?,tgl_mcu=?,diagnosa=?,kesimpulan=?,saran=?,saran=?,ekg=?,hbsag=?,pt=?",10,new String[]{
//                        NoRawat.getText(),Valid.SetTgl(TglMCU.getSelectedItem()+""),kesimpulan.getSelectedItem().toString(),
//                        tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
//                    })!=true){
//                } else {
//                    emptTeks();
//                    tampil();
//                }
//            }else{
//                JOptionPane.showMessageDialog(null,"Maaf silahkan pilih data terlebih dahulu..!!");
//            }
//        }
    }//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
    }//GEN-LAST:event_BtnEditKeyPressed

    private void cetak_pemeriksaan_report_mcuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetak_pemeriksaan_report_mcuActionPerformed
                                                    
        if (NamaPasien.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Maaf, Silahkan anda pilih dulu pasien...!!!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
             
             param.put("tgl_mcu", Sequel.cariIsi("select date_format(tgl_mcu,'%d-%m-%Y') as tgl_mcu from pemeriksaan_mcu_dr where no_rawat=?", NoRawat.getText()));
             param.put("ekg", Sequel.cariIsi("select ekg from pemeriksaan_mcu_dr where no_rawat=?", NoRawat.getText()));
             param.put("hbsag", Sequel.cariIsi("select hbsag from pemeriksaan_mcu_dr where no_rawat=?", NoRawat.getText()));
             param.put("pt", Sequel.cariIsi("select pt from pemeriksaan_mcu_dr where no_rawat=?", NoRawat.getText()));
             param.put("diagnosa", Sequel.cariIsi("select diagnosa from pemeriksaan_mcu_dr where no_rawat=?", NoRawat.getText()));
             param.put("kesimpulan", Sequel.cariIsi("select kesimpulan from pemeriksaan_mcu_dr where no_rawat=?", NoRawat.getText()));
             param.put("saran", Sequel.cariIsi("select saran from pemeriksaan_mcu_dr where no_rawat=?", NoRawat.getText()));
             param.put("hasil_rd", Sequel.cariIsi(" select hasil from hasil_radiologi where  no_rawat=? ", NoRawat.getText()));
             param.put("lab1", Sequel.cariIsiList(" select Pemeriksaan from detail_periksa_lab inner join template_laboratorium ON detail_periksa_lab.id_template=template_laboratorium.id_template where detail_periksa_lab.no_rawat=?", NoRawat.getText()));
             param.put("lab2", Sequel.cariIsiList(" select nilai  from detail_periksa_lab inner join template_laboratorium ON detail_periksa_lab.id_template=template_laboratorium.id_template where detail_periksa_lab.no_rawat=?", NoRawat.getText()));
//param.put("td", Sequel.cariIsi("select tensi from pemeriksaan_ralan where no_rawat=?", TNoRw.getText()));
            //param.put("tb", Sequel.cariIsi("select tinggi from pemeriksaan_ralan where no_rawat=?", TNoRw.getText()));
            param.put("logo", Sequel.cariGambar("select logo from setting"));
            param.put("bismillah", Sequel.cariGambar("select gambar FROM testgambar WHERE nm_gambar='bismillah' "));
            Valid.MyReportqry("rptmcureport.jasper", "report", "::[ Cetak Pemeriksaan MCU report]::",
                    "SELECT pemeriksaan_mcu_dr.no_rawat, \n" +
                    " reg_periksa.no_rkm_medis,\n" +
                    " pasien.nm_pasien,\n" +
                    " pasien.alamat,\n" +
                    " date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,\n" +
                    " if (pasien.jk='L','Laki-laki','Perempuan') AS jk,\n" +
                    " dokter.nm_dokter, \n" +
                    " date_format(pemeriksaan_mcu_dr.tgl_mcu,'%d-%m-%Y') as tgl_mcu, \n" +
                    " pemeriksaan_mcu_dr.ekg,\n" +
                    "pemeriksaan_mcu_dr.hbsag,\n" +
                    "pemeriksaan_mcu_dr.pt,\n" +
                    "pemeriksaan_mcu_dr.diagnosa,\n" +
                    "pemeriksaan_mcu_dr.kesimpulan,\n" +
                    "pemeriksaan_mcu_dr.saran\n" +
                    "FROM reg_periksa inner join pemeriksaan_mcu_dr inner join pasien inner join dokter on\n" +
                    "reg_periksa.no_rawat=pemeriksaan_mcu_dr.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis\n" +
                    "AND reg_periksa.kd_dokter=dokter.kd_dokter\n" +
                    "where reg_periksa.no_rawat='" + NoRawat.getText() + "' ", param);
                                this.setCursor(Cursor.getDefaultCursor());
        }

    }//GEN-LAST:event_cetak_pemeriksaan_report_mcuActionPerformed

    private void ptKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ptKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ptKeyPressed

    private void ptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ptActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ptActionPerformed

    private void hbsagKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hbsagKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_hbsagKeyPressed

    private void hbsagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hbsagActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hbsagActionPerformed

    private void ekgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ekgKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ekgKeyPressed

    private void ekgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ekgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ekgActionPerformed

    private void kesimpulanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kesimpulanKeyPressed

    }//GEN-LAST:event_kesimpulanKeyPressed

    private void kesimpulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kesimpulanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kesimpulanActionPerformed

    private void kesimpulanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_kesimpulanItemStateChanged
        isHitung();
    }//GEN-LAST:event_kesimpulanItemStateChanged

    private void TglMCUKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglMCUKeyPressed

    }//GEN-LAST:event_TglMCUKeyPressed

    private void TglMCUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TglMCUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TglMCUActionPerformed

    private void diagnosaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diagnosaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(evt.isShiftDown()){
                diagnosa.requestFocus();
            }
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            diagnosa.requestFocus();
        }
    }//GEN-LAST:event_diagnosaKeyPressed

    private void saranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saranKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_saranKeyPressed

    private void DTPCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DTPCari1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPCari1ActionPerformed

    private void cetak_pemeriksaan_report_mcuMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cetak_pemeriksaan_report_mcuMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cetak_pemeriksaan_report_mcuMouseReleased

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMpemeriksaanMCUdokter dialog = new RMpemeriksaanMCUdokter(new javax.swing.JFrame(), true);
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
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.PanelBiasa FormInput;
    private widget.TextArea HasilLaborat;
    private widget.TextArea HasilRadiologi;
    private widget.Label LCount;
    private widget.TextBox NamaPasien;
    private widget.TextBox NoRM;
    private widget.TextBox NoRawat;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TglLahir;
    private widget.Tanggal TglMCU;
    private javax.swing.JMenuItem cetak_pemeriksaan_report_mcu;
    private widget.TextArea diagnosa;
    private widget.TextBox ekg;
    private widget.TextBox hbsag;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel44;
    private widget.Label jLabel52;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private widget.ComboBox kesimpulan;
    private widget.panelisi panelGlass7;
    private widget.panelisi panelGlass8;
    private javax.swing.JPopupMenu popup;
    private widget.TextBox pt;
    private widget.TextArea saran;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    public void tampil() {     
        Valid.tabelKosong(tabMode);
        try{    
            ps=koneksi.prepareStatement(
                    "SELECT pemeriksaan_mcu_dr.no_rawat, \n" +
"pasien.no_rkm_medis, \n" +
"pasien.nm_pasien, \n" +
"pasien.alamat, \n" +
"pasien.tgl_lahir,\n" +
"pemeriksaan_mcu_dr.tgl_mcu,\n" +
"pemeriksaan_mcu_dr.diagnosa,\n" +
"pemeriksaan_mcu_dr.kesimpulan,\n" +
"pemeriksaan_mcu_dr.saran,\n" +
"pemeriksaan_mcu_dr.ekg,\n" +
"pemeriksaan_mcu_dr.hbsag,\n" + 
"pemeriksaan_mcu_dr.pt\n" +
                            
"FROM  reg_periksa INNER JOIN pemeriksaan_mcu_dr INNER JOIN pasien ON\n" +
"reg_periksa.no_rawat=pemeriksaan_mcu_dr.no_rawat AND reg_periksa.no_rkm_medis=pasien.no_rkm_medis \n"+
"where  pemeriksaan_mcu_dr.tgl_mcu between ? and ? "+
                    (TCari.getText().trim().equals("")?"":" and pasien.no_rkm_medis like ? or pemeriksaan_mcu_dr.no_rawat like ? or pasien.nm_pasien like ? order by pemeriksaan_mcu_dr.tgl_mcu"));
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().trim().equals("")){
                    ps.setString(3,"%"+TCari.getText().trim()+"%");
                    ps.setString(4,"%"+TCari.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText().trim()+"%");
//                    ps.setString(6,"%"+TCari.getText().trim()+"%");
//                    ps.setString(7,"%"+TCari.getText().trim()+"%");
//                    ps.setString(8,"%"+TCari.getText().trim()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){    
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"), 
                        rs.getString("no_rkm_medis"), 
                        rs.getString("nm_pasien"),
                        rs.getString("alamat"), 
                        rs.getString("tgl_lahir"),
                        rs.getString("tgl_mcu"),
                        rs.getString("ekg"),
                        rs.getString("hbsag"),
                        rs.getString("pt"),
                        rs.getString("diagnosa"),
                        rs.getString("kesimpulan"),
                        rs.getString("saran")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        NoRM.setText("");
        NamaPasien.setText("");
        NoRawat.setText("");
      
        TglLahir.setText("");
        Alamat.setText("");
        TglMCU.setDate(new Date());
        ekg.setText("");
        hbsag.setText("");
        pt.setText("");
       
    
        diagnosa.setText("");
        kesimpulan.setSelectedIndex(0);
        saran.setText("");
        
        HasilLaborat.setText("");
        HasilRadiologi.setText("");
        ChkInput.setSelected(true);
        isForm(); 
       
    }

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){      
            NoRawat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            NoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            NamaPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            ekg.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            hbsag.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            pt.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            diagnosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            kesimpulan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            saran.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
           
//            isLabRad(NoRawat.getText());
//            Valid.SetTgl(TglMCU,tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
//           
           
        }
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,this.getHeight()-122));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getdeteksi_corona());
        BtnHapus.setEnabled(akses.getdeteksi_corona());
        BtnEdit.setEnabled(akses.getdeteksi_corona());
        if(akses.getjml2()>=1){
         
        }
    }
    
    public void setNoRm(String norawat,Date tgl2){
        NoRawat.setText(norawat);
        TCari.setText(norawat);
        try {
            ps=koneksi.prepareStatement(
                    "select pasien.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"+
                    "pasien.no_ktp,pasien.no_tlp,pasien.pekerjaan,pasien.tgl_lahir "+
                    "from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                    "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                    "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                    "inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis where reg_periksa.no_rawat=?");
            try {            
                ps.setString(1,norawat);
                rs=ps.executeQuery();
                while(rs.next()){
                    NoRM.setText(rs.getString("no_rkm_medis"));
                    NamaPasien.setText(rs.getString("nm_pasien"));
                    
                    Alamat.setText(rs.getString("asal"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }finally{
                if(rs != null ){
                    rs.close();
                }
                
                if(ps != null ){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        DTPCari2.setDate(tgl2);  
        isLabRad(norawat);
    }
    
    private void isLabRad(String norawat){
        try{
            HasilRadiologi.setText("");
            ps=koneksi.prepareStatement(
                    "select hasil from hasil_radiologi where "+
                    "no_rawat=? ");
            try{
                ps.setString(1,norawat);
                rs=ps.executeQuery();
                while(rs.next()){
                    HasilRadiologi.append(rs.getString("hasil")+"\n\n");
                }
            }catch(Exception ex){
                System.out.println(ex);
            }finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        
        try{
            HasilLaborat.setText("");
            ps=koneksi.prepareStatement(
                    "select template_laboratorium.Pemeriksaan, detail_periksa_lab.nilai "+
                    "from detail_periksa_lab inner join template_laboratorium on detail_periksa_lab.id_template=template_laboratorium.id_template where "+
                    "detail_periksa_lab.no_rawat=?");
            try{
                ps.setString(1,norawat);
                rs=ps.executeQuery();
                while(rs.next()){
                    HasilLaborat.append(rs.getString("Pemeriksaan")+" : "+rs.getString("nilai")+"\n");
                }
            }catch(Exception ex){
                System.out.println(ex);
            }finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public JTable getTable(){
        return tbObat;
    }
    
    private void isHitung(){
        gejala1=0;gejala2=0;gejala3=0;resiko1=0;resiko2a=0;resiko2b=0;resiko2c=0;resiko2d=0;resiko2e=0;gejala=0;resiko=0;
        if(kesimpulan.getSelectedItem().toString().equals("Ya")){
            resiko1=1;
        }
 
        
        gejala=gejala1+gejala2+gejala3;
        resiko=resiko1+resiko2a+resiko2b+resiko2c+resiko2d+resiko2e;
        if((gejala==5)&&(resiko==1)){
        }
    }
}
