package in.org.cris.icms.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.org.cris.icms.R;
import in.org.cris.icms.activities.ShopMarkingActivity;

/**
 * Created by anurag on 27/6/17.
 */
public class ShopMarkingDialogFragment extends DialogFragment {
    private String[] shopReasons = {"None", "Accident Damage", "POH"};
    private String[] shops = {"None", "Shop 1", "Shop 2", "Shop 3"};

    public ShopMarkingDialogFragment() {
    }

    public static ShopMarkingDialogFragment createInstance(){
        return new ShopMarkingDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_shop_mark, container, false);

        final Spinner shopReasonSpinner = (Spinner)view.findViewById(R.id.shop_reason_spinner);
        final Spinner shopSpinner = (Spinner)view.findViewById(R.id.shop_spinner);

        TextView markingDate = (TextView)view.findViewById(R.id.shop_marking_date_text_view);
        final TextView errorMessage = (TextView)view.findViewById(R.id.shop_error_message_text_view);
        TextView confirm = (TextView)view.findViewById(R.id.shop_confirm_text_view);
        TextView cancel = (TextView)view.findViewById(R.id.shop_cancel_text_view);
        TextView coachInfo = (TextView)view.findViewById(R.id.shop_mark_dialog_coach_info);

        shopReasonSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_custom_spinner, shopReasons));
        shopSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_custom_spinner, shops));

        coachInfo.setText(getArguments().getString("coachInfo", ""));

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.US);

        Date date = new Date();
        markingDate.setText(sdf1.format(date)+" "+sdf2.format(date));

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                errorMessage.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };

        shopReasonSpinner.setOnItemSelectedListener(listener);
        shopSpinner.setOnItemSelectedListener(listener);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopReasonSpinner.getSelectedItem().toString().equals(getString(R.string.none))
                        || shopSpinner.getSelectedItem().toString().equals(getString(R.string.none))){
                    errorMessage.setVisibility(View.VISIBLE);
                }
                else{
                    errorMessage.setVisibility(View.GONE);
                    dismiss();
                    ((ShopMarkingActivity)getActivity()).confirmMarking();
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

    public interface ConfirmShopMarking{
        void confirmMarking();
    }
}
