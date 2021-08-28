/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */

package simrskhanza;

import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import inventory.DlgCariKonversi;
import inventory.DlgCariObat;
import inventory.DlgCariObat2;
import inventory.DlgCariObat3;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author perpustakaan
 */
public class DlgAktivasi extends javax.swing.JDialog {
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;

    /** Creates new form DlgPemberianObat
     * @param parent
     * @param modal */
    public DlgAktivasi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(885,674);
        
        //TCatatan.setText(TCatatan); 
//        TCatatan.setLineWrap(true);
//        TCatatan.setWrapStyleWord(true);
        PasSportifc.setDocument(new batasInput((byte) 60).getKata(PasSportifc));
        NoUrut.setVisible(false);
    }

    //private DlgCariObatPenyakit dlgobtpny=new DlgCariObatPenyakit(null,false);
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        FormInput = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        TNoRM = new widget.TextBox();
        TPasien = new widget.TextBox();
        jLabel4 = new widget.Label();
        PasSportifc = new widget.TextBox();
        NoUrut = new widget.TextBox();
        jLabel10 = new widget.Label();
        jLabel11 = new widget.Label();
        jLabel12 = new widget.Label();
        NoPasien = new widget.TextBox();
        SttsOnline = new widget.TextBox();
        jLabel13 = new widget.Label();
        DigitCode = new widget.TextBox();
        jLabel5 = new widget.Label();
        jLabel14 = new widget.Label();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnKeluar = new widget.Button();
        jLabel9 = new widget.Label();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput.setLayout(null);

        jLabel3.setText("Code 6 Digit : ");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput.add(jLabel3);
        jLabel3.setBounds(470, 70, 90, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput.add(TNoRM);
        TNoRM.setBounds(90, 70, 80, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.setPreferredSize(new java.awt.Dimension(25, 28));
        FormInput.add(TPasien);
        TPasien.setBounds(180, 70, 300, 23);

        jLabel4.setText("Status Online : ");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 40, 90, 23);

        PasSportifc.setHighlighter(null);
        PasSportifc.setName("PasSportifc"); // NOI18N
        FormInput.add(PasSportifc);
        PasSportifc.setBounds(120, 100, 120, 23);

        NoUrut.setEditable(false);
        NoUrut.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        NoUrut.setForeground(new java.awt.Color(255, 255, 255));
        NoUrut.setCaretColor(new java.awt.Color(255, 255, 255));
        NoUrut.setHighlighter(null);
        NoUrut.setName("NoUrut"); // NOI18N
        FormInput.add(NoUrut);
        NoUrut.setBounds(590, 40, 50, 23);

        jLabel10.setText("Password SPORTIFC : ");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 100, 120, 23);

        jLabel11.setText("Pastikan Data Sosial Pasien Sudah Lengkap, dan pastikan no.Hp yang terdaftar aktif");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(170, 40, 410, 23);

        jLabel12.setText("Harus Aktif WA/SMS");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(530, 100, 100, 23);

        NoPasien.setEditable(false);
        NoPasien.setHighlighter(null);
        NoPasien.setName("NoPasien"); // NOI18N
        FormInput.add(NoPasien);
        NoPasien.setBounds(410, 100, 120, 23);

        SttsOnline.setEditable(false);
        SttsOnline.setHighlighter(null);
        SttsOnline.setName("SttsOnline"); // NOI18N
        FormInput.add(SttsOnline);
        SttsOnline.setBounds(90, 40, 80, 23);

        jLabel13.setText("No. Telepon Pasien : ");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(290, 100, 120, 23);

        DigitCode.setEditable(false);
        DigitCode.setHighlighter(null);
        DigitCode.setName("DigitCode"); // NOI18N
        FormInput.add(DigitCode);
        DigitCode.setBounds(560, 70, 70, 23);

        jLabel5.setText("No.R.M. :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(20, 70, 65, 23);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Form Aktivasi Akun SPORTIFC");
        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(230, 10, 220, 17);

        internalFrame1.add(FormInput, java.awt.BorderLayout.CENTER);
        FormInput.getAccessibleContext().setAccessibleName("");
        FormInput.getAccessibleContext().setAccessibleDescription("");

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(100, 56));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/checked.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Aktivasi");
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

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnKeluar.setMnemonic('T');
        BtnKeluar.setText("Tutup");
        BtnKeluar.setToolTipText("Alt+T");
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

        jLabel9.setText("Pastikan No.HP yang terdaftar hanya satu nomor");
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        panelGlass8.add(jLabel9);

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(PasSportifc.getText().trim().equals("")){
            Valid.textKosong(PasSportifc,"Password");
        }else if (Sequel.cariInteger("select count(pasien_id) from sms_code where pasien_id=?", TNoRM.getText()) > 0) {
                JOptionPane.showMessageDialog(null, "Maaf, Pasien sudah melakukan Aktivasi..!!!");
        }else{
            isNumber();
            Sequel.menyimpan("sms_code"," '"+NoUrut.getText()+"','000000','"+Valid.DateToString(new Date())+"','(NULL)','','"+PasSportifc.getText()+"','ONLINE','"+PasSportifc.getText()+"','"+TNoRM.getText()+"','"+Valid.DateToString(new Date())+"' ");
            Sequel.mengedit("pasien", "no_rkm_medis=?", "online='ONLINE'", 1, new String[]{TNoRM.getText()});
            BtnKeluarActionPerformed(evt);        
        } 
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        
    }//GEN-LAST:event_formWindowActivated

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgAktivasi dialog = new DlgAktivasi(new javax.swing.JFrame(), true);
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
    private widget.Button BtnKeluar;
    private widget.Button BtnSimpan;
    private widget.TextBox DigitCode;
    private widget.PanelBiasa FormInput;
    private widget.TextBox NoPasien;
    private widget.TextBox NoUrut;
    private widget.TextBox PasSportifc;
    private widget.TextBox SttsOnline;
    private widget.TextBox TNoRM;
    private widget.TextBox TPasien;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel3;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel9;
    private widget.panelisi panelGlass8;
    // End of variables declaration//GEN-END:variables
    

    private void isPsien() {
        Sequel.cariIsi("select nm_pasien from pasien where no_rkm_medis=? ",TPasien,TNoRM.getText());
    }

    public void setNoRm(String norm) {
        TNoRM.setText(norm);  
        isPsien();   
        Sequel.cariIsi("select password from sms_code where pasien_id=?",PasSportifc,TNoRM.getText());
        Sequel.cariIsi("select status from sms_code where pasien_id=?",SttsOnline,TNoRM.getText());
        Sequel.cariIsi("select code from sms_code where pasien_id=?",DigitCode,TNoRM.getText());
        Sequel.cariIsi("select no_tlp from pasien where no_rkm_medis=?",NoPasien,TNoRM.getText());
        //Sequel.cariIsi("SELECT max(id)+1 AS code_id FROM sms_code",NoUrut);
    }
    
    
    public void isCek(){
        BtnSimpan.setEnabled(true);
//        BtnHapus.setEnabled(true);
//        BtnEdit.setEnabled(true);
    }
       
    private void isNumber() {
        Sequel.cariIsi("SELECT max(id)+1 AS code_id FROM sms_code",NoUrut);
    
    }
        
    }