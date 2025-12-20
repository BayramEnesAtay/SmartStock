package com.inventory.gui;

import com.inventory.dao.AnalysisDAO;
import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Analysis panel that calls a stored procedure to estimate
 * stock depletion time per product.
 */
public class AnalysisPanelModern extends JPanel {

    private final ProductDAO productDAO = new ProductDAO();
    private final AnalysisDAO analysisDAO = new AnalysisDAO();

    private final JLabel statusLabel;
    private final JProgressBar progressBar;
    private final JPanel resultPanel;

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

        /* ---------- LOADING ---------- */
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        add(progressBar, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        /* ---------- RESULT PANEL ---------- */
        resultPanel = new JPanel();
        resultPanel.setOpaque(false);
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        resultPanel.setVisible(false);
        add(resultPanel, BorderLayout.EAST);

        runBtn.addActionListener(e -> runAnalysis());
    }

    private void runAnalysis() {

        // reset
        resultPanel.removeAll();
        resultPanel.setVisible(false);
        statusLabel.setText("Yükleniyor...");
        progressBar.setVisible(true);

        // minimal loading (1.2 sn)
        Timer timer = new Timer(1200, e -> {
            progressBar.setVisible(false);
            statusLabel.setText("Analiz tamamlandı");
            showResults();
        });

        timer.setRepeats(false);
        timer.start();
    }

    private void showResults() {

        List<Product> products = productDAO.getAllProducts();

        if (products.isEmpty()) {
            statusLabel.setText("Hiç ürün bulunamadı.");
            return;
        }

        for (Product p : products) {

            Integer days = analysisDAO.getStockPrediction(p.getId());

            JLabel label = new JLabel(
                    "• " + p.getName() + " → " +
                            (days == null ? "Tahmin yok" : days + " gün")
            );

            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

            resultPanel.add(label);
        }

        resultPanel.setVisible(true);
        revalidate();
        repaint();
    }
}
