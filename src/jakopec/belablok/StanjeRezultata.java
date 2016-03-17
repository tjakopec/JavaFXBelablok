/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jakopec.belablok;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author tjakopec
 */
public class StanjeRezultata extends Application {

    private static final int SIRINA_PROZORA = 600;
    private static final int VISINA_PROZORA = 300;

    private static final int SIRINA_LISTE_REZULTATA = 250;
    private static final int VISINA_LISTE_REZULTATA = VISINA_PROZORA;
    private static final int VISINA_CELIJE = 50;
    private Group grupa;
    private Scene scena;
    private ListView<Igra> listaRezultata;
    private Text ukupniRezultat;
    private TextField miIgra;
    private TextField miZvanje;
    private TextField viIgra;
    private TextField viZvanje;
    private Igra igra;
    private boolean promjena = false;

    @Override
    public void start(Stage prozor) {
        definirajGrupu();
        definirajListuRezultata();
        definirajUkupniRezultat();
        definirajUnosnaPolja();
        scena = new Scene(grupa, SIRINA_PROZORA, VISINA_PROZORA);
        prozor.setResizable(false);
        prozor.setTitle("Bela blok");
        prozor.setScene(scena);
        prozor.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void definirajIscrtavanjeCelije() {
        listaRezultata.setCellFactory((ListView<Igra> param) -> {
            ListCell<Igra> cell = new ListCell<Igra>() {
                
                
               

                @Override
                protected void updateItem(Igra igra, boolean empty) {
                    super.updateItem(igra, empty);
                    Canvas canvas = new Canvas(SIRINA_LISTE_REZULTATA - 30, VISINA_CELIJE);

                    if (igra == null) {
                        setGraphic(canvas);
                        return;
                    }

                    GraphicsContext g = canvas.getGraphicsContext2D();

                    if (igra.getIgraMi() == -1) {
                        g.setFont(new Font("Courier", 40));
                        g.fillText("Nova igra", 5, 35);
                        setGraphic(canvas);

                        return;
                    }

                    g.setFont(new Font("Courier", 50));

                    String s = "   " + String.valueOf(igra.getUkupnoMi());
                    s = s.substring(s.length() - 3);
                    g.fillText(s, 5, 45);

                    s = "   " + String.valueOf(igra.getUkupnoVi());
                    s = s.substring(s.length() - 3);

                    g.fillText(s, 130, 45);

                    setGraphic(canvas);

                }
            };
            
            if(cell.itemProperty().get()!=null && cell.itemProperty().get().getIgraMi()>-1){
                return cell;
            }
             ContextMenu contextMenu = new ContextMenu();
                
                 MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Obriši"));
           
            deleteItem.setOnAction(event -> param.getItems().remove(cell.getItem()));
          
            contextMenu.getItems().add( deleteItem);
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            
            
            return cell;
        });
    }

    private void testniPodaci() {
        Igra i = new Igra();
        i.setIgraMi(152);
        i.setZvanjeMi(20);
        i.setIgraVi(10);
        i.setZvanjeVi(50);
        listaRezultata.getItems().add(i);
        i = new Igra();
        i.setIgraMi(59);
        i.setIgraVi(103);
        listaRezultata.getItems().add(i);
    }

    private void definirajListuRezultata() {
        ScrollPane pane = new ScrollPane();
        listaRezultata = new ListView<>();
        listaRezultata.setMinSize(SIRINA_LISTE_REZULTATA, VISINA_LISTE_REZULTATA);
        listaRezultata.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Igra> observable, Igra oldValue, Igra newValue) -> {
            promjenaStavkeListe(oldValue, newValue);
        });
        definirajIscrtavanjeCelije();
        // testniPodaci();

