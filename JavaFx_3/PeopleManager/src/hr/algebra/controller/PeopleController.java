/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Person;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.ImageUtils;
import hr.algebra.utilities.ValidationUtils;
import hr.algebra.viewmodel.EsportsTeamViewModel;
import hr.algebra.viewmodel.PersonViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author 38591
 */
public class PeopleController implements Initializable {

    private Map<TextField, Label> validationMap;
    private Map<TextField, Label> validationMapEsports;
    
    private final ObservableList<PersonViewModel> list = FXCollections.observableArrayList();
    private final ObservableList<EsportsTeamViewModel> teams = FXCollections.observableArrayList();
    
    private PersonViewModel selectedPersonViewModel;
    private EsportsTeamViewModel selectedEsportsTeamViewModel;
    
    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private Tab tabEdit;
    
    @FXML
    private TableColumn<PersonViewModel, String> tcFirstName, tcLastName, tcEmail;
    
    @FXML
    private TableColumn<PersonViewModel, Integer> tcAge;
    
    @FXML
    private ImageView ivPerson;
    
    @FXML
    private TextField tfFirstName, tfLastName, tfAge, tfEmail;
    @FXML
    private Label lbFirstName, lbLastName, lbAge, lbEmail, lbIcon;
    
    @FXML
    private TableView<PersonViewModel> tvPeople;
    @FXML
    private Tab tabListEsports;
    @FXML
    private TableView<EsportsTeamViewModel> tvEsportsTeam;
    @FXML
    private Tab tabEditEsports;
    @FXML
    private TextField tfTeamName, tfCountry;
    @FXML
    private Label lbTeamName;
    @FXML
    private Label lbCountry;
    @FXML
    private ComboBox<PersonViewModel> cbPlayer;
    @FXML
    private TableColumn<EsportsTeamViewModel, String> tcTeamName, tcCountry;
    @FXML
    private TableColumn<EsportsTeamViewModel, Person> tcPlayer;

   
    /**
     * Initialises the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValidation();
        initPeople();
        initTable();
        addIntegerMask(tfAge);
        setListeners();
        initPlayer();
    } 

    @FXML
    private void edit() {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            bindPerson(tvPeople.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEdit);
        }
    }

    @FXML
    private void delete() {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            //removeamo elemente iz liste
            list.remove(tvPeople.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void upload() {
        File file = FileUtils.uploadFileDialog(tfAge.getScene().getWindow(), "jpg", "jpeg", "png");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            // c://Ds/milicaITvrtko.jpg, izrezi ".jpg", uzmi taj file i nadi zadnju tocku, pomakni se i daj mi extenziju
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            try {
                selectedPersonViewModel.getPerson().setPicture(ImageUtils.imageToByteArray(image, ext));
                ivPerson.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void commit() {
        if (formValid()) {
            
            selectedPersonViewModel.getPerson().setFirstName(tfFirstName.getText().trim());
            selectedPersonViewModel.getPerson().setLastName(tfLastName.getText().trim());
            selectedPersonViewModel.getPerson().setAge(Integer.valueOf(tfAge.getText().trim()));
            selectedPersonViewModel.getPerson().setEmail(tfEmail.getText().trim());
            
            if (selectedPersonViewModel.getPerson().getIDPerson() == 0) { //add
                list.add(selectedPersonViewModel); //ovaj ce mu assignati id
            } else {
                try {
                    RepositoryFactory.getRepository().updatePerson(selectedPersonViewModel.getPerson());
                    tvPeople.refresh(); // manual refresh
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedPersonViewModel = null;
            tpContent.getSelectionModel().select(tabList); // vrati se na listu
            resetForm();
        }
    }

    private void initValidation() {
        validationMap = Stream.of(
                // map se sastoji od -> pair<first, second>
                new AbstractMap.SimpleImmutableEntry<>(tfFirstName, lbFirstName),
                new AbstractMap.SimpleImmutableEntry<>(tfLastName, lbLastName),
                new AbstractMap.SimpleImmutableEntry<>(tfAge, lbAge),
                new AbstractMap.SimpleImmutableEntry<>(tfEmail, lbEmail)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        validationMapEsports = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfTeamName, lbTeamName),
                new AbstractMap.SimpleImmutableEntry<>(tfCountry, lbCountry))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initPeople() {
        try {
            RepositoryFactory.getRepository().getPeople().forEach(person -> list.add(new PersonViewModel(person)));
            RepositoryFactory.getRepositoryEsportsTeam().getEsportsTeams().forEach(team -> teams.add(new EsportsTeamViewModel(team)));
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Going to exit.").show();
        }
    }

    private void initTable() {
        tcFirstName.setCellValueFactory(person -> person.getValue().getFirstNameProperty());
        tcLastName.setCellValueFactory(person -> person.getValue().getLastNameProperty());
        tcAge.setCellValueFactory(person -> person.getValue().getAgeProperty().asObject());
        tcEmail.setCellValueFactory(person -> person.getValue().getEmailProperty());
        tvPeople.setItems(list);
        
        tcTeamName.setCellValueFactory(team -> team.getValue().getNameProperty());
        tcCountry.setCellValueFactory(team -> team.getValue().getCountryProperty());
        tcPlayer.setCellValueFactory(team -> team.getValue().getPersonIDProperty());
        tvEsportsTeam.setItems(teams);
    }

    private void addIntegerMask(TextField tf) {
        // zabrana pisanja icega osim brojeva u Age field
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String input = change.getText();
            if (input.matches("\\d*")) {
                return change;
            }
            
            return null;
        };
        tf.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, filter));
    }

    private Tab priorTab;
    
    private void setListeners() {
        tpContent.setOnMouseClicked(event ->{
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabEdit) && !tabEdit.equals(priorTab)) {
                bindPerson(null);
            } else if(tpContent.getSelectionModel().getSelectedItem().equals(tabEditEsports) && !tabEdit.equals(priorTab)) {
                bindEsports(null);
            }
                priorTab = tpContent.getSelectionModel().getSelectedItem();
        });
        addListenersPeople();
        addListenersEsportsTeams();
    }
        
    private void addListenersPeople() {
        list.addListener((ListChangeListener.Change<? extends PersonViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository().deletePerson(pvm.getPerson());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idPerson = RepositoryFactory.getRepository().addPerson(pvm.getPerson());
                            pvm.getPerson().setIDPerson(idPerson);
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }
    
    private void addListenersEsportsTeams() {
        teams.addListener((ListChangeListener.Change<? extends EsportsTeamViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(fvm -> {
                        try {
                            RepositoryFactory.getRepositoryEsportsTeam().deleteEsportsTeam(fvm.getTeam());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(fvm -> {
                        try {
                            int idTeam = RepositoryFactory.getRepositoryEsportsTeam().addEsportsTeam(fvm.getTeam());
                            fvm.getTeam().setIDETeam(idTeam);
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }
    

    private void bindPerson(PersonViewModel personViewModel) {
        
        resetForm();
        
        selectedPersonViewModel = personViewModel != null ? personViewModel : new PersonViewModel(null);
        tfFirstName.setText(selectedPersonViewModel.getFirstNameProperty().get());
        tfLastName.setText(selectedPersonViewModel.getLastNameProperty().get());
        tfAge.setText(String.valueOf(selectedPersonViewModel.getAgeProperty().get()));
        tfEmail.setText(selectedPersonViewModel.getEmailProperty().get());
        
        ivPerson.setImage(
                selectedPersonViewModel.getPictureProperty().get() != null 
                ? new Image(new ByteArrayInputStream(selectedPersonViewModel.getPictureProperty().get()))
                : new Image(new File("src/assets/no_image.png").toURI().toString())
        );
    }

    //metoda cisti formu
    private void resetForm() {
        validationMap.values().forEach(l -> l.setVisible(false));
        lbIcon.setVisible(false);
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty() 
                    || (tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText().trim()))) {
                ok.set(false);
                validationMap.get(tf).setVisible(true); //label visible
            } else {
                validationMap.get(tf).setVisible(false); // label not visible
            }
        });
        
        if (selectedPersonViewModel.getPictureProperty().get() == null) {
            lbIcon.setVisible(true);
            ok.set(false);
        } else {
            lbIcon.setVisible(false);
        }
        
        return ok.get();
    }

    private void initPlayer() {
        cbPlayer.setItems(list);
    }

    private void bindEsports(EsportsTeamViewModel esportsTeamViewModel) {
        
        resetEsportsForm();
        
        selectedEsportsTeamViewModel = esportsTeamViewModel != null ? esportsTeamViewModel : new EsportsTeamViewModel(null);
        tfTeamName.setText(selectedEsportsTeamViewModel.getNameProperty().get());
        tfCountry.setText(selectedEsportsTeamViewModel.getCountryProperty().get());
        cbPlayer.setValue(new PersonViewModel((Person)selectedEsportsTeamViewModel.getPersonIDProperty().get()));
    }

    @FXML
    private void editEsportsTeam(ActionEvent event) {
        if (tvEsportsTeam.getSelectionModel().getSelectedItem() != null) {
            bindEsports(tvEsportsTeam.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEditEsports);            
        }
    }

    @FXML
    private void deleteEsportsTeam(ActionEvent event) {
        if (tvEsportsTeam.getSelectionModel().getSelectedItem() != null) {
            teams.remove(tvEsportsTeam.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void commitEsportsTeam(ActionEvent event) {
        if (formEsportsValid()) {
            selectedEsportsTeamViewModel.getTeam().setTeamName(tfTeamName.getText().trim());
            selectedEsportsTeamViewModel.getTeam().setCountry(tfCountry.getText().trim());
            selectedEsportsTeamViewModel.getTeam().setPersonID(cbPlayer.getSelectionModel().getSelectedItem().getPerson());
            
            if (selectedEsportsTeamViewModel.getTeam().getIDETeam()== 0) {
                teams.add(selectedEsportsTeamViewModel);
            } else {
                try {
                    
                    RepositoryFactory.getRepositoryEsportsTeam().updateEsportsTeam(selectedEsportsTeamViewModel.getTeam());
                    tvEsportsTeam.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedEsportsTeamViewModel = null;
            tpContent.getSelectionModel().select(tabListEsports);
            resetForm();
        }
    }

    private boolean formEsportsValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMapEsports.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()) {
                validationMapEsports.get(tf).setVisible(true);
                ok.set(false);
            } else {
                validationMapEsports.get(tf).setVisible(false);
            }
        });
        
        if (cbPlayer.getValue() == null) {
            cbPlayer.backgroundProperty().setValue(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            ok.set(false);
        } 
        return ok.get();
    }

    private void resetEsportsForm() {
        validationMapEsports.values().forEach(lb -> lb.setVisible(false));
        cbPlayer.getSelectionModel().clearSelection();
    }
    
}
