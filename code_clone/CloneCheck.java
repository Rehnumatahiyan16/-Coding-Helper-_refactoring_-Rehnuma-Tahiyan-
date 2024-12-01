private void processProjectFiles(String projectName, String pathname, int projectIndex) throws IOException {
    if (!new File(pathname).exists()) {
        ProjectReader.fileRead(Command.currentPath + "//" + projectName, projectIndex);
        Path projectPath = Paths.get(pathname);
        Files.createDirectories(projectPath);

        HashMap<String, String> projectFiles = (projectIndex == 0)
                ? ProjectReader.projectOne
                : ProjectReader.projectTwo;

        for (HashMap.Entry<String, String> entry : projectFiles.entrySet()) {
            new PreProcessing().ProcessFile(entry.getKey(), entry.getValue(), pathname);
        }
    }
}

private void calculateTfIdfVectors(String path1, String path2) {
    TfIdfCalculate ob = new TfIdfCalculate();
    ob.getUniqueWordProject1(path1);
    ob.getUniqueWordProject2(path2);
    ob.IdfCal();
    ob.tfIdfVectorProject1();
    ob.tfIdfVectorProject2();
}

private void clearData() {
    CosineSimilarity.similarArray.clear();
    ProjectReader.projectOne.clear();
    ProjectReader.projectTwo.clear();
    TfIdfCalculate.tfidfvectorProject1.clear();
    TfIdfCalculate.tfidfvectorProject2.clear();
    ProjectFileName1.clear();
    ProjectFileName2.clear();
}

public void Code_clone(String project1, String project2) throws IOException {
    path1 = pathGenerate(project1);
    path2 = pathGenerate(project2);

    processProjectFiles(project1, path1, 0);
    getFileListforProject1(project1);

    processProjectFiles(project2, path2, 1);
    getFileListforProject2(project2);

    calculateTfIdfVectors(path1, path2);

    CosineSimilarity sim = new CosineSimilarity();
    sim.getCosinesimilarity();

    new BoxAndWhiskerChart().display();

    clearData();
}

