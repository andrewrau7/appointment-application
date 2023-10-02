package helper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Objects;


/** Class for validating user input and updating the UI in response to input errors. */
public abstract class InputHandler {


    /** Method for resetting TextField borders and their corresponding error labels.
     Useful for updating any corrected errors after a submission attempt.
     @param textField The TextField to be updated with a normal border.
     @param label The Label to be reset.
     */
    public static void resetTextFieldAndLabel(TextField textField, Label label){
        textField.setStyle(null);
        label.setText("");
    }


    /** Method for resetting DatePicker borders and their corresponding error labels.
     Useful for updating any corrected errors after a submission attempt.
     @param datePicker The DatePicker to be updated with a normal border.
     @param label The Label to be reset.
     */
    public static void resetDatePickerAndLabel(DatePicker datePicker, Label label){
        datePicker.setStyle(null);
        label.setText("");
    }


    /** Method for resetting ComboBox borders and their corresponding error labels.
     Useful for updating any corrected errors after a submission attempt.
     @param comboBox The ComboBox to be updated with a normal border.
     @param label The Label to be reset.
     */
    public static void resetComboBoxAndLabel(ComboBox comboBox, Label label){
        comboBox.setStyle("");
        label.setText("");
    }

    /** Method for ensuring a TextField contains valid input.
     If input is not valid, error message is displayed in the label respective of the
     TextField. The TextField border is also changed to red for a more prominent error display in
     the UI.
     @param textField The TextField to be checked.
     @param label The Label to be changed if necessary.
     */
    public static int checkTextField(TextField textField, Label label){
        if (Objects.equals(textField.getText(), "")){
            textField.setStyle("-fx-border-color: red");
            label.setText("Required");
            return 1;
        }
        return 0;
    }

    /** Method for ensuring a DatePicker contains valid input.
     If input is not valid, error message is displayed in the label respective of the
     DatePicker. The DatePicker border is also changed to red for a more prominent error display in
     the UI.
     @param datePicker The DatePicker to be checked.
     @param label The Label to be changed if necessary.
     */
    public static int checkDatePicker(DatePicker datePicker, Label label){
        if (datePicker.getValue() == null){
            datePicker.setStyle("-fx-border-color: red");
            label.setText("Required");
            return 1;
        }
        return 0;
    }


    /** Method for ensuring a ComboBox contains valid input.
     If input is not valid, error message is displayed in the label respective of the
     ComboBox. The ComboBox border is also changed to red for a more prominent error display in
     the UI.
     @param comboBox The TextField to be checked.
     @param label The Label to be changed if necessary.
     */
    public static int checkComboBox(ComboBox comboBox, Label label){
        if (comboBox.getValue() == null){
            comboBox.setStyle("-fx-border-color: red");
            label.setText("Required");
            return 1;
        }
        return 0;
    }
}
