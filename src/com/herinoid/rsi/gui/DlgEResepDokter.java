/*
  
 */
package com.herinoid.rsi.gui;

import com.herinoid.rsi.dao.ObatDao;
import com.herinoid.rsi.dao.ResepDao;
import static com.herinoid.rsi.dao.ResepDao.getNoResepForUpdate;
import com.herinoid.rsi.gui.dialog.DialogAddQtyResepDokter;
import com.herinoid.rsi.gui.dialog.DialogRacikanResep;
import com.herinoid.rsi.model.Obat;
import com.herinoid.rsi.model.ObatResep;
import com.herinoid.rsi.model.PemeriksaanRalan;
import com.herinoid.rsi.model.Resep;
import com.herinoid.rsi.model.RegPeriksa;
import com.herinoid.rsi.dao.RegPeriksaDao;
import com.herinoid.rsi.table.TabelCariObatResep;
import com.herinoid.rsi.table.TabelObatResepPilihan;
import com.herinoid.rsi.util.Konstan;
import fungsi.validasi;
import inventory.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import kepegawaian.DlgCariDokter;
import com.herinoid.rsi.dao.BangsalDao;
import com.herinoid.rsi.dao.DisplayDao;
import com.herinoid.rsi.dao.MarginDao;
import com.herinoid.rsi.gui.dialog.DlgHistoriResepPasien;
import com.herinoid.rsi.gui.dialog.DlgTemplateResepDokter;
import com.herinoid.rsi.model.Bangsal;
import com.herinoid.rsi.model.ClientData;
import com.herinoid.rsi.model.DisplayFarmasi;
import com.herinoid.rsi.model.MarginBpjs;
import com.herinoid.rsi.model.MarginObatNonBpjs;
import com.herinoid.rsi.model.api.BaseResponse;
import com.herinoid.rsi.model.api.CreateResepRequest;
import com.herinoid.rsi.model.api.RestFull;
import com.herinoid.rsi.session.SessionLogin;
import com.herinoid.rsi.util.Utils;
import fungsi.sekuel;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.Header;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author herinoid
 */
public final class DlgEResepDokter extends javax.swing.JDialog {

    private TabelCariObatResep model;
    private TabelObatResepPilihan modelPilihan;
    private TableRowSorter<TableModel> rowSorter;
    private DialogAddQtyResepDokter addQty = new DialogAddQtyResepDokter(null, false);
    private DlgHistoriResepPasien cekHistory = new DlgHistoriResepPasien(null, false);
    private DlgTemplateResepDokter templateResep = new DlgTemplateResepDokter(null, false);
    private ObatResep obatFromDialog;
    private ObatResep obatRacikan;
    private List<ObatResep> racikanList;
    private DialogRacikanResep dlgRacikan = new DialogRacikanResep(null, false);
    private DlgCariDokter dokter = new DlgCariDokter(null, false);
    private validasi Valid = new validasi();
    private String jenisPasien;
    private String depoKode, kdJaminan, jaminan;
    private double total;
    private sekuel Sequel = new sekuel();
    private String kodePoli;
    private Properties pro = new Properties();
    private String wsurl;

