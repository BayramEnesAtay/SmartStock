package com.inventory.gui;

import com.inventory.dao.AnalysisDAO;
import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AnalysisPanelModern extends JPanel {

    private final ProductDAO productDAO = new ProductDAO();
    private final AnalysisDAO analysisDAO = new AnalysisDAO();

    private final JLabel statusLabel;
    private final JPanel loadingPanel;
    private final JLabel loadingLabel;

    private final JTable resultTable;
    private final DefaultTableModel tableModel;
    private final JScrollPane tableScroll;

    private Timer loadingAnimTimer;
    private int dotCount = 0;

    public AnalysisPanelModern() {

        setLayout(new BorderLayout());
        setBackground(new Color(0x1e272c));

        /* ---------- TOP BAR ---------- */
        JButton runBtn = new JButton("Analizi Çalıştır");
        runBtn.putClientProperty("JButton.buttonType", "roundRect");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(runBtn);
        add(top, BorderLayout.NORTH);

        /* ---------- LOADING PANEL ---------- */
        loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setOpaque(false);

        loadingLabel = new JLabel("Analiz yapılıyor");
        loadingLabel.setForeground(Color.LIGHT_GRAY);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadingPanel.add(loadingLabel);

        /* ---------- STATUS ---------- */
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.GRAY);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        /* ---------- RESULT TABLE ---------- */
        tableModel = new DefaultTableModel(
                new Object[]{"Ürün", "Tahmini Stok Bitişi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(32);
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );
        resultTable.setFillsViewportHeight(true);

        // 🔹 Hücreleri ortala
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        resultTable.setDefaultRenderer(Object.class, centerRenderer);

        // 🔹 Header'ı ortala
        ((DefaultTableCellRenderer) resultTable
                .getTableHeader()
                .getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        tableScroll = new JScrollPane(resultTable);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        runBtn.addActionListener(e -> runAnalysis());
    }

    private void runAnalysis() {

        tableModel.setRowCount(0);
        statusLabel.setText(" ");

        remove(tableScroll);
        add(loadingPanel, BorderLayout.CENTER);

        revalidate();
        repaint();

        startLoadingAnimation();

        Timer timer = new Timer(1200, e -> {
            stopLoadingAnimation();
            remove(loadingPanel);
            statusLabel.setText("Analiz tamamlandı");
            showResults();
        });

        timer.setRepeats(false);
        timer.start();
    }

    private void startLoadingAnimation() {

        dotCount = 0;

        loadingAnimTimer = new Timer(350, e -> {
            dotCount = (dotCount + 1) % 4;
            loadingLabel.setText("Analiz yapılıyor" + ".".repeat(dotCount));
        });

        loadingAnimTimer.start();
    }

    private void stopLoadingAnimation() {
        if (loadingAnimTimer != null) {
            loadingAnimTimer.stop();
        }
    }

    private void showResults() {

        List<Product> products = productDAO.getAllProducts();

        if (products.isEmpty()) {
            statusLabel.setText("Hiç ürün bulunamadı.");
            return;
        }

        for (Product p : products) {

            Integer days = analysisDAO.getStockPrediction(p.getId());

            tableModel.addRow(new Object[]{
                    p.getName(),
                    days == null ? "Tahmin yok" : days + " gün"
            });
        }

        add(tableScroll, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
