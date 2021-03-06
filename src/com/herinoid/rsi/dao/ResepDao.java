/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herinoid.rsi.dao;

import com.herinoid.rsi.model.DataEResep;
import com.herinoid.rsi.model.EtiketObat;
import com.herinoid.rsi.model.Obat;
import com.herinoid.rsi.model.ObatResep;
import com.herinoid.rsi.model.RegPeriksa;
import com.herinoid.rsi.model.Resep;
import com.herinoid.rsi.model.RincianResepVerifikasi;
import com.herinoid.rsi.util.Konstan;
import com.herinoid.rsi.util.NumberUtils;
import com.herinoid.rsi.dao.MarginDao;
import com.herinoid.rsi.model.MarginBpjs;
import com.herinoid.rsi.model.MarginObatNonBpjs;
import com.herinoid.rsi.session.SessionLogin;
import static com.herinoid.rsi.util.NumberUtils.DATE_FORMAT_NUMBERING;
import com.herinoid.rsi.util.Utils;
import fungsi.koneksiDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Hewrei
 */
public class ResepDao {

    private static Connection koneksi = koneksiDB.condb();
//    private static PreparedStatement ps;
//    private static ResultSet rs;

    public static boolean updateNoResep(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update no_e_resep set no_resep = ?");
            try {
                pst.setString(1, noresep);
                pst.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean deleteDetailRacikanByNoResep(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("delete from e_resep_racikan_rsifc_detail where no_resep = ?");
            try {
                pst.setString(1, noresep);
                pst.execute();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean deleteRacikanByNoResep(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("delete from e_resep_racikan_rsifc where no_resep = ?");
            try {
                pst.setString(1, noresep);
                pst.execute();
                deleteDetailRacikanByNoResep(noresep);
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean saveRacikanDetail(String norawat, String noResep, List<ObatResep> reseps) {
        boolean sukses = true;
        PreparedStatement psttmn = null;
        PreparedStatement psttObtRck = null;
        try {
            Connection conDetailObat = koneksiDB.condb();
            conDetailObat.setAutoCommit(false);
            Connection conObatRck = koneksiDB.condb();
            conObatRck.setAutoCommit(false);
            try {
                psttObtRck = conObatRck.prepareStatement("insert into obat_racikan_eresep_rsifc(no_resep,tgl_perawatan,jam,no_rawat,no_racik,nama_racik,kd_racik,metode_racik,jml_dr,aturan_pakai,aturan_pakai_farmasi,keterangan) values(?,?,?,?,?,?,?,?,?,?,?,?)");
                psttmn = conDetailObat.prepareStatement("insert into e_resep_racikan_rsifc_detail(no_resep,kode_racik,nama_racik,is_parent,kandungan,kode_brng,jml,embalase,tuslah,p1,p2,aturan_pakai,code) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
                int i = 0, x = 0;
                for (ObatResep o : reseps) {
                    if (o.isParent()) {
                        x++;
                        psttObtRck.setString(1, noResep);
                        psttObtRck.setString(2, Utils.formatDb(new Date()));
                        psttObtRck.setString(3, Utils.formatTime(new Date()));
                        psttObtRck.setString(4, norawat);
                        psttObtRck.setString(5, String.valueOf(o.getNomorRacik()));
                        psttObtRck.setString(6, o.getNamaObat());
                        psttObtRck.setString(7, o.getKodeRacikan());
                        psttObtRck.setString(8, o.getMetodeRacikKode());
                        psttObtRck.setDouble(9, o.getJumlah());
                        psttObtRck.setString(10, o.getAturanPakai());
                        psttObtRck.setString(11, o.getAturanPakai());
                        psttObtRck.setDouble(12, o.getKandungan());
                        psttObtRck.addBatch();
//                        saveObatRacikan(noResep, norawat, o);
                    } else {
                        i++;
                        psttmn.setString(1, noResep);
                        psttmn.setString(2, o.getKodeRacikan());
                        psttmn.setString(3, o.getRacikan());
                        psttmn.setBoolean(4, o.isParent());
                        psttmn.setDouble(5, o.getKandungan());
                        psttmn.setString(6, o.getKodeObat());
                        psttmn.setDouble(7, o.getJumlah());
                        psttmn.setDouble(8, o.getEmbalase());
                        psttmn.setDouble(9, o.getTuslah());
                        psttmn.setInt(10, o.getPembilang());
                        psttmn.setInt(11, o.getPenyebut());
                        psttmn.setString(12, o.getAturanPakai());
                        psttmn.setString(13, Utils.TSID(new Date()));
                        psttmn.addBatch();
                    }
                }
                if (x > 0) {
                    psttObtRck.executeBatch();
                }
                if (i > 0) {
                    psttmn.executeBatch();
                }
            } catch (Exception e) {
                sukses = false;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (psttObtRck != null) {
                    psttObtRck.close();
                }
                if (conObatRck != null) {
                    if (sukses) {
                        conObatRck.commit();
                    } else {
                        conObatRck.rollback();
                    }
                }
                if (psttmn != null) {
                    psttmn.close();
                }
                if (conDetailObat != null) {
                    if (sukses) {
                        conDetailObat.commit();
                    } else {
                        conDetailObat.rollback();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean deleteDetailByNoResep(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("delete from e_resep_rsifc_detail where no_resep = ?");
            try {
                pst.setString(1, noresep);
                pst.execute();
                koneksi.commit();
            } catch (Exception e) {
                koneksi.rollback();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean deleteResepByNoResep(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("delete from e_resep_rsifc where no_resep = ?");
            try {
                pst.setString(1, noresep);
                pst.execute();
                deleteDetailByNoResep(noresep);
                koneksi.commit();
            } catch (Exception e) {
                koneksi.rollback();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean saveDetail(String noResep, List<ObatResep> reseps) {
        boolean sukses = true;
        PreparedStatement psttmn = null;
        try {
            Connection conDetailObat = koneksiDB.condb();
            conDetailObat.setAutoCommit(false);

            try {
                int i = 0;
                psttmn = conDetailObat.prepareStatement("insert into e_resep_rsifc_detail(no_resep,kode_brng,jml,embalase,tuslah,aturan_pakai) values(?,?,?,?,?,?)");
                for (ObatResep o : reseps) {
                    i++;
                    psttmn.setString(1, noResep);
                    psttmn.setString(2, o.getKodeObat());
                    psttmn.setDouble(3, o.getJumlah());
                    psttmn.setDouble(4, o.getEmbalase());
                    psttmn.setDouble(5, o.getTuslah());
                    psttmn.setString(6, o.getAturanPakai());
                    psttmn.addBatch();
                }
                if (i > 0) {
                    System.out.println("insert detail obat ada " + i + " obat");
                    psttmn.executeBatch();
                }
            } catch (Exception e) {
                sukses = false;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (psttmn != null) {
                    psttmn.close();
                }
                if (conDetailObat != null) {
                    if (sukses) {
                        conDetailObat.commit();
                    } else {
                        conDetailObat.rollback();
                    }
                }
            }
        } catch (Exception e) {
            sukses = false;
            System.out.println("Eroor resep : " + e);
        }
        return sukses;
    }

    public static boolean save(Resep resep) {
        boolean sukses = true;
        PreparedStatement psttmn = null;
        try {
//            String noresep = getNoResepForUpdate();
            psttmn = koneksi.prepareStatement("insert into e_resep_rsifc(no_resep,tgl_resep,jam_resep,no_rawat,kd_dokter_peresep,status,jenis_pasien) values(?,?,?,?,?,?,?)");
            try {
                psttmn.setString(1, resep.getNoResep());
                psttmn.setString(2, Utils.formatDb(resep.getTglResep()));
                psttmn.setString(3, resep.getJamResep());
                psttmn.setString(4, resep.getNoRawat());
                psttmn.setString(5, resep.getKdDokter());
                psttmn.setString(6, resep.getStatus());
                psttmn.setString(7, resep.getJenisPasien());
                psttmn.executeUpdate();
                saveDetail(resep.getNoResep(), resep.getObatResepDetail());
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (psttmn != null) {
                    psttmn.close();
                }
            }

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            sukses = false;
            JOptionPane.showMessageDialog(null, "error : " + e);
        }
        return sukses;
    }

    public static boolean saveRacikan(Resep resep) {
        boolean sukses = true;
        PreparedStatement psttmn = null;
        try {
            psttmn = koneksi.prepareStatement("insert into e_resep_racikan_rsifc(no_resep,tgl_resep,jam_resep,no_rawat,kd_dokter_peresep,status,jenis_pasien) values(?,?,?,?,?,?,?)");
            koneksi.setAutoCommit(false);
            try {
                psttmn.setString(1, resep.getNoResep());
                psttmn.setString(2, Utils.formatDb(resep.getTglResep()));
                psttmn.setString(3, resep.getJamResep());
                psttmn.setString(4, resep.getNoRawat());
                psttmn.setString(5, resep.getKdDokter());
                psttmn.setString(6, resep.getStatus());
                psttmn.setString(7, resep.getJenisPasien());
                psttmn.executeUpdate();
                saveRacikanDetail(resep.getNoRawat(), resep.getNoResep(), resep.getObatResepRacikanDetail());
                koneksi.commit();
            } catch (Exception e) {
                koneksi.rollback();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (psttmn != null) {
                    psttmn.close();
                }
            }

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            sukses = false;
            JOptionPane.showMessageDialog(null, "error : " + e);
        }
        return sukses;
    }

    public static boolean saveObatRacikan(String noresep, String norawat, ObatResep obat) {
        boolean sukses = true;
        PreparedStatement psttmn = null;
        try {

            psttmn = koneksi.prepareStatement("insert into obat_racikan_eresep_rsifc(no_resep,tgl_perawatan,jam,no_rawat,no_racik,nama_racik,kd_racik,metode_racik,jml_dr,aturan_pakai,aturan_pakai_farmasi,keterangan) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            try {
                psttmn.setString(1, noresep);
                psttmn.setString(2, Utils.formatDb(new Date()));
                psttmn.setString(3, Utils.formatTime(new Date()));
                psttmn.setString(4, norawat);
                psttmn.setString(5, String.valueOf(obat.getNomorRacik()));
                psttmn.setString(6, obat.getNamaObat());
                psttmn.setString(7, obat.getKodeRacikan());
                psttmn.setString(8, obat.getMetodeRacikKode());
                psttmn.setDouble(9, obat.getJumlah());
                psttmn.setString(10, obat.getAturanPakai());
                psttmn.setString(11, obat.getAturanPakai());
                psttmn.setDouble(12, obat.getKandungan());
                psttmn.executeUpdate();
                koneksi.commit();
            } catch (Exception e) {
                koneksi.rollback();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (psttmn != null) {
                    psttmn.close();
                }
            }

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            sukses = false;
            JOptionPane.showMessageDialog(null, "error : " + e);
        }
        return sukses;
    }

    public static String getNoResepForUpdate() {
        String noresep = null;
        PreparedStatement psttmn = null;
        ResultSet rset = null;
        try {
            psttmn = koneksi.prepareStatement("select * from no_e_resep for update");
            rset = psttmn.executeQuery();
            while (rset.next()) {
                noresep = rset.getString("no_resep");
            }
            updateNoResep(NumberUtils.getNoResep(noresep));
            String oldDate = noresep.substring(6, 14);
            Date tglnoresep = Utils.convertToDate(oldDate, DATE_FORMAT_NUMBERING);
            if (tglnoresep.before(Utils.getDateOnly(new Date()))) {
                String noresepnew = getNoResepForUpdateDiferentDate();
                updateNoResep(NumberUtils.getNoResep(noresepnew));
                noresep = noresepnew;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (psttmn != null) {
                    psttmn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return noresep;
    }

    public static String getNoResepForUpdateDiferentDate() {
        String noresep = null;
        PreparedStatement psttmn = null;
        ResultSet rset = null;
        try {
            psttmn = koneksi.prepareStatement("select * from no_e_resep for update");
            rset = psttmn.executeQuery();
            while (rset.next()) {
                noresep = rset.getString("no_resep");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (psttmn != null) {
                    psttmn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return noresep;
    }

    public static List<DataEResep> getResepByDateAndDepo(String fromDate, String toDate, String depo, String tarif, String jenisPasien) {
        List<DataEResep> obatList = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String queriRajal = "SELECT r.`no_rawat`,e.`no_resep`,e.`tgl_resep`,e.`jam_resep`,p.`nm_poli`,j.`png_jawab`,d.`nm_dokter`,s.`no_rkm_medis`,s.`nm_pasien`,e.`validasi`,e.`packing`,e.`sampai_pasien`,"
                    + "e.`status`,r.`kd_pj` FROM e_resep_rsifc e "
                    + "INNER JOIN reg_periksa r ON r.`no_rawat`=e.`no_rawat` "
                    + "INNER JOIN poliklinik p ON p.`kd_poli`=r.`kd_poli` "
                    + "INNER JOIN penjab j ON j.`kd_pj`=r.`kd_pj` "
                    + "INNER JOIN dokter d ON e.`kd_dokter_peresep`=d.`kd_dokter` "
                    + "INNER JOIN pasien s ON r.`no_rkm_medis` =s.`no_rkm_medis` "
                    + "WHERE e.tgl_resep BETWEEN ? AND ? AND e.jenis_pasien = ? ";
            if(tarif.equalsIgnoreCase("-")){
                queriRajal += "ORDER BY e.`no_resep`";
            }else{
                queriRajal += "and r.kd_poli = ? ORDER BY e.`no_resep`";
            }
            String queriRanap = "SELECT r.`no_rawat`,e.`no_resep`,e.`tgl_resep`,e.`jam_resep`,b.`nm_bangsal`,j.`png_jawab`,d.`nm_dokter`,s.`no_rkm_medis`,s.`nm_pasien`,e.`validasi`,e.`packing`,e.`sampai_pasien`,"
                    + "e.`status`,r.`kd_pj` FROM e_resep_rsifc e "
                    + "INNER JOIN reg_periksa r ON r.`no_rawat`=e.`no_rawat` "
                    + "INNER JOIN penjab j ON j.`kd_pj`=r.`kd_pj` "
                    + "INNER JOIN dokter d ON e.`kd_dokter_peresep`=d.`kd_dokter` "
                    + "INNER JOIN pasien s ON r.`no_rkm_medis` =s.`no_rkm_medis` "
                    + "INNER JOIN kamar_inap i ON r.`no_rawat` =i.`no_rawat` "
                    + "INNER JOIN kamar k ON k.`kd_kamar` =i.`kd_kamar` "
                    + "INNER JOIN bangsal b ON b.`kd_bangsal` =k.`kd_bangsal` "
                    + "WHERE i.`tgl_masuk`=(SELECT MAX(tgl_masuk) FROM kamar_inap WHERE no_rawat=r.`no_rawat`) AND e.tgl_resep BETWEEN ? AND ? AND e.jenis_pasien = ? ";
            if(tarif.equalsIgnoreCase("-")){
                queriRanap += "group by e.no_resep ORDER BY i.tgl_masuk,e.`no_resep`";
            }else{
                queriRanap += "and b.`kd_bangsal` = ? group by e.no_resep ORDER BY i.tgl_masuk,e.`no_resep`";
            }
            if (jenisPasien.equals(Konstan.PASIEN_RALAN)) {
                ps = koneksi.prepareStatement(queriRajal);                
            } else {
                ps = koneksi.prepareStatement(queriRanap);
            }

            ps.setString(1, fromDate);
            ps.setString(2, toDate);
            ps.setString(3, jenisPasien);
            if(!tarif.equalsIgnoreCase("-")){  
                ps.setString(4, tarif);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                DataEResep obat = new DataEResep();
                obat.setNoResep(rs.getString("no_resep"));
                obat.setTglResep(Utils.formatDateSql(rs.getDate("tgl_resep")) + " " + rs.getString("jam_resep"));
                if (jenisPasien.equals(Konstan.PASIEN_RALAN)) {
                    obat.setPoli(rs.getString("nm_poli"));
                } else {
                    obat.setPoli(rs.getString("nm_bangsal"));
                }

                obat.setDokter(rs.getString("nm_dokter"));
                obat.setJaminan(rs.getString("png_jawab"));
                obat.setNorm(rs.getString("no_rkm_medis"));
                obat.setPasien(rs.getString("nm_pasien") + " (" + rs.getString("no_rkm_medis") + ")");
                obat.setValidasi(rs.getString("validasi"));
                obat.setDiterima(rs.getString("sampai_pasien"));
                obat.setPacking(rs.getString("packing"));
                obat.setStatus(rs.getString("status"));
                obat.setNoRawat(rs.getString("no_rawat"));
                RegPeriksa reg = RegPeriksaDao.get(rs.getString("no_rawat"));
                obat.setStatusBayar(reg.getStatusBayar());
                List<ObatResep> obatDetails = getObatResepDetail(obat.getNoResep(), depo, obat.getJaminan(), rs.getString("kd_pj"));
                Collections.sort(obatDetails, Comparator
                        .comparing(ObatResep::getNamaObat)
                        .thenComparing(ObatResep::getNamaObat));
                obat.setObatDetails(obatDetails);

                obatList.add(obat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatList;
    }

    public static List<ObatResep> getObatResepDetail(String noResep, String depo, String jaminan, String kdJaminan) {
        List<ObatResep> obatDetailList = new LinkedList<>();
        PreparedStatement psttmn = null;
        ResultSet rset = null;
        try {
            psttmn = koneksi.prepareStatement("SELECT d.no_resep,o.kode_brng,o.nama_brng,d.jml,s.satuan,d.embalase,d.tuslah,d.aturan_pakai,g.stok,o.h_beli,o.karyawan,o.ralan,o.beliluar,o.kelas1,o.kelas2,o.kelas3,o.vip,o.vvip,o.status,j.nama AS jenis,k.nama AS kategori "
                    + "FROM e_resep_rsifc_detail d "
                    + "INNER JOIN databarang o ON d.kode_brng = o.kode_brng "
                    + "INNER JOIN gudangbarang g ON d.kode_brng = g.kode_brng "
                    + "INNER JOIN kodesatuan s ON s.kode_sat=o.kode_sat INNER JOIN jenis j ON j.kdjns=o.kdjns INNER JOIN kategori_barang k ON k.kode=o.kode_kategori "
                    + "WHERE d.no_resep = ? AND g.kd_bangsal=?");
//                    + "WHERE d.no_resep = ? AND g.kd_bangsal=? and o.status='1' and o.kode_kategori=?");
            psttmn.setString(1, noResep);
            psttmn.setString(2, depo);
//            psttmn.setString(3, jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)?"K01":"K02");
            rset = psttmn.executeQuery();
            while (rset.next()) {
                ObatResep obat = new ObatResep();
                obat.setKodeObat(rset.getString("kode_brng"));
                obat.setNamaObat(rset.getString("nama_brng"));
                obat.setJumlah(rset.getDouble("jml"));
                obat.setAturanPakai(rset.getString("aturan_pakai"));
                obat.setHargaBeli(rset.getDouble("h_beli"));
                obat.setSatuan(rset.getString("satuan"));
                obat.setJenisObat(rset.getString("jenis"));
                obat.setKategori(rset.getString("kategori"));
                obat.setEmbalase(rset.getDouble("embalase"));
                obat.setTuslah(rset.getDouble("tuslah"));
                obat.setStok(rset.getDouble("stok"));
                obat.setStatus(Integer.parseInt(rset.getString("status")));
                obat.setParent(false);

                double marginPersen = 28;
                if (jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)) {
                    MarginBpjs marginBpjs = MarginDao.getMarginBpjs(obat.getKodeObat());
                    if (marginBpjs != null) {
                        marginPersen = marginBpjs.getRalan();
                    }

                } else {
                    MarginObatNonBpjs marginNon = MarginDao.getMarginNonBpjs(kdJaminan);
                    if (marginNon != null) {
                        marginPersen = marginNon.getMargin();
                    }

                }

                double margin = (obat.getHargaBeli() * marginPersen) / 100;
                double hpp = margin + obat.getHargaBeli();
//                System.out.println("margin persen = "+marginPersen+" || margin rupiah = "+margin+" || harga beli obat = "+obat.getHargaBeli()+ " || hpp = "+hpp);
                obat.setHarga(Utils.roundUp(hpp));
                obatDetailList.add(obat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (psttmn != null) {

                    psttmn.close();

                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatDetailList;
    }

    public static ObatResep getObatStock(String kdObat, String depo, String tarif) {
        ObatResep obat = new ObatResep();
        PreparedStatement psttmn = null;
        ResultSet rset = null;
        try {
            psttmn = koneksi.prepareStatement("SELECT o.kode_brng,o.nama_brng,s.satuan,g.stok,o.h_beli,o.karyawan,o.ralan,o.beliluar,o.kelas1,o.kelas2,o.kelas3,o.vip,o.vvip,j.nama AS jenis,k.nama AS kategori "
                    + "FROM databarang o "
                    + "INNER JOIN gudangbarang g ON o.kode_brng = g.kode_brng "
                    + "INNER JOIN kodesatuan s ON s.kode_sat=o.kode_sat INNER JOIN jenis j ON j.kdjns=o.kdjns INNER JOIN kategori_barang k ON k.kode=o.kode_kategori "
                    + "WHERE g.kd_bangsal=? AND o.kode_brng = ?");
            psttmn.setString(1, depo);
            psttmn.setString(2, kdObat);
            rset = psttmn.executeQuery();
            while (rset.next()) {
                obat.setKodeObat(rset.getString("kode_brng"));
                obat.setNamaObat(rset.getString("nama_brng"));
                obat.setJumlah(0);
                obat.setAturanPakai("");
                obat.setHargaBeli(rset.getDouble("h_beli"));
                obat.setSatuan(rset.getString("satuan"));
                obat.setJenisObat(rset.getString("jenis"));
                obat.setKategori(rset.getString("kategori"));
                obat.setEmbalase(0);
                obat.setTuslah(0);
                obat.setStok(rset.getDouble("stok"));
                double harga = rset.getDouble("ralan");
                if (tarif.equals(Konstan.PASIEN_KARYAWAN)) {
                    harga = rset.getDouble("karyawan");
                } else if (tarif.equals(Konstan.PASIEN_KELAS_VVIP)) {
                    harga = rset.getDouble("vvip");
                } else if (tarif.equals(Konstan.PASIEN_KELAS_VIP)) {
                    harga = rset.getDouble("vip");
                } else if (tarif.equals(Konstan.PASIEN_KELAS1)) {
                    harga = rset.getDouble("kelas1");
                } else if (tarif.equals(Konstan.PASIEN_KELAS2)) {
                    harga = rset.getDouble("kelas2");
                } else if (tarif.equals(Konstan.PASIEN_KELAS3)) {
                    harga = rset.getDouble("kelas3");
                } else if (tarif.equals(Konstan.PASIEN_BELILUAR)) {
                    harga = rset.getDouble("beliluar");
                }
                obat.setHarga(harga);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (psttmn != null) {

                    psttmn.close();

                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obat;
    }

    public static boolean isDataKasirExist(String noresep) {
        boolean exist = false;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        try {
            ps1 = koneksi.prepareStatement("SELECT * from detail_pemberian_obat where no_faktur = ? ");
            ps1.setString(1, noresep);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                exist = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs1 != null) {

                    rs1.close();

                }
                if (ps1 != null) {
                    ps1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return exist;
    }

    public static boolean saveDetailPemberianObat(String sttRawat, String norawat, List<ObatResep> obats, String depo, String noResep, String jaminan) throws SQLException {
        boolean sukses = true;
        PreparedStatement psttmn = null, statemenGudang = null, statemenValidasi = null, statemenAturanPakai = null;
        try {
            Connection conDetailObat = koneksiDB.condb();
            conDetailObat.setAutoCommit(false);
            Connection conGudang = koneksiDB.condb();
            conGudang.setAutoCommit(false);
            Connection conValidasi = koneksiDB.condb();
            conValidasi.setAutoCommit(false);
            Connection conAturanPakai = koneksiDB.condb();
            conAturanPakai.setAutoCommit(false);
            try {
                RegPeriksa reg = RegPeriksaDao.get(norawat);
                int i = 0, x = 0, z = 0, w = 0;
                psttmn = conDetailObat.prepareStatement("insert into detail_pemberian_obat(tgl_perawatan,jam,no_rawat,kode_brng,h_beli,biaya_obat,jml,embalase,tuslah,total,status,kd_bangsal,no_batch,no_faktur) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                statemenGudang = conGudang.prepareStatement("update gudangbarang set stok = ? where kode_brng = ? and kd_bangsal = ?");
                statemenValidasi = conValidasi.prepareStatement("insert into obat_validasi_eresep_rsifc(no_resep,kode_brng,is_racikan,kode_racikan,nama_racikan,jml,embalase,tuslah,aturan_pakai,code) values(?,?,?,?,?,?,?,?,?,?)");
                statemenAturanPakai = conAturanPakai.prepareStatement("update obat_racikan_eresep_rsifc set aturan_pakai_farmasi = ? where no_resep = ? and kd_racik = ?");
                for (ObatResep obat : obats) {
                    if (obat.isParent() == false) {
                        i++;
//                        Obat obt = ObatDao.getObatForSave(depo, obat.getKodeObat(), jaminan, reg.getKdPj());
                        psttmn.setString(1, Utils.formatDb(new Date()));
                        psttmn.setString(2, Utils.formatTime(new Date()));
                        psttmn.setString(3, norawat);
                        psttmn.setString(4, obat.getKodeObat());
                        psttmn.setDouble(5, obat.getHargaBeli());
                        psttmn.setDouble(6, obat.getHarga());
                        psttmn.setDouble(7, obat.getJumlah());
                        psttmn.setDouble(8, obat.getEmbalase());
                        psttmn.setDouble(9, obat.getTuslah());
                        psttmn.setDouble(10, Utils.roundUpKhanza((obat.getHarga() * obat.getJumlah()) + obat.getEmbalase() + obat.getTuslah(), 100));
                        psttmn.setString(11, Utils.isBlank(sttRawat) ? "Ralan" : sttRawat);
                        psttmn.setString(12, depo);
                        psttmn.setString(13, "");
                        psttmn.setString(14, noResep);
                        psttmn.addBatch();
//                        if (i == obats.size()) {
//                            System.out.println("insert pemberian obat ada "+i+" obat");
//                            psttmn.executeBatch();
//                        }

                        if (obat.getStok() > 0) {
                            x++;
                            statemenGudang.setDouble(1, obat.getStok() - Utils.rounding(obat.getJumlah()));
                            statemenGudang.setString(2, obat.getKodeObat());
                            statemenGudang.setString(3, depo);
                            statemenGudang.addBatch();
//                            if (x == obats.size()) {
//                                System.out.println("update gudang ada " + x + " obat");
//                                statemenGudang.executeBatch();
//                            }
                        }
                        /// validasi
                        z++;
                        statemenValidasi.setString(1, noResep);
                        statemenValidasi.setString(2, obat.getKodeObat());
                        statemenValidasi.setBoolean(3, !Utils.isBlank(obat.getRacikan()));
                        statemenValidasi.setString(4, !Utils.isBlank(obat.getRacikan()) ? obat.getKodeRacikan() : "");
                        statemenValidasi.setString(5, !Utils.isBlank(obat.getRacikan()) ? obat.getRacikan() : "");
                        statemenValidasi.setDouble(6, Utils.rounding(obat.getJumlah()));
                        statemenValidasi.setDouble(7, obat.getEmbalase());
                        statemenValidasi.setDouble(8, obat.getTuslah());
                        statemenValidasi.setString(9, obat.getAturanPakai());
                        statemenValidasi.setString(10, Utils.TSID(new Date()));
                        statemenValidasi.addBatch();
//                        if (z == obats.size()) {
//                            System.out.println("di posisi ini mengexsekusi validasi obat " + z);
//                            statemenValidasi.executeBatch();
//                        }
//                    updateStokGudang(obat.getStok() - Utils.rounding(obat.getJumlah()), obat.getKodeObat(), depo);
//                    saveObatValidasi(noResep, obat);//                   
                    } else {
                        if (isResepRacikanExist(noResep)) {
                            w++;
                            statemenAturanPakai.setString(1, obat.getAturanPakai());
                            statemenAturanPakai.setString(2, noResep);
                            statemenAturanPakai.setString(3, obat.getKodeObat());
                            statemenAturanPakai.addBatch();
//                            if (w == obats.size()) {
//                                statemenAturanPakai.executeBatch();
//                            }
//                        updateAturanPakaiRacikan(obat.getAturanPakai(), noResep, obat.getKodeObat());
                        }
                    }
                }
                if (i > 0) {
                    System.out.println("insert pemberian obat ada " + i + " obat");
                    psttmn.executeBatch();
                }
                if (x > 0) {
                    System.out.println("update gudang ada " + x + " obat");
                    statemenGudang.executeBatch();
                }
                if (z > 0) {
                    System.out.println("di posisi ini mengexsekusi validasi obat " + z);
                    statemenValidasi.executeBatch();
                }

                if (w > 0) {
                    statemenAturanPakai.executeBatch();
                }

//                for (ObatResep obat : obats) {
//                    if (obat.isParent() == false) {
//                        if (obat.getStok() > 0) {
//                            x++;
//                            statemenGudang.setDouble(1, obat.getStok() - Utils.rounding(obat.getJumlah()));
//                            statemenGudang.setString(2, obat.getKodeObat());
//                            statemenGudang.setString(3, depo);
//                            statemenGudang.addBatch();
//                            if (x % 100 == 0 || x == obats.size()) {
//                                statemenGudang.executeBatch();
//                            }
//                        }
//                    }
//                }
//
//                for (ObatResep obat : obats) {
//                    if (obat.isParent() == false) {
//                        z++;
//                        statemenValidasi.setString(1, noResep);
//                        statemenValidasi.setString(2, obat.getKodeObat());
//                        statemenValidasi.setBoolean(3, !Utils.isBlank(obat.getRacikan()));
//                        statemenValidasi.setString(4, !Utils.isBlank(obat.getRacikan()) ? obat.getKodeRacikan() : "");
//                        statemenValidasi.setString(5, !Utils.isBlank(obat.getRacikan()) ? obat.getRacikan() : "");
//                        statemenValidasi.setDouble(6, Utils.rounding(obat.getJumlah()));
//                        statemenValidasi.setDouble(7, obat.getEmbalase());
//                        statemenValidasi.setDouble(8, obat.getTuslah());
//                        statemenValidasi.setString(9, obat.getAturanPakai());
//                        statemenValidasi.setString(10, Utils.TSID(new Date()));
//                        statemenValidasi.addBatch();
//                        if (z % 100 == 0 || z == obats.size()) {
//                            statemenValidasi.executeBatch();
//                        }
//                    }
//                }
                sukses = isDataKasirExist(noResep);
            } catch (Exception e) {
                sukses = false;
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "error : " + e);
            } finally {
                if (psttmn != null) {
                    psttmn.close();
                }
                if (conDetailObat != null) {
                    if (sukses) {
                        conDetailObat.commit();
                    } else {
                        conDetailObat.rollback();
                    }
//                    conDetailObat.close();
                }

                if (statemenGudang != null) {
                    statemenGudang.close();
                }
                if (conGudang != null) {
                    if (sukses) {
                        conGudang.commit();
                    } else {
                        conGudang.rollback();
                    }
//                    conGudang.close();
                }

                if (statemenValidasi != null) {
                    statemenValidasi.close();
                }
                if (conValidasi != null) {
                    if (sukses) {
                        conValidasi.commit();
                    } else {
                        conValidasi.rollback();
                    }
//                    conValidasi.close();
                }

                if (statemenAturanPakai != null) {
                    statemenAturanPakai.close();
                }
                if (conAturanPakai != null) {
                    if (sukses) {
                        conAturanPakai.commit();
                    } else {
                        conAturanPakai.rollback();
                    }
//                    conAturanPakai.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sukses;
    }

    public static boolean updateValidasi(String norawat, String noresep, Date validasi) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update e_resep_rsifc set validasi = ?, status = ?,user_validator=? where no_resep = ?");
            koneksi.setAutoCommit(false);
            try {
                pst.setString(1, Utils.formatDateTimeDb(validasi));
                pst.setString(2, Resep.STATUS_SUDAH_VERIFIKASI);
                pst.setString(3, SessionLogin.getInstance().getUser());
                pst.setString(4, noresep);
                pst.executeUpdate();
                koneksi.commit();
            } catch (Exception e) {
                koneksi.rollback();
                sukses = false;
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    koneksi.setAutoCommit(true);
                    pst.close();
                }
            }
            if (isResepRacikanExist(noresep)) {
                updateValidasiResepRacikan(noresep);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean updateStokGudang(double stok, String kdBarang, String kdBangsal) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            if (stok > 0) {
                pst = koneksi.prepareStatement("update gudangbarang set stok = ? where kode_brng = ? and kd_bangsal = ?");
                try {
                    pst.setDouble(1, stok);
                    pst.setString(2, kdBarang);
                    pst.setString(3, kdBangsal);
                    pst.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Notifikasi : " + e);
                } finally {
                    if (pst != null) {
                        pst.close();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean updateValidasiResepRacikan(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update e_resep_racikan_rsifc set validasi = ?, status = ?,user_validator = ? where no_resep = ?");
            koneksi.setAutoCommit(false);
            try {
                pst.setString(1, Utils.formatDateTimeDb(new Date()));
                pst.setString(2, Resep.STATUS_SUDAH_VERIFIKASI);
                pst.setString(3, SessionLogin.getInstance().getUser());
                pst.setString(4, noresep);
                pst.executeUpdate();
                koneksi.commit();
            } catch (Exception e) {
                koneksi.rollback();
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    koneksi.setAutoCommit(true);
                    pst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static List<DataEResep> getResepRacikanByDateAndDepo(String fromDate, String toDate, String depo, String tarif, String jenisPasien) {
        List<DataEResep> obatList = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String queriRajal = "SELECT r.`no_rawat`,e.`no_resep`,e.`tgl_resep`,e.`jam_resep`,p.`nm_poli`,j.`png_jawab`,d.`nm_dokter`,s.`no_rkm_medis`,s.`nm_pasien`,e.`validasi`,e.`packing`,e.`sampai_pasien`,"
                    + "e.`status`,r.`kd_pj` FROM e_resep_racikan_rsifc e "
                    + "INNER JOIN reg_periksa r ON r.`no_rawat`=e.`no_rawat` "
                    + "INNER JOIN poliklinik p ON p.`kd_poli`=r.`kd_poli` "
                    + "INNER JOIN penjab j ON j.`kd_pj`=r.`kd_pj` "
                    + "INNER JOIN dokter d ON e.`kd_dokter_peresep`=d.`kd_dokter` "
                    + "INNER JOIN pasien s ON r.`no_rkm_medis` =s.`no_rkm_medis` "
                    + "WHERE e.tgl_resep BETWEEN ? AND ? AND e.jenis_pasien = ? ";
            if(tarif.equalsIgnoreCase("-")){
                queriRajal += "ORDER BY e.`no_resep`";
            }else{
                queriRajal += "and r.kd_poli = ? ORDER BY e.`no_resep`";
            }
            String queriRanap = "SELECT r.`no_rawat`,e.`no_resep`,e.`tgl_resep`,e.`jam_resep`,b.`nm_bangsal`,j.`png_jawab`,d.`nm_dokter`,s.`no_rkm_medis`,s.`nm_pasien`,e.`validasi`,e.`packing`,e.`sampai_pasien`,"
                    + "e.`status`,r.`kd_pj` FROM e_resep_racikan_rsifc e "
                    + "INNER JOIN reg_periksa r ON r.`no_rawat`=e.`no_rawat` "
                    + "INNER JOIN penjab j ON j.`kd_pj`=r.`kd_pj` "
                    + "INNER JOIN dokter d ON e.`kd_dokter_peresep`=d.`kd_dokter` "
                    + "INNER JOIN pasien s ON r.`no_rkm_medis` =s.`no_rkm_medis` "
                    + "INNER JOIN kamar_inap i ON r.`no_rawat` =i.`no_rawat` "
                    + "INNER JOIN kamar k ON k.`kd_kamar` =i.`kd_kamar` "
                    + "INNER JOIN bangsal b ON b.`kd_bangsal` =k.`kd_bangsal` "
                    + "WHERE i.`tgl_masuk`=(SELECT MAX(tgl_masuk) FROM kamar_inap WHERE no_rawat=r.`no_rawat`) AND e.tgl_resep BETWEEN ? AND ? AND e.jenis_pasien = ? ";
            if(tarif.equalsIgnoreCase("-")){
                queriRanap += "group by e.no_resep ORDER BY i.tgl_masuk,e.`no_resep`";
            }else{
                queriRanap += "and b.`kd_bangsal` = ? group by e.no_resep ORDER BY i.tgl_masuk,e.`no_resep`";
            }
            if (jenisPasien.equals(Konstan.PASIEN_RALAN)) {
                ps = koneksi.prepareStatement(queriRajal);
            } else {
                ps = koneksi.prepareStatement(queriRanap);
            }
            ps.setString(1, fromDate);
            ps.setString(2, toDate);
            ps.setString(3, jenisPasien);
             if(!tarif.equalsIgnoreCase("-")){  
                ps.setString(4, tarif);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                DataEResep obat = new DataEResep();
                obat.setNoResep(rs.getString("no_resep"));
                obat.setTglResep(Utils.formatDateSql(rs.getDate("tgl_resep")) + " " + rs.getString("jam_resep"));
                if (jenisPasien.equals(Konstan.PASIEN_RALAN)) {
                    obat.setPoli(rs.getString("nm_poli"));
                } else {
                    obat.setPoli(rs.getString("nm_bangsal"));
                }
                obat.setDokter(rs.getString("nm_dokter"));
                obat.setJaminan(rs.getString("png_jawab"));
                obat.setNorm(rs.getString("no_rkm_medis"));
                obat.setPasien(rs.getString("nm_pasien") + " (" + rs.getString("no_rkm_medis") + ")");
                obat.setValidasi(rs.getString("validasi"));
                obat.setDiterima(rs.getString("sampai_pasien"));
                obat.setPacking(rs.getString("packing"));
                obat.setStatus(rs.getString("status"));
                obat.setNoRawat(rs.getString("no_rawat"));
                RegPeriksa reg = RegPeriksaDao.get(rs.getString("no_rawat"));
                obat.setStatusBayar(reg.getStatusBayar());
                List<ObatResep> obatDetails = getObatResepRacikanDetail(obat.getNoResep(), depo, obat.getJaminan(), rs.getString("kd_pj"));
                Collections.sort(obatDetails, Comparator
                        .comparing(ObatResep::getKodeRacikan)
                        .thenComparing(ObatResep::getNamaObat));
                obat.setObatDetails(obatDetails);

                obatList.add(obat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatList;
    }

    public static boolean isResepRacikanExist(String noresep) {
        boolean isExist = false;
        PreparedStatement pre = null;
        ResultSet rset = null;
        try {
            pre = koneksi.prepareStatement("SELECT * FROM e_resep_racikan_rsifc WHERE no_resep = ?");
            pre.setString(1, noresep);
            rset = pre.executeQuery();
            while (rset.next()) {
                isExist = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isExist;
    }

    public static boolean isResepExist(String noresep) {
        boolean isExist = false;
        PreparedStatement pre = null;
        ResultSet rset = null;
        try {
            pre = koneksi.prepareStatement("SELECT * FROM e_resep_rsifc WHERE no_resep = ?");
            pre.setString(1, noresep);
            rset = pre.executeQuery();
            while (rset.next()) {
                isExist = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isExist;
    }

    public static boolean isResepRacikanExistByNorawat(String norawat) {
        boolean isExist = false;
        PreparedStatement pre = null;
        ResultSet rset = null;
        try {
            pre = koneksi.prepareStatement("SELECT * FROM e_resep_racikan_rsifc WHERE no_rawat = ?");
            pre.setString(1, norawat);
            rset = pre.executeQuery();
            while (rset.next()) {
                isExist = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isExist;
    }

    public static boolean isResepExistByNorawat(String norawat) {
        boolean isExist = false;
        PreparedStatement pre = null;
        ResultSet rset = null;
        try {
            pre = koneksi.prepareStatement("SELECT * FROM e_resep_rsifc WHERE no_rawat = ?");
            pre.setString(1, norawat);
            rset = pre.executeQuery();
            while (rset.next()) {
                isExist = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isExist;
    }

    public static boolean isResepExistByNoResep(String noresep) {
        boolean isExist = false;
        PreparedStatement pre = null;
        ResultSet rset = null;
        try {
            pre = koneksi.prepareStatement("SELECT * FROM e_resep_rsifc WHERE no_resep = ?");
            pre.setString(1, noresep);
            rset = pre.executeQuery();
            while (rset.next()) {
                isExist = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isExist;
    }

    public static boolean isResepRacikanExistByNoResep(String noresep) {
        boolean isExist = false;
        PreparedStatement pre = null;
        ResultSet rset = null;
        try {
            pre = koneksi.prepareStatement("SELECT * FROM e_resep_racikan_rsifc WHERE no_resep = ?");
            pre.setString(1, noresep);
            rset = pre.executeQuery();
            while (rset.next()) {
                isExist = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isExist;
    }

    public static List<ObatResep> getObatResepRacikanDetail(String noResep, String depo, String jaminan, String kdJaminan) {
        List<ObatResep> obatDetailList = new LinkedList<>();
        PreparedStatement psttmn = null;
        ResultSet rset = null;
        try {
            psttmn = koneksi.prepareStatement("SELECT d.no_resep,o.kode_brng,o.nama_brng,d.kode_racik,d.nama_racik,d.jml,d.is_parent,d.kandungan,s.satuan,d.embalase,d.tuslah,d.aturan_pakai,g.stok,o.h_beli,o.karyawan,o.ralan,o.beliluar,o.kelas1,o.kelas2,o.kelas3,o.vip,o.vvip,o.status,j.nama AS jenis,k.nama AS kategori "
                    + "FROM e_resep_racikan_rsifc_detail d "
                    + "INNER JOIN databarang o ON d.kode_brng = o.kode_brng "
                    + "INNER JOIN gudangbarang g ON d.kode_brng = g.kode_brng "
                    + "INNER JOIN kodesatuan s ON s.kode_sat=o.kode_sat INNER JOIN jenis j ON j.kdjns=o.kdjns INNER JOIN kategori_barang k ON k.kode=o.kode_kategori "
                    + "WHERE d.no_resep = ? AND g.kd_bangsal=?");
//                    + "WHERE d.no_resep = ? AND g.kd_bangsal=? and o.status='1' and o.kode_kategori=?");
            psttmn.setString(1, noResep);
            psttmn.setString(2, depo);
//            psttmn.setString(3, jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)?"K01":"K02");
            rset = psttmn.executeQuery();
            while (rset.next()) {
                ObatResep obat = new ObatResep();
                obat.setKodeObat(rset.getString("kode_brng"));
                obat.setNamaObat(rset.getString("nama_brng"));
                obat.setKodeRacikan(rset.getString("kode_racik"));
                obat.setRacikan(rset.getString("nama_racik"));
                obat.setJumlah(rset.getDouble("jml"));
                obat.setParent(rset.getBoolean("is_parent"));
                obat.setKandungan(rset.getDouble("kandungan"));
                obat.setAturanPakai(rset.getString("aturan_pakai"));
                obat.setHargaBeli(rset.getDouble("h_beli"));
                obat.setSatuan(rset.getString("satuan"));
                obat.setJenisObat(rset.getString("jenis"));
                obat.setKategori(rset.getString("kategori"));
                obat.setEmbalase(rset.getDouble("embalase"));
                obat.setTuslah(rset.getDouble("tuslah"));
                obat.setStok(rset.getDouble("stok"));
                obat.setStatus(Integer.parseInt(rset.getString("status")));
                obat.setParent(false);
                double marginPersen = 28;
                if (jaminan.equals(Konstan.PASIEN_BPJS_KESEHATAN)) {
                    MarginBpjs marginBpjs = MarginDao.getMarginBpjs(obat.getKodeObat());
                    if (marginBpjs != null) {
                        marginPersen = marginBpjs.getRalan();
                    }

                } else {
                    MarginObatNonBpjs marginNon = MarginDao.getMarginNonBpjs(kdJaminan);
                    if (marginNon != null) {
                        marginPersen = marginNon.getMargin();
                    }

                }
                double margin = (obat.getHargaBeli() * marginPersen) / 100;
                double hpp = margin + obat.getHargaBeli();
                obat.setHarga(Utils.roundUp(hpp));
//                double harga = rset.getDouble("ralan");
//                if (tarif.equals(Konstan.PASIEN_KARYAWAN)) {
//                    harga = rset.getDouble("karyawan");
//                } else if (tarif.equals(Konstan.PASIEN_KELAS_VVIP)) {
//                    harga = rset.getDouble("vvip");
//                } else if (tarif.equals(Konstan.PASIEN_KELAS_VIP)) {
//                    harga = rset.getDouble("vip");
//                } else if (tarif.equals(Konstan.PASIEN_KELAS1)) {
//                    harga = rset.getDouble("kelas1");
//                } else if (tarif.equals(Konstan.PASIEN_KELAS2)) {
//                    harga = rset.getDouble("kelas2");
//                } else if (tarif.equals(Konstan.PASIEN_KELAS3)) {
//                    harga = rset.getDouble("kelas3");
//                } else if (tarif.equals(Konstan.PASIEN_BELILUAR)) {
//                    harga = rset.getDouble("beliluar");
//                }
//                obat.setHarga(harga);
                obatDetailList.add(obat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (psttmn != null) {

                    psttmn.close();

                }
                if (rset != null) {
                    rset.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatDetailList;
    }

    public static boolean updateDiterimaPasien(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            if (isResepExist(noresep)) {
                pst = koneksi.prepareStatement("update e_resep_rsifc set sampai_pasien = ?,status = ? where no_resep = ?");
                try {
                    pst.setString(1, Utils.formatDateTimeDb(new Date()));
                    pst.setString(2, Resep.STATUS_SAMPAI_PASIEN);
                    pst.setString(3, noresep);
                    pst.executeUpdate();
                    if (isResepRacikanExist(noresep)) {
                        updateDiterimaPasienRacikan(noresep);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Notifikasi : " + e);
                } finally {
                    if (pst != null) {
                        pst.close();
                    }
                }

            } else {
                if (isResepRacikanExist(noresep)) {
                    updateDiterimaPasienRacikan(noresep);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean updateDiterimaPasienRacikan(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update e_resep_racikan_rsifc set sampai_pasien = ?,status = ? where no_resep = ?");
            try {
                pst.setString(1, Utils.formatDateTimeDb(new Date()));
                pst.setString(2, Resep.STATUS_SAMPAI_PASIEN);
                pst.setString(3, noresep);
                pst.executeUpdate();
//                if (isResepRacikanExist(noresep)) {
//
//                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean saveObatValidasi(String noResep, ObatResep obat) {
        boolean sukses = true;
        PreparedStatement psttmn = null;
        try {
            psttmn = koneksi.prepareStatement("insert into obat_validasi_eresep_rsifc(no_resep,kode_brng,is_racikan,kode_racikan,nama_racikan,jml,embalase,tuslah,aturan_pakai,code) values(?,?,?,?,?,?,?,?,?,?)");
            try {
                psttmn.setString(1, noResep);
                psttmn.setString(2, obat.getKodeObat());
                psttmn.setBoolean(3, !Utils.isBlank(obat.getRacikan()));
                psttmn.setString(4, !Utils.isBlank(obat.getRacikan()) ? obat.getKodeRacikan() : "");
                psttmn.setString(5, !Utils.isBlank(obat.getRacikan()) ? obat.getRacikan() : "");
                psttmn.setDouble(6, Utils.rounding(obat.getJumlah()));
                psttmn.setDouble(7, obat.getEmbalase());
                psttmn.setDouble(8, obat.getTuslah());
                psttmn.setString(9, obat.getAturanPakai());
                psttmn.setString(10, Utils.TSID(new Date()));
                psttmn.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (psttmn != null) {
                    psttmn.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Notifikasi : " + e);
            sukses = false;
            JOptionPane.showMessageDialog(null, "error : " + e);
        }
        return sukses;
    }

    public static boolean updatePackingRacikan(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update e_resep_racikan_rsifc set packing = ?,status = ? where no_resep = ?");
            try {
                pst.setString(1, Utils.formatDateTimeDb(new Date()));
                pst.setString(2, Resep.STATUS_PACKING);
                pst.setString(3, noresep);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean updatePacking(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            if (isResepExist(noresep)) {
                pst = koneksi.prepareStatement("update e_resep_rsifc set packing = ?,status = ? where no_resep = ?");
                try {
                    pst.setString(1, Utils.formatDateTimeDb(new Date()));
                    pst.setString(2, Resep.STATUS_PACKING);
                    pst.setString(3, noresep);
                    pst.executeUpdate();
                    if (isResepRacikanExist(noresep)) {
                        updatePackingRacikan(noresep);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Notifikasi : " + e);
                } finally {
                    if (pst != null) {
                        pst.close();
                    }
                }

            } else {
                if (isResepRacikanExist(noresep)) {
                    updatePackingRacikan(noresep);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static List<EtiketObat> getEtiketByNoResep(String noResep, String depo) {
        List<EtiketObat> obatList = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = koneksi.prepareStatement("SELECT e.`no_resep`,e.`no_rawat`,e.`tgl_resep`,e.`jam_resep`,p.`no_rkm_medis`,p.`nm_pasien`,b.`nama_brng`,d.`jml`,s.`satuan`,b.`expire`,d.`aturan_pakai`,l.`nm_bangsal`,k.`nm_poli`,d.kode_racikan FROM obat_validasi_eresep_rsifc d "
                    + "JOIN e_resep_rsifc e ON e.`no_resep`=d.`no_resep` "
                    + "JOIN databarang b ON d.kode_brng = b.kode_brng "
                    + "JOIN gudangbarang g ON b.`kode_brng`=g.`kode_brng` "
                    + "JOIN bangsal l ON l.`kd_bangsal`= g.`kd_bangsal` "
                    + "JOIN kodesatuan s ON s.`kode_sat` = b.`kode_sat` "
                    + "JOIN reg_periksa r ON r.`no_rawat` = e.`no_rawat` "
                    + "JOIN poliklinik k ON r.`kd_poli`= k.`kd_poli` "
                    + "JOIN pasien p ON p.`no_rkm_medis` = r.`no_rkm_medis` "
                    + "WHERE d.`no_resep` = ? AND g.`kd_bangsal` = ?");
            ps.setString(1, noResep);
            ps.setString(2, depo);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (Utils.isBlank(rs.getString("kode_racikan"))) {
                    EtiketObat obat = new EtiketObat();
                    obat.setNoResep(rs.getString("no_resep"));
                    obat.setNoRawat(rs.getString("no_rawat"));
                    obat.setTanggal(Utils.formatDateSql(rs.getDate("tgl_resep")));
                    obat.setJam(rs.getString("jam_resep"));
                    obat.setNoRekamMedis(rs.getString("no_rkm_medis"));
                    obat.setPasien(rs.getString("nm_pasien"));
                    obat.setObat(rs.getString("nama_brng"));
                    obat.setSatuan(rs.getString("satuan"));
                    obat.setJml(rs.getDouble("jml"));
                    obat.setExpire(rs.getDate("expire") == null ? "00:00:00" : Utils.formatDateSql(rs.getDate("expire")));
                    obat.setAturanPakai(rs.getString("aturan_pakai"));
                    obat.setLokasi(rs.getString("nm_poli"));
                    obatList.add(obat);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatList;
    }

    public static List<EtiketObat> getEtiketRanapByNoResep(String noResep, String depo) {
        List<EtiketObat> obatList = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = koneksi.prepareStatement("SELECT e.`no_resep`,e.`no_rawat`,e.`tgl_resep`,e.`jam_resep`,p.`no_rkm_medis`,p.`nm_pasien`,b.`nama_brng`,d.`jml`,s.`satuan`,b.`expire`,d.`aturan_pakai`,bg.nm_bangsal,d.kode_racikan "
                    + "FROM obat_validasi_eresep_rsifc d "
                    + "JOIN e_resep_rsifc e ON e.`no_resep`=d.`no_resep` "
                    + "JOIN databarang b ON d.kode_brng = b.kode_brng "
                    + "JOIN gudangbarang g ON b.`kode_brng`=g.`kode_brng` "
                    + "JOIN bangsal l ON l.`kd_bangsal`= g.`kd_bangsal` "
                    + "JOIN kodesatuan s ON s.`kode_sat` = b.`kode_sat` "
                    + "JOIN reg_periksa r ON r.`no_rawat` = e.`no_rawat` "
                    + "JOIN kamar_inap i ON r.`no_rawat` =i.`no_rawat` "
                    + "JOIN kamar k ON k.`kd_kamar` =i.`kd_kamar` "
                    + "JOIN bangsal bg ON bg.`kd_bangsal` =k.`kd_bangsal` "
                    + "JOIN pasien p ON p.`no_rkm_medis` = r.`no_rkm_medis` "
                    + "WHERE d.`no_resep` = ? AND g.`kd_bangsal` = ?");
            ps.setString(1, noResep);
            ps.setString(2, depo);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (Utils.isBlank(rs.getString("kode_racikan"))) {
                    EtiketObat obat = new EtiketObat();
                    obat.setNoResep(rs.getString("no_resep"));
                    obat.setNoRawat(rs.getString("no_rawat"));
                    obat.setTanggal(Utils.formatDateSql(rs.getDate("tgl_resep")));
                    obat.setJam(rs.getString("jam_resep"));
                    obat.setNoRekamMedis(rs.getString("no_rkm_medis"));
                    obat.setPasien(rs.getString("nm_pasien"));
                    obat.setObat(rs.getString("nama_brng"));
                    obat.setSatuan(rs.getString("satuan"));
                    obat.setJml(rs.getDouble("jml"));
                    obat.setExpire(rs.getDate("expire") == null ? "00:00:00" : Utils.formatDateSql(rs.getDate("expire")));
                    obat.setAturanPakai(rs.getString("aturan_pakai"));
                    obat.setLokasi(rs.getString("nm_bangsal"));
                    obatList.add(obat);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatList;
    }

    public static List<DataEResep> getResepByDokterAndPasien(String kodeDokter, String norm, String depo) {
        List<DataEResep> obatList = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = koneksi.prepareStatement("SELECT r.`no_rawat`,e.`no_resep`,e.`tgl_resep`,e.`jam_resep`,p.`nm_poli`,j.`png_jawab`,d.`nm_dokter`,s.`no_rkm_medis`,s.`nm_pasien`,e.`validasi`,e.`packing`,e.`sampai_pasien`,"
                    + "e.`status`,r.`kd_pj` FROM e_resep_rsifc e "
                    + "INNER JOIN reg_periksa r ON r.`no_rawat`=e.`no_rawat` "
                    + "INNER JOIN poliklinik p ON p.`kd_poli`=r.`kd_poli` "
                    + "INNER JOIN penjab j ON j.`kd_pj`=r.`kd_pj` "
                    + "INNER JOIN dokter d ON e.`kd_dokter_peresep`=d.`kd_dokter` "
                    + "INNER JOIN pasien s ON r.`no_rkm_medis` =s.`no_rkm_medis` "
                    + "WHERE e.kd_dokter_peresep = ? AND r.no_rkm_medis = ? ORDER BY e.`tgl_resep`");
            ps.setString(1, kodeDokter);
            ps.setString(2, norm);
            rs = ps.executeQuery();
            while (rs.next()) {
                DataEResep obat = new DataEResep();
                obat.setNoResep(rs.getString("no_resep"));
                obat.setTglResep(Utils.formatDateSql(rs.getDate("tgl_resep")) + " " + rs.getString("jam_resep"));
                obat.setPoli(rs.getString("nm_poli"));
                obat.setDokter(rs.getString("nm_dokter"));
                obat.setJaminan(rs.getString("png_jawab"));
                obat.setNorm(rs.getString("no_rkm_medis"));
                obat.setPasien(rs.getString("nm_pasien"));
                obat.setValidasi(rs.getString("validasi"));
                obat.setDiterima(rs.getString("sampai_pasien"));
                obat.setPacking(rs.getString("packing"));
                obat.setStatus(rs.getString("status"));
                obat.setNoRawat(rs.getString("no_rawat"));
                List<ObatResep> obatDetails = getObatResepDetail(obat.getNoResep(), depo, obat.getJaminan(), rs.getString("kd_pj"));
                Collections.sort(obatDetails, Comparator
                        .comparing(ObatResep::getNamaObat)
                        .thenComparing(ObatResep::getNamaObat));
                obat.setObatDetails(obatDetails);

                obatList.add(obat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatList;
    }

    public static List<DataEResep> getResepRacikanByDokterAndPasien(String kdDokter, String norm, String depo) {
        List<DataEResep> obatList = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = koneksi.prepareStatement("SELECT r.`no_rawat`,e.`no_resep`,e.`tgl_resep`,e.`jam_resep`,p.`nm_poli`,j.`png_jawab`,d.`nm_dokter`,s.`no_rkm_medis`,s.`nm_pasien`,e.`validasi`,e.`packing`,e.`sampai_pasien`,"
                    + "e.`status`,r.`kd_pj` FROM e_resep_racikan_rsifc e "
                    + "INNER JOIN reg_periksa r ON r.`no_rawat`=e.`no_rawat` "
                    + "INNER JOIN poliklinik p ON p.`kd_poli`=r.`kd_poli` "
                    + "INNER JOIN penjab j ON j.`kd_pj`=r.`kd_pj` "
                    + "INNER JOIN dokter d ON e.`kd_dokter_peresep`=d.`kd_dokter` "
                    + "INNER JOIN pasien s ON r.`no_rkm_medis` =s.`no_rkm_medis` "
                    + "WHERE e.kd_dokter_peresep = ? AND r.no_rkm_medis = ? ORDER BY e.`tgl_resep`");
            ps.setString(1, kdDokter);
            ps.setString(2, norm);
            rs = ps.executeQuery();
            while (rs.next()) {
                DataEResep obat = new DataEResep();
                obat.setNoResep(rs.getString("no_resep"));
                obat.setTglResep(Utils.formatDateSql(rs.getDate("tgl_resep")) + " " + rs.getString("jam_resep"));
                obat.setPoli(rs.getString("nm_poli"));
                obat.setDokter(rs.getString("nm_dokter"));
                obat.setJaminan(rs.getString("png_jawab"));
                obat.setNorm(rs.getString("no_rkm_medis"));
                obat.setPasien(rs.getString("nm_pasien"));
                obat.setValidasi(rs.getString("validasi"));
                obat.setDiterima(rs.getString("sampai_pasien"));
                obat.setPacking(rs.getString("packing"));
                obat.setStatus(rs.getString("status"));
                obat.setNoRawat(rs.getString("no_rawat"));
                List<ObatResep> obatDetails = getObatResepRacikanDetail(obat.getNoResep(), depo, obat.getJaminan(), rs.getString("kd_pj"));
                Collections.sort(obatDetails, Comparator
                        .comparing(ObatResep::getKodeRacikan)
                        .thenComparing(ObatResep::getNamaObat));
                obat.setObatDetails(obatDetails);

                obatList.add(obat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatList;
    }

    public static boolean updateAturanPakaiFarmasi(String aturanPakai, String noresep, String kodeObat) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update obat_validasi_eresep_rsifc set aturan_pakai = ? where no_resep = ? and kode_brng = ?");
            try {
                pst.setString(1, aturanPakai);
                pst.setString(2, noresep);
                pst.setString(3, kodeObat);
                pst.executeUpdate();
            } catch (SQLException e) {
                sukses = false;
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean updateAturanPakaiRacikan(String aturanPakai, String noresep, String kodeObat) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update obat_racikan_eresep_rsifc set aturan_pakai_farmasi = ? where no_resep = ? and kd_racik = ?");
            try {
                pst.setString(1, aturanPakai);
                pst.setString(2, noresep);
                pst.setString(3, kodeObat);
                pst.executeUpdate();
            } catch (SQLException e) {
                sukses = false;
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean deleteDataObatValidasiFarmasi(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("delete from obat_validasi_eresep_rsifc where no_resep = ?");
            try {
                pst.setString(1, noresep);
                pst.execute();
            } catch (Exception e) {
                sukses = false;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean deleteDataObatValidasiFarmasiSatuan(String noresep, String kodeObat) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("delete from obat_validasi_eresep_rsifc where no_resep = ? and kode_brng = ?");
            try {
                pst.setString(1, noresep);
                pst.setString(2, kodeObat);
                pst.execute();
            } catch (Exception e) {
                sukses = false;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean updateValidasiAfterHapus(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update e_resep_rsifc set validasi = ?, status = ? where no_resep = ?");
            try {
                pst.setString(1, null);
                pst.setString(2, Resep.STATUS_BELUM_VERIFIKASI);
                pst.setString(3, noresep);
                pst.executeUpdate();
            } catch (Exception e) {
                sukses = false;
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
            if (isResepRacikanExist(noresep)) {
                updateValidasiResepRacikanAfterHapus(noresep);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static boolean updateValidasiResepRacikanAfterHapus(String noresep) {
        boolean sukses = true;
        PreparedStatement pst = null;
        try {
            pst = koneksi.prepareStatement("update e_resep_racikan_rsifc set validasi = ?, status = ? where no_resep = ?");
            try {
                pst.setString(1, null);
                pst.setString(2, Resep.STATUS_BELUM_VERIFIKASI);
                pst.setString(3, noresep);
                pst.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Notifikasi : " + e);
            } finally {
                if (pst != null) {
                    pst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sukses = false;
            System.out.println("Notifikasi : " + e);
        }
        return sukses;
    }

    public static List<EtiketObat> getEtiketRacikanByNoResep(String noResep) {
        List<EtiketObat> obatList = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = koneksi.prepareStatement("SELECT r.`no_rawat`,r.`no_resep`,r.`kd_racik`,r.`nama_racik`,r.`aturan_pakai_farmasi`,r.`jml_dr`,m.`nm_racik`,r.`tgl_perawatan`,r.`jam`,k.`nm_poli`,s.`nm_pasien`,s.`no_rkm_medis` FROM obat_racikan_eresep_rsifc r  "
                    + "JOIN metode_racik m ON r.`metode_racik`=m.`kd_racik` "
                    + "JOIN reg_periksa p ON p.`no_rawat` = r.`no_rawat`  "
                    + "JOIN poliklinik k ON p.`kd_poli`= k.`kd_poli`  "
                    + "JOIN pasien s ON s.`no_rkm_medis` = p.`no_rkm_medis`  "
                    + "WHERE r.`no_resep` = ?");
            ps.setString(1, noResep);
            rs = ps.executeQuery();
            while (rs.next()) {
                EtiketObat obat = new EtiketObat();
                obat.setNoResep(rs.getString("no_resep"));
                obat.setNoRawat(rs.getString("no_rawat"));
                obat.setTanggal(Utils.formatDateSql(rs.getDate("tgl_perawatan")));
                obat.setJam(rs.getString("jam"));
                obat.setPasien(rs.getString("nm_pasien"));
                obat.setObat(rs.getString("nama_racik"));
                obat.setSatuan(rs.getString("nm_racik"));
                obat.setJml(rs.getDouble("jml_dr"));
                obat.setExpire(Utils.getNextMonthDate(rs.getDate("tgl_perawatan")));
                obat.setAturanPakai(rs.getString("aturan_pakai_farmasi"));
                obat.setLokasi(rs.getString("nm_poli"));
                obat.setNoRekamMedis(rs.getString("no_rkm_medis"));
                obatList.add(obat);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatList;
    }
    
    public static List<EtiketObat> getEtiketRacikanRanapByNoResep(String noResep) {
        List<EtiketObat> obatList = new LinkedList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = koneksi.prepareStatement("SELECT r.`no_rawat`,r.`no_resep`,r.`kd_racik`,r.`nama_racik`,r.`aturan_pakai_farmasi`,r.`jml_dr`,m.`nm_racik`,r.`tgl_perawatan`,r.`jam`,bg.`nm_bangsal`,s.`nm_pasien`,s.`no_rkm_medis` FROM obat_racikan_eresep_rsifc r  "
                    + "JOIN metode_racik m ON r.`metode_racik`=m.`kd_racik` "
                    + "JOIN reg_periksa p ON p.`no_rawat` = r.`no_rawat`  "
                    + "JOIN kamar_inap i ON p.`no_rawat` =i.`no_rawat` "
                    + "JOIN kamar k ON k.`kd_kamar` =i.`kd_kamar` "
                    + "JOIN bangsal bg ON bg.`kd_bangsal` =k.`kd_bangsal` "
                    + "JOIN pasien s ON s.`no_rkm_medis` = p.`no_rkm_medis`  "
                    + "WHERE r.`no_resep` = ?");
            ps.setString(1, noResep);
            rs = ps.executeQuery();
            while (rs.next()) {
                EtiketObat obat = new EtiketObat();
                obat.setNoResep(rs.getString("no_resep"));
                obat.setNoRawat(rs.getString("no_rawat"));
                obat.setTanggal(Utils.formatDateSql(rs.getDate("tgl_perawatan")));
                obat.setJam(rs.getString("jam"));
                obat.setPasien(rs.getString("nm_pasien"));
                obat.setObat(rs.getString("nama_racik"));
                obat.setSatuan(rs.getString("nm_racik"));
                obat.setJml(rs.getDouble("jml_dr"));
                obat.setExpire(Utils.getNextMonthDate(rs.getDate("tgl_perawatan")));
                obat.setAturanPakai(rs.getString("aturan_pakai_farmasi"));
                obat.setLokasi(rs.getString("nm_bangsal"));
                obat.setNoRekamMedis(rs.getString("no_rkm_medis"));
                obatList.add(obat);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obatList;
    }

    public static int getNewRecordEResep() {
        int x = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = koneksi.prepareStatement("SELECT COUNT(*) as jumlah FROM e_resep_rsifc WHERE tgl_resep = CURRENT_DATE");

            rs = ps.executeQuery();
            while (rs.next()) {
                x = rs.getInt("jumlah");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {

                    rs.close();

                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return x;
    }

    public static boolean isResepRacikanValidasi(String noresep) {
        boolean isVaidasi = false;
        PreparedStatement pre = null;
        ResultSet rset = null;
        try {
            pre = koneksi.prepareStatement("SELECT * FROM e_resep_racikan_rsifc WHERE no_resep = ?");
            pre.setString(1, noresep);
            rset = pre.executeQuery();
            while (rset.next()) {
                if (rset.getDate("validasi") != null) {
                    isVaidasi = true;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isVaidasi;
    }

    public static boolean isResepValidasi(String noresep) {
        boolean isVaidasi = false;
        PreparedStatement pre = null;
        ResultSet rset = null;
        try {
            pre = koneksi.prepareStatement("SELECT * FROM e_resep_rsifc WHERE no_resep = ?");
            pre.setString(1, noresep);
            rset = pre.executeQuery();
            while (rset.next()) {
                if (rset.getDate("validasi") != null) {
                    isVaidasi = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResepDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rset != null) {

                    rset.close();

                }
                if (pre != null) {
                    pre.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ObatDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return isVaidasi;
    }

}
