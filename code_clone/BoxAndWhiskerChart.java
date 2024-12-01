private JFrame createFrame(String title) {
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    return frame;
}

private DefaultBoxAndWhiskerCategoryDataset createDataset() {
    DefaultBoxAndWhiskerCategoryDataset boxData = new DefaultBoxAndWhiskerCategoryDataset();
    for (int i = 0; i < CosineSimilarity.similarArray.size(); i++) {
        boxData.add(
                getInputData(CosineSimilarity.similarArray.get(i)),
                "First_Project vs Second_Project",
                CloneCheck.ProjectFileName1.get(i)
        );
    }
    return boxData;
}

private BoxAndWhiskerRenderer configureRenderer() {
    BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
    renderer.setFillBox(true);
    renderer.setUseOutlinePaintForWhiskers(true);
    renderer.setMedianVisible(true);
    renderer.setMeanVisible(false);
    return renderer;
}

private JFreeChart createChart(DefaultBoxAndWhiskerCategoryDataset dataset) {
    CategoryAxis xAxis = new CategoryAxis("First_Project_Files");
    NumberAxis yAxis = new NumberAxis("Second_Project_Values");
    BoxAndWhiskerRenderer renderer = configureRenderer();
    CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

    JFreeChart chart = new JFreeChart(
            "Box-and-Whisker Plot",
            new Font("SansSerif", Font.BOLD, 20),
            plot,
            true
    );
    chart.setBackgroundPaint(Color.LIGHT_GRAY);
    return chart;
}

public void display() {
    JFrame frame = createFrame("Clone_Check");
    DefaultBoxAndWhiskerCategoryDataset dataset = createDataset();
    JFreeChart chart = createChart(dataset);

    ChartPanel chartPanel = new ChartPanel(chart) {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }
    };

    frame.add(chartPanel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}