        dodajZadnjuStavku();
        pane.setContent(listaRezultata);
        grupa.getChildren().add(pane);
    }

    private void definirajGrupu() {
        grupa = new Group();
    }

    private void definirajUkupniRezultat() {
        ukupniRezultat = new Text();
        ukupniRezultat.setX(SIRINA_LISTE_REZULTATA + 10);
        ukupniRezultat.setY(80);

        ukupniRezultat.setFont(Font.font("Courier", FontWeight.BOLD, 60));

        ukupniRezultat.setText("220 : 234");
        grupa.getChildren().add(ukupniRezultat);
        nadopuniUkupniRezultat();
    }

    private void nadopuniUkupniRezultat() {
        int ukupniMi = 0, ukupnoVi = 0;
        for (Igra i : listaRezultata.getItems()) {
            if(i.getIgraMi()==-1){
                continue;
            }
            ukupniMi += i.getUkupnoMi();
            ukupnoVi += i.getIgraVi();
        }
        if (ukupniMi < 0 || ukupnoVi < 0) {
            ukupniRezultat.setText("  0 :   0");
            return;
        }
        String um = "   " + String.valueOf(ukupniMi);
        um = um.substring(um.length() - 3);
        String uv = "   " + String.valueOf(ukupnoVi);
        uv = uv.substring(uv.length() - 3);
        ukupniRezultat.setText(um + " : " + uv);
    }

    private void definirajUnosnaPolja() {
        GridPane grid = new GridPane();
        grid.setLayoutX(SIRINA_LISTE_REZULTATA + 30);
        grid.setLayoutY(120);

        VBox vertikala = new VBox();
        vertikala.setPrefWidth(80);
        miIgra = kreirajTextFiled("miIgra");
        miZvanje = kreirajTextFiled("miZvanje");
        vertikala.getChildren().addAll(new Label("Igra"), miIgra, new Label("Zvanje"), miZvanje);
        grid.add(vertikala, 0, 0);

        vertikala = new VBox();
        vertikala.setPrefWidth(135);
        grid.add(vertikala, 1, 0);

        vertikala = new VBox();
        vertikala.setPrefWidth(80);
        viIgra = kreirajTextFiled("viIgra");
        viZvanje = kreirajTextFiled("viZvanje");
        vertikala.getChildren().addAll(new Label("Igra"), viIgra, new Label("Zvanje"), viZvanje);
        grid.add(vertikala, 2, 0);

        Label l = new Label();
        l.setPrefHeight(10);
        grid.add(l, 0, 2, 3, 1);

        Button b = new Button("Gotov");
        b.setOnMouseClicked((MouseEvent) -> klikGotov());

        b.setPrefSize(300, 40);

        grid.add(b, 0, 3, 3, 1);
        grupa.getChildren().add(grid);

    }

    private TextField kreirajTextFiled(String id) {

        TextField t = new TextField();
        t.setId(id);
        t.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            private boolean willConsume = false;

            @Override
            public void handle(KeyEvent event) {
                if (willConsume) {
                    event.consume();
                }
                if (!event.getCode().isDigitKey()) {
                    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                        willConsume = true;
                    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                        willConsume = false;
                    }
                }
            }
        });
        t.setAlignment(Pos.BASELINE_RIGHT);
        t.setOnKeyReleased((KeyEvent keyEvent) -> {
            nadopunjujem = true;
            preracunaj(t);
        });
        t.setFont(new Font("Courier", 20));
        return t;
    }

    private void klikGotov() {

        if (!promjena) {
            igra = new Igra();
        }

        try {
            igra.setIgraMi(Integer.parseInt(miIgra.getText()));
        } catch (NumberFormatException e) {
        }

        try {
            igra.setZvanjeMi(Integer.parseInt(miZvanje.getText()));
        } catch (NumberFormatException e) {
        }

        try {
            igra.setIgraVi(Integer.parseInt(viIgra.getText()));
        } catch (NumberFormatException e) {
        }

        try {
            igra.setZvanjeVi(Integer.parseInt(viZvanje.getText()));
        } catch (NumberFormatException e) {
        }

        if (!promjena) {
            if(igra.getUkupnoMi()==0 && igra.getUkupnoVi()==0){
                return;
            }
            listaRezultata.getItems().remove(listaRezultata.getItems().size() - 1);
            listaRezultata.getItems().add(igra);
            dodajZadnjuStavku();
            pocistiPolja();
        }
        listaRezultata.refresh();

        nadopuniUkupniRezultat();
        promjena = false;
    }

    private void promjenaStavkeListe(Igra oldValue, Igra newValue) {
        if (newValue == null) {
            return;
        }

        igra = newValue;

        if (igra.getIgraMi() == -1) {
            igra = null;
            promjena = false;
            pocistiPolja();
            return;
        }
        miIgra.setText(String.valueOf(igra.getIgraMi()));
        miZvanje.setText(String.valueOf(igra.getZvanjeMi() == 0 ? "" : igra.getZvanjeMi()));
        viIgra.setText(String.valueOf(igra.getIgraVi()));
        viZvanje.setText(String.valueOf(igra.getZvanjeVi() == 0 ? "" : igra.getZvanjeVi()));
        promjena = true;

    }

    private void dodajZadnjuStavku() {
        listaRezultata.getItems().add(new Igra(-1));
    }
    private boolean nadopunjujem;

    private void preracunaj(TextField t) {

        int b = 0;
        try {
            // System.out.println(t.getText());
            b = Integer.parseInt(t.getText());
            if (b > 162) {
                t.setText("162");
            }
        } catch (Exception e) {
        }
        int rez;
        int i = 0;
        if (nadopunjujem) {
            nadopunjujem = false;
            switch (t.getId()) {
                case "miIgra":

                    try {
                        i = Integer.parseInt(viZvanje.getText());
                    } catch (NumberFormatException e) {
                    }

                    rez = 162 - b + i;

                    viIgra.setText(rez > 0 ? String.valueOf(rez) : "");
                    break;
                case "viIgra":

                    try {
                        i = Integer.parseInt(miZvanje.getText());
                    } catch (NumberFormatException e) {
                    }

                    rez = 162 - b + i;
                    miIgra.setText(rez > 0 ? String.valueOf(rez) : "");
                    break;
                case "miZvanje":

                    try {
                        i = Integer.parseInt(miIgra.getText());
                    } catch (NumberFormatException e) {
                    }
                    rez = i + b;
                    miIgra.setText(rez > 0 ? String.valueOf(rez) : "");
                    break;
                case "viZvanje":

                    try {
                        i = Integer.parseInt(viIgra.getText());
                    } catch (NumberFormatException e) {
                    }
                    rez = i + b;
                    viIgra.setText(rez > 0 ? String.valueOf(rez) : "");
                    break;
            }
            nadopunjujem = true;
        }

    }

    private void pocistiPolja() {
        miIgra.setText("");
        miZvanje.setText("");
        viIgra.setText("");
        viZvanje.setText("");
    }

}
