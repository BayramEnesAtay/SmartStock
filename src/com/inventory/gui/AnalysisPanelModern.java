package com.inventory.gui;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Simulates a stored procedure analysis with a loading bar and result
 * card.  Computes an estimate of stock depletion based on the total
 * quantity of products.  When the analysis is started a progress bar
 * animates for a short duration before the result is displayed.
 */
public class AnalysisPanelModern extends JPanel {
    private final ProductDAO dao = new ProductDAO();
    private final JProgressBar progressBar;
    private final JPanel resultCard;
    private final JLabel resultLabel;
    private final DecimalFormat df = new DecimalFormat("0.0");

    public AnalysisPanelModern() {
        setLayout(new BorderLayout());
        setBackground(new Color(0x1e272c));
        JButton runBtn = new JButton("Analizi Çalıştır");
        runBtn.putClientProperty("JButton.buttonType", "roundRect");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(runBtn);
        add(top, BorderLayout.NORTH);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        add(progressBar, BorderLayout.CENTER);
        resultCard = new JPanel();
        resultCard.setOpaque(false);
        resultCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x455a64)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        resultLabel = new JLabel();
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 18f));
        resultCard.add(resultLabel);
        resultCard.setVisible(false);
        add(resultCard, BorderLayout.SOUTH);
        runBtn.addActionListener(e -> runAnalysis());
    }
    private void runAnalysis() {
        // reset UI
        resultCard.setVisible(false);
        progressBar.setVisible(true);
        // Use Swing Timer to simulate loading for 2 seconds
        Timer timer = new Timer(2000, e -> {
            progressBar.setVisible(false);
            showResult();
        });
        timer.setRepeats(false);
        timer.start();
    }
    private void showResult() {
        List<Product> products = dao.getAllProducts();
        if (products.isEmpty()) {
            resultLabel.setText("Hiç ürün yok.");
        } else {
            int totalQty = products.stream().mapToInt(Product::getQuantity).sum();
            double avgDaily = totalQty / 30.0;
            double days = totalQty / avgDaily;
            // Equivalent to 30
            resultLabel.setText("Tahmini Stok Bitiş Süresi: " + df.format(days) + " gün");
        }
        resultCard.setVisible(true);
    }
}