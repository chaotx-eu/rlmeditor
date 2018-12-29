package fh.sem.gui.control;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.geometry.Pos;


public class Selector extends HBox {
    private Label text;
    private IntegerProperty selected;
    private BooleanProperty circular;
    private String[] values;

    public Selector(String... values) {
        this(false, values);
    }

    public Selector(boolean circular, String... values) {
        int size = values.length;
        this.values = values;
        this.text = new Label();
        this.selected = new SimpleIntegerProperty();
        this.circular = new SimpleBooleanProperty(circular);

        selected.addListener((p, o, n) -> {
            if(n.intValue() > size-1)
                selected.set(circular ? 0 : size-1);

            if(n.intValue() < 0)
                selected.set(circular ? size-1 : 0);

            text.setText(values[selected.get()]);
        });

        Button btn_inc = new Button("+");
        Button btn_dec = new Button("-");

        btn_inc.setScaleX(0.7f);
        btn_inc.setScaleY(0.7f);
        btn_dec.setScaleX(0.7f);
        btn_dec.setScaleY(0.7f);

        btn_inc.disableProperty().bind(this.circular.not()
            .and(selected.isEqualTo(size-1)));

        btn_dec.disableProperty().bind(this.circular.not()
            .and(selected.isEqualTo(0)));

        btn_inc.setOnAction(e -> selected.set(selected.get()+1));
        btn_dec.setOnAction(e -> selected.set(selected.get()-1));

        if(values.length > 0) text.setText(values[0]);
        HBox hbx = new HBox(btn_dec, text, btn_inc);
        VBox.setVgrow(hbx, Priority.SOMETIMES);
        hbx.setAlignment(Pos.CENTER);

        getChildren().add(hbx);
        setFillHeight(true);
        setSpacing(4);
    }

    public StringProperty textProperty() {
        return text.textProperty();
    }

    public BooleanProperty circularProperty() {
        return circular;
    }

    public IntegerProperty selectedProperty() {
        return selected;
    }

    public void select(String value) {
        for(int i = 0; i < values.length; ++i) {
            if(value.trim().equals(values[i].trim())) {
                selected.set(i);
                break;
            }
        }
    }
}