    /**
     * Creates new form DlgPenyakit
     *
     * @param parent
     * @param modal
     */
    public DlgEResepDokter(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        try {
            initComponents();
            racikanList = new LinkedList<>();
            model = new TabelCariObatResep();
            modelPilihan = new TabelObatResepPilihan();
            this.setLocation(10, 2);
            setSize(656, 250);
            pro.loadFromXML(new FileInputStream("setting/database.xml"));
            txtCari.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    String text = txtCari.getText();

                    if (text.trim().length() == 0) {
                        rowSorter.setRowFilter(null);
                    } else {
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    String text = txtCari.getText();

                    if (text.trim().length() == 0) {
                        rowSorter.setRowFilter(null);
                    } else {
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            });

            addQty.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    obatFromDialog = addQty.getData();
                    if (obatFromDialog != null) {
                        if (obatFromDialog.isFlag()) {
                            if (obatFromDialog.isEdit()) {
                                modelPilihan.remove(tblPilihan.getSelectedRow());
                            }

                            double marginPersen = 28;
                            if (jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)) {
                                MarginBpjs marginBpjs = MarginDao.getMarginBpjs(obatFromDialog.getKodeObat());
                                if (marginBpjs != null) {
                                    marginPersen = marginBpjs.getRalan();
                                }
                            } else {
                                MarginObatNonBpjs marginNon = MarginDao.getMarginNonBpjs(kdJaminan);
                                if (marginNon != null) {
                                    marginPersen = marginNon.getMargin();
                                }
                            }
                            double margin = (obatFromDialog.getHargaBeli() * marginPersen) / 100;
                            double hpp = margin + obatFromDialog.getHargaBeli();
                            total = total + (hpp * obatFromDialog.getJumlah());
                            lblTotal.setText(Utils.format(total, 2));

                            removeDuplicate(obatFromDialog);
                            tblPilihan.setModel(modelPilihan);
                        }
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
            dlgRacikan.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    obatFromDialog = dlgRacikan.getData();
                    if (obatFromDialog != null) {
                        if (obatFromDialog.isFlag()) {
                            if (obatFromDialog.isEdit()) {
                                modelPilihan.remove(tblPilihan.getSelectedRow());
                            } else {
                                removeDuplicateRacikans(obatFromDialog);
                            }
                            modelPilihan.add(obatFromDialog);
                            tblPilihan.setModel(modelPilihan);
//
                        }
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

            dokter.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    if (dokter.getTable().getSelectedRow() != -1) {
                        txtKodeDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        txtNamaDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    }
                    txtKodeDokter.requestFocus();
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
            jam();

            cekHistory.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    if (cekHistory.isDuplikate()) {
                        List<ObatResep> obats = cekHistory.getData();
                        List<ObatResep> obatAmbils = new LinkedList<>();
                        List<String> namaObat = new ArrayList<>();
                        if (obats != null && obats.size() > 0) {
                            for (ObatResep o : obats) {
                                if (o.getStatus() == 1) {
                                    if (jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)) {
                                        if (o.getKategori().equals("FORNAS PLUS") && !o.isParent()) {
                                            obatAmbils.add(o);
                                        } else if (o.getKategori().equals("NON FORNAS") && !o.isParent()) {
                                            namaObat.add(o.getNamaObat());
                                        }
                                    } else {
                                        obatAmbils.add(o);
                                    }
                                } else {
                                    namaObat.add(o.getNamaObat());
                                }

                                double marginPersen = 28;
                                if (jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)) {
                                    MarginBpjs marginBpjs = MarginDao.getMarginBpjs(o.getKodeObat());
                                    if (marginBpjs != null) {
                                        marginPersen = marginBpjs.getRalan();
                                    }

                                } else {
                                    MarginObatNonBpjs marginNon = MarginDao.getMarginNonBpjs(kdJaminan);
                                    if (marginNon != null) {
                                        marginPersen = marginNon.getMargin();
                                    }

                                }
                                double margin = (o.getHargaBeli() * marginPersen) / 100;
                                double hpp = margin + o.getHargaBeli();
                                total = total + (hpp * o.getJumlah());
                                lblTotal.setText(Utils.format(total, 2));
                            }
                            if (namaObat.size() > 0) {
                                int oke = JOptionPane.showConfirmDialog(null, "Data obat ini : " + namaObat.toString() + " tidak bisa diambil, apakah mau di lanjut?", "Perhatian", JOptionPane.YES_NO_OPTION);
                                if (oke == 0) {
                                    modelPilihan.add(obatAmbils);
                                    tblPilihan.setModel(modelPilihan);
                                }
                            } else {
                                modelPilihan.add(obatAmbils);
                                tblPilihan.setModel(modelPilihan);
                            }
                            cekHistory.clean();
                        }
                    } else {
                        cekHistory.clean();
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

            templateResep.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    if (!templateResep.batal()) {
                        List<ObatResep> obats = templateResep.getData();
                        List<ObatResep> obatAmbils = new LinkedList<>();
                        List<String> namaObat = new ArrayList<>();
                        if (obats != null && obats.size() > 0) {
                            modelPilihan.add(obats);
                            for (ObatResep o : obats) {
                                if (o.isParent()) {
                                    removeDuplicateRacikans(o);
                                } else {
                                    if (o.getStatus() == 1) {
                                        if (jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)) {
                                            if (o.getKategori().equals("FORNAS PLUS") && !o.isParent()) {
                                                obatAmbils.add(o);
                                            } else if (o.getKategori().equals("NON FORNAS") && !o.isParent()) {
                                                namaObat.add(o.getNamaObat());
                                            }
                                        } else {
                                            obatAmbils.add(o);
                                        }
                                    } else {
                                        namaObat.add(o.getNamaObat());
                                    }
                                }
                                double marginPersen = 28;
                                if (jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)) {
                                    MarginBpjs marginBpjs = MarginDao.getMarginBpjs(o.getKodeObat());
                                    if (marginBpjs != null) {
                                        marginPersen = marginBpjs.getRalan();
                                    }

                                } else {
                                    MarginObatNonBpjs marginNon = MarginDao.getMarginNonBpjs(kdJaminan);
                                    if (marginNon != null) {
                                        marginPersen = marginNon.getMargin();
                                    }

                                }
                                double margin = (o.getHargaBeli() * marginPersen) / 100;
                                double hpp = margin + o.getHargaBeli();
                                total = total + (hpp * o.getJumlah());
                                lblTotal.setText(Utils.format(total, 2));
                            }
                            if (namaObat.size() > 0) {
                                int oke = JOptionPane.showConfirmDialog(null, "Data obat ini : " + namaObat.toString() + " tidak bisa diambil, apakah mau di lanjut?", "Perhatian", JOptionPane.YES_NO_OPTION);
                                if (oke == 0) {
                                    modelPilihan.add(obatAmbils);
                                    tblPilihan.setModel(modelPilihan);
                                }
                            } else {
                                modelPilihan.add(obatAmbils);
                                tblPilihan.setModel(modelPilihan);
                            }
                            templateResep.clean();
                        }
                    } else {
                        templateResep.clean();
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
        } catch (IOException ex) {
            Logger.getLogger(DlgEResepDokter.class.getName()).log(Level.SEVERE, null, ex);
        }
        wsurl = pro.getProperty("WS_URL");
    }

    public void setData(String kdDokter, String nmDokter, String kodeDepo, String kategoriObat, String jenisPasien, PemeriksaanRalan periksa) {
        Bangsal bangsal = BangsalDao.get(kodeDepo);
        this.depoKode = kodeDepo;
        lblDepo.setText(bangsal.getNama());
        model.removeAllElements();
        if (kategoriObat.equals("K01")) {
            model.add(ObatDao.getObatByCategory(kodeDepo, kategoriObat, jenisPasien));
        } else {
            model.add(ObatDao.getObatByDepo(kodeDepo, jenisPasien));
        }

        tblObat.setModel(model);
        rowSorter = new TableRowSorter<>(tblObat.getModel());
        tblObat.setRowSorter(rowSorter);
        txtKodeDokter.setText(kdDokter);
        txtNamaDokter.setText(nmDokter);
        lbBB.setText(periksa.getBeratBadan() == null ? " kg" : periksa.getBeratBadan() + " kg");
        lbTB.setText(periksa.getTinggiBadan() == null ? " cm" : periksa.getTinggiBadan() + " cm");
        lbSuhuBadan.setText(periksa.getSuhuTubuh());
        lbTekanDarah.setText(periksa.getTekanDarah());
        lbAlergi.setText(periksa.getAlergi());
    }

    public void setPasien(String norawat, String norm, String nmPasien, String jaminan, String jenisPasien, String poli) {
        RegPeriksa reg = RegPeriksaDao.get(norawat);
        this.kodePoli = reg.getKdPoli();
        LblNoRawat.setText(norawat);
        LblNoRM.setText(norm);
        LblNamaPasien.setText(nmPasien);
        lblJaminan.setText(jaminan);
        this.jaminan = jaminan;
        this.kdJaminan = reg.getKdPj();
        lblPoli.setText(poli);
        this.jenisPasien = jenisPasien;
    }

    private void removeDuplicate(ObatResep obatResep) {
        for (Iterator<ObatResep> i = modelPilihan.getAll().iterator(); i.hasNext();) {
            ObatResep obat = i.next();
            if (obat.getKodeObat().equals(obatResep.getKodeObat())) {
                int index = modelPilihan.getAll().indexOf(obat);
                modelPilihan.remove(index);
                break;
            }
        }
        modelPilihan.add(obatResep);
    }

    private void removeDuplicateRacikans(ObatResep obatResep) {
        for (Iterator<ObatResep> i = racikanList.iterator(); i.hasNext();) {
            ObatResep obat = i.next();
            if (obat.getKodeObat().equals(obatResep.getKodeObat())) {
                int index = racikanList.indexOf(obat);
                racikanList.remove(index);
                break;
            }
        }
        
        racikanList.add(obatResep);
    }

    private void jam() {
        ActionListener taskPerformer = new ActionListener() {
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;

            @Override
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                // Membuat Date
                //Date dt = new Date();
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if (ChkJln.isSelected() == true) {
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                } else if (ChkJln.isSelected() == false) {
                    nilai_jam = cmbJam.getSelectedIndex();
                    nilai_menit = cmbMnt.getSelectedIndex();
                    nilai_detik = cmbDtk.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                cmbJam.setSelectedItem(jam);
                cmbMnt.setSelectedItem(menit);
                cmbDtk.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popup = new javax.swing.JPopupMenu();
        ppBersihkan = new javax.swing.JMenuItem();
        ppStok = new javax.swing.JMenuItem();
        ppStok1 = new javax.swing.JMenuItem();
        popObatPilihan = new javax.swing.JPopupMenu();
        mnEditObat = new javax.swing.JMenuItem();
        mnHapusItem = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        panelisi3 = new widget.panelisi();
        BtnTambah = new widget.Button();
        btnHistory = new widget.Button();
        btnTemplateResep = new widget.Button();
        label13 = new widget.Label();
        btnSimpan = new widget.Button();
        BtnKeluar = new widget.Button();
        FormInput = new widget.PanelBiasa();
        jLabel5 = new widget.Label();
        cmbTanggal = new widget.Tanggal();
        cmbJam = new widget.ComboBox();
        cmbMnt = new widget.ComboBox();
        cmbDtk = new widget.ComboBox();
        label21 = new widget.Label();
        txtKodeDokter = new widget.TextBox();
        txtNamaDokter = new widget.TextBox();
        BtnPilihDokter = new widget.Button();
        jLabel10 = new widget.Label();
        LblNoRawat = new widget.TextBox();
        jLabel11 = new widget.Label();
        LblNamaPasien = new widget.TextBox();
        lbAlergi = new widget.Label();
        jLabel14 = new widget.Label();
        lblJaminan = new widget.Label();
        ChkJln = new widget.CekBox();
        LblNoRM = new widget.TextBox();
        jLabel13 = new widget.Label();
        jLabel15 = new widget.Label();
        jLabel16 = new widget.Label();
        jLabel17 = new widget.Label();
        jLabel18 = new widget.Label();
        jLabel19 = new widget.Label();
        lbTB = new widget.Label();
        lbBB = new widget.Label();
        lbTekanDarah = new widget.Label();
        lbSuhuBadan = new widget.Label();
        jLabel20 = new widget.Label();
        lblPoli = new widget.Label();
        jLabel21 = new widget.Label();
        lblTotal = new widget.Label();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        scrollPane1 = new widget.ScrollPane();
        tblPilihan = new widget.Table();
        panelBiasa1 = new widget.PanelBiasa();
        panelBiasa2 = new widget.PanelBiasa();
        label1 = new widget.Label();
        txtCari = new widget.TextBox();
        panelBiasa3 = new widget.PanelBiasa();
        label2 = new widget.Label();
        lblDepo = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        tblObat = new widget.Table();

        Popup.setName("Popup"); // NOI18N

        ppBersihkan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBersihkan.setText("Bersihkan Jumlah");
        ppBersihkan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan.setName("ppBersihkan"); // NOI18N
        ppBersihkan.setPreferredSize(new java.awt.Dimension(200, 25));
        ppBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanActionPerformed(evt);
            }
        });
        Popup.add(ppBersihkan);

        ppStok.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppStok.setForeground(new java.awt.Color(50, 50, 50));
        ppStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppStok.setText("Tampilkan Semua Stok");
        ppStok.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppStok.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppStok.setName("ppStok"); // NOI18N
        ppStok.setPreferredSize(new java.awt.Dimension(200, 25));
        ppStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppStokActionPerformed(evt);
            }
        });
        Popup.add(ppStok);

        ppStok1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppStok1.setForeground(new java.awt.Color(50, 50, 50));
        ppStok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppStok1.setText("Tampilkan Stok Lokasi Lain");
        ppStok1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppStok1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppStok1.setName("ppStok1"); // NOI18N
        ppStok1.setPreferredSize(new java.awt.Dimension(200, 25));
        ppStok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppStok1ActionPerformed(evt);
            }
        });
        Popup.add(ppStok1);

        popObatPilihan.setName("popObatPilihan"); // NOI18N

        mnEditObat.setBackground(new java.awt.Color(255, 255, 255));
        mnEditObat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mnEditObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/EDIT2.png"))); // NOI18N
        mnEditObat.setText("Edit Obat Resep");
        mnEditObat.setName("mnEditObat"); // NOI18N
        mnEditObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEditObatActionPerformed(evt);
            }
        });
        popObatPilihan.add(mnEditObat);

        mnHapusItem.setBackground(new java.awt.Color(255, 255, 255));
        mnHapusItem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mnHapusItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/101.png"))); // NOI18N
        mnHapusItem.setText("Hapus Obat");
        mnHapusItem.setName("mnHapusItem"); // NOI18N
        mnHapusItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnHapusItemActionPerformed(evt);
            }
        });
        popObatPilihan.add(mnHapusItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ E-Resep Dokter ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        BtnTambah.setForeground(new java.awt.Color(0, 0, 0));
        BtnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambah.setMnemonic('3');
        BtnTambah.setText("Racikan");
        BtnTambah.setToolTipText("Alt+3");
        BtnTambah.setName("BtnTambah"); // NOI18N
        BtnTambah.setPreferredSize(new java.awt.Dimension(100, 23));
        BtnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahActionPerformed(evt);
            }
        });
        panelisi3.add(BtnTambah);

        btnHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/History.png"))); // NOI18N
        btnHistory.setText("History Resep");
        btnHistory.setName("btnHistory"); // NOI18N
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });
        panelisi3.add(btnHistory);

        btnTemplateResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Pills.png"))); // NOI18N
        btnTemplateResep.setText("Template Resep");
        btnTemplateResep.setName("btnTemplateResep"); // NOI18N
        btnTemplateResep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemplateResepActionPerformed(evt);
            }
        });
        panelisi3.add(btnTemplateResep);

        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(500, 23));
        panelisi3.add(label13);

        btnSimpan.setForeground(new java.awt.Color(0, 0, 0));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setName("btnSimpan"); // NOI18N
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        panelisi3.add(btnSimpan);

        BtnKeluar.setForeground(new java.awt.Color(0, 0, 0));
        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('4');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+4");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 23));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        panelisi3.add(BtnKeluar);

        internalFrame1.add(panelisi3, java.awt.BorderLayout.PAGE_END);

        FormInput.setBackground(new java.awt.Color(255, 204, 255));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(864, 108));
        FormInput.setLayout(null);

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Tanggal :");
        jLabel5.setName("jLabel5"); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel5);
        jLabel5.setBounds(10, 40, 68, 23);

        cmbTanggal.setForeground(new java.awt.Color(50, 70, 50));
        cmbTanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "12-10-2020" }));
        cmbTanggal.setDisplayFormat("dd-MM-yyyy");
        cmbTanggal.setName("cmbTanggal"); // NOI18N
        cmbTanggal.setOpaque(false);
        cmbTanggal.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(cmbTanggal);
        cmbTanggal.setBounds(80, 40, 90, 23);

        cmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam.setName("cmbJam"); // NOI18N
        cmbJam.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(cmbJam);
        cmbJam.setBounds(180, 40, 62, 23);

        cmbMnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt.setName("cmbMnt"); // NOI18N
        cmbMnt.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(cmbMnt);
        cmbMnt.setBounds(250, 40, 62, 23);

        cmbDtk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk.setName("cmbDtk"); // NOI18N
        cmbDtk.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(cmbDtk);
        cmbDtk.setBounds(320, 40, 62, 23);

        label21.setForeground(new java.awt.Color(0, 0, 0));
        label21.setText("Peresep : ");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label21);
        label21.setBounds(10, 70, 68, 23);

        txtKodeDokter.setEditable(false);
        txtKodeDokter.setForeground(new java.awt.Color(0, 0, 0));
        txtKodeDokter.setName("txtKodeDokter"); // NOI18N
        txtKodeDokter.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(txtKodeDokter);
        txtKodeDokter.setBounds(80, 70, 100, 23);

        txtNamaDokter.setEditable(false);
        txtNamaDokter.setForeground(new java.awt.Color(0, 0, 0));
        txtNamaDokter.setName("txtNamaDokter"); // NOI18N
        txtNamaDokter.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(txtNamaDokter);
        txtNamaDokter.setBounds(180, 70, 197, 23);

        BtnPilihDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Doctor.png"))); // NOI18N
        BtnPilihDokter.setMnemonic('2');
        BtnPilihDokter.setToolTipText("Alt+2");
        BtnPilihDokter.setName("BtnPilihDokter"); // NOI18N
        BtnPilihDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPilihDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPilihDokterActionPerformed(evt);
            }
        });
        FormInput.add(BtnPilihDokter);
        BtnPilihDokter.setBounds(380, 70, 28, 23);

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("No.Rawat :");
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel10);
        jLabel10.setBounds(10, 10, 80, 23);

        LblNoRawat.setEditable(false);
        LblNoRawat.setForeground(new java.awt.Color(0, 0, 0));
        LblNoRawat.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        LblNoRawat.setName("LblNoRawat"); // NOI18N
        LblNoRawat.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(LblNoRawat);
        LblNoRawat.setBounds(90, 10, 160, 23);

        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("No.RM :");
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel11);
        jLabel11.setBounds(250, 10, 60, 23);

        LblNamaPasien.setEditable(false);
        LblNamaPasien.setForeground(new java.awt.Color(0, 0, 0));
        LblNamaPasien.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        LblNamaPasien.setName("LblNamaPasien"); // NOI18N
        LblNamaPasien.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(LblNamaPasien);
        LblNamaPasien.setBounds(490, 10, 440, 28);

        lbAlergi.setForeground(new java.awt.Color(0, 0, 0));
        lbAlergi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbAlergi.setName("lbAlergi"); // NOI18N
        lbAlergi.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(lbAlergi);
        lbAlergi.setBounds(1030, 80, 250, 23);

        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Poli/Bangsal :");
        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(430, 40, 100, 23);

        lblJaminan.setForeground(new java.awt.Color(0, 0, 0));
        lblJaminan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblJaminan.setText("umum");
        lblJaminan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblJaminan.setName("lblJaminan"); // NOI18N
        FormInput.add(lblJaminan);
        lblJaminan.setBounds(540, 60, 290, 20);

        ChkJln.setBorder(null);
        ChkJln.setSelected(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkJln.setName("ChkJln"); // NOI18N
        ChkJln.setOpaque(false);
        ChkJln.setPreferredSize(new java.awt.Dimension(22, 23));
        ChkJln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkJlnActionPerformed(evt);
            }
        });
        FormInput.add(ChkJln);
        ChkJln.setBounds(390, 40, 20, 23);

        LblNoRM.setEditable(false);
        LblNoRM.setForeground(new java.awt.Color(0, 0, 0));
        LblNoRM.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        LblNoRM.setName("LblNoRM"); // NOI18N
        LblNoRM.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(LblNoRM);
        LblNoRM.setBounds(310, 10, 80, 23);

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Nama Pasien :");
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N
        jLabel13.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel13);
        jLabel13.setBounds(390, 10, 100, 23);

        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Tinggi Badan :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel15);
        jLabel15.setBounds(940, 0, 80, 23);

        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Berat Badan :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel16);
        jLabel16.setBounds(940, 20, 80, 23);

        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Tekanan Darah :");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel17);
        jLabel17.setBounds(940, 40, 80, 23);

        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Suhu Badan :");
        jLabel18.setName("jLabel18"); // NOI18N
        jLabel18.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel18);
        jLabel18.setBounds(940, 60, 80, 23);

        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Alergi :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel19);
        jLabel19.setBounds(940, 80, 80, 23);

        lbTB.setForeground(new java.awt.Color(0, 0, 0));
        lbTB.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbTB.setText("cm");
        lbTB.setName("lbTB"); // NOI18N
        lbTB.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(lbTB);
        lbTB.setBounds(1030, 0, 80, 23);

        lbBB.setForeground(new java.awt.Color(0, 0, 0));
        lbBB.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbBB.setText("kg");
        lbBB.setName("lbBB"); // NOI18N
        lbBB.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(lbBB);
        lbBB.setBounds(1030, 20, 80, 23);

        lbTekanDarah.setForeground(new java.awt.Color(0, 0, 0));
        lbTekanDarah.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbTekanDarah.setName("lbTekanDarah"); // NOI18N
        lbTekanDarah.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(lbTekanDarah);
        lbTekanDarah.setBounds(1030, 40, 80, 23);

        lbSuhuBadan.setForeground(new java.awt.Color(0, 0, 0));
        lbSuhuBadan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbSuhuBadan.setName("lbSuhuBadan"); // NOI18N
        lbSuhuBadan.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(lbSuhuBadan);
        lbSuhuBadan.setBounds(1030, 60, 80, 23);

        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("Jaminan :");
        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(470, 60, 60, 23);

        lblPoli.setForeground(new java.awt.Color(0, 0, 0));
        lblPoli.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPoli.setText("umum");
        lblPoli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPoli.setName("lblPoli"); // NOI18N
        FormInput.add(lblPoli);
        lblPoli.setBounds(540, 40, 270, 20);

        jLabel21.setForeground(new java.awt.Color(0, 153, 0));
        jLabel21.setText("Total  :");
        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N
        FormInput.add(jLabel21);
        jLabel21.setBounds(470, 80, 60, 23);

        lblTotal.setForeground(new java.awt.Color(0, 153, 0));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotal.setText("0.0");
        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotal.setName("lblTotal"); // NOI18N
        FormInput.add(lblTotal);
        lblTotal.setBounds(540, 80, 290, 20);

        internalFrame1.add(FormInput, java.awt.BorderLayout.PAGE_START);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 1, 0));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, ".:: OBAT TERPILIH ::.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 102, 0))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        scrollPane1.setName("scrollPane1"); // NOI18N
        scrollPane1.setOpaque(true);

        tblPilihan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPilihan.setComponentPopupMenu(popObatPilihan);
        tblPilihan.setName("tblPilihan"); // NOI18N
        tblPilihan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPilihanMouseClicked(evt);
            }
        });
        scrollPane1.setViewportView(tblPilihan);

        jPanel2.add(scrollPane1);

        jPanel1.add(jPanel2);

        panelBiasa1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, ".:: DATA OBAT ::.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        panelBiasa1.setName("panelBiasa1"); // NOI18N
        panelBiasa1.setLayout(new java.awt.BorderLayout());

        panelBiasa2.setName("panelBiasa2"); // NOI18N

        label1.setText("Cari Obat :");
        label1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label1.setName("label1"); // NOI18N
        panelBiasa2.add(label1);

        txtCari.setForeground(new java.awt.Color(0, 0, 0));
        txtCari.setName("txtCari"); // NOI18N
        txtCari.setPreferredSize(new java.awt.Dimension(400, 24));
        panelBiasa2.add(txtCari);

        panelBiasa3.setBorder(null);
        panelBiasa3.setName("panelBiasa3"); // NOI18N
        panelBiasa3.setPreferredSize(new java.awt.Dimension(400, 30));
        panelBiasa3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        label2.setText("Lokasi Obat : ");
        label2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label2.setName("label2"); // NOI18N
        panelBiasa3.add(label2);

        lblDepo.setText("Depo Obat");
        lblDepo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDepo.setName("lblDepo"); // NOI18N
        panelBiasa3.add(lblDepo);

        panelBiasa2.add(panelBiasa3);

        panelBiasa1.add(panelBiasa2, java.awt.BorderLayout.PAGE_START);

        scrollPane2.setName("scrollPane2"); // NOI18N

        tblObat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblObat.setName("tblObat"); // NOI18N
        tblObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblObatMouseClicked(evt);
            }
        });
        scrollPane2.setViewportView(tblObat);

        panelBiasa1.add(scrollPane2, java.awt.BorderLayout.CENTER);

        jPanel1.add(panelBiasa1);

        internalFrame1.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        clean();
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahActionPerformed
        dlgRacikan.setData(racikanList.size() + 1, false);
        dlgRacikan.setVisible(true);

    }//GEN-LAST:event_BtnTambahActionPerformed

