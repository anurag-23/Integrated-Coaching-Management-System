package in.org.cris.icms.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.models.LineType;

/**
 * Created by anurag on 22/6/17.
 */
public class ConfirmArrivalDialogFragment extends DialogFragment {
    private String[] ends = {"End 1", "End 2"};
    private String[] lineTypesArray = {"None", "Platform", "Sick Line", "Stabling Line", "Spare Line", "Siding", "Washing Line"};
    private List<LineType> lineTypesList = new ArrayList<>();
    private List<String> lineTypeStrings = new ArrayList<>();
    private List<String> lineStrings = new ArrayList<>();
    private Spinner lineTypeSpinner;
    private Spinner lineSpinner;
    private Spinner endSpinner;
    private TextView capacity;
    private TextView coachesOnLine;
    private TextView availableSpace;

    public ConfirmArrivalDialogFragment() {
    }

    public static ConfirmArrivalDialogFragment createInstance(){
        return new ConfirmArrivalDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_confirm_arrival, container, false);
        lineTypeSpinner = (Spinner)view.findViewById(R.id.line_type_spinner);
        lineSpinner = (Spinner)view.findViewById(R.id.line_spinner);
        endSpinner = (Spinner)view.findViewById(R.id.end_spinner);

        final LinearLayout lineInfo = (LinearLayout)view.findViewById(R.id.line_info);
        TextView confirmArrival = (TextView)view.findViewById(R.id.confirm_arrival_text_view);
        TextView cancel = (TextView)view.findViewById(R.id.cancel_text_view);
        final TextView errorMessage = (TextView)view.findViewById(R.id.error_message_text_view);

        populateSpinners();

        lineTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lineInfo.setVisibility(View.GONE);
                errorMessage.setVisibility(View.GONE);
                if (lineTypeSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    lineStrings.clear();
                    lineStrings.add(getString(R.string.none));
                }
                else{
                    lineStrings.clear();
                    lineStrings.add(getString(R.string.none));
                    lineStrings.addAll(lineTypesList.get(i).getLinesList());
                }
                lineSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View childView, int i, long l) {
                lineInfo.setVisibility(View.GONE);
                errorMessage.setVisibility(View.GONE);
                if (!lineSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    lineInfo.setVisibility(View.VISIBLE);
                    displayLineInfo(view);
                }
                else{
                    lineInfo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        confirmArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noneString = getString(R.string.none);
                if (lineTypeSpinner.getSelectedItem().toString().equals(noneString) || lineSpinner.getSelectedItem().toString().equals(noneString)) {
                    errorMessage.setText(getString(R.string.specify_details));
                    errorMessage.setVisibility(View.VISIBLE);
                }
                else if (getActivity() instanceof ConfirmArrivalInterface) {
                    if (Integer.parseInt(availableSpace.getText().toString()) < ((ConfirmArrivalInterface) getActivity()).getCoachCount()) {
                        errorMessage.setText(getString(R.string.no_sufficient_space));
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                    else{
                        errorMessage.setVisibility(View.GONE);
                        dismiss();
                        ((ConfirmArrivalInterface)getActivity()).confirmArrival();
                    }
                }
                else{
                    errorMessage.setText(getString(R.string.error_occurred));
                    errorMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void populateSpinners(){
        //initialising dummy data
        for (String l : lineTypesArray){
            LineType newLineType = new LineType();
            newLineType.setLineType(l);
            List<String> linesList = new ArrayList<>();

            for (int j=0; j<5; j++){
                linesList.add(l + " " + (j+1));
            }

            newLineType.setLinesList(linesList);
            lineTypesList.add(newLineType);
        }

        //initialising list of stings for spinners
        lineStrings.add(getString(R.string.none));

        for (LineType lt : lineTypesList){
            lineTypeStrings.add(lt.getLineType());
        }

        lineTypeSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_custom_spinner, lineTypeStrings));
        endSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_custom_spinner, ends));
        lineSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_custom_spinner, lineStrings));

        lineTypeSpinner.setSelection(0);
        endSpinner.setSelection(0);
        lineSpinner.setSelection(0);
    }

    private void displayLineInfo(View view){
        if (capacity == null) capacity = (TextView) view.findViewById(R.id.capacity_text_view);
        if (coachesOnLine == null) coachesOnLine = (TextView) view.findViewById(R.id.coaches_on_line_text_view);
        if (availableSpace == null) availableSpace = (TextView) view.findViewById(R.id.available_space_text_view);

        capacity.setText("20");
        coachesOnLine.setText("6");
        availableSpace.setText("14");
    }

    public interface ConfirmArrivalInterface{
        void confirmArrival();
        int getCoachCount();
    }
}
