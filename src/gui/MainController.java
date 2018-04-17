package gui;

import java.util.ArrayList;

public class MainController{
    private ArrayList<ControllerParent> controllerList = new ArrayList<ControllerParent>();

    public void refreshSC(ControllerParent pc){
        ((SecretaryScreenController) pc).displayWeekView();
        ((SecretaryScreenController) pc).displayDayView();
        ((SecretaryScreenController) pc).displayAgenda();
    }

    public void refreshDC(ControllerParent dc){
        ((DoctorScreenController) dc).displayWeekView();
        ((DoctorScreenController) dc).displayDayView();
        ((DoctorScreenController) dc).displayAgenda();
    }
    public void refreshPC(ControllerParent pc){
        //((PatientScreenController) pc).displayWeekView();
        ((PatientScreenController) pc).displayDayView();
        ((PatientScreenController) pc).displayAgenda();
    }


    public void refreshAll(){
        for(int i = 0; i<controllerList.size(); i++ ){
            ControllerParent CurCon = controllerList.get(i);

            if(CurCon instanceof SecretaryScreenController ){
                refreshSC(CurCon);
            }
            else if(CurCon instanceof DoctorScreenController ){
                refreshDC(CurCon);
            }
            else if(CurCon instanceof PatientScreenController ){
                refreshPC(CurCon);
            }

        }

    }

    public ArrayList<ControllerParent> getControllerList() {
        return controllerList;
    }

    public void setControllerList(ArrayList<ControllerParent> controllerList) {
        this.controllerList = controllerList;
    }
}