private void ppBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanActionPerformed

}//GEN-LAST:event_ppBersihkanActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

        label13.setPreferredSize(new Dimension(65, 23));
    }//GEN-LAST:event_formWindowActivated

    private void ppStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppStokActionPerformed

    }//GEN-LAST:event_ppStokActionPerformed

    private void BtnPilihDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPilihDokterActionPerformed
        dokter.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        dokter.isCek();
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnPilihDokterActionPerformed

    private void ppStok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppStok1ActionPerformed

    }//GEN-LAST:event_ppStok1ActionPerformed

    private void tblObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblObatMouseClicked
        // TODO add your handling code here:
        int row = tblObat.getSelectedRow();
        if (row > -1) {
            Obat obtTbl = model.get(tblObat.convertRowIndexToModel(row));
            Obat obat = ObatDao.getObat(depoKode, obtTbl.getKodeObat());
            if (obat != null) {
                obtTbl.setHargaBeli(obat.getHargaBeli());
                addQty.setData(obtTbl, racikanList);
                addQty.setVisible(true);
            }
        }

    }//GEN-LAST:event_tblObatMouseClicked

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
//        List<DisplayFarmasi> displays = DisplayDao.getAllDisplayFarmasi();
        String viaWS = pro.getProperty("VIA_WS");
        if (LblNoRawat.getText().trim().equals("") || LblNamaPasien.getText().trim().equals("")) {
            Valid.textKosong(LblNoRawat, "pasien");
        } else if (txtKodeDokter.getText().trim().equals("") || txtKodeDokter.getText().trim().equals("xxx")) {
            Valid.textKosong(txtKodeDokter, "Dokter");
        } else {
            int oke = JOptionPane.showConfirmDialog(null, "Apakah data sudah benar, silahkan cek kembali sebelum disimpan?", "Perhatian", JOptionPane.YES_NO_OPTION);
            if (oke == 0) {
                if (viaWS.equals("1")) {
                    CreateResepRequest request = new CreateResepRequest();
                    request.setNoRawat(LblNoRawat.getText());
                    request.setJenisPasien(jenisPasien);
                    request.setKdDokter(txtKodeDokter.getText());
                    List<ObatResep> biasas = new LinkedList<>();
                    List<ObatResep> racikans = new LinkedList<>();
                    for (ObatResep o : modelPilihan.getAll()) {
                        if (o.getJenisObat().equals(Obat.OBAT_RACIKAN)) {
                            if(o.isParent()){
                                o.setJmlRacik(o.getJumlah());
                            }
                            racikans.add(o);
                        } else {
                            biasas.add(o);
                        }
                    }
                    request.setObatResepDetail(biasas);
                    request.setObatResepRacikanDetail(racikans);
                    BaseResponse response = RestFull.postSimpanResep(wsurl,request);
                    if (!response.getResponseCode().equalsIgnoreCase("500")) {
                        JOptionPane.showMessageDialog(null, "SUKSES SIMPAN RESEP", "SUKSES!", JOptionPane.INFORMATION_MESSAGE);
                        Sequel.saveTrace(SessionLogin.getInstance().getUser(), "create e-resep dengan no rawat : " + LblNoRawat.getText() + " dan no resep : " + response.getResponseCode());
                        clean();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, response.getResponseMessage(), "GAGAL!", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Resep resep = new Resep();
                    resep.setKdDokter(txtKodeDokter.getText());
                    resep.setNoRawat(LblNoRawat.getText());
                    resep.setTglResep(cmbTanggal.getDate());
                    resep.setJamResep(cmbJam.getSelectedItem().toString() + ":" + cmbMnt.getSelectedItem().toString() + ":" + cmbDtk.getSelectedItem().toString());
                    resep.setStatus(Resep.STATUS_BELUM_VERIFIKASI);
                    resep.setJenisPasien(jenisPasien);
                    List<ObatResep> biasas = new LinkedList<>();
                    List<ObatResep> racikans = new LinkedList<>();
                    for (ObatResep o : modelPilihan.getAll()) {
                        if (o.getJenisObat().equals(Obat.OBAT_RACIKAN)) {
                            racikans.add(o);
                        } else {
                            biasas.add(o);
                        }
                    }
                    resep.setObatResepRacikanDetail(racikans);
                    resep.setObatResepDetail(biasas);
                    boolean sukses = false;
                    String noresep = ResepDao.getNoResepForUpdate();
                    if (noresep != null) {
                        resep.setNoResep(noresep);
                        if (biasas.size() > 0) {
                            ResepDao.save(resep);
                            sukses = ResepDao.isResepExistByNoResep(noresep);
                            if (sukses) {
                                Sequel.saveTrace(SessionLogin.getInstance().getUser(), "create e-resep obat tunggal dengan no rawat : " + resep.getNoRawat() + " dan no resep : " + resep.getNoResep());
//                            ClientData data = new ClientData();
//                            data.setUnit(Konstan.getPoli(this.kodePoli));
//                            data.setNonracik(true);
//                            data.setTunggal(1);
//                            for(DisplayFarmasi f:displays){
//                                System.out.println("no ip = "+f.getIp());
//                                sendToDisplay(data,f.getIp());
//                            }

                                JOptionPane.showMessageDialog(null, "SUKSES SIMPAN RESEP", "SUKSES!", JOptionPane.INFORMATION_MESSAGE);
                                clean();
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "RESEP GAGAL DISIMPAN", "GAGAL!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        if (racikans.size() > 0) {
                            ResepDao.saveRacikan(resep);
                            sukses = ResepDao.isResepRacikanExistByNoResep(noresep);
                            if (sukses) {
                                Sequel.saveTrace(SessionLogin.getInstance().getUser(), "create e-resep racikan dengan no rawat : " + resep.getNoRawat() + " dan no resep : " + resep.getNoResep());
//                            ClientData data = new ClientData();
//                            data.setUnit(Konstan.getPoli(this.kodePoli));
//                            data.setNonracik(false);
//                            data.setRacik(1);
//                            for(DisplayFarmasi f:displays){
//                                sendToDisplay(data,f.getIp());
//                            }
                                JOptionPane.showMessageDialog(null, "SUKSES SIMPAN RESEP RACIKAN", "SUKSES!", JOptionPane.INFORMATION_MESSAGE);
                                clean();
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "RESEP RACIKAN GAGAL DISIMPAN", "GAGAL!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        sukses = false;
                        JOptionPane.showMessageDialog(null, "RESEP GAGAL DISIMPAN, Terkendala dalam mendapatkan No. Resep dari database", "NO.RESEP GAGAL!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

    }//GEN-LAST:event_btnSimpanActionPerformed

    private void sendToDisplay(ClientData data, String ip) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);
            HttpEntity<ClientData> requestEntity = new HttpEntity<>(data, headers);
            restTemplate.postForObject("http://" + ip + ":1981", requestEntity, ClientData.class);
        } catch (Exception e) {
        }
    }


    private void tblPilihanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPilihanMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_tblPilihanMouseClicked

    private void mnHapusItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnHapusItemActionPerformed
        // TODO add your handling code here:
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int wkwkw = JOptionPane.showConfirmDialog(null, "Serius mau menghapus obat ini..?", "Perhatian", dialogButton);
        if (wkwkw == 0) {
            int baris = tblPilihan.convertRowIndexToModel(tblPilihan.getSelectedRow());

            if (baris > -1) {
                ObatResep oresep = modelPilihan.get(tblPilihan.convertRowIndexToModel(baris));
                double marginPersen = 28;
                if (jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)) {
                    MarginBpjs marginBpjs = MarginDao.getMarginBpjs(oresep.getKodeObat());
                    if (marginBpjs != null) {
                        marginPersen = marginBpjs.getRalan();
                    }

                } else {
                    MarginObatNonBpjs marginNon = MarginDao.getMarginNonBpjs(kdJaminan);
                    if (marginNon != null) {
                        marginPersen = marginNon.getMargin();
                    }

                }
                double margin = (oresep.getHargaBeli() * marginPersen) / 100;
                double hpp = margin + oresep.getHargaBeli();
                total = total - (hpp * oresep.getJumlah());
                modelPilihan.remove(baris);
                lblTotal.setText(Utils.format(total, 2));
            } else {
                JOptionPane.showMessageDialog(null, "Silahkan pilih baris obat yang mau di hapus..");
            }
        }
    }//GEN-LAST:event_mnHapusItemActionPerformed

    private void mnEditObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEditObatActionPerformed
        // TODO add your handling code here:
        int row = tblPilihan.getSelectedRow();
        if (row > -1) {
            ObatResep obatDetail = modelPilihan.get(tblPilihan.convertRowIndexToModel(row));
            if (obatDetail.isParent()) {
                dlgRacikan.setEditData(obatDetail);
                dlgRacikan.setVisible(true);
            } else {
                if (!Utils.isBlank(obatDetail.getRacikan())) {
                    if (!obatDetail.getRacikan().equals("-")) {
                        for (ObatResep o : modelPilihan.getAll()) {
                            if (o.isParent() && o.getRacikan().equals(obatDetail.getRacikan())) {
                                removeDuplicateRacikans(o);
                                break;
                            }
                        }
                    }
                }

                addQty.setDataEdit(obatDetail, racikanList);
                addQty.setVisible(true);
            }

        }
    }//GEN-LAST:event_mnEditObatActionPerformed

    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        // TODO add your handling code here:
        cekHistory.setData(txtKodeDokter.getText(), LblNoRM.getText(), depoKode);
        cekHistory.setSize(internalFrame1.getWidth() - 30, internalFrame1.getHeight() - 30);
        cekHistory.setLocationRelativeTo(internalFrame1);
        cekHistory.setVisible(true);
    }//GEN-LAST:event_btnHistoryActionPerformed

    private void btnTemplateResepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTemplateResepActionPerformed
        // TODO add your handling code here:
        templateResep.setData(txtKodeDokter.getText(), depoKode);
        templateResep.setSize(internalFrame1.getWidth() - 30, internalFrame1.getHeight() - 30);
        templateResep.setLocationRelativeTo(internalFrame1);
        templateResep.setVisible(true);
    }//GEN-LAST:event_btnTemplateResepActionPerformed

    private void clean() {
        racikanList = new LinkedList<>();
        model.removeAllElements();
        modelPilihan.removeAllElements();
        obatRacikan = null;
        LblNoRawat.setText(null);
        LblNoRM.setText(null);
        LblNamaPasien.setText(null);
        lblJaminan.setText(null);
        total = 0;
        lblTotal.setText(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgEResepDokter dialog = new DlgEResepDokter(new javax.swing.JFrame(), true);
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
    private widget.Button BtnPilihDokter;
    private widget.Button BtnTambah;
    private widget.CekBox ChkJln;
    private widget.PanelBiasa FormInput;
    private widget.TextBox LblNamaPasien;
    private widget.TextBox LblNoRM;
    private widget.TextBox LblNoRawat;
    private javax.swing.JPopupMenu Popup;
    private widget.Button btnHistory;
    private widget.Button btnSimpan;
    private widget.Button btnTemplateResep;
    private widget.ComboBox cmbDtk;
    private widget.ComboBox cmbJam;
    private widget.ComboBox cmbMnt;
    private widget.Tanggal cmbTanggal;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private widget.Label label1;
    private widget.Label label13;
    private widget.Label label2;
    private widget.Label label21;
    private widget.Label lbAlergi;
    private widget.Label lbBB;
    private widget.Label lbSuhuBadan;
    private widget.Label lbTB;
    private widget.Label lbTekanDarah;
    private widget.Label lblDepo;
    private widget.Label lblJaminan;
    private widget.Label lblPoli;
    private widget.Label lblTotal;
    private javax.swing.JMenuItem mnEditObat;
    private javax.swing.JMenuItem mnHapusItem;
    private widget.PanelBiasa panelBiasa1;
    private widget.PanelBiasa panelBiasa2;
    private widget.PanelBiasa panelBiasa3;
    private widget.panelisi panelisi3;
    private javax.swing.JPopupMenu popObatPilihan;
    private javax.swing.JMenuItem ppBersihkan;
    private javax.swing.JMenuItem ppStok;
    private javax.swing.JMenuItem ppStok1;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.Table tblObat;
    private widget.Table tblPilihan;
    private widget.TextBox txtCari;
    private widget.TextBox txtKodeDokter;
    private widget.TextBox txtNamaDokter;
    // End of variables declaration//GEN-END:variables

}
