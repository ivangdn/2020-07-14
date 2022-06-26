/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnClassifica"
    private Button btnClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbSquadra"
    private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doClassifica(ActionEvent event) {
    	txtResult.clear();
    	Team s = cmbSquadra.getValue();
    	if(s == null) {
    		txtResult.setText("Selezionare una squadra");
    		return;
    	}
    	
    	txtResult.appendText("SQUADRE MIGLIORI:\n");
    	for(Team migliore : model.squadreMigliori(s)) {
    		txtResult.appendText(String.format("%s(%d)\n", migliore, migliore.getPunti()-s.getPunti()));
    	}
    	
    	txtResult.appendText("\nSQUADRE PEGGIORI:\n");
    	for(Team peggiore : model.squadrePeggiori(s)) {
    		txtResult.appendText(String.format("%s(%d)\n", peggiore, s.getPunti()-peggiore.getPunti()));
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	model.creaGrafo();
    	txtResult.appendText("Grafo creato\n");
    	txtResult.appendText("# VERTICI: "+model.nVertici()+"\n");
    	txtResult.appendText("# ARCHI: "+model.nArchi());
    	cmbSquadra.getItems().clear();
    	cmbSquadra.getItems().addAll(model.getVertici());
    } 

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	
    	int N;
    	int X;
    	try {
    		N = Integer.parseInt(txtN.getText());
    		X = Integer.parseInt(txtX.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("N e X devono essere valori numerici interi");
    		return;
    	}
    	
    	this.model.simula(N, X);
    	txtResult.appendText("Numero medio di reporter per match: "+model.getAvgReporter()+"\n");
    	txtResult.appendText("Numero di partite critiche: "+model.getMatchCritici());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
