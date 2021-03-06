package src.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HistologicLamella {
    private int id;
    private int idLesion;
    private StringProperty NumLame;
    private StringProperty siteCoupe;
    private IntegerProperty orientationNoir;
    private IntegerProperty orientationVert;
    private StringProperty coloration;
    private String photo;

    public HistologicLamella() {
        this.NumLame = new SimpleStringProperty();
        this.siteCoupe = new SimpleStringProperty();
        this.orientationNoir = new SimpleIntegerProperty();
        this.orientationVert = new SimpleIntegerProperty();
        this.coloration = new SimpleStringProperty();
    }

    public HistologicLamella(int id, int idLesion, String numLame, String siteCoupe, int orientationNoir, int orientationVert, String coloration, String photo) {
        this();
        this.setId(id);
        this.setIdLesion(idLesion);
        this.setNumLame(numLame);
        this.setSiteCoupe(siteCoupe);
        this.setOrientationNoir(orientationNoir);
        this.setOrientationVert(orientationVert);
        this.setColoration(coloration);
        this.setPhoto(photo);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLesion() {
        return this.idLesion;
    }

    public void setIdLesion(int idLesion) {
        this.idLesion = idLesion;
    }

    public String getNumLame() {
        return NumLame.get();
    }

    public void setNumLame(String numLame) {
        this.NumLame.set(numLame);
    }

    public String getSiteCoupe() {
        return this.siteCoupe.get();
    }

    public void setSiteCoupe(String siteCoupe) {
        this.siteCoupe.set(siteCoupe);
    }

    public int getOrientationNoir() {
        return this.orientationNoir.get();
    }

    public void setOrientationNoir(int orientationNoir) {
        this.orientationNoir.set(orientationNoir);
    }

    public int getOrientationVert() {
        return this.orientationVert.get();
    }

    public void setOrientationVert(int orientationVert) {
        this.orientationVert.set(orientationVert);
    }

    public String getColoration() {
        return this.coloration.get();
    }

    public void setColoration(String coloration) {
        this.coloration.set(coloration);
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public StringProperty numLameProperty() {
        return NumLame;
    }

    public StringProperty siteCoupeProperty() {
        return this.siteCoupe;
    }

    public IntegerProperty orientationNoirProperty() {
        return this.orientationNoir;
    }

    public IntegerProperty orientationVertProperty() {
        return this.orientationVert;
    }

    public StringProperty colorationProperty() {
        return this.coloration;
    }
}
