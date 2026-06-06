package assignment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import assignment.db.DBManager;


public class CareerRecommendationGUI extends JFrame implements ModelListener {

    private final RecommendationController controller = new RecommendationController();

    private JComboBox<String> educationCombo;
    private JTextField skillsField;
    private final List<JCheckBox> interestBoxes = new ArrayList<>();
    private JTextField workingStyleField;
    private JTextField careerGoalField;
    private JTextField strengthsField;

    private DefaultTableModel tableModel;
    private JTable resultsTable;
    private JTextArea detailsArea;
    private JTextArea reflectionArea;
    private JLabel statusLabel;

    private List<RecommendationResult> displayed = new ArrayList<>();

    public CareerRecommendationGUI() {
        super("Career Path Planner — Recommendation System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DBManager.shutdown();
                dispose();
                System.exit(0);
            }
        });
        setSize(950, 640);
        setLocationRelativeTo(null);

        controller.addListener(this);

        setLayout(new BorderLayout(10, 10));
        add(buildFormPanel(), BorderLayout.WEST);
        add(buildResultsPanel(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Your Profile"));
        panel.setPreferredSize(new Dimension(340, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1.0;

        panel.add(new JLabel("Education level:"), g);
        educationCombo = new JComboBox<>(new String[]{
            "Bachelor", "Master", "PhD / Doctorate", "Diploma", "Certificate", "Other"
        });
        g.gridy++;
        panel.add(educationCombo, g);

        g.gridy++;
        panel.add(new JLabel("Skills (comma-separated, e.g. Java, SQL, Python):"), g);
        skillsField = new JTextField();
        g.gridy++;
        panel.add(skillsField, g);

        g.gridy++;
        panel.add(new JLabel("Interests (careers you are interested in):"), g);
        JPanel interestsPanel = new JPanel(new GridLayout(0, 1));
        for (CareerPath career : controller.getAvailableCareers()) {
            JCheckBox box = new JCheckBox(career.getName());
            interestBoxes.add(box);
            interestsPanel.add(box);
        }
        g.gridy++;
        panel.add(interestsPanel, g);

        g.gridy++;
        panel.add(new JLabel("Working style (optional):"), g);
        workingStyleField = new JTextField();
        g.gridy++;
        panel.add(workingStyleField, g);

        g.gridy++;
        panel.add(new JLabel("Career goal (optional):"), g);
        careerGoalField = new JTextField();
        g.gridy++;
        panel.add(careerGoalField, g);

        g.gridy++;
        panel.add(new JLabel("Strengths (comma-separated, optional):"), g);
        strengthsField = new JTextField();
        g.gridy++;
        panel.add(strengthsField, g);

        JButton generateBtn = new JButton("Generate Recommendations");
        generateBtn.addActionListener(e -> onGenerate());
        g.gridy++;
        g.insets = new Insets(12, 6, 4, 6);
        panel.add(generateBtn, g);

        JButton clearBtn = new JButton("Clear Form");
        clearBtn.addActionListener(e -> onClear());
        g.gridy++;
        g.insets = new Insets(4, 6, 4, 6);
        panel.add(clearBtn, g);

        g.gridy++;
        g.weighty = 1.0;
        panel.add(Box.createGlue(), g);

        return panel;
    }

    private JPanel buildResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(8, 0, 8, 8));

        tableModel = new DefaultTableModel(new Object[]{"Rank", "Career", "Score"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onRowSelected();
            }
        });
        JScrollPane tableScroll = new JScrollPane(resultsTable);
        tableScroll.setBorder(new TitledBorder("Ranked Recommendations"));
        tableScroll.setPreferredSize(new Dimension(0, 180));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(new TitledBorder("Explanation (select a row above)"));

        JPanel reflectionPanel = new JPanel(new BorderLayout(4, 4));
        reflectionPanel.setBorder(new TitledBorder("Your Reflection"));
        reflectionArea = new JTextArea(3, 20);
        reflectionArea.setLineWrap(true);
        reflectionArea.setWrapStyleWord(true);
        JButton saveReflectionBtn = new JButton("Save Reflection to Selected Career");
        saveReflectionBtn.addActionListener(e -> onSaveReflection());
        reflectionPanel.add(new JScrollPane(reflectionArea), BorderLayout.CENTER);
        reflectionPanel.add(saveReflectionBtn, BorderLayout.SOUTH);

        JSplitPane lower = new JSplitPane(JSplitPane.VERTICAL_SPLIT, detailsScroll, reflectionPanel);
        lower.setResizeWeight(0.7);

        JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, lower);
        main.setResizeWeight(0.35);
        panel.add(main, BorderLayout.CENTER);

        return panel;
    }


    private JPanel buildStatusBar() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBorder(new EmptyBorder(4, 8, 6, 8));

        JButton saveBtn = new JButton("Save to Database");
        saveBtn.addActionListener(e -> onSaveToDatabase());

        JButton loadBtn = new JButton("Load Latest from Database");
        loadBtn.addActionListener(e -> onLoadFromDatabase());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(saveBtn);
        buttons.add(loadBtn);

        statusLabel = new JLabel("Ready. Enter a profile and click Generate Recommendations.");

        panel.add(buttons, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.EAST);
        return panel;
    }

    private void onGenerate() {
        String education = (String) educationCombo.getSelectedItem();

        List<String> skills = parseCsv(skillsField.getText());
        if (skills.isEmpty()) {
            warn("Please enter at least one skill.");
            return;
        }

        List<String> interests = new ArrayList<>();
        for (JCheckBox box : interestBoxes) {
            if (box.isSelected()) {
                interests.add(box.getText());
            }
        }
        if (interests.isEmpty()) {
            warn("Please select at least one interest.");
            return;
        }

        List<String> strengths = parseCsv(strengthsField.getText());

        UserProfile profile = new UserProfile(
            education, skills, interests,
            workingStyleField.getText().trim(),
            careerGoalField.getText().trim(),
            strengths
        );

        if (!profile.isValid()) {
            warn("Profile is incomplete. Education, skills and interests are required.");
            return;
        }

        List<RecommendationResult> results = controller.generate(profile);

        statusLabel.setText("Generated recommendations for " + results.size() + " careers.");
        if (!results.isEmpty()) {
            resultsTable.setRowSelectionInterval(0, 0);
        }
    }

    private void onClear() {
        educationCombo.setSelectedIndex(0);
        skillsField.setText("");
        for (JCheckBox box : interestBoxes) {
            box.setSelected(false);
        }
        workingStyleField.setText("");
        careerGoalField.setText("");
        strengthsField.setText("");
        refreshTable(new ArrayList<>());
        detailsArea.setText("");
        reflectionArea.setText("");
        statusLabel.setText("Form cleared.");
    }

    private void onRowSelected() {
        int row = resultsTable.getSelectedRow();
        if (row < 0 || row >= displayed.size()) {
            detailsArea.setText("");
            reflectionArea.setText("");
            return;
        }
        RecommendationResult result = displayed.get(row);
        detailsArea.setText(result.getDetailedView());
        detailsArea.setCaretPosition(0);
        reflectionArea.setText(result.getUserReflection());
    }

    private void onSaveReflection() {
        int row = resultsTable.getSelectedRow();
        if (row < 0 || row >= displayed.size()) {
            warn("Select a career in the table first.");
            return;
        }
        RecommendationResult result = displayed.get(row);
        result.setUserReflection(reflectionArea.getText().trim());
        detailsArea.setText(result.getDetailedView());
        detailsArea.setCaretPosition(0);

        boolean persisted = controller.updateReflection(result);
        if (persisted) {
            statusLabel.setText("Reflection saved to the database for "
                    + result.getCareerName() + ".");
        } else {
            statusLabel.setText("Reflection saved (in memory) for " + result.getCareerName()
                    + ". Use 'Save to Database' to persist it.");
        }
    }

    private void onSaveToDatabase() {
        if (!controller.hasResults()) {
            warn("Nothing to save. Generate recommendations first.");
            return;
        }
        int profileId = controller.saveToDatabase();
        if (profileId == -1) {
            warn("Save failed. Check that the Derby database library is on the classpath.");
            statusLabel.setText("Save failed.");
        } else {
            statusLabel.setText("Saved profile #" + profileId + " and "
                    + controller.getCurrentResults().size() + " results to the database.");
            JOptionPane.showMessageDialog(this,
                    "Saved successfully (profile id = " + profileId + ").",
                    "Database", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onLoadFromDatabase() {
        List<RecommendationResult> loaded = controller.loadLatestFromDatabase();
        if (loaded.isEmpty()) {
            warn("No saved results found in the database.");
            statusLabel.setText("Nothing to load.");
            return;
        }
        UserProfile profile = controller.getCurrentProfile();
        if (profile != null) {
            fillFormFromProfile(profile);
        }
        resultsTable.setRowSelectionInterval(0, 0);
        statusLabel.setText("Loaded " + loaded.size() + " results from the database.");
    }

    @Override
    public void onResultsChanged(List<RecommendationResult> results) {
        refreshTable(results);
    }

    private void refreshTable(List<RecommendationResult> results) {
        this.displayed = (results != null) ? results : new ArrayList<>();
        tableModel.setRowCount(0);
        for (RecommendationResult r : displayed) {
            tableModel.addRow(new Object[]{r.getRank(), r.getCareerName(), r.getScore()});
        }
        if (displayed.isEmpty()) {
            detailsArea.setText("");
            reflectionArea.setText("");
        }
    }

    private void fillFormFromProfile(UserProfile profile) {
        String edu = (profile.getEducationLevel() == null) ? "" : profile.getEducationLevel().toLowerCase();
        String match = "Other";
        if (edu.contains("phd") || edu.contains("doctorate")) match = "PhD / Doctorate";
        else if (edu.contains("master")) match = "Master";
        else if (edu.contains("bachelor") || edu.contains("degree")) match = "Bachelor";
        else if (edu.contains("diploma")) match = "Diploma";
        else if (edu.contains("certificate")) match = "Certificate";
        educationCombo.setSelectedItem(match);

        skillsField.setText(String.join(", ", profile.getSkills()));
        for (JCheckBox box : interestBoxes) {
            box.setSelected(profile.getInterests().contains(box.getText()));
        }
        workingStyleField.setText(profile.getWorkingStyle());
        careerGoalField.setText(profile.getCareerGoal());
        strengthsField.setText(String.join(", ", profile.getStrengths()));
    }

    private List<String> parseCsv(String raw) {
        List<String> out = new ArrayList<>();
        if (raw == null) return out;
        for (String part : raw.split(",")) {
            String t = part.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Input needed", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {

        }
        SwingUtilities.invokeLater(() -> new CareerRecommendationGUI().setVisible(true));
    }
}