package helper;

import javafx.collections.ObservableList;
import models.Appointment;


/** Functional Interface for filtering ObservableList's. */
@FunctionalInterface
public interface ObservableListFilter {


     /** Method declaration for ObservableList filtering.
      @param observableList The ObservableList to be filtered.
      @return An ObservableList that has been filtered.
      */
     ObservableList<Appointment> filter(ObservableList<Appointment> observableList);
}
