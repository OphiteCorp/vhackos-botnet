package cz.ophite.mimic.vhackos.botnet.db.service;

import cz.ophite.mimic.vhackos.botnet.api.net.response.data.BankTransactionData;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.IpScanData;
import cz.ophite.mimic.vhackos.botnet.db.HibernateManager;
import cz.ophite.mimic.vhackos.botnet.db.dao.ScannedIpDao;
import cz.ophite.mimic.vhackos.botnet.db.dao.TransactionDao;
import cz.ophite.mimic.vhackos.botnet.db.dao.UserDao;
import cz.ophite.mimic.vhackos.botnet.db.entity.UserEntity;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Autowired;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Služba pro práci s databází.
 *
 * @author mimic
 */
@Inject
public final class DatabaseService {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private ScannedIpDao scannedIpDao;

    /**
     * Vyhldá uživatele podle IP.
     */
    public List<UserEntity> searchIp(String ip) {
        if (!HibernateManager.isConnected()) {
            return null;
        }
        LOG.info("Processing a database query for user IP: {}", ip);
        return userDao.getByIp(ip);
    }

    /**
     * Vyhledá všechny IP podle ID uživatele.
     */
    public List<String> searchIpByUserId(int userId) {
        if (!HibernateManager.isConnected()) {
            return Collections.emptyList();
        }
        LOG.info("Processing a database query for user ID: {}", userId);
        return userDao.getIps(userId);
    }

    /**
     * Přidá záznam o skenu IP.
     */
    public void addScanIp(IpScanData scan) {
        if (!HibernateManager.isConnected()) {
            return;
        }
        LOG.info("Processing a database query for scan IP: {}", scan.getIp());
        scannedIpDao.createOrUpdate(scan.getIp(), scan.getLevel(), scan.getFirewall(), null);
    }

    /**
     * Založí nebo aktualizuje naskenovanou IP.
     */
    public void updateScanIp(String ip, String userName, Integer level) {
        if (!HibernateManager.isConnected()) {
            return;
        }
        LOG.info("Processing a database query for brute IP: {}", ip);
        var user = userDao.getByUserName(userName);
        if (user != null) {
            // uživatel si změnil IP
            if (!user.getIps().contains(ip)) {
                user.getIps().add(ip);
                userDao.update(user);
            }
        } else {
            userDao.createOrUpdate(null, userName, ip);
        }
        // aktualizuje naskenovanou IP
        var scan = scannedIpDao.getByIp(ip);
        if (scan != null) {
            scan.setUserName(userName);

            if (level != null) {
                scan.setLevel(level);
            }
            scannedIpDao.update(scan);
        }
    }

    /**
     * Založí novou bankovní transakci.
     */
    public Map.Entry<Boolean, Boolean> addTransaction(BankTransactionData trans) {
        if (!HibernateManager.isConnected()) {
            return Map.entry(false, false);
        }
        LOG.info("Processing a database query for transaction: {} -> {}", trans.getFromIp(), trans.getToIp());

        var userFrom = userDao.create(trans.getFromId(), trans.getFromIp());
        var userTo = userDao.create(trans.getToId(), trans.getToIp());
        var existsTrans = transactionDao.isExistsByUserIds(trans.getFromId(), trans.getToId());
        var revealFrom = false;
        var revealTo = false;

        if (!existsTrans) {
            transactionDao.create(new Date(trans.getTime() * 1000), userFrom, trans.getFromIp(), userTo, trans
                    .getToIp(), trans.getAmount());
        }
        if (!Utils.isValidIp(trans.getFromIp())) {
            if (!userFrom.getIps().isEmpty()) {
                trans.setFromIp(userFrom.getIps().iterator().next());
                revealFrom = true;
            }
        }
        if (!Utils.isValidIp(trans.getToIp())) {
            if (!userTo.getIps().isEmpty()) {
                trans.setToIp(userTo.getIps().iterator().next());
                revealTo = true;
            }
        }
        return Map.entry(revealFrom, revealTo);
    }

    /**
     * Přidá log k IP uživatele.
     */
    public void addLog(String ip, String log) {
        if (!HibernateManager.isConnected()) {
            return;
        }
        LOG.info("Processing a database query for IP log: {}", ip);
        var users = userDao.getByIp(ip);

        if (!users.isEmpty()) {
            for (var u : users) {
                u.getLogs().add(log);
                userDao.update(u);
            }
        }
    }

    /**
     * Odebere neplatnou IP. U skenu jí označí za neplatnou.
     */
    public void invalidIp(String ip) {
        if (!HibernateManager.isConnected()) {
            return;
        }
        LOG.info("Processing a database query for invalidate IP: {}", ip);
        // odebere IP na uživateli
        var users = userDao.getByIp(ip);
        if (!users.isEmpty()) {
            for (var u : users) {
                if (u.getIps().contains(ip)) {
                    u.getIps().remove(ip);
                    userDao.update(u);
                }
            }
        }
        // u scanu jí označí za neplatnou
        var scan = scannedIpDao.getByIp(ip);
        if (scan != null) {
            scan.setValid(false);
            scannedIpDao.update(scan);
        }
    }
}
