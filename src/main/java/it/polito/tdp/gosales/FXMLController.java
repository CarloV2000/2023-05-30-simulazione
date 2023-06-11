package it.polito.tdp.gosales;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.gosales.model.CoppiaA;
import it.polito.tdp.gosales.model.Model;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;
import it.polito.tdp.gosales.model.SimResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnalizzaComponente;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Integer> cmbAnno;

    @FXML
    private ComboBox<String> cmbNazione;

    @FXML
    private ComboBox<Products> cmbProdotto;

    @FXML
    private ComboBox<Retailers> cmbRivenditore;

    @FXML
    private TextArea txtArchi;

    @FXML
    private TextField txtN;

    @FXML
    private TextField txtNProdotti;

    @FXML
    private TextField txtQ;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextArea txtVertici;

    @FXML
    void doAnalizzaComponente(ActionEvent event) {
    	Retailers r = this.cmbRivenditore.getValue();
    	Integer anno = this.cmbAnno.getValue();

    	if(r == null){
    		this.txtResult.setText("Inserire un anno nella boxRivenditore!");
    		return;
    	}
    	if(anno == null){
    		this.txtResult.setText("Inserire un anno nella boxAnno!");
    		return;
    	}
    	int n = model.getNumberOfConnectedComponents(r);
    	int peso = model.getWeightOfConnectedComponents(r);
    	this.txtResult.appendText("\nComponente connessa composta da "+n+" vertici e avente peso "+peso);
    	
    	for(Products x : model.getAllProducts(r)) {
    		this.cmbProdotto.getItems().add(x);
    	}
    	this.cmbProdotto.setDisable(false);
    	this.btnSimula.setDisable(false);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Integer anno = this.cmbAnno.getValue();
    	String nazione = this.cmbNazione.getValue();
    	String input = this.txtNProdotti.getText();
    	Integer inputNUM;
    	if(anno == null){
    		this.txtResult.setText("Inserire un anno nella boxAnno!");
    		return;
    	}
    	if(nazione == null){
    		this.txtResult.setText("Inserire una nazione nella boxNazione!");
    		return;
    	}
    	try {
    		inputNUM = Integer.parseInt(input);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore numerico nel campo n");
    		return;
    	}
    	String s = model.creaGrafo(anno, nazione, inputNUM);
    	this.txtResult.setText(s);
    	
    	String vertici = "";
    	String archi = "";
    	for(Retailers x : model.getGrafo().vertexSet()) {
    		vertici += x.getName() + "\n";
    	}
    	for(CoppiaA x : model.listArchi()) {
    		archi += x.getR1() +" <---> "+ x.getR2()+" ("+x.getPeso()+")\n";
    	}
    	this.txtVertici.setText(vertici);
    	this.txtArchi.setText(archi);
    	this.cmbRivenditore.setDisable(false);
    	for(Retailers x : model.getGrafo().vertexSet()) {
    		this.cmbRivenditore.getItems().add(x);
    	}
    	this.btnAnalizzaComponente.setDisable(false);
    	this.txtN.setDisable(false);
    	this.txtQ.setDisable(false);
    	
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	Products p = this.cmbProdotto.getValue();
    	Retailers r = this.cmbRivenditore.getValue();
    	int anno = this.cmbAnno.getValue();
    	String n = this.txtN.getText();
    	String q = this.txtQ.getText();
    	int Nnum;
    	int Qnum;
    	int nConnesse;
    	if(p == null) {
    		this.txtResult.setText("Inserire un prodotto nella boxProducts");
    		return;
    	}
    	try {
    		Nnum = Integer.parseInt(n);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore numerico nel campo n");
    		return;
    	}
    	try {
    		Qnum = Integer.parseInt(q);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore numerico nel campo q");
    		return;
    	}
    	nConnesse = model.getNumberOfConnectedComponents(r)-1;
    	SimResult x = model.simula(r, p, Qnum, Nnum, anno, nConnesse);
    	this.txtResult.setText("CostoTOT = "+x.getCostoTOT());
    	this.txtResult.appendText("\nRicavoTOT = "+x.getRicavoTOT());
    	this.txtResult.appendText("\npercentualeSoddisfazione = "+x.getPercentSoddisfatti());
    	
    }

    @FXML
    void initialize() {
        assert btnAnalizzaComponente != null : "fx:id=\"btnAnalizzaComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNazione != null : "fx:id=\"cmbNazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProdotto != null : "fx:id=\"cmbProdotto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbRivenditore != null : "fx:id=\"cmbRivenditore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtArchi != null : "fx:id=\"txtArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNProdotti != null : "fx:id=\"txtNProdotti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtQ != null : "fx:id=\"txtQ\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtVertici != null : "fx:id=\"txtVertici\" was not injected: check your FXML file 'Scene.fxml'.";
        for(int i = 2015; i<=2018; i++) {
        	this.cmbAnno.getItems().add(i);
        }
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(String n : model.getAllCountries()) {
    		this.cmbNazione.getItems().add(n);
    	}
    }

}
