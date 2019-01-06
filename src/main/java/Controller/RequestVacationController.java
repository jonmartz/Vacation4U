package Controller;

import Model.RequestMessage;
import Model.Vacation;
import View.RequestVacationView;
import View.VacationSearchView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.ArrayList;

/**
 * Controller for the vacation details view
 */
public class RequestVacationController extends AbstractController {

    @Override
    protected void FillAllData() {
        ArrayList<Vacation> vacations = GetAllVacationsOfCurrentUser();
        ObservableList<VacationSearchView.VacationEntry> items = FXCollections.observableArrayList();
        for (Vacation vacation : vacations){
            items.add(new VacationSearchView.VacationEntry(vacation.ID, vacation.destinationCountryTXT,
                    vacation.fromDateTXT, vacation.toDateTXT, vacation.price));
        }
        RequestVacationView requestVacationView = (RequestVacationView) this.view;
        TableView vacationsTable = requestVacationView.vacationsTable;
        vacationsTable.setItems(items);
        vacationsTable.getSortOrder().add(requestVacationView.priceColumn);
        vacationsTable.setVisible(true);
    }

    /**
     * Activated by the offer vacation button from the vacations table
     */
    public void RequestVacationTrade(String offeredVacationID) {
        submitRequestForVacation();
        Vacation offeredVacation = database.getVacation(offeredVacationID);
        // todo: add the trade message to database

        viewChanger.closeSecondaryStage();
    }

    /**
     * Activated by the requestVacation button
     */
    public void RequestVacationCash() {
        submitRequestForVacation();
        viewChanger.closeSecondaryStage();
    }

    public void submitRequestForVacation(){
        Vacation requestedVacation = database.getCurrentVacation();
        view.ShowPopUp("Buy request sent to vacation owner!\nCheck your mailbox for confirmation.");
        RequestMessage requestMessage = new RequestMessage(this.getCurrentUser().username, requestedVacation.ownerID, requestedVacation);
        this.database.addMessage(requestMessage.getSender(), requestMessage.getReceiver(), requestedVacation.ID,
                false, requestMessage.getDate(), requestMessage.getTime(), requestMessage.getKind());
    }
}