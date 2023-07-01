package divingcalculations;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class DriverGui implements ActionListener {

    private static final float PG_VALUE_DEFAULT = 1.4f;
    private static final int FG_VALUE_DEFAULT = 32;
    private static final int D_VALUE_DEFAULT = 33;

    private static final int START_O2_DEFAULT = 21;
    private static final int END_O2_DEFAULT = 25;
    private static final int START_DEPTH_DEFAULT = 3;
    private static final int END_DEPTH_DEFAULT = 31;

    private DefaultTableModel model;

    private JSlider jSliderO2;
    private JSlider jSliderDepth;
    private O2DepthPanel o2DepthPanel;

    private DiveFormulas diveFormulas;

    private JTable table;
    private Box topLeftContainer;
    private JFrame frame;
    private Container container;
    private Box topContainer;
    private String calculateType;  //选择的计算类型
    private String showTableType;  //选择的表格类型
    private JTextArea resultArea; //计算结果显示区域

    private JSpinner spinnerPg;
    private JSpinner spinnerFg;
    private JSpinner spinnerDepth;
    private String tip = "";
    private float pgValue = PG_VALUE_DEFAULT; //pg的值
    private int fgValue = FG_VALUE_DEFAULT; //fg的值
    private int depthValue = D_VALUE_DEFAULT; //D(m)的值

    private int startO2Value = START_O2_DEFAULT;
    private int endO2Value = END_O2_DEFAULT;
    private int startDepthValue = START_DEPTH_DEFAULT;
    private int endDepthValue = END_DEPTH_DEFAULT;

    private JSpinner spinnerStartO2;
    private JSpinner spinnerEndO2;
    private JSpinner spinnerStartDepth;
    private JSpinner spinnerEndDepth;


    public static void main(String[] args) {
        new DriverGui();
    }

    public DriverGui() {
        diveFormulas = new DiveFormulas();
        frame = new JFrame("Computer Programming");
        frame.setSize(1280, 720);
        container = frame.getContentPane();
        container.setLayout(new GridLayout(2, 1));
        topContainer = Box.createHorizontalBox();
        topLeftContainer = Box.createVerticalBox();

        //top left panel init
        JLabel standardLabel = new JLabel("Standard Calculations");
        standardLabel.setFont(new Font("Default", Font.BOLD, 18));
        standardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLeftContainer.add(standardLabel);
        JPanel standardJPanel = new JPanel();
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        standardJPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        standardJPanel.setLayout(new GridLayout(1, 3, 20, 5));
        standardJPanel.add(createTypeSelector()); //calculation模块，选择要计算的内容
        standardJPanel.add(createCirclePanel()); //Pg + Fg + D(m)
        standardJPanel.add(createResultPanel()); //计算结果展示模块
        topLeftContainer.add(standardJPanel);
        JLabel tabularLabel = new JLabel("Tabular Calculations");
        tabularLabel.setFont(new Font("Default", Font.BOLD, 18));
        tabularLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLeftContainer.add(tabularLabel);
        Box tabularBox = Box.createHorizontalBox();

        tabularBox.add(createTabularPanel());
        tabularBox.add(createLogo());

        topLeftContainer.add(tabularBox);

        topContainer.add(topLeftContainer);
        //top right panel init
        jSliderO2 = new JSlider(JSlider.VERTICAL, 18, 98, FG_VALUE_DEFAULT);
        jSliderO2.setMajorTickSpacing(5);
        jSliderO2.setMinorTickSpacing(1);
        jSliderO2.setPaintTrack(true);
        jSliderO2.setPaintTicks(true);
        jSliderO2.setSnapToTicks(true);
        jSliderO2.setPaintLabels(true);
        jSliderO2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                if (!slider.getValueIsAdjusting()) {
                    int value = slider.getValue();
                    fgValue = value;
                    o2DepthPanel.setO2(value);
                    spinnerFg.setValue(value);
                }

            }
        });
        topContainer.add(jSliderO2);

        o2DepthPanel = new O2DepthPanel(FG_VALUE_DEFAULT);
        topContainer.add(o2DepthPanel);

        jSliderDepth = new JSlider(JSlider.VERTICAL, 0, 80, D_VALUE_DEFAULT);
        jSliderDepth.setMajorTickSpacing(5);
        jSliderDepth.setMinorTickSpacing(1);
        jSliderDepth.setPaintTrack(true);
        jSliderDepth.setPaintTicks(true);
        jSliderDepth.setSnapToTicks(true);
        jSliderDepth.setPaintLabels(true);
        jSliderDepth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                if (!slider.getValueIsAdjusting()) {
                    int value = slider.getValue();
                    depthValue = value;
                    spinnerDepth.setValue(value);
                }

            }
        });
        topContainer.add(jSliderDepth);

        container.add(topContainer);

        // 加载空表格数据
        Vector vData = new Vector(); // 数据行向量集，因为列表不止一行，往里面添加数据行向量，添加方法add(row)
        Vector vName = new Vector(); // 列名（标题）向量，使用它的add()方法添加列名
        model = new DefaultTableModel(vData, vName) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable();
        table.setModel(model);

        JScrollPane jScrollPane = new JScrollPane(table);
        container.add(jScrollPane);


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private JPanel createTabularPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2, 2, 10, 10));
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        jPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        Box box = Box.createHorizontalBox();
        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titleButtons = BorderFactory.createTitledBorder(etchedBorder, "O2 Percentage");
        titleButtons.setTitleJustification(TitledBorder.LEFT);
        box.setBorder(titleButtons);
        box.add(new JLabel("start o2:"));
        SpinnerModel startO2 = new SpinnerNumberModel(START_O2_DEFAULT, 0, 100, 1);
        spinnerStartO2 = new JSpinner(startO2);
        spinnerStartO2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) ((JSpinner) e.getSource()).getValue();
                startO2Value = value;
            }
        });
        box.add(spinnerStartO2);
        box.add(new JLabel("end o2:"));
        SpinnerModel endO2 = new SpinnerNumberModel(END_O2_DEFAULT, 0, 100, 1);
        spinnerEndO2 = new JSpinner(endO2);
        spinnerEndO2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) ((JSpinner) e.getSource()).getValue();
                endO2Value = value;
            }
        });
        box.add(spinnerEndO2);

        jPanel.add(box);

        Box whichTable = Box.createHorizontalBox();
        Border whichTableBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titleWhichTable = BorderFactory.createTitledBorder(whichTableBorder, "Which Table");
        titleWhichTable.setTitleJustification(TitledBorder.LEFT);
        whichTable.setBorder(titleWhichTable);
        ButtonGroup group = new ButtonGroup();
        JRadioButton jRadioButtonEAD = new JRadioButton("EDA Table");
        jRadioButtonEAD.addActionListener(this);
        JRadioButton jRadioButtonPP = new JRadioButton("PP Table");
        jRadioButtonPP.addActionListener(this);

        group.add(jRadioButtonPP);
        group.add(jRadioButtonEAD);

        whichTable.add(jRadioButtonPP);
        whichTable.add(jRadioButtonEAD);

        jPanel.add(whichTable);

        Box boxDepths = Box.createHorizontalBox();
        Border depthBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titleDepthButtons = BorderFactory.createTitledBorder(depthBorder, "O2 Percentage");
        titleDepthButtons.setTitleJustification(TitledBorder.LEFT);
        boxDepths.setBorder(titleDepthButtons);
        boxDepths.add(new JLabel("start depth:"));
        SpinnerModel startDepth = new SpinnerNumberModel(START_DEPTH_DEFAULT, 0, 100, 1);
        spinnerStartDepth = new JSpinner(startDepth);
        spinnerStartDepth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) ((JSpinner) e.getSource()).getValue();
                startDepthValue = value;
            }
        });
        boxDepths.add(spinnerStartDepth);
        boxDepths.add(new JLabel("end depth:"));
        spinnerEndDepth = new JSpinner(new SpinnerNumberModel(END_DEPTH_DEFAULT, 0, 100, 1));
        spinnerEndDepth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) ((JSpinner) e.getSource()).getValue();
                endDepthValue = value;
            }
        });
        boxDepths.add(spinnerEndDepth);

        jPanel.add(boxDepths);

        JButton jButton = new JButton("Create Table");
        jButton.addActionListener(this);
        jPanel.add(jButton);

        return jPanel;
    }


    private ImagePanel createLogo() {
        ImagePanel imagePanel = new ImagePanel("resources/sdc_logo.png");
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        imagePanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        return imagePanel;
    }

    private JPanel createCirclePanel() {
        JPanel panelCircle = new JPanel();
        panelCircle.setBackground(Color.BLACK);
        panelCircle.setOpaque(true);
        panelCircle.setLayout(new GridLayout(2, 1, 5, 5));
        SpinnerModel spinnerModelPg = new SpinnerNumberModel(PG_VALUE_DEFAULT, 1.1f, 1.6f, 0.1f);
        spinnerPg = new JSpinner(spinnerModelPg);
        spinnerPg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = (Double) ((JSpinner) e.getSource()).getValue();
                System.out.println("====》 : " + Math.round(value * 10) / 10f);
                pgValue = Math.round(value * 10) / 10f;
            }
        });

        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.CENTER));
        top.add(new Label("Pg"));
        top.add(spinnerPg);

        top.setBackground(Color.ORANGE);
        panelCircle.add(top);
        SpinnerModel spinnerModelFg = new SpinnerNumberModel(FG_VALUE_DEFAULT, 0, 100, 1);
        spinnerFg = new JSpinner(spinnerModelFg);
        spinnerFg.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) ((JSpinner) e.getSource()).getValue();
                System.out.println("====》 : " + value);
                fgValue = value;
                // 右侧O2刻度同时变化
                jSliderO2.setValue(value);
                o2DepthPanel.setO2(value);
            }
        });
        JPanel bLeft = new JPanel();
        bLeft.setLayout(new FlowLayout(FlowLayout.CENTER));
        bLeft.add(new Label("Fg"));
        bLeft.add(spinnerFg);


        SpinnerModel spinnerModelDepth = new SpinnerNumberModel(D_VALUE_DEFAULT, 0, 100, 1);
        spinnerDepth = new JSpinner(spinnerModelDepth);
        spinnerDepth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) ((JSpinner) e.getSource()).getValue();
                System.out.println("====》 : " + value);
                depthValue = value;
                // 右侧Depth刻度同时变化
                jSliderDepth.setValue(value);
            }
        });
        JPanel panelCircleBottom = new JPanel();
        panelCircleBottom.setBackground(Color.BLACK);
        panelCircleBottom.setLayout(new GridLayout(1, 2, 5, 5));
        JPanel bRight = new JPanel();
        bRight.setLayout(new FlowLayout(FlowLayout.CENTER));
        bRight.add(new Label("D(m)"));
        bRight.add(spinnerDepth);

        bLeft.setBackground(Color.ORANGE);
        bRight.setBackground(Color.ORANGE);
        panelCircleBottom.add(bLeft);
        panelCircleBottom.add(bRight);

        panelCircle.add(panelCircleBottom);

        return panelCircle;
    }

    private JPanel createResultPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setColumns(14);
        resultArea.setRows(3);
        resultArea.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(resultArea);
        jPanel.add(scroll, BorderLayout.CENTER);
        JButton jButton = new JButton("Do Calculation");
        jButton.addActionListener(this);
        jPanel.add(jButton, BorderLayout.SOUTH);

        return jPanel;
    }

    private JPanel createTypeSelector() {
        Border typeBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titleButtons = BorderFactory.createTitledBorder(typeBorder, "Calculation");
        titleButtons.setTitleJustification(TitledBorder.LEFT);
        JPanel panelTypes = new JPanel();
        panelTypes.setBorder(titleButtons);
        panelTypes.setLayout(new GridLayout(5, 1));
        ButtonGroup group = new ButtonGroup();
        JRadioButton jRadioButtonMOD = new JRadioButton("Maximum Operating Depth");
        jRadioButtonMOD.addActionListener(this);
        JRadioButton jRadioButtonSMOD = new JRadioButton("Standard Maximum Operating Depth");
        jRadioButtonSMOD.addActionListener(this);
        JRadioButton jRadioButtonBM = new JRadioButton("Best Mix");
        jRadioButtonBM.addActionListener(this);
        JRadioButton jRadioButtonPP = new JRadioButton("Partial Pressure");
        jRadioButtonPP.addActionListener(this);
        JRadioButton jRadioButtonEAD = new JRadioButton("Equivalent Air Depth");
        jRadioButtonEAD.addActionListener(this);
        group.add(jRadioButtonMOD);
        group.add(jRadioButtonSMOD);
        group.add(jRadioButtonBM);
        group.add(jRadioButtonPP);
        group.add(jRadioButtonEAD);

        panelTypes.add(jRadioButtonMOD);
        panelTypes.add(jRadioButtonSMOD);
        panelTypes.add(jRadioButtonBM);
        panelTypes.add(jRadioButtonPP);
        panelTypes.add(jRadioButtonEAD);

        return panelTypes;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Standard Maximum Operating Depth":
                spinnerPg.setValue(1.4);
                spinnerPg.setEnabled(false);
                calculateType = command;
                break;
            case "Maximum Operating Depth":
            case "Best Mix":
            case "Partial Pressure":
            case "Equivalent Air Depth":
                spinnerPg.setEnabled(true);
                calculateType = command;
                break;
            case "Do Calculation":
                doCalculation();
                break;
            case "Create Table":
                createTable();
                break;
            case "EDA Table":
            case "PP Table":
                showTableType = command;
                break;
            default:
                break;
        }
    }

    private void createTable() {
        if (showTableType == null || showTableType.equals("")) {
            return;
        }

        //todo 加载表格数据
        Vector datas = new Vector(); // 数据行向量集，因为列表不止一行，往里面添加数据行向量，添加方法add(row)
        Vector headerNames = new Vector(); // 列名（标题）向量，使用它的add()方法添加列名

        //数据行向量，使用它的add()添加元素，比如整数、String、Object等，有几行就new几个行向量。 其中的元素就是单元格的内容


        if (showTableType.equals("EDA Table")) {
            headerNames.add("\\");
            for (int i = startO2Value; i <= endO2Value; i++) {
                headerNames.add(i + "");
            }
            for (int i = startDepthValue; i <= endDepthValue; i = i + 3) {
                Vector vRow = new Vector();
                vRow.add(i + "");
                for (int k = startO2Value; k <= endO2Value; k++) {
                    int res = diveFormulas.calculateEAD(i, k);
                    vRow.add(res + "");
                }
                datas.add(vRow);
            }
        } else if (showTableType.equals("PP Table")) {
            headerNames.add("\\");
            for (int i = startO2Value; i <= endO2Value; i++) {
                headerNames.add(i + "");
            }
            for (int i = startDepthValue; i <= endDepthValue; i = i + 3) {
                Vector vRow = new Vector();
                vRow.add(i + "");
                for (int k = startO2Value; k <= endO2Value; k++) {
                    float res = diveFormulas.calculatePP(i, k);
                    if (res > 1.6f) {
                        vRow.add("");
                    } else {
                        vRow.add(String.format("%.2f",res));
                    }
                }
                datas.add(vRow);
            }
        }

        model = new DefaultTableModel(datas, headerNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setGridColor(Color.red);
        table.setShowGrid(true);
        table.setModel(model);

    }

    private void doCalculation() {
        if (calculateType == null || calculateType.equals("")) {
            return;
        }

        switch (calculateType) {
            case "Standard Maximum Operating Depth":
                int resultSMOD = diveFormulas.calculateMOD(pgValue, fgValue);
                spinnerDepth.setValue(resultSMOD);
                tip += "Maximum operating depth for a dive with " + fgValue + "% O2 with a partial pressure of 1.4 is " +
                        resultSMOD + " metres";
                break;
            case "Maximum Operating Depth":
                int resultMOD = diveFormulas.calculateMOD(pgValue, fgValue);
                spinnerDepth.setValue(resultMOD);
                tip += "Maximum operating depth for a dive with " + fgValue + "% O2 with a partial pressure of " +
                        pgValue + " is " + resultMOD + " metres";
                break;
            case "Best Mix":
                int resultBM = diveFormulas.calculateBM(pgValue, depthValue);
                spinnerFg.setValue(resultBM);
                tip += "Best mix for a dive to " + depthValue + "m is " + resultBM + "% O2";
                break;
            case "Partial Pressure":
                float resultPP = diveFormulas.calculatePP(depthValue, fgValue);
                spinnerPg.setValue(resultPP);
                tip += "The partial pressure of Oxygen for a dive to " + depthValue + " metres with a percentage of Oxygen of "
                        + fgValue + "% is " + resultPP + " ata";
                break;
            case "Equivalent Air Depth":
                int resultEAD = diveFormulas.calculateEAD(depthValue, fgValue);
                tip += "Equivalent Air Depth for a dive with " + fgValue + "% O2 to a depth of " + depthValue
                        + " metres is " + resultEAD + " metres";
                break;
        }
        tip += "\n\n===============================\n";
        resultArea.setText(tip);
    }


}
