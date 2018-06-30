/**
 * Sample Skeleton for 'Artsmia.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {

	private Model model;
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxLUN"
	private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaComponenteConnessa"
	private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

	@FXML // fx:id="btnCercaOggetti"
	private Button btnCercaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaOggetti"
	private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="txtObjectId"
	private TextField txtObjectId; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doAnalizzaOggetti(ActionEvent event) {
		this.txtResult.clear();
		
		model.creaGrafo();
		this.txtResult.appendText(String.format("Grafo creato: %d vertici e %d archi\n", 
												model.getVertici(), model.getArchi()));
	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {
		this.txtResult.clear();
		this.boxLUN.getItems().clear();
		
		try {
			int objectId = Integer.parseInt(this.txtObjectId.getText());
			
			// verifica che l'objectId inserito è presente nel DB, cioè è uno dei vertici del grafo
			if (!model.isValid(objectId)) {
				this.txtResult.appendText("Non esiste alcun object con l'ID inserito\n");
				return;
			}
			
			int dimComponenteConnessa = model.getComponenteConnessa(objectId).size();
			

			if (dimComponenteConnessa > 1)
				// il programma ha difficoltà a caricare nel menù a tendina troppi elementi
				// (ci si limita a 10, considerando bassi valori per la ricorsione)
				for (int i = 2; i <= 10; i++) 
					this.boxLUN.getItems().add(i);

			this.txtResult.appendText(String.format("La componente connessa che contiene il "
					+ "vertice %d ha %d vertici", objectId, dimComponenteConnessa));

		}
		catch (NumberFormatException e) {
			this.txtResult.appendText("Inserire un valore numerico nel campo 'objectId'\n");
		}
	
	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		this.txtResult.clear();
		
		try {
			int objectId = Integer.parseInt(this.txtObjectId.getText());
			
			// verifica che l'objectId inserito è presente nel DB, cioè è uno dei vertici del grafo
			if (!model.isValid(objectId)) {
				this.txtResult.appendText("Non esiste alcun object con l'ID inserito\n");
				return;
			}
			
			int lun = this.boxLUN.getValue();
			
			if (this.boxLUN == null) {
				this.txtResult.appendText("Selezionare un LUN\n");
				return;
			}
			
			List <ArtObject> cammino = model.getCamminoMassimo (objectId, lun);
			Collections.sort(cammino);
			
			this.txtResult.appendText(String.format("%s \nIl peso totale del cammino è %d", cammino, model.getPesoMax()));
			
		}
		catch (NumberFormatException e) {
			this.txtResult.appendText("Inserire un valore numerico nel campo 'objectId'\n");
		}
		
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
}
