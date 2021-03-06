package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.daoImpl.LesionDaoImpl;
import src.daoImpl.SiteCutaneDaoImpl;
import src.daoImpl.TranscriptomieDaoImpl;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.view.AddTranscriptomieView;
import src.view.SiteView;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class TranscriptomieController extends Controller implements Initializable {
    @FXML
    Label ID;

    @FXML
    Label emplacement;

    @FXML
    Label numSerie;

    @FXML
    Label concentration;

    @FXML
    Label ARNc;

    @FXML
    Label cy3;

    @FXML
    Label rendement;

    @FXML
    Label activitesSpec;

    @FXML
    Label critExclusion;

    @FXML
    Label RIN;

    @FXML
    Button fichierBrut;

    @FXML
    Button qualityReport;

    @FXML
    Button retour;



    @FXML
    Button ajouter;

    @FXML
    Button supprimer;

    private TranscriptomieDaoImpl transcriptomieDaoImpl;
    private TranscriptomicAnalysis transcriptomicAnalysis;
    private int siteId;

    public TranscriptomieController(Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        this.connection = connection;
        this.transcriptomicAnalysis = transcriptomicAnalysis;
        this.fileManager = fileManager;
        this.siteId = siteId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (this.transcriptomicAnalysis != null) {
            this.ajouter.setText("Modifier");
            if (this.transcriptomicAnalysis.getFichierBrut() == null)
                fichierBrut.setDisable(true);
            if (this.transcriptomicAnalysis.getQualityReport() == null)
                qualityReport.setDisable(true);

        } else {
            enableButtons(false, false);
        }



        this.display(this.transcriptomicAnalysis);
        this.transcriptomieDaoImpl = new TranscriptomieDaoImpl(connection);
    }

    void display(TranscriptomicAnalysis transcriptomicAnalysis) {
        if (transcriptomicAnalysis != null) {
            this.ID.setText(Integer.toString(transcriptomicAnalysis.getIdBdd()));
            this.emplacement.setText(Integer.toString(transcriptomicAnalysis.getLamellaLocation()));
            this.numSerie.setText(transcriptomicAnalysis.getSerialNumber());
            this.cy3.setText(Double.toString(transcriptomicAnalysis.getCyanine()));
            this.rendement.setText(Double.toString(transcriptomicAnalysis.getYield()));
            this.concentration.setText(Double.toString(transcriptomicAnalysis.getConcentration()));
            this.critExclusion.setText(transcriptomicAnalysis.getExclusionCriteria());
            this.activitesSpec.setText(Double.toString(transcriptomicAnalysis.getSpecificActivity()));
            this.ARNc.setText(Double.toString(transcriptomicAnalysis.getARNC()));
            this.RIN.setText(Double.toString(transcriptomicAnalysis.getRIN()));
        } else {
            enableButtons(false, false);
            this.ID.setText("");
            this.emplacement.setText("");
            this.numSerie.setText("");
            this.cy3.setText("");
            this.rendement.setText("");
            this.concentration.setText("");
            this.critExclusion.setText("");
            this.activitesSpec.setText("");
            this.ARNc.setText("");
            this.RIN.setText("");
        }
    }

    @FXML
    private void fichierBrutAction() {

        this.startDownload(this.transcriptomicAnalysis.getFichierBrut(), this.fichierBrut);
    }

    @FXML
    private void qualityReportAction() {
        this.startDownload(this.transcriptomicAnalysis.getQualityReport(), this.qualityReport);
    }

    @FXML
    private void retour() {
        this.setStage(this.retour);
        new SiteView(this.stage, new LesionDaoImpl(this.connection).selectById(new SiteCutaneDaoImpl(this.connection).selectById(this.siteId).getIdLesion()), this.connection, this.fileManager);

        this.stage.close();
    }

    @FXML
    private void addButtonAction() {
        this.transcriptomicAnalysis = this.transcriptomieDaoImpl.selectBySite(siteId);
        this.setStage(this.ajouter);

        new AddTranscriptomieView(this.stage, this, this.connection, this.fileManager, this.transcriptomicAnalysis, this.siteId);
        this.stage.close();
    }

    @FXML
    private void dellButtonAction() {
        this.transcriptomicAnalysis = this.transcriptomieDaoImpl.selectBySite(this.siteId);
        if (this.transcriptomicAnalysis != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppresion");
            alert.setHeaderText("Vous allez supprimer une analyse transcriptomique");
            alert.setContentText("Confirmer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                this.enableButtons(false, true);
                RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.supprimer, null, this.progressBar, this.progressLabel);
                this.remove(removeTask, this.transcriptomicAnalysis);
            } else alert.close();
        }
    }

    private void remove(RemoveTask removeTask, TranscriptomicAnalysis transcriptomicAnalysis) {
        TranscriptomieController.remove(removeTask, new ArrayList<TranscriptomicAnalysis>() {{
            add(transcriptomicAnalysis);
        }});
    }

    static void remove(RemoveTask task, ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses) {
        TranscriptomieController.removeFTPSQL(task, transcriptomicAnalyses);

        new Thread(task).start();
    }

    private static void removeFTPSQL(RemoveTask removeTask, ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses) {
        TranscriptomieController.removeFTP(removeTask, transcriptomicAnalyses);
        TranscriptomieController.removeSQL(transcriptomicAnalyses);
    }

    private static void removeSQL(ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses) {
        for (TranscriptomicAnalysis transcriptomicAnalysis : transcriptomicAnalyses)
            TranscriptomieDaoImpl.delete(transcriptomicAnalysis.getId());
    }

    private static void removeFTP(RemoveTask removeTask, ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses) {
        ArrayList<String> urls = new ArrayList<>();

        for (TranscriptomicAnalysis transcriptomicAnalysis : transcriptomicAnalyses) {
            String fichierBrut, qualityReport;

            if ((fichierBrut = transcriptomicAnalysis.getFichierBrut()) != null)
                urls.add(fichierBrut);

            if ((qualityReport = transcriptomicAnalysis.getQualityReport()) != null)
                urls.add(qualityReport);
        }

        removeTask.addUrls(urls);
    }

    public void endRemove(Button button, ProgressBar progressBar, Label label){
        super.endRemove(button, progressBar, label);
        this.transcriptomicAnalysis = null;
        this.ajouter.setText("Ajouter");
        this.display(null);
    }

    public void enableButtons(boolean enable, boolean all) {
        if (this.transcriptomicAnalysis == null)
            this.fichierBrut.setDisable(true);
        else if (this.transcriptomicAnalysis.getFichierBrut() == null || this.transcriptomicAnalysis.getFichierBrut().equals(""))
            this.fichierBrut.setDisable(true);
        else this.fichierBrut.setDisable(!enable);

        if (this.transcriptomicAnalysis == null)
            this.qualityReport.setDisable(true);
        else if (this.transcriptomicAnalysis.getQualityReport() == null || this.transcriptomicAnalysis.getQualityReport().equals(""))
            this.qualityReport.setDisable(true);
        else this.qualityReport.setDisable(!enable);

        this.supprimer.setDisable(!enable);

        if (all) {
            this.retour.setDisable(!enable);
            this.ajouter.setDisable(!enable);
        }
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
    }
}